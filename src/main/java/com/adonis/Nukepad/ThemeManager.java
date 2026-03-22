/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adonis.Nukepad;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author croco
 */
public class ThemeManager {
    private static final File THEME_FILE = new File(
    System.getProperty("user.home") + "/.nukepad_theme.txt"
    );
    
    public static void apply() throws UnsupportedLookAndFeelException {
        try {
            if(load().equals("dark")) {
                UIManager.setLookAndFeel(new FlatDarculaLaf());
            } else {
                UIManager.setLookAndFeel(new FlatIntelliJLaf());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static String load() {
        if(!THEME_FILE.exists()) return "light";
        try(BufferedReader r = new BufferedReader(new FileReader(THEME_FILE))) {
            String line = r.readLine();
            return (line != null && !line.isBlank()) ? line.trim() : "light";
        } catch (IOException ex) {
            return "light";
        }
    }
    
    public static void save (String theme) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(THEME_FILE))) {
            w.write(theme);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
