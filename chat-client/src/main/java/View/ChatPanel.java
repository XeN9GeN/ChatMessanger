package View;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.io.IOException;
import java.net.URL;

public class ChatPanel extends JPanel {
    private JPanel messagesPanel;
    private JPanel usersContainer;
    private JTextField inputField;
    private JButton sendButton;
    private Image bgImage;
    public static Font pixelFont;
    private JButton logoutButton;

    public ChatPanel() {
        loadResources();
        initMainLayout();
        initMessagesArea();
        initUsersArea();
        initInputArea();
    }

    public static void loadPixelFont() {
        try {
            InputStream is = ChatPanel.class.getResourceAsStream("/PressStart2P-Regular.ttf");

            if (is != null) {
                pixelFont = Font.createFont(Font.TRUETYPE_FONT, is);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(pixelFont);
                System.out.println("Шрифт Press Start 2P успешно загружен!");
            } else {
                System.err.println("Файл шрифта не найден в resources! Используем Monospaced.");
                pixelFont = new Font("Monospaced", Font.BOLD, 14);
            }

        } catch (Exception e) {
            e.printStackTrace();
            pixelFont = new Font("Monospaced", Font.BOLD, 12);
        }
    }
    private void loadResources() {
        try {
            URL imgUrl = getClass().getResource("/SakuraBag.png");
            if (imgUrl != null) bgImage = ImageIO.read(imgUrl);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

            double scale = Math.max((double) getWidth() / bgImage.getWidth(null), (double) getHeight() / bgImage.getHeight(null));
            int w = (int) (bgImage.getWidth(null) * scale);
            int h = (int) (bgImage.getHeight(null) * scale);
            g2.drawImage(bgImage, (getWidth()-w)/2, (getHeight()-h)/2, w, h, null);

            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void initMainLayout() { setLayout(new BorderLayout()); }

    private void initMessagesArea() {
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setOpaque(false);

        JPanel topAlign = new JPanel(new BorderLayout());
        topAlign.setOpaque(false);
        topAlign.add(messagesPanel, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(topAlign);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        styleScrollBar(scroll);

        add(scroll, BorderLayout.CENTER);
    }

    private void initUsersArea() {
        JPanel rightSidebar = new JPanel(new BorderLayout());
        rightSidebar.setOpaque(false);
        rightSidebar.setPreferredSize(new Dimension(320, 0));

        usersContainer = new JPanel();
        usersContainer.setLayout(new BoxLayout(usersContainer, BoxLayout.Y_AXIS));
        usersContainer.setOpaque(false);

        JPanel topAlign = new JPanel(new BorderLayout());
        topAlign.setOpaque(false);
        topAlign.add(usersContainer, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(topAlign);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 105, 180), 2),
                " [ONLINE] ", 0, 0,
                (ChatPanel.pixelFont.deriveFont(Font.BOLD, 16f)), new Color(255, 105, 180)));
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        styleScrollBar(scroll);

        logoutButton = new JButton("LOG OUT");
        logoutButton.setFont(ChatPanel.pixelFont.deriveFont(Font.BOLD, 14f));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(255, 50, 100));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        rightSidebar.add(scroll, BorderLayout.CENTER);
        rightSidebar.add(logoutButton, BorderLayout.SOUTH);

        add(rightSidebar, BorderLayout.EAST);
    }

    private void initInputArea() {
        JPanel bottom = new JPanel(new BorderLayout(15, 0));
        bottom.setBackground(new Color(30, 15, 20, 220));
        bottom.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        inputField = new JTextField();
        inputField.setFont(ChatPanel.pixelFont.deriveFont(Font.BOLD, 16f));
        inputField.setForeground(new Color(255, 230, 235));
        inputField.setBackground(new Color(45, 25, 30));
        inputField.setCaretColor(new Color(255, 105, 180));
        inputField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(255, 105, 180), 2), BorderFactory.createEmptyBorder(12, 15, 12, 15)));
        inputField.setDocument(new PlainDocument(){
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if(str==null) return;
                if((getLength() + str.length())<=150) {
                    super.insertString(offs,str,a);
                }
            }
        });

        sendButton = new JButton("SEND");
        sendButton.setFont(ChatPanel.pixelFont.deriveFont(Font.BOLD, 16f));
        sendButton.setForeground(Color.WHITE);
        sendButton.setBackground(new Color(255, 105, 180));
        sendButton.setBorder(BorderFactory.createEmptyBorder(10, 35, 10, 35));

        bottom.add(inputField, BorderLayout.CENTER);
        bottom.add(sendButton, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);
    }

    public void addMessageComponent(ChatMSGPanel msgPanel) {
        JPanel row = new JPanel(new FlowLayout(msgPanel.isMine() ? FlowLayout.RIGHT : FlowLayout.LEFT));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, msgPanel.getPreferredSize().height + 10));
        row.add(msgPanel);

        messagesPanel.add(row);
        messagesPanel.add(Box.createVerticalStrut(8));
        messagesPanel.revalidate();

        SwingUtilities.invokeLater(() -> {
            messagesPanel.scrollRectToVisible(new Rectangle(0, messagesPanel.getHeight(), 1, 1));
        });
    }

    public void updateOnlineList(List<String> names, String myName) {
        SwingUtilities.invokeLater(() -> {
            usersContainer.removeAll();
            for (String name : names) {
                usersContainer.add(new UsersPanel(name, myName));
            }
            usersContainer.revalidate();
            usersContainer.repaint();
        });
    }

    private void styleScrollBar(JScrollPane scroll) {
        scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() { this.thumbColor = new Color(120, 40, 60); this.trackColor = new Color(0,0,0,50); }
            @Override protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                g.setColor(thumbColor);
                g.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
            }
        });
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
    }

    public JTextField getInputField() { return inputField; }
    public JButton getSendButton() { return sendButton; }
    public JButton getLogoutButton() { return logoutButton; }
}
