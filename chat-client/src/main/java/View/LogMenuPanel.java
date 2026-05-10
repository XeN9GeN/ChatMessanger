package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class LogMenuPanel extends JPanel {
    private final JTextField name_field;
    private final JTextField host_field;
    private final JTextField port_field;


    private static final Color BG = new Color(10, 10, 14);
    private static final Color RED = new Color(220, 20, 60);
    private static final Color WHITE = new Color(245, 245, 245);

    public LogMenuPanel() {
        setLayout(new GridBagLayout());
        setBackground(BG);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel title = new JLabel("ACCESS TERMINAL");
        title.setFont(new Font("Arial", Font.BOLD, 34));
        title.setForeground(WHITE);
        gbc.gridy = 0; gbc.insets = new Insets(0,0,30,0);
        add(title, gbc);

        name_field = createStyledField("CODENAME");
        gbc.gridy = 1; gbc.insets = new Insets(0,0,15,0);
        add(name_field, gbc);

        host_field = createStyledField("Host");
        gbc.gridy = 2;
        add(host_field, gbc);

        port_field = createStyledField("Port");
        gbc.gridy = 3;
        add(port_field, gbc);
    }
    private JTextField createStyledField(String hint) {
        JTextField field = new JTextField(hint, 18);
        field.setFont(new Font("Segoe UI", Font.BOLD, 18));
        field.setForeground(WHITE);
        field.setBackground(new Color(20,20,25));
        field.setCaretColor(RED);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(RED, 2),
                new EmptyBorder(10,15,10,15)
        ));
        field.setPreferredSize(new Dimension(320, 50));
        return field;
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        GradientPaint gp = new GradientPaint(
                0,0,
                new Color(15,15,20),
                getWidth(),
                getHeight(),
                new Color(40,0,0)
        );

        g2.setPaint(gp);
        g2.fillRect(0,0,getWidth(),getHeight());

        g2.setColor(new Color(255,0,0,30));

        for(int i = 0; i < 20; i++) {

            int x = (int)(Math.random() * getWidth());
            int y = (int)(Math.random() * getHeight());

            Polygon p = new Polygon();

            p.addPoint(x,y);
            p.addPoint(x + 80, y + 20);
            p.addPoint(x + 20, y + 90);

            g2.fillPolygon(p);
        }
    }

    public String getHost() { return host_field.getText().trim(); }
    public String getPort() { return port_field.getText().trim(); }
    public String getPlayerName() { return name_field.getText().trim(); }
    public JTextField getNameField() { return name_field; }

    public JTextField getHostField() { return host_field; }
    public JTextField getPortField() { return port_field; }
}