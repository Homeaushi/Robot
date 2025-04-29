package src.main.java.gui;

import javax.swing.*;
import java.io.*;
import java.util.Properties;

public class WindowConfig {
    private final String CONFIG_FILE = System.getProperty("user.home") + "/windowsState.cfg";
    private final Properties windowStates = new Properties();
    private static final Properties BASE_CONFIG = new Properties();
    static {
        BASE_CONFIG.setProperty("gamewindow.x", "0");
        BASE_CONFIG.setProperty("gamewindow.y", "0");
        BASE_CONFIG.setProperty("gamewindow.width", "400");
        BASE_CONFIG.setProperty("gamewindow.height", "400");
        BASE_CONFIG.setProperty("gamewindow.iscollaps", "false");
        BASE_CONFIG.setProperty("logwindow.x", "10");
        BASE_CONFIG.setProperty("logwindow.y", "10");
        BASE_CONFIG.setProperty("logwindow.width", "212");
        BASE_CONFIG.setProperty("logwindow.height", "525");
        BASE_CONFIG.setProperty("logwindow.iscollaps", "false");
    }

    

    public WindowConfig() {
        windowStates.putAll(loadConfig());
    }

    public void saveConfig(){
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            windowStates.store(output, "Конфигурация окон приложения");
        } catch (IOException error) {
            System.out.println("Не удалось сохранить файл с конфигурацией окон");
        }
    }

    public Properties loadConfig() {
        Properties props = new Properties();
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            saveBaseConfig();
        }
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            props.load(input);
        } catch (IOException error) {
            System.out.println("Не удалось загрузить конфигурации окон");
        }
        return props;
    }

    public void saveWindowStates(JDesktopPane desktopPane){
        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            if (frame instanceof LogWindow) {
                saveWindowState(frame, "logWindow");
            } else if (frame instanceof GameWindow) {
                saveWindowState(frame, "gameWindow");
            }
        }
        saveConfig();
    }

    public Properties getWindowsStates() {
        return windowStates;
    }

    private void saveWindowState(JInternalFrame frame, String windowPrefix) {
        windowStates.setProperty(windowPrefix + ".x", String.valueOf(frame.getX()));
        windowStates.setProperty(windowPrefix + ".y", String.valueOf(frame.getY()));
        windowStates.setProperty(windowPrefix + ".width", String.valueOf(frame.getWidth()));
        windowStates.setProperty(windowPrefix + ".height", String.valueOf(frame.getHeight()));
        windowStates.setProperty(windowPrefix + ".isCollaps", String.valueOf(frame.isIcon()));
    }

    private void saveBaseConfig() {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            BASE_CONFIG.store(output, "Базовая конфигурация окон приложения");
        } catch (IOException error) {
            System.out.println("Не удалось создать файл с базовой конфигурацией окон");
        }
    }
}