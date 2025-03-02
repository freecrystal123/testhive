package swingpkg.components;
import javax.swing.*;
import javax.swing.plaf.ComboBoxUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class roundedComboBox extends JComboBox<String> {

    private boolean isPressed = false;   // Track if the component is pressed
    private boolean isHovered = false;   // Track if the component is hovered

    public roundedComboBox(String[] items) {
        super(items);
        setFont(new Font("Arial", Font.BOLD, 16)); // Set font size
        setBackground(Color.white); // Set background color
        setForeground(Color.black); // Set foreground color
        setFocusable(false); // Prevent focusing

        // Add mouse listener to track hover and press states
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint(); // Repaint when button is pressed
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint(); // Repaint when button is released
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint(); // Repaint when mouse enters
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint(); // Repaint when mouse exits
            }
        });

        // Set a custom renderer to paint the items with specific colors
        setRenderer(new ComboBoxRenderer());
    }

    // Custom renderer to paint combo box items
    private class ComboBoxRenderer extends JLabel implements ListCellRenderer<String> {
        public ComboBoxRenderer() {
            setOpaque(true);
            setFont(new Font("Arial", Font.BOLD, 16)); // Set font for items
            setForeground(Color.black); // Set text color
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value);
            if (isSelected) {
                setBackground(Color.lightGray); // Highlight selected item
            } else {
                setBackground(Color.white); // Default background for unselected items
            }
            return this;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Determine the color to use based on the state (pressed, hover, etc.)
        if (isPressed) {
            g.setColor(Color.darkGray); // Color when pressed
        } else if (isHovered) {
            g.setColor(Color.lightGray); // Color when hovered
        } else {
            g.setColor(Color.white); // Default color
        }

        // Draw the rounded rectangle background
        g.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight() / 2, getHeight() / 2);

        // Ensure text is drawn on top of the custom background
        super.paintComponent(g);
    }

    @Override
    public void setUI(ComboBoxUI ui) {
        super.setUI(ui);
    }
}