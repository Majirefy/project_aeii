package com.toyknight.aeii.audio;

import java.util.Random;

/**
 *
 * @author adams
 */
public class AudioManager {

    public static Audio at = null;
    public static Audio sound_effect = null;
    public static String[] audio_list;

    private static int bgm_volumn;
    private static int se_volumn;

    private AudioManager() {

    }

    public static void init() {
        bgm_volumn = 80;
        se_volumn = 60;
    }

    //one at a time. once starting a new bgm, stop the playing one
    public static void playBgm(String filename, boolean loop) {
        at = new Audio(filename, loop);
        at.initAudioVolumn(new AudioListenerVolumn() {

            @Override
            public void initMusicVolumn() {
                at.changeVolumn(bgm_volumn);
            }

        });
        at.start();
    }

    public static void playBgm(String[] file_list, int start_index) {
        audio_list = file_list;
        at = new Audio(file_list[start_index], false);
        at.initAudioVolumn(new AudioListenerVolumn() {

            @Override
            public void initMusicVolumn() {
                at.changeVolumn(bgm_volumn);
            }

        });
        at.addAudioListener(listener);
        at.start();
    }

    public static void stopBgm() {
        at.Stop();
    }

    //can play multiple SE at a time
    public static void playSe(String filename) {
        boolean loop = false;
        sound_effect = new Audio(filename, loop);
        sound_effect.initAudioVolumn(new AudioListenerVolumn() {

            @Override
            public void initMusicVolumn() {
                sound_effect.changeVolumn(se_volumn);
            }

        });
        sound_effect.start();
    }

    //0~100
    public static void setBgmVolumn(int volumn) {
        if (at != null) {
            at.changeVolumn(volumn);
        }
        bgm_volumn = volumn;
    }

    public static int getBgmVolumn() {
        return bgm_volumn;
    }

    //0~100
    public static void setSeVolumn(int volumn) {
        if (sound_effect != null) {
            sound_effect.changeVolumn(volumn);
        }
        se_volumn = volumn;
    }

    public static int getSeVolumn() {
        return se_volumn;
    }

    static AudioListener listener = new AudioListener() {

        @Override
        public void nextMusic() {
            Random rs = new Random(System.currentTimeMillis());
            int index = rs.nextInt(audio_list.length);
            at = new Audio(audio_list[index], false);
            at.initAudioVolumn(new AudioListenerVolumn() {

                @Override
                public void initMusicVolumn() {
                    at.changeVolumn(bgm_volumn);
                }

            });
            at.addAudioListener(listener);
            at.start();
        }

    };
}
