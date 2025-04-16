package src.main.java.gui;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class WindowConfig {
    private final String CONFIG_FILE = System.getProperty("user.home") + "/windowsState.cfg";
    private final Properties windowStates = new Properties();

    public WindowConfig() {
        windowStates.putAll(loadConfig());
    }

    public void saveConfig(Properties props){
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            props.store(output, "Конфигурация окон приложения");
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
        saveConfig(windowStates);
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
        final String baseFileConfig = """
                #\\u041A\\u043E\\u043D\\u0444\\u0438\\u0433\\u0443\\u0440\\u0430\\u0446\\u0438\\u044F \\u043E\\u043A\\u043E\\u043D \\u043F\\u0440\\u0438\\u043B\\u043E\\u0436\\u0435\\u043D\\u0438\\u044F
                #Wed Apr 16 04:21:58 GMT+05:00 2025
                gameWindow.height=400
                gameWindow.isCollaps=false
                gameWindow.width=400
                gameWindow.x=0
                gameWindow.y=0
                logWindow.height=525
                logWindow.isCollaps=false
                logWindow.width=212
                logWindow.x=10
                logWindow.y=10
                """;
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            output.write(baseFileConfig.getBytes(StandardCharsets.UTF_8));
        } catch (IOException error) {
            System.out.println("Не удалось создать файл с базовой конфигурацией окон");
        }
    }
}