package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LogMenuPanel extends JPanel {
    private final JTextField name_field;

    private static final Color BACKGROUND_COLOR = new Color(44, 62, 80); // Темно-синий
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font MAIN_FONT = new Font("Segoe UI", Font.BOLD, 20);


    //Скопировано из тетриса
    public LogMenuPanel(){
        setLayout(new GridBagLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel label = new JLabel("ENTER YOUR NAME:");
        label.setFont(MAIN_FONT);
        label.setForeground(TEXT_COLOR);
        gbc.gridy = 0;
        this.add(label, gbc);

        name_field = new JTextField(15);
        name_field.setFont(new Font("Monospaced", Font.PLAIN, 19));
        name_field.setBackground(new Color(236, 240, 241));
        name_field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        gbc.gridy = 1;
        this.add(name_field, gbc);
    }

    public String getPlayerName() {
        return name_field.getText().trim();
    }

    public JTextField getNameField() {
        return name_field;
    }

}
