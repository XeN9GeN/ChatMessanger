package View;

import javax.swing.*;
import java.awt.*;

public class UsersPanel extends JPanel {
    public UsersPanel(String name){
        setLayout(new BorderLayout());
        JLabel nameLabel = new JLabel(name);

        if (name.contains("(online)")) {
            nameLabel.setForeground(new Color(46, 204, 113)); // Зеленый
        } else {
            nameLabel.setForeground(Color.LIGHT_GRAY); // Серый
        }

        add(nameLabel, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
    }
}
