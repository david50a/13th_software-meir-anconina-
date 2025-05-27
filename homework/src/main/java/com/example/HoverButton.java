import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HoverButton extends JFrame {
    public HoverButton() {
        setTitle("Hover Button Color");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Create a button
        JButton button = new JButton("Hover Me");
        button.setBounds(90, 60, 120, 40);
        button.setBackground(Color.LIGHT_GRAY); // Default color
        button.setOpaque(true);
        button.setBorderPainted(false);

        // Add MouseListener to change color on hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.RED); // Hover color
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.LIGHT_GRAY); // Original color
            }
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(Color.YELLOW); // Pressed color
            }
        });

        add(button);
        setVisible(true);
    }

    public static void main(String[] args) {
        new HoverButton();
    }
}
