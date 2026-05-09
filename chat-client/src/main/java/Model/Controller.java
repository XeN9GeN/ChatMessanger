package Model;

import General.MSGType;
import General.Message;
import General.NetworkConnection;
import General.User;
import View.*;
import View.ChatMSGPanel;
import View.ChatPanel;
import View.LogMenuPanel;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Controller {
    private final LogMenuPanel logMenuPanel;
    private final ChatPanel chatPanel;
    private JFrame frame;

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
    }

    private void connectToServer(String name) {
        try{
            Socket socket = new Socket("127.0.0.1",8081);
            this.networkConnection = new NetworkConnection(socket);

            this.currentUser = new User(name,(int) (Math.random() * 1000));
            Message loginmsg = new Message(null,currentUser, MSGType.LOGIN);
            networkConnection.sendMSG(loginmsg);

            showChat();
            startMessageListener();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void startMessageListener(){
        new Thread(()->{
            try{
                while(true){
                    Message resp = (Message) networkConnection.recvMSG();
                    handleIncoming(resp);
                }
            } catch (IOException | ClassNotFoundException e) { throw new RuntimeException(e); }
        }).start();
    }

    private void handleIncoming(Message m) {
        if (m.getMessageType() == MSGType.TEXT || m.getMessageType() == MSGType.UPDATE_USERS) {
            String sender =m.getUser().getName();
            String text = "Вошел в чат";

            SwingUtilities.invokeLater(() -> {
                ChatMSGPanel msgWidget = new ChatMSGPanel(sender, text);
                chatPanel.addMessageComponent(msgWidget);
            });
        }
    }

    private void sendMessage(String text) {
        try {
            Message msg = new Message(text, currentUser, MSGType.TEXT);
            networkConnection.sendMSG(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showChat() {
        frame.remove(logMenuPanel);
        frame.add(chatPanel);
        frame.revalidate();
        frame.repaint();
        chatPanel.getInputField().requestFocusInWindow();
    }


}
