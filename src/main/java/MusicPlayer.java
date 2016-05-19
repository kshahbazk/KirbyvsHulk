/**
 * Created by shkhan on 5/18/16.
 */

import javax.sound.sampled.*;

public class MusicPlayer {

    private Clip clip;

    // Based on sample found here http://www.dreamincode.net/forums/topic/343804-how-to-add-background-music-to-my-2d-platformer-game/
    public static MusicPlayer song = new MusicPlayer("/song.wav");
    public static MusicPlayer laserShot = new MusicPlayer("/Laser.wav");

    public MusicPlayer (String fileName) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(MusicPlayer.class.getResource(fileName));
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        try {
            if (clip != null) {
                new Thread() {
                    public void run() {
                        synchronized (clip) {
                            clip.stop();
                            clip.setFramePosition(0);
                            clip.start();
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        if(clip == null) return;
        clip.stop();
    }

    public void loop() {
        try {
            if (clip != null) {
                new Thread() {
                    public void run() {
                        synchronized (clip) {
                            clip.stop();
                            clip.setFramePosition(0);
                            clip.loop(Clip.LOOP_CONTINUOUSLY);
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isActive(){
        return clip.isActive();
    }
    public static void main(String[] args)
    {
        MusicPlayer m = new MusicPlayer("music/Sure dire dock success.m4a");
        m.play();
    }

}
