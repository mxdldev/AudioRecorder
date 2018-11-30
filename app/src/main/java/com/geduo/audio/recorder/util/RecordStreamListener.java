package com.geduo.audio.recorder.util;

/**
 * Description: <><br>
 * Author:      gxl<br>
 * Date:        2018/11/30<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
public interface RecordStreamListener {
    void recordOfByte(byte[] data, int begin, int end);
}