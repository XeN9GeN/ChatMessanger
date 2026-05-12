package View;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class ChatMSGPanel extends JPanel {
    private final String username;
    private final String message;
    private final boolean isMine;
    private Color accent;
    private JTextArea textArea;

    public ChatMSGPanel(String username, String message, String currentUserName) {
        this.username = username;
        this.message = message;
        this.isMine = username != null && currentUserName != null &&
                username.trim().equalsIgnoreCase(currentUserName.trim());

        setOpaque(false);
        setLayout(new BorderLayout());

        textArea = new JTextArea(message);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(false);
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setForeground(Color.WHITE);

        Font f = ChatPanel.pixelFont != null ? ChatPanel.pixelFont.deriveFont(Font.PLAIN, 12f) : new Font("Monospaced", Font.PLAIN, 12);
        textArea.setFont(f);

        int fixedWidth = 380;
        int horizontalPadding = 40;
        textArea.setSize(new Dimension(fixedWidth - horizontalPadding, Short.MAX_VALUE));

        JPanel textWrapper = new JPanel(new BorderLayout());
        textWrapper.setOpaque(false);
        textWrapper.setBorder(BorderFactory.createEmptyBorder(45, 20, 15, 20));
        textWrapper.add(textArea, BorderLayout.CENTER);
        add(textWrapper, BorderLayout.CENTER);

        int textHeight = textArea.getPreferredSize().height;
        int finalHeight = Math.max(100, textHeight + 65);

        Dimension size = new Dimension(fixedWidth, finalHeight);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        accent = getUserColor(username);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2.setColor(new Color(20, 10, 15, 140));
        g2.fillRect(5, 5, w - 10, h - 10);

        g2.setColor(new Color(255, 105, 180));
        g2.setStroke(new BasicStroke(2.5f));
        g2.drawRect(5, 5, w - 10, h - 10);

        if (ChatPanel.pixelFont != null) {
            g2.setFont(ChatPanel.pixelFont.deriveFont(Font.BOLD, 14f));
        } else {
            g2.setFont(new Font("Monospaced", Font.BOLD, 14));
        }

        g2.setColor(accent);
        g2.drawString(username.toUpperCase(), 20, 35);
    }

    private Color getUserColor(String name) {
        Color[] colors = {new Color(255, 150, 180), new Color(255, 220, 240), new Color(220, 180, 255), new Color(255, 120, 150)};
        int index = Math.abs(name != null ? name.hashCode() : 0) % colors.length;
        return colors[index];
    }


    public boolean isMine() { return isMine; }
}
