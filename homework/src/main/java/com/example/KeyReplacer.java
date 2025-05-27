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
    private static final int TYPING_DELAY = 5; // Even smaller delay

    public KeyReplacer() {
        try {
            robot = new Robot();
            robot.setAutoDelay(TYPING_DELAY);
            writer = new FileWriter("output.txt", true);
        } catch (AWTException | IOException e) {
            e.printStackTrace();
        }
    }

    public void typeCharacter(char c) {
        boolean upper = Character.isUpperCase(c);
        int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);

        if (upper) robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(keyCode);
        robot.keyRelease(keyCode);
        if (upper) robot.keyRelease(KeyEvent.VK_SHIFT);
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        char original = e.getKeyChar();

        if (original >= 'a' && original <= 'z') {
            try {
                // Block original char
                robot.keyPress(KeyEvent.VK_BACK_SPACE);
                robot.keyRelease(KeyEvent.VK_BACK_SPACE);

                // Transform
                char replaced = (char) (original + 1);
                if (replaced > 'z') replaced = 'a';

                // Type new char
                typeCharacter(replaced);

                // Log
                writer.write(replaced);
                writer.flush();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {}

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {}

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
            return;
        }

        GlobalScreen.addNativeKeyListener(new KeyReplacer());
    }
}