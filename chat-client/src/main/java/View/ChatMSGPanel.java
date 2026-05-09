package View;

import javax.swing.*;
import java.awt.*;

public class ChatMSGPanel extends JPanel {

    public ChatMSGPanel(String name, String text) {
        setLayout(new BorderLayout(5, 2));

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5), // Отступ снаружи
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1) // Рамка "квадрата"
        ));

        setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        nameLabel.setForeground(new Color(41, 128, 185)); // Синий ник

        JLabel msgLabel = new JLabel(text);
        msgLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        add(nameLabel, BorderLayout.NORTH);
        add(msgLabel, BorderLayout.CENTER);

        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));
    }
}
