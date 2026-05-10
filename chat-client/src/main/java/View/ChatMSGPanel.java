package View;

import javax.swing.*;
import java.awt.*;

public class ChatMSGPanel extends JPanel {

    private final String username;
    private final String message;

    private Color accent;

    public ChatMSGPanel(String username, String message) {
        this.username = username;
        this.message = message;

        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(8,10,8,10));

        Dimension size = new Dimension(Integer.MAX_VALUE, 80);
        setPreferredSize(size);
        int parentWidth = 380;
        setMaximumSize(new Dimension(parentWidth, 90));
        setPreferredSize(new Dimension(parentWidth, 90));

        accent = getUserColor(username);
    }

    private Color getUserColor(String name) {
        if (name == null || name.isEmpty()) return Color.GRAY;

        Color[] colors = {
                new Color(255, 82, 82),   // Красный
                new Color(255, 196, 0),  // Желтый
                new Color(180, 80, 255), // Фиолетовый
                new Color(120, 255, 100),// Зеленый
                new Color(80, 160, 255), // Синий
                new Color(255, 64, 129),  // Розовый
                new Color(0, 191, 165),  // Бирюзовый
                new Color(255, 110, 64)   // Оранжевый
        };
        int index = Math.abs(name.toLowerCase().hashCode()) % colors.length;
        return colors[index];
    }


    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        int w = getWidth();
        int h = getHeight();

        Polygon bubble = new Polygon();

        bubble.addPoint(0, 15);
        bubble.addPoint(35, 0);
        bubble.addPoint(w - 20, 0);
        bubble.addPoint(w, h / 2);
        bubble.addPoint(w - 20, h);
        bubble.addPoint(15, h);
        bubble.addPoint(0, h - 15);

        g2.setColor(new Color(15,15,18));
        g2.fillPolygon(bubble);

        g2.setStroke(new BasicStroke(3f));
        g2.setColor(Color.WHITE);
        g2.drawPolygon(bubble);

        g2.setColor(accent);
        g2.fillRect(0,0,8,h);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g2.setColor(accent);

        g2.drawString(username + ":", 20, 28);

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        g2.setColor(Color.WHITE);

        g2.drawString(message, 20, 55);
    }
}