package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class LogMenuPanel extends JPanel {
    private final JTextField name_field;
    private final JTextField host_field;
    private final JTextField port_field;

    private static final Color BG = new Color(25, 10, 15);
    private static final Color PINK_BRIGHT = new Color(255, 105, 180);
    private static final Color SAKURA_SOFT = new Color(255, 230, 235);

    public LogMenuPanel() {
        setLayout(new GridBagLayout());
        setBackground(BG);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel title = new JLabel("8-BIT-SAKURA-CHAT");
        title.setFont(ChatPanel.pixelFont.deriveFont(Font.BOLD, 16f));
        title.setForeground(PINK_BRIGHT);
        gbc.gridy = 0; gbc.insets = new Insets(0,0,40,0);
        add(title, gbc);

        name_field = createStyledField("USER_NAME");
        gbc.gridy = 1; gbc.insets = new Insets(0,0,15,0);
        add(name_field, gbc);
        name_field.setDocument(new PlainDocument(){
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if(str==null) return;
                if((getLength() + str.length())<=150) {
                    super.insertString(offs,str,a);
                }
            }
        });


        host_field = createStyledField("127.0.0.1");
        gbc.gridy = 2;
        add(host_field, gbc);

        port_field = createStyledField("8081");
        gbc.gridy = 3; gbc.insets = new Insets(15,0,0,0);
        add(port_field, gbc);
    }

    private JTextField createStyledField(String hint) {
        JTextField field = new JTextField(hint, 18);
        field.setFont(ChatPanel.pixelFont.deriveFont(Font.BOLD, 16f));
        field.setForeground(SAKURA_SOFT);
        field.setBackground(new Color(45, 20, 30));
        field.setCaretColor(PINK_BRIGHT);
        field.setBorder(BorderFactory.createLineBorder(PINK_BRIGHT, 2));
        field.setPreferredSize(new Dimension(380, 60));
        return field;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        GradientPaint gp = new GradientPaint(0, 0, new Color(40, 10, 20), 0, getHeight(), new Color(10, 5, 10));
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    public String getHost() { return host_field.getText().trim(); }
    public String getPort() { return port_field.getText().trim(); }
    public String getPlayerName() {
        return name_field.getText().trim();
    }
    public JTextField getNameField() { return name_field; }
    public JTextField getHostField() { return host_field; }
    public JTextField getPortField() { return port_field; }
    public JTextField getInputField() { return name_field; }
}
