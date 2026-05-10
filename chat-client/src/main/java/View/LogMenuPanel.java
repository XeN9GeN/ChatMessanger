package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class LogMenuPanel extends JPanel {

    private final JTextField name_field;

    private static final Color BG = new Color(10, 10, 14);
    private static final Color RED = new Color(220, 20, 60);
    private static final Color WHITE = new Color(245, 245, 245);

    public LogMenuPanel() {

        setLayout(new GridBagLayout());
        setBackground(BG);

        name_field = new JTextField(18);

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel title = new JLabel("ENTER CODENAME");
        title.setFont(new Font("Arial", Font.BOLD, 34));
        title.setForeground(WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,0,25,0);

        add(title, gbc);

        name_field.setFont(new Font("Segoe UI", Font.BOLD, 20));
        name_field.setForeground(WHITE);
        name_field.setCaretColor(RED);

        name_field.setBackground(new Color(20,20,25));

        name_field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(RED, 2),
                new EmptyBorder(12,15,12,15)
        ));

        name_field.setPreferredSize(new Dimension(320,55));

        gbc.gridy = 1;

        add(name_field, gbc);
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

    public String getPlayerName() {
        return name_field.getText().trim();
    }

    public JTextField getNameField() {
        return name_field;
    }
}