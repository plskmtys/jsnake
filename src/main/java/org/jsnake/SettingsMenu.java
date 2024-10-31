package org.jsnake;

import javax.swing.*;
import java.awt.*;

public class SettingsMenu extends JPanel {
    private JFrame frame;

    public SettingsMenu(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());

        JButton colorChooserButton = new JButton("Válassz színt");
        colorChooserButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Válassz színt", Color.WHITE);
            // Apply the chosen color to the snake or background
        });

        add(colorChooserButton, BorderLayout.CENTER);
    }
}