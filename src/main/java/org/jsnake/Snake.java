package org.jsnake;

import java.awt.CardLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Snake extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Board gameBoard;

    public Snake() {
        initUI();
    }

    private void initUI() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        MainMenu mainMenu = new MainMenu(
            e -> showGameBoard(),
            e -> System.exit(0)
        );

        gameBoard = new Board();

        mainPanel.add(mainMenu, "MainMenu");
        mainPanel.add(gameBoard, "GameBoard");

        add(mainPanel);

        setResizable(false);
        pack();

        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }

    public void showMainMenu() {
        cardLayout.show(mainPanel, "MainMenu");
    }

    private void showGameBoard() {
        cardLayout.show(mainPanel, "GameBoard");
        gameBoard.requestFocusInWindow(); // Request focus for the game board
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame ex = new Snake();
            ex.setVisible(true);
        });
    }
}