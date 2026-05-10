package View;

import javax.swing.*;
import java.awt.*;

public class UsersPanel extends JPanel {
    public UsersPanel(String fullName, String currentUserName){
        setLayout(new BorderLayout());
        setOpaque(false);

        setPreferredSize(new Dimension(300, 65));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 10));

        JLabel nameLabel = new JLabel(fullName.toUpperCase());
        nameLabel.setFont(ChatPanel.pixelFont.deriveFont(Font.BOLD, 13f));

        if (fullName.contains(currentUserName)) {
            nameLabel.setForeground(new Color(255, 50, 100));
        } else if (fullName.contains("(online)")) {
            nameLabel.setForeground(new Color(150, 255, 150));
        } else {
            nameLabel.setForeground(new Color(255, 183, 197));
        }

        add(nameLabel, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(255, 105, 180, 40));
        g.drawLine(10, getHeight() - 1, getWidth() - 10, getHeight() - 1);
    }
}
