package com.geduo.audio.recorder;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description: <音频录制、播放管理类><br>
 *     <ul>
 *         <li>1.开始录制</li>
 *         <li>2.停止录制</li>
 *         <li>3.播放</li>
 *     </ul>
 * Author:      gxl<br>
 * Date:        2018/12/4<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
public class SuperAudioManager {
    public static final String TAG = "MYTAG";
    public final int sampleRateInHz = 44100;//设置采样率
    public final int channelInMono = AudioFormat.CHANNEL_IN_MONO;//设置输入声道
    public final int channelOutMono = AudioFormat.CHANNEL_OUT_MONO;//设置输出声道
    public final int encodingPcm16bit = AudioFormat.ENCODING_PCM_16BIT;//设置编码格式
    public final int mReadMinBufferSize;//读取的最小缓存
    private final int mWriteMinBufferSize;//写入的最小缓存
    private AudioRecord mAudioRecord;
    private AudioTrack mAudioTrack;
    private boolean isRecording;
    private Context mContext;
    private ExecutorService mExecutorService;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SuperAudioManager(Context context) {
        mContext = context;
        //获取最小的输入缓存
        mReadMinBufferSize = AudioRecord.getMinBufferSize(sampleRateInHz, channelInMono, encodingPcm16bit);
        //初始化音频录制的最小缓存
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRateInHz, channelInMono, encodingPcm16bit, mReadMinBufferSize);

        //获取最小的输出缓存
        mWriteMinBufferSize = AudioTrack.getMinBufferSize(sampleRateInHz, channelOutMono, encodingPcm16bit);
        //获取媒体格式
        AudioFormat audioFormat = new AudioFormat.Builder().setSampleRate(sampleRateInHz).setEncoding(encodingPcm16bit).setChannelMask(channelOutMono).build();
        //获取媒体属性
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
        //创建播放媒体流对象
        mAudioTrack = new AudioTrack(audioAttributes, audioFormat, mWriteMinBufferSize, AudioTrack.MODE_STREAM, android.media.AudioManager.AUDIO_SESSION_ID_GENERATE);
    }

    public void startRecord(final String filepath) {
        Log.v(TAG, "startRecord startRecord...");
        Log.v(TAG, "filePath:" + filepath);
        Log.v(TAG, "minBufferSize:" + mReadMinBufferSize);
        //如果正在录制，就返回了
        if(isRecording){
            return;
        }
        //开始录制
        mAudioRecord.startRecording();
        isRecording = true;
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(filepath));
                    //声明一个缓存的buffer
                    byte[] buffer = new byte[mReadMinBufferSize];
                    while (isRecording) {
                        //获取读取的字节长度
                        int read = mAudioRecord.read(buffer, 0, mReadMinBufferSize);
                        Log.v(TAG, "recording...");
                        // 如果读取音频数据没有出现错误，就将数据写入到文件
                        if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                            //将读取的字节数据写入文件
                            fileOutputStream.write(buffer);
                        }
                    }
                    //停止录制后关闭流
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void stopRecord() {
        Log.v(TAG, "stopRecord startRecord...");
        // 停止录制，释放资源
        if (mAudioRecord != null && isRecording) {
            mAudioRecord.stop();
            //mAudioRecord.release();这个不能调用，如果此次再次录制，就会报错
            isRecording = false;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void play(final String filepath) {
        mAudioTrack.play();
        if(mExecutorService == null){
            mExecutorService = Executors.newSingleThreadExecutor();
        }
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //创建一个文件输入流
                    FileInputStream fileInputStream = new FileInputStream(filepath);
                    //创建一个buffer缓存
                    byte[] tempBuffer = new byte[mWriteMinBufferSize];
                    //读入文件流
                    while (fileInputStream.available() > 0) {
                        int readCount = fileInputStream.read(tempBuffer);
                        //如果读到的数据有问题，则略过本次循环
                        if (readCount == AudioTrack.ERROR_INVALID_OPERATION || readCount == AudioTrack.ERROR_BAD_VALUE) {
                            continue;
                        }
                        //将读取到的文件流输入到播放流进行播放
                        if (readCount != 0 && readCount != -1) {
                            mAudioTrack.write(tempBuffer, 0, readCount);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void destory(){
        if(mAudioRecord != null){
            mAudioRecord.release();
            mAudioRecord = null;
        }
        if(mAudioTrack != null){
            mAudioTrack.release();
            mAudioTrack = null;
        }
    }

}
