package src.main.java.gui;

import src.main.java.log.Logger;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.util.Properties;
import javax.swing.*;


public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final WindowConfig windowConfig = new WindowConfig();

    public MainApplicationFrame() {
        int inset = 50;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);
        setContentPane(desktopPane);

        LogWindow logWindow = new LogWindow();
        restoreWindowState(logWindow, "logWindow");
        addWindow(logWindow);


        GameWindow gameWindow = new GameWindow();
        restoreWindowState(gameWindow, "gameWindow");
        addWindow(gameWindow);


        setJMenuBar(createMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                confirmAndExit();
            }
        });
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private void restoreWindowState(JInternalFrame frame, String windowPrefix) {
        Properties windowStates = windowConfig.getWindowsStates();
        String x = windowStates.getProperty(windowPrefix + ".x");
        String y = windowStates.getProperty(windowPrefix + ".y");
        String width = windowStates.getProperty(windowPrefix + ".width");
        String height = windowStates.getProperty(windowPrefix + ".height");
        String isCollaps = windowStates.getProperty(windowPrefix + ".isCollaps");

        try {
            frame.setBounds(
                    Integer.parseInt(x),
                    Integer.parseInt(y),
                    Integer.parseInt(width),
                    Integer.parseInt(height)
            );
            if (Boolean.parseBoolean(isCollaps)) {
                try {
                    frame.setIcon(true);
                } catch (Exception error) {
                    System.out.println("Ошибка при восстановление состояния");
                }
            }
        } catch (NumberFormatException error) {
            System.out.println("Ошибка при чтении конфигурации окна");
        }
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createGameMenu());
        menuBar.add(createExitMenu());


        return menuBar;
    }

    private JMenu createGameMenu() {
        JMenu menu = new JMenu("Игра");
        menu.setMnemonic(KeyEvent.VK_M);
        menu.getAccessibleContext().setAccessibleDescription(
                "Взаимодействия с меню игры");

        JMenuItem gameItem = new JMenuItem("Открыть игровое окно");
        gameItem.addActionListener(_ -> addWindow(new GameWindow()));
        menu.add(gameItem);

        return menu;
    }

    private JMenu createTestMenu() {
        JMenu menu = new JMenu("Тесты");
        menu.setMnemonic(KeyEvent.VK_T);
        menu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        JMenuItem logItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
        logItem.addActionListener(_ -> Logger.debug("Новая строка"));
        menu.add(logItem);

        JMenuItem openingItem = new JMenuItem("Открыть меню логов", KeyEvent.VK_C);
        openingItem.addActionListener(_ -> addWindow(new LogWindow()));
        menu.add(openingItem);

        return menu;
    }

    private JMenu createExitMenu() {
        JMenu menu = new JMenu("Выход");
        menu.setMnemonic(KeyEvent.VK_Q);

        JMenuItem exitItem = new JMenuItem("Закрыть приложение", KeyEvent.VK_ESCAPE);
        exitItem.addActionListener(_ -> confirmAndExit());
        menu.add(exitItem);
        return menu;
    }

    private void confirmAndExit() {
        String[] exitOptions = {YesOrNoState.YES.getTitle(), YesOrNoState.No.getTitle()};
        int result = JOptionPane.showOptionDialog(
                this,
                "Вы действительно хотите закрыть приложение?",
                "Подтверждение выхода",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                exitOptions,
                exitOptions[0]);

        if (result == JOptionPane.YES_OPTION) {
            windowConfig.saveWindowStates(desktopPane);
            System.exit(0);
        }
    }

    private JMenu createLookAndFeelMenu() {
        JMenu menu = new JMenu("Режим отображения");
        menu.setMnemonic(KeyEvent.VK_V);
        menu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        JMenuItem systemItem = new JMenuItem("Системная схема", KeyEvent.VK_S);
        systemItem.addActionListener(_ -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        menu.add(systemItem);

        JMenuItem crossplatformItem = new JMenuItem("Универсальная схема", KeyEvent.VK_U);
        crossplatformItem.addActionListener(_ -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        menu.add(crossplatformItem);

        return menu;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }
}