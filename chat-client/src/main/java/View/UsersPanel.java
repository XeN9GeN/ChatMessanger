package View;

import javax.swing.*;
import java.awt.*;

public class UsersPanel extends JPanel {
    public UsersPanel(String name){
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2, 5, 2, 5),
                BorderFactory.createLineBorder(new Color(210, 214, 222), 1)
        ));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(nameLabel, BorderLayout.CENTER);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
    }
}
