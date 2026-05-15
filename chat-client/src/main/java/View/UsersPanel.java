package View;

import javax.swing.*;
import java.awt.*;

public class UsersPanel extends JPanel {
    public UsersPanel(String fullName, String currentUserName){
        setLayout(new BorderLayout());
        //setOpaque(false);

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
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(new Color(0, 0, 0, 140));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(new Color(255, 105, 180, 100));
        g2d.drawLine(10, getHeight() - 1, getWidth() - 10, getHeight() - 1);

        g2d.dispose();
    }
}
