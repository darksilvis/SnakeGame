import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 600;
        int boardHeight = boardWidth;

        // Mod seçim ekranı
        String[] difficultyOptions = {"Normal", "Hard"};
        String difficultyChoice = (String) JOptionPane.showInputDialog(
                null,
                "Select difficulty mode:",
                "Snake Game",
                JOptionPane.QUESTION_MESSAGE,
                null,
                difficultyOptions,
                difficultyOptions[0]);

        boolean hardMode = "Hard".equals(difficultyChoice);

        String[] gameModeOptions = {"Normal Mode", "Fire Mode"};
        String gameModeChoice = (String) JOptionPane.showInputDialog(
                null,
                "Select game mode:",
                "Snake Game",
                JOptionPane.QUESTION_MESSAGE,
                null,
                gameModeOptions,
                gameModeOptions[0]);

        boolean fireMode = "Fire Mode".equals(gameModeChoice);

        JFrame frame = new JFrame("Snake Game");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight, fireMode, hardMode);
        frame.add(snakeGame);
        frame.pack();
        snakeGame.requestFocus();
    }
}
