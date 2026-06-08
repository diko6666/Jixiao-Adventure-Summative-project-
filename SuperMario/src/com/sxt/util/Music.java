package com.sxt.util;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
public class Music {
    private static final String MUSIC_RESOURCE = "/Music/music.wav";
    private static final String MUSIC_SOURCE_PATH = "src/Music/music.wav";
    private static final String PLAYER_CLASS = "javazoom.jl.player.Player";
    private static final String JLAYER_JAR = "lib/jlayer-1.0.1.jar";
    private static Music instance = null;
    private Object currentPlayer = null;
    private Method closeMethod = null;
    public Music() {
        instance = this;
        Thread thread = new Thread(this::playLoop);
        thread.setDaemon(true);
        thread.start();
    }
    private void playLoop() {
        try {
            Class<?> playerClass = loadPlayerClass();
            Constructor<?> constructor = playerClass.getConstructor(InputStream.class);
            Method play = playerClass.getMethod("play");
            closeMethod = playerClass.getMethod("close");
            while (true) {
                if (!GameSettings.musicEnabled) {
                    stopCurrentPlayer();
                    Thread.sleep(100);
                    continue;
                }
                try (InputStream music = openMusic()) {
                    currentPlayer = constructor.newInstance(music);
                    play.invoke(currentPlayer);
                    currentPlayer = null;
                } catch (Exception e) {
                    if (GameSettings.musicEnabled) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void stopCurrentPlayer() {
        if (currentPlayer != null && closeMethod != null) {
            try {
                closeMethod.invoke(currentPlayer);
            } catch (Exception e) {
            }
            currentPlayer = null;
        }
    }
    public static void stopMusic() {
        if (instance != null) {
            instance.stopCurrentPlayer();
        }
    }
    private Class<?> loadPlayerClass() throws Exception {
        try {
            return Class.forName(PLAYER_CLASS);
        } catch (ClassNotFoundException e) {
            File jarFile = findJLayerJar();
            URLClassLoader loader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()});
            return Class.forName(PLAYER_CLASS, true, loader);
        }
    }
    private File findJLayerJar() {
        File userDir = new File(System.getProperty("user.dir")).getAbsoluteFile();
        for (File dir = userDir; dir != null; dir = dir.getParentFile()) {
            File directJar = new File(dir, JLAYER_JAR);
            if (directJar.isFile()) {
                return directJar;
            }
            File[] children = dir.listFiles(File::isDirectory);
            if (children != null) {
                for (File child : children) {
                    File childJar = new File(child, JLAYER_JAR);
                    if (childJar.isFile()) {
                        return childJar;
                    }
                }
            }
        }
        throw new IllegalStateException("Cannot find " + JLAYER_JAR + " from user.dir=" + userDir.getAbsolutePath());
    }
    private InputStream openMusic() throws Exception {
        InputStream resource = Music.class.getResourceAsStream(MUSIC_RESOURCE);
        if (resource != null) {
            return new BufferedInputStream(resource);
        }
        File userDir = new File(System.getProperty("user.dir"));
        File[] candidates = {
                new File(userDir, MUSIC_SOURCE_PATH),
                new File(userDir, "SuperMario/" + MUSIC_SOURCE_PATH)
        };
        for (File candidate : candidates) {
            if (candidate.isFile()) {
                return new BufferedInputStream(new FileInputStream(candidate));
            }
        }
        throw new IllegalStateException("Cannot find music.wav from user.dir=" + userDir.getAbsolutePath());
    }
}
