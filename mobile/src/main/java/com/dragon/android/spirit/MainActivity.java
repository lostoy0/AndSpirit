package com.dragon.android.spirit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.dragon.android.spirit.eventbus.LocationMessage;
import com.dragon.android.spirit.location.LocationManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

import static com.dragon.android.spirit.utilities.Constants.PERMISSIONS;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private static final int REQUEST_CODE_PERMISSION = 1000;

    private TextView mContentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void init() {
        // Example of a call to a native method
        mContentTextView = findViewById(R.id.text);
        mContentTextView.setText(stringFromJNI());

        setOnClickListener();

        SpiritManager.startCoreService(this);

        if(EasyPermissions.hasPermissions(this, PERMISSIONS)) {
            LocationManager.getInstance().init(this);
        } else {
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, REQUEST_CODE_PERMISSION, PERMISSIONS)
//                            .setRationale(R.string.camera_and_location_rationale)
//                            .setPositiveButtonText(R.string.rationale_ask_ok)
//                            .setNegativeButtonText(R.string.rationale_ask_cancel)
//                            .setTheme(R.style.my_fancy_style)
                            .build());
        }
    }

    private void setOnClickListener() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContentTextView.setText("");
                LocationManager.getInstance().requestLocation(MainActivity.this);
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            if(EasyPermissions.hasPermissions(this, PERMISSIONS)) {
                LocationManager.getInstance().init(this);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        // Some permissions have been granted
        // ...

        LocationManager.getInstance().init(this);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // Some permissions have been denied
        // ...

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final LocationMessage event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null != event && !TextUtils.isEmpty(event.message)) {
                    mContentTextView.setText(event.message);
                }
            }
        });
    }
}
