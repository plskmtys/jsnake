package org.jsnake;

import javax.swing.JFrame;

public class SnakeGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        MainMenu mainMenu = new MainMenu(frame);
        frame.setContentPane(mainMenu); // Add the MainMenu to the frame
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(800, 800); // Set the size explicitly
        frame.setVisible(true);
        
    }
}