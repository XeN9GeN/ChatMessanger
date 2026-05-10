package Model;

import General.MSGType;
import General.Message;
import General.NetworkConnection;
import General.User;
import Utils.ClientException;
import View.*;
import View.ChatMSGPanel;
import View.ChatPanel;
import View.LogMenuPanel;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class Controller {
    private final LogMenuPanel logMenuPanel;
    private final ChatPanel chatPanel;
    private final JFrame frame;

    private NetworkConnection networkConnection;
    private User currentUser;


    public Controller(LogMenuPanel lmp, ChatPanel cp) {
        logMenuPanel = lmp;
        chatPanel = cp;

        this.frame = new JFrame("8-BIT_CHAT");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(400, 600);
        this.frame.setLocationRelativeTo(null);

        this.frame.add(logMenuPanel);
        this.frame.setVisible(true);

        setupLogic();
    }



    private void setupLogic() {
        //enter in login
        logMenuPanel.getNameField().addActionListener(e -> {
            String name = logMenuPanel.getPlayerName();
            if (!name.isEmpty()) {
                connectToServer(name);
            }
        });

        //enter in chat
        chatPanel.getInputField().addActionListener(e -> {
            String text = chatPanel.getInputField().getText();
            if (!text.isEmpty()) {
                sendMessage(text);
                chatPanel.getInputField().setText("");
            }
        });
        // Слушатель для кнопки
        chatPanel.getSendButton().addActionListener(e -> {
            String text = chatPanel.getInputField().getText();
            if (!text.isEmpty()) {
                sendMessage(text);
                chatPanel.getInputField().setText("");
            }
        });

    }

    private void connectToServer(String name) {
        String host = "127.0.0.1";
        int port = 8081;
        try{
            Socket socket = new Socket(host,port);
            this.networkConnection = new NetworkConnection(socket);

            this.currentUser = new User(name,(int) (Math.random() * 1000));
            Message loginmsg = new Message(null,currentUser, MSGType.LOGIN);
            networkConnection.sendMSG(loginmsg);

            showChat();
            startMessageListener();
        } catch (IOException e) {
            handleClientError(new ClientException.ConnectionException(host));
        }
    }

    private void startMessageListener(){
        new Thread(()->{
            try{
                while(true){
                    Object obj = networkConnection.recvMSG();
                    if (obj instanceof Message) {
                        handleIncoming((Message) obj);
                    }
                    else {
                        throw new ClientException.ProtocolException();
                    }
                }
            } catch (ClientException e) {
                handleClientError(e);
            } catch (Exception e) {
                System.out.println("Соединение разорвано");
            }
        }).start();
    }

    private void handleIncoming(Message m) {
        if (m.getOnlineUsers() != null && !m.getOnlineUsers().isEmpty()) {
            chatPanel.updateOnlineList(m.getOnlineUsers(), currentUser.getName());
        }

        if (m.getMessageType() == MSGType.TEXT || m.getMessageType() == MSGType.UPDATE_USERS) {
            String sender =m.getUser().getName();
            String text = m.getText();

            SwingUtilities.invokeLater(() -> {
                ChatMSGPanel msgWidget = new ChatMSGPanel(sender, text);
                chatPanel.addMessageComponent(msgWidget);
            });

            System.out.println("good for client from server with msg: " + m.getText());
        }
    }

    private void sendMessage(String text) {
        try {
            Message msg = new Message(text, currentUser, MSGType.TEXT);
            networkConnection.sendMSG(msg);
        } catch (IOException e) {
            handleClientError(new ClientException.ConnectionException("серверу"));
        }
    }

    private void showChat() {
        frame.remove(logMenuPanel);
        frame.add(chatPanel);
        frame.revalidate();
        frame.repaint();
        chatPanel.getInputField().requestFocusInWindow();
    }
    private void handleClientError(ClientException e) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame, e.getMessage(),
                "Ошибка", JOptionPane.ERROR_MESSAGE));
    }

}
