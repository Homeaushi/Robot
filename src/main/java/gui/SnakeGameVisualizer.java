package src.main.java.gui;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class SnakeGameVisualizer extends JPanel {
    private static final int DEFAULT_WIDTH = 30;
    private static final int DEFAULT_HEIGHT = 20;

    private int tileSize;
    private int width;
    private int height;
    private List<Point> snake;
    private Point food;
    private Direction direction = Direction.RIGHT;
    private boolean running = false;

    public SnakeGameVisualizer() {
        setBackground(Color.WHITE);
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        if (direction != Direction.DOWN) direction = Direction.UP;
                        break;
                    case KeyEvent.VK_S:
                        if (direction != Direction.UP) direction = Direction.DOWN;
                        break;
                    case KeyEvent.VK_A:
                        if (direction != Direction.RIGHT) direction = Direction.LEFT;
                        break;
                    case KeyEvent.VK_D:
                        if (direction != Direction.LEFT) direction = Direction.RIGHT;
                        break;
                    case KeyEvent.VK_SPACE:
                        if (!running) initGame();
                        break;
                }
            }
        });

        initGame();

        Timer timer = new Timer(150, _ -> {
            if (running) {
                move();
                checkCollision();
                checkFood();
            }
            repaint();
        });
        timer.start();
    }

    @Override
    public void doLayout() {
        super.doLayout();
        calculateFieldDimensions();
    }

    private void calculateFieldDimensions() {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        tileSize = Math.min(panelWidth / DEFAULT_WIDTH, panelHeight / DEFAULT_HEIGHT);
        tileSize = Math.max(tileSize, 5);

        width = panelWidth / tileSize;
        height = panelHeight / tileSize;

        width = Math.max(width, 10);
        height = Math.max(height, 10);
    }

    private void initGame() {
        calculateFieldDimensions();
        snake = new LinkedList<>();
        snake.add(new Point(width / 2, height / 2));
        spawnFood();
        running = true;
    }

    private void spawnFood() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(width);
            y = random.nextInt(height);
            food = new Point(x, y);
        } while (snake.contains(food));
    }

    private void move() {
        Point head = snake.getFirst();
        Point newHead = switch (direction) {
            case UP -> new Point(head.x, (head.y - 1 + height) % height);
            case DOWN -> new Point(head.x, (head.y + 1) % height);
            case LEFT -> new Point((head.x - 1 + width) % width, head.y);
            case RIGHT -> new Point((head.x + 1) % width, head.y);
        };

        snake.addFirst(newHead);
    }

    private void checkCollision() {
        Point head = snake.getFirst();

        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                running = false;
                return;
            }
        }
    }

    private void checkFood() {
        if (snake.getFirst().equals(food)) {
            spawnFood();
        } else {
            snake.removeLast();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (running) {
            g.setColor(Color.RED);
            g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

            g.setColor(Color.GREEN);
            for (Point p : snake) {
                g.fillRect(p.x * tileSize, p.y * tileSize, tileSize, tileSize);
            }

//            g.setColor(Color.DARK_GRAY);
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    g.drawRect(x * tileSize, y * tileSize, tileSize, tileSize);
//                }
//            }
        } else {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String message = "Змейка бО-Бо. Ваш счёт: " + (snake.size() - 1);
            int messageWidth = g.getFontMetrics().stringWidth(message);
            g.drawString(message, (getWidth() - messageWidth) / 2, getHeight() / 2);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            String restartMessage = "Нажмите пробел, чтобы продолжить";
            int restartWidth = g.getFontMetrics().stringWidth(restartMessage);
            g.drawString(restartMessage, (getWidth() - restartWidth) / 2, getHeight() / 2 + 40);
        }
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
}