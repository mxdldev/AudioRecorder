## 音频录制、播放
最近在项目要用到录音的功能，研究了几天，看了很多的资料，今天在这里分享记录一下以便回头查看。Android给我们提供了两个录音的API接口：MediaRecord、AudioRecoder
### MediaRecord：
#### 1. 优点：
* 可以录制音频、视频
* 提供了录制、压缩、编码等功能
* 使用简单方便，几行代码就可实现
#### 2. 缺点：
* 可以录制的视频格式较少
* 录制的过程中不能暂停
* 不能实时处理音频数据（实时对讲的话用它就不适合了）
#### 3. 注意：
* 模拟器不支持音频、视频录制，只能在真机上测试
* 录制一定打开录制权限
```<uses-permission android:name="android.permission.RECORD_AUDIO" />```
* Android 9(API级别28)或更高,应用程序在后台运行不能访问麦克风
#### 4.使用：
##### 初始化
```
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
```
##### 开始录制
```
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
```
##### 停止录制
```
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
```
##### 录制监听
* 录制错误监听：MediaRecorder.OnErrorListener 
```
onError(MediaRecorder mr, int what, int extra)
```
MEDIA_RECORDER_ERROR_UNKNOWN：未知错误 
MEDIA_ERROR_SERVER_DIED：媒体服务卡死，在这种情况下，应用程序必须释放MediaRecorder对象并实例化一个新对象
* 录制警告信息监听：MediaRecorder.OnInfoListener	
```
onInfo(MediaRecorder mr, int what, int extra)	
```
MediaRecorder.MEDIA_RECORDER_INFO_UNKNOWN：未知的错误  
MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED：录制超时了
MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED：录制文件超过指定大小了，需要setNextOutputFile(File)指定一个新的文件进行存储
		
##### 播放
```
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
```
#### 录制状态图：
### AudioRecoder：
#### 优点：
* 专业的音频录制API，支持实时录制、支持暂停等，以流的形式进行录制和播放的操作
#### 缺点：
* 录制格式为pcm，在播放器不能直接播放，需要编码和压缩		
#### 使用	
##### 开始录制

##### 停止录制
##### 录制监听
