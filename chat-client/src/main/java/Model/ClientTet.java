package Model;

import View.ChatMSGPanel;
import View.ChatPanel;
import View.LogMenuPanel;

import java.io.IOException;

public class ClientTet {
    public static void main(String[] args) throws IOException {
        // 1. Создаем только "каркасы" интерфейса
        LogMenuPanel lmp = new LogMenuPanel();
        ChatPanel cp = new ChatPanel();

        // 2. Запускаем контроллер.
        // Теперь ОН отвечает за Socket, NetworkConnection и циклы чтения.
        new Controller(lmp, cp);

        // Больше в main ничего писать не нужно.
        // Приложение будет жить, пока открыто окно JFrame.

    }
}
