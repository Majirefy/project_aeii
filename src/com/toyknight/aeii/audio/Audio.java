//Audiotest.java
package com.toyknight.aeii.audio;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * 1.定义一个文件对象引用，指向名为filename那个文件 <br>
 * 2.定义一个AudioInputStream用于接收输入的音频数据<br>
 * 它是具有指定长度和格式的音频数据流<br>
 * 3.使用AudioSystem来获取音频的音频输入流<br>
 * 4,用AudioFormat来获取AudioInputStream的格式<br>
 * 5.源数据行SoureDataLine是可以写入数据的数据行<br>
 * 6。获取受数据行支持的音频格式DataLine.info<br>
 * 7.打开具有指定格式的行<br>
 * 8.写出数据<br>
 */
public class Audio extends Thread {

    /**
     * 1.定义音频文件的变量，变量需要： 一个用于存储音频文件对象名字的String对象 filename； 2.构造函数，初始化filename
     * 3.线程运行函数重写
     *     
*/
//1.定义音频文件的变量，变量需要：一个用于存储音频文件对象名字的String对象 filename
    private final String filename;
    private final boolean loop;
    private boolean isRunning = true;
    private AudioListener al = null;
    private AudioListenerVolumn alv = null;
    private SourceDataLine auline = null;
    private AudioFormat format = null;
    private boolean isWave = true;

//2.构造函数，初始化filename
    public Audio(String filename, boolean loop) {
        this.filename = filename;
        this.loop = loop;

    }

    public void Stop() {
        isRunning = false;
    }

    public void addAudioListener(AudioListener al) {
        this.al = al;
    }

    public void finish() {
        if (al != null) {
            al.nextMusic();
        }
    }

    public void initAudioVolumn(AudioListenerVolumn alv) {
        this.alv = alv;
    }

    public void initVolumn() {
        alv.initMusicVolumn();
    }

//3.线程运行函数重写  
    @Override
    public void run() {
//1.定义一个文件对象引用，指向名为filename那个文件
        File sourceFile = new File(filename);
        // System.out.println(sourceFile.getAbsolutePath());
//2定义一个AudioInputStream用于接收输入的音频数据
        AudioInputStream audioInputStream = null;
        AudioInputStream othersAudioInputStream = null;
//3使用AudioSystem来获取音频的音频输入流
        try {
            audioInputStream = AudioSystem.getAudioInputStream(sourceFile);
        } catch (UnsupportedAudioFileException e) {
        } catch (IOException e) {
        }
//4,用AudioFormat来获取AudioInputStream的格式
        format = audioInputStream.getFormat();

        if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
            format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 16, format.getChannels(), format.getChannels() * 2, format.getSampleRate(),false);
            othersAudioInputStream = AudioSystem.getAudioInputStream(format, audioInputStream);
            isWave = false;
        }else {
            othersAudioInputStream = audioInputStream;
        }

//        System.out.println(format.toString());
//        System.out.println(format.getEncoding() + "\n" + format.getSampleRate() + "\n" + format.getSampleSizeInBits() + "\n" + format.getChannels() + "\n" + format.getFrameSize() + "\n" + format.getFrameRate() + "\n");
//5.源数据行SoureDataLine是可以写入数据的数据行
//获取受数据行支持的音频格式DataLine.info
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format, AudioSystem.NOT_SPECIFIED);

        //获得与指定info类型相匹配的行
        try {
            auline = (SourceDataLine) AudioSystem.getLine(info);
////打开具有指定格式的行，这样可使行获得所有所需系统资源并变得可操作
            auline.open();

        } catch (LineUnavailableException e) {
        }
//允许某一个数据行执行数据i/o
        auline.start();

        initVolumn();

//写出数据
        int nBytesRead = 0;
        //System.out.println(audioInputStream.markSupported());
        byte[] abData = new byte[48];
//从音频流读取指定的最大数量的数据字节，并将其放入给定的字节数组中。
        try {
            while (nBytesRead != -1 && isRunning) {
                nBytesRead = othersAudioInputStream.read(abData, 0, abData.length);
//通过此源数据行将数据写入混频器
                if (nBytesRead >= 0) {
                    auline.write(abData, 0, nBytesRead);
                    // System.out.println(nBytesRead);
                } else if (loop) {
                    audioInputStream = AudioSystem.getAudioInputStream(sourceFile);
                    if(isWave) {
                        othersAudioInputStream = audioInputStream;
                    }else {
                        othersAudioInputStream = AudioSystem.getAudioInputStream(format, audioInputStream);
                    }
                    nBytesRead = othersAudioInputStream.read(abData, 0, abData.length);
                }
            }
        } catch (IOException ex) {
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Audio.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            auline.drain();
            auline.close();
        }
        if (isRunning) {
            finish();
        }
    }

    public void changeVolumn(int volumn) {
        float new_volumn = 30 + volumn * 70 / 100.0f;
        FloatControl volctrl = (FloatControl) auline.getControl(FloatControl.Type.MASTER_GAIN);
        float min = volctrl.getMinimum();
        //System.out.println(min);
        float max = volctrl.getMaximum();
        //System.out.println(max);
        float width = max - min;
        float f_volumn = min + (new_volumn * width / 100.0f);
//        System.out.println(volumn);
//        System.out.println(f_volumn);
        volctrl.setValue(f_volumn);

    }
}
