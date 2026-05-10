package View;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.util.List;

public class ChatPanel extends JPanel {
    private JPanel messagesPanel;
    private JPanel usersContainer;
    private JTextField inputField;
    private JButton sendButton;

    public ChatPanel() {
        initMainLayout();
        initMessagesArea();
        initUsersArea();
        initInputArea();
    }

    private void initMainLayout() {
        setLayout(new BorderLayout());
        setBackground(new Color(8, 8, 10));
    }

    private void initMessagesArea() {
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(new Color(8, 8, 10));

        JPanel alignTopMessages = new JPanel(new BorderLayout());
        alignTopMessages.setBackground(new Color(8, 8, 10));
        alignTopMessages.add(messagesPanel, BorderLayout.NORTH);

        JScrollPane messageScroll = new JScrollPane(alignTopMessages);
        messageScroll.setBorder(null);
        messageScroll.getViewport().setBackground(new Color(8, 8, 10));
        styleScrollBar(messageScroll);

        add(messageScroll, BorderLayout.CENTER);
    }

    private void initUsersArea() {
        usersContainer = new JPanel();
        usersContainer.setLayout(new BoxLayout(usersContainer, BoxLayout.Y_AXIS));
        usersContainer.setBackground(new Color(12, 12, 15));

        JPanel alignTopUsers = new JPanel(new BorderLayout());
        alignTopUsers.setBackground(new Color(12, 12, 15));
        alignTopUsers.add(usersContainer, BorderLayout.NORTH);

        JScrollPane userScroll = new JScrollPane(alignTopUsers);
        userScroll.setPreferredSize(new Dimension(180, 0));
        userScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Online", 0, 0, null, Color.WHITE));
        userScroll.getViewport().setBackground(new Color(12, 12, 15));
        styleScrollBar(userScroll);

        add(userScroll, BorderLayout.EAST);
    }

    private void initInputArea() {
        JPanel bottom = new JPanel(new BorderLayout(10, 0));
        bottom.setBackground(new Color(12, 12, 15));
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        inputField.setForeground(Color.WHITE);
        inputField.setBackground(new Color(20, 20, 25));
        inputField.setCaretColor(Color.RED);
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        sendButton = new JButton("SEND");
        sendButton.setFocusPainted(false);
        sendButton.setFont(new Font("Arial", Font.BOLD, 18));
        sendButton.setForeground(Color.WHITE);
        sendButton.setBackground(new Color(220, 20, 60));
        sendButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        bottom.add(inputField, BorderLayout.CENTER);
        bottom.add(sendButton, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);
    }

    public void addMessageComponent(ChatMSGPanel msgPanel) {
        messagesPanel.add(msgPanel);
        messagesPanel.add(Box.createVerticalStrut(10));
        messagesPanel.revalidate();
        messagesPanel.repaint();

        SwingUtilities.invokeLater(() -> {
            msgPanel.scrollRectToVisible(msgPanel.getBounds());
        });
    }

    public void updateOnlineList(List<String> names, String myName) {
        SwingUtilities.invokeLater(() -> {
            usersContainer.removeAll();
            for (String name : names) {
                usersContainer.add(new UsersPanel(name, myName));
                usersContainer.add(Box.createVerticalStrut(2));
            }
            usersContainer.revalidate();
            usersContainer.repaint();
        });
    }

    private void styleScrollBar(JScrollPane scroll) {
        scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(45, 45, 50);
                this.trackColor = new Color(20, 20, 25);
            }
            @Override
            protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
            @Override
            protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }

            private JButton createZeroButton() {
                JButton jb = new JButton();
                jb.setPreferredSize(new Dimension(0, 0));
                return jb;
            }
        });
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
    }

    public JTextField getInputField() { return inputField; }
    public JButton getSendButton() { return sendButton; }
}
