package Model;

import View.ChatPanel;
import View.LogMenuPanel;

import java.io.IOException;

public class ClientTest {
    public static void main(String[] args) throws IOException {
        View.ChatPanel.loadPixelFont();
        LogMenuPanel lmp = new LogMenuPanel();
        ChatPanel cp = new ChatPanel();
        new Controller(lmp, cp);
    }
}
