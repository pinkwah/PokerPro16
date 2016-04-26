package org.gruppe2;

import javafx.application.Application;
import org.gruppe2.ui.Resources;
import org.gruppe2.ui.console.ConsoleApplication;
import org.gruppe2.ui.javafx.PokerApplication;

import java.io.*;
import java.util.Properties;

public class Main {
    private enum EntryPoint {
        CONSOLE, JAVAFX
    }

    private static Properties properties = new Properties();
    private static EntryPoint entryPoint = EntryPoint.JAVAFX;
    private static boolean autostart = false;

    public static void main(String[] args) {
        parseArgs(args);
        loadProperties();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                saveProperties();
            }
        });

        switch (entryPoint) {
            case CONSOLE:
                new ConsoleApplication().run();
                break;

            case JAVAFX:
                Application.launch(PokerApplication.class, args);
                break;
        }
    }

    private static void parseArgs(String[] args) {
        for (String arg : args) {
            switch (arg.toLowerCase()) {
                case "--console":
                case "--nogui":
                case "-c":
                    entryPoint = EntryPoint.CONSOLE;
                    break;

                case "-a":
                case "--autostart":
                    autostart = true;
                    break;

                default:
                    System.out.println("Unknown argument: " + arg);
                    break;
            }
        }
    }

    private static void loadProperties() {
        try {
            FileInputStream stream = new FileInputStream(Resources.getProperties());

            properties.load(stream);

            stream.close();
        } catch (IOException e) {
            System.err.println("Couldn't load properties: " + e.getMessage());
        }
    }

    private static void saveProperties() {
        try {
            FileOutputStream stream = new FileOutputStream(Resources.getProperties());

            properties.store(stream, "Poker Pro 16 Best Poker");

            stream.close();
        } catch (IOException e) {
            System.err.println("Couldn't save properties: " + e.getMessage());
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public static boolean isAutostart() {
        return autostart;
    }
}
