package com.geduo.audio.recorder;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

/**
 * Description: <音频录制、播放管理类><br>
 *     <ul><li>1.开始录制</li>
 *     <li>2.停止录制</li>
 *     <li>3.开始播放</li>
 *     <li>4.销毁管理器</li>
 *     </ul>
 * Author:      gxl<br>
 * Date:        2018/12/4<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
public class SuperMediaManager {
    public static final String TAG = "MYTAG";
    private Context mContext;
    private MediaRecorder mMediaRecorder;
    private MediaPlayer mMediaPlayer;
    private boolean isRecording;

    public SuperMediaManager(Context context) {
        mMediaRecorder = new MediaRecorder();
        //设置音频的来源
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置音频的输出格式
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//设置输出文件的格式
        //设置音频文件的编码
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);//设置音频文件的编码

        mMediaPlayer = new MediaPlayer();
    }

    public void startRecord(String filepath) {
        Log.v(TAG, "startRecord startRecord");
        Log.v(TAG, "file path:" + filepath);
        //如果正在录制，就返回了
        if(isRecording){
            return;
        }
        mMediaRecorder.setOutputFile(filepath);
        try {
            //录制前准备工作
            mMediaRecorder.prepare();
            //开始录制
            mMediaRecorder.start();

            isRecording = true;
            Log.v(TAG, "startRecord record succ...");
        } catch (Exception e) {
            Log.v(TAG, "startRecord record fail:" + e.toString());
        }
    }

    public void stopRecord() {
        if (mMediaRecorder != null && isRecording) {
            //停止录制
            mMediaRecorder.stop();
            //重新开始
            mMediaRecorder.reset();//注意：可以通过返回setAudioSource（）步骤来重用该对象
            //mMediaRecorder.release();注意：这个对象不能再次被使用，如果此次再次录制，就会报错
            isRecording = false;
        }
    }

    public void play(String filepath) {
        try {
            //如果正在播放，然后在播放其他文件就直接崩溃了
            if(mMediaPlayer.isPlaying()){
                return;
            }
            //设置数据源
            mMediaPlayer.setDataSource(filepath);
            //这个准备工作必须要做
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //播放完毕再重置一下状态，下次播放可以再次使用
                    mp.reset();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void destory(){
        if(mMediaRecorder != null){
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        if(mMediaPlayer != null){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
