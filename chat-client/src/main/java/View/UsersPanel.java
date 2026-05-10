package View;

import javax.swing.*;
import java.awt.*;

public class UsersPanel extends JPanel {
    public UsersPanel(String fullName, String currentUserName){
        setLayout(new BorderLayout());
        setOpaque(false);

        setPreferredSize(new Dimension(180, 45));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel nameLabel = new JLabel(fullName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        if (fullName.contains(currentUserName)) {
            nameLabel.setForeground(new Color(220, 20, 60)); // Твой ник — красный
            setBorder(BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(220, 20, 60)));
        } else if (fullName.contains("(online)")) {
            nameLabel.setForeground(new Color(46, 204, 113)); // Онлайн — зеленый
        } else {
            nameLabel.setForeground(new Color(100, 100, 105)); // Офлайн — серый
        }

        add(nameLabel, BorderLayout.CENTER);
    }
}
