package com.geduo.audio.recorder;

import android.Manifest;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * <h1>录音测试</h1>
 * <ul>
 * <li>1.开始</li>
 * <li>2.结束</li>
 * <li>3.播放</li>
 * <li>4.权限检查</li>
 * </ul>
 *
 * Description: <MainActivity><br>
 * Author: gxl<br>
 * Date: 2018/7/5<br>
 * Version: V1.0.0<br>
 * Update: <br>
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MYTAG";
    private MediaRecorder mMediaRecorder;//格式较少，录制过程不能暂停
    private SuperAudioManager mSuperAudioManager;
    private SuperMediaManager mSuperMediaManager;
    @BindView(R.id.btn_start)
    Button btnStart;

    @BindView(R.id.btn_stop)
    Button btnStop;

    @BindView(R.id.btn_play)
    Button btnPaly;

    @BindView(R.id.btn_start1)
    Button btnStart1;

    @BindView(R.id.btn_stop1)
    Button btnStop1;

    @BindView(R.id.btn_play1)
    Button btnPaly1;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSuperAudioManager = new SuperAudioManager(this);
        mSuperMediaManager = new SuperMediaManager(this);
        new RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (!aBoolean) {
                    Toast.makeText(MainActivity.this, "没有相关权限", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //开始录制
    @OnClick(R.id.btn_start)
    void onStartClick() {
        Log.v(TAG, "onStartClick startRecord...");
        String filepath = getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath() + File.separator + "test.m4a";
        mSuperMediaManager.startRecord(filepath);

    }

    //停止录制
    @OnClick(R.id.btn_stop)
    void onStopClick() {
        Log.v(TAG, "onStopClick startRecord...");
        mSuperMediaManager.stopRecord();
    }

    @OnClick(R.id.btn_play)
    void onPlayClick() {
        Log.v(TAG, "onPlayClick startRecord...");
        String filepath = getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath() + File.separator + "test.m4a";
        mSuperMediaManager.play(filepath);
    }

    @OnClick(R.id.btn_start1)
    void onStart1Click() {
        Log.v(TAG, "onStart1Click startRecord...");
        String filepath = getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath() + File.separator + "test1.pcm";
        mSuperAudioManager.startRecord(filepath);

    }

    @OnClick(R.id.btn_stop1)
    void onStop1Click() {
        if (mSuperAudioManager != null) {
            mSuperAudioManager.stopRecord();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.btn_play1)
    void onPlay1Click() {
        if (mSuperAudioManager != null) {
            String filepath = getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath() + File.separator + "test1.pcm";
            mSuperAudioManager.play(filepath);
        }

    }

}
