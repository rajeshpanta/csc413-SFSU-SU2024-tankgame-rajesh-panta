package tankwargame.game;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Sound {
    public static final String MUSIC = "music";
    public static final String EXPLOSION = "explosion";
    public static final String FIRE = "fire";

    private Map<String, Clip> soundEffects;
    private Clip backgroundMusic;
    private boolean isMusicPlaying = false;

    public Sound() {
        soundEffects = new HashMap<>();
    }

    public void loadSound(String name, String path) {
        try {
            URL url = getClass().getClassLoader().getResource(path);
            if (url == null) {
                System.err.println("Error: Sound file not found - " + path);
                return;
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            soundEffects.put(name, clip);
            System.out.println("Loaded sound: " + name + " from " + path);
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Error: Unsupported audio file - " + path);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error: IO exception - " + path);
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.err.println("Error: Line unavailable - " + path);
            e.printStackTrace();
        }
    }

    public void playSound(String name) {
        Clip clip = soundEffects.get(name);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
            System.out.println("Playing sound: " + name);
        } else {
            System.err.println("Error: Sound not found - " + name);
        }
    }

    public void loopSound(String name) {
        Clip clip = soundEffects.get(name);
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            System.out.println("Looping sound: " + name);
        } else {
            System.err.println("Error: Sound not found - " + name);
        }
    }

    public void stopSound(String name) {
        Clip clip = soundEffects.get(name);
        if (clip != null) {
            clip.stop();
            System.out.println("Stopped sound: " + name);
        } else {
            System.err.println("Error: Sound not found - " + name);
        }
    }

    public void playBackgroundMusic(String path) {
        if (isMusicPlaying) {
            stopBackgroundMusic();
        }

        try {
            URL url = getClass().getClassLoader().getResource(path);
            if (url == null) {
                System.err.println("Error: Music file not found - " + path);
                return;
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            isMusicPlaying = true;
            System.out.println("Playing background music: " + path);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null && isMusicPlaying) {
            backgroundMusic.stop();
            backgroundMusic.close();
            isMusicPlaying = false;
            System.out.println("Stopped background music");
        }
    }

    public void stopAllSounds() {
        for (Clip clip : soundEffects.values()) {
            clip.stop();
        }
        stopBackgroundMusic();
    }
}
