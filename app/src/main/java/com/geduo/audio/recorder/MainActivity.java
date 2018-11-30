package com.geduo.audio.recorder;

import android.Manifest;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.geduo.audio.recorder.util.AudioRecorder;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * <h1>录音测试</h1>
 * <ul>
 * <li>1.点击开始</li>
 * <li>2.点击结束</li>
 * <li>3.注意事项：a.权限检查</li>
 * </ul>
 *
 * Description: <MainActivity><br>
 * Author: gxl<br>
 * Date: 2018/7/5<br>
 * Version: V1.0.0<br>
 * Update: <br>
 */
public class MainActivity extends AppCompatActivity {
    private MediaRecorder mMediaRecorder;//格式较少，录制过程不能暂停
    private AudioRecorder mAudioRecorder;//
    @BindView(R.id.btn_start)
    Button btnStart;

    @BindView(R.id.btn_stop)
    Button btnStop;

    @BindView(R.id.btn_start1)
    Button btnStart1;

    @BindView(R.id.btn_stop1)
    Button btnStop1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn_start)
    void onStartClick() {
        new RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    startRecord();
                } else {
                    Toast.makeText(MainActivity.this, "没有相关权限", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @OnClick(R.id.btn_stop)
    void onStopClick() {
        stopRecord();
    }

    //=======================================================
    @OnClick(R.id.btn_start1)
    void onStart1Click() {
        new RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    startRecord1();
                } else {
                    Toast.makeText(MainActivity.this, "没有相关权限", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @OnClick(R.id.btn_stop1)
    void onStop1Click() {
        stopRecord1();
    }

    private void startRecord() {
        Log.v("MYTAG", "onStartClick start...");
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//设置输出文件的格式
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);//设置音频文件的编码
        mMediaRecorder.setOutputFile(getAudioFilePath());
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            Log.v("MYTAG", "start record succ...");
        } catch (Exception e) {
            Log.v("MYTAG", "start record fail:" + e.toString());
        }
    }

    private String getAudioFilePath() {
        String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "audio";
        Log.v("MYTAG", "audio folder:" + folderPath);
        File file = new File(folderPath);
        if (!file.exists()) {
            file.mkdir();
        }
        String fileName = DateFormat.format("yyyyMMddHHmmss", Calendar.getInstance(Locale.CHINA)) + ".m4a";//".m4a";
        String filePath = folderPath + File.separator + fileName;
        Log.v("MYTAG", "audio file path:" + filePath);
        return filePath;
    }

    private void stopRecord() {
        Log.v("MYTAG", "onStopClick start...");
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.reset();//重新开始
            //mMediaRecorder.release();
        }
    }

    private void startRecord1() {
        if (mAudioRecorder == null) {
            mAudioRecorder = new AudioRecorder();
        }
        mAudioRecorder.createDefaultAudio(DateFormat.format("yyyyMMddHHmmss",Calendar.getInstance(Locale.CANADA))+"");
        mAudioRecorder.startRecord();
    }
    private void stopRecord1(){
        if (mAudioRecorder != null) {
            mAudioRecorder.stopRecord();
            mAudioRecorder.release();
        }
    }
}
