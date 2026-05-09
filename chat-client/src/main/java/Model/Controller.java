package Model;

import View.*;
import View.ChatMSGPanel;
import View.ChatPanel;
import View.LogMenuPanel;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller {
    private final LogMenuPanel logMenuPanel;
    private final ChatMSGPanel chatmsgPanel;
    private final ChatPanel chatPanel;
    private JFrame frame;

    public Controller(LogMenuPanel lmp, ChatMSGPanel cmp, ChatPanel cp) {
        logMenuPanel = lmp;
        chatmsgPanel = cmp;
        chatPanel = cp;

        setupFrame();
        setupKey();
    }

    private void setupFrame(){
        JFrame frame = new JFrame("8-BIT_CHAT");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }



    private synchronized void setupKey(){
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

            }
        });
    }

}
