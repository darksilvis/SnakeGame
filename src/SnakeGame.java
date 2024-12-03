import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x, y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth, boardHeight, tileSize = 25;
    boolean fireMode, hardMode, gameOver = false;
    Tile snakeHead, food;
    ArrayList<Tile> snakeBody;
    Random random;
    int velocityX, velocityY;
    Timer gameLoop;

    SnakeGame(int boardWidth, int boardHeight, boolean fireMode, boolean hardMode) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.fireMode = fireMode;
        this.hardMode = hardMode;

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

        initializeGame();
        gameLoop = new Timer(hardMode ? 67 : 100, this);
        gameLoop.start();
    }

    private void initializeGame() {
        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<>();
        food = new Tile(10, 10);
        random = new Random();
        placeFood();
        velocityX = 1;
        velocityY = 0;
        gameOver = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        // Draw grid
        g.setColor(Color.gray);
        for (int i = 0; i < boardWidth / tileSize; i++) {
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }

        // Draw food
        g.setColor(Color.red);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

        // Draw snake head
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        // Draw snake body
        for (Tile part : snakeBody) {
            g.fill3DRect(part.x * tileSize, part.y * tileSize, tileSize, tileSize, true);
        }

        // Draw score and game over text
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.white);
        g.drawString("Score: " + snakeBody.size(), 10, 20);

        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over! Press SPACE to restart.", boardWidth / 4, boardHeight / 2);
        }
    }

    private void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);
    }

    private void move() {
        if (gameOver) return;

        // Add new head position
        Tile newHead = new Tile(snakeHead.x + velocityX, snakeHead.y + velocityY);

        // Handle fire mode
        if (fireMode) {
            if (newHead.x < 0) newHead.x = boardWidth / tileSize - 1;
            if (newHead.y < 0) newHead.y = boardHeight / tileSize - 1;
            if (newHead.x >= boardWidth / tileSize) newHead.x = 0;
            if (newHead.y >= boardHeight / tileSize) newHead.y = 0;
        } else {
            if (newHead.x < 0 || newHead.y < 0 || newHead.x >= boardWidth / tileSize || newHead.y >= boardHeight / tileSize) {
                gameOver = true;
                return;
            }
        }

        // Check for collisions with the snake itself
        for (Tile part : snakeBody) {
            if (collision(newHead, part)) {
                gameOver = true;
                return;
            }
        }

        // Check for food
        if (collision(newHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        } else if (!snakeBody.isEmpty()) {
            snakeBody.add(0, new Tile(snakeHead.x, snakeHead.y));
            snakeBody.remove(snakeBody.size() - 1);
        }

        // Update head position
        snakeHead = newHead;
    }

    private boolean collision(Tile a, Tile b) {
        return a.x == b.x && a.y == b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP && velocityY == 0) {
            velocityX = 0;
            velocityY = -1;
        } else if (key == KeyEvent.VK_DOWN && velocityY == 0) {
            velocityX = 0;
            velocityY = 1;
        } else if (key == KeyEvent.VK_LEFT && velocityX == 0) {
            velocityX = -1;
            velocityY = 0;
        } else if (key == KeyEvent.VK_RIGHT && velocityX == 0) {
            velocityX = 1;
            velocityY = 0;
        } else if (key == KeyEvent.VK_SPACE && gameOver) {
            initializeGame();
            gameLoop.restart();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
