package View;

import javax.swing.*;
import java.awt.*;
import java.util.List;



public class ChatPanel extends JPanel {
    private final JPanel messagesContainer;
    private final JPanel usersContainer;
    private final JTextField inputField;

    public ChatPanel() {
        setLayout(new BorderLayout());

        //msg
        messagesContainer = new JPanel();
        messagesContainer.setLayout(new BoxLayout(messagesContainer, BoxLayout.Y_AXIS));
        JPanel alignTopMessages = new JPanel(new BorderLayout());
        alignTopMessages.add(messagesContainer, BorderLayout.NORTH);
        JScrollPane messageScroll = new JScrollPane(alignTopMessages);
        add(messageScroll, BorderLayout.CENTER);

        //cписок
        usersContainer = new JPanel();
        usersContainer.setLayout(new BoxLayout(usersContainer, BoxLayout.Y_AXIS));
        usersContainer.setBackground(new Color(236, 240, 241));

        JPanel alignTopUsers = new JPanel(new BorderLayout());
        alignTopUsers.add(usersContainer, BorderLayout.NORTH);

        JScrollPane userScroll = new JScrollPane(alignTopUsers);
        userScroll.setPreferredSize(new Dimension(120, 0));
        userScroll.setBorder(BorderFactory.createTitledBorder("Online"));
        add(userScroll, BorderLayout.EAST);

        inputField = new JTextField();
        add(inputField, BorderLayout.SOUTH);
    }

    public void updateOnlineList(List<String> names) {
        SwingUtilities.invokeLater(() -> {
            usersContainer.removeAll();

            for (String name : names) {
                usersContainer.add(new UsersPanel(name));
                usersContainer.add(Box.createVerticalStrut(2));
            }
            usersContainer.revalidate();
            usersContainer.repaint();
        });
    }

    public void addMessageComponent(ChatMSGPanel msgPanel) {
        messagesContainer.add(msgPanel);
        messagesContainer.revalidate();
        messagesContainer.repaint();
        SwingUtilities.invokeLater(() -> msgPanel.scrollRectToVisible(msgPanel.getBounds()));
    }

    public JTextField getInputField() {
        return inputField;
    }
}
