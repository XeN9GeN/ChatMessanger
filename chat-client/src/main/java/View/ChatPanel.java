package View;

import javax.swing.*;
import java.awt.*;

public class ChatPanel extends JPanel {
    private final JPanel messagesContainer;
    private JTextField inputField;

    public ChatPanel() {
        setLayout(new BorderLayout());

        messagesContainer = new JPanel();
        messagesContainer.setLayout(new BoxLayout(messagesContainer, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(messagesContainer);
        add(scrollPane, BorderLayout.CENTER);

        inputField = new JTextField();
        add(inputField, BorderLayout.SOUTH);
    }

    public void addMessageComponent(ChatMSGPanel msgPanel) {
        messagesContainer.add(msgPanel);
        messagesContainer.revalidate();
        messagesContainer.repaint();

        SwingUtilities.invokeLater(() -> {
            msgPanel.scrollRectToVisible(msgPanel.getBounds());
        });

    }

    public JTextField getInputField() {
        return inputField;
    }
}
