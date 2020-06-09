import com.fazecast.jSerialComm.SerialPort;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.Scanner;

public class Main extends JComponent implements ActionListener, MouseMotionListener {

    private JButton play = new JButton("Play");
    private JButton pause = new JButton("Pause");
    private JButton sd = new JButton("Social Distancing tips");
    private JPanel p1 = new JPanel();
    private JFrame frame = new JFrame();
    private int points = 1000;
    private int quantity = 10;
    private int xplayer = 300;
    private Timer t;
    private int[] xVirus = new int[quantity];
    private int[] yVirus = new int[quantity];
    private double line;
    private int cloudX[][] = {{177, 150}, {290, 263}, {70,43}};

    public Main() throws IOException {
        frame.setTitle("SDgame2");
        frame.add(this);
        frame.pack();
        displayDialog();
        JTextArea area = new JTextArea(1,4);
        JPanel p2 = new JPanel(new FlowLayout());
        JLabel dis = new JLabel();
        dis.setText("Distance: ");
        dis.setFont(new Font("Arial Black", Font.BOLD,20));

        area.setFont(new Font("Arial Black", Font.BOLD,20));
        area.setSize(20,50);
        area.setLocation(100,100);
        area.setLineWrap(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.CYAN);
        frame.setLocationRelativeTo(null);
        ImageIcon icon1 = new ImageIcon(new ImageIcon("media_play.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        ImageIcon icon2 = new ImageIcon(new ImageIcon("pauseIcon.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));

        p1.setLayout(new FlowLayout());
        p1.add(play);
        p1.add(pause);
        p1.add(sd);

        play.setIcon(icon1);
        pause.setIcon(icon2);

        p2.setLocation(200,200);
        p2.add(dis);
        p2.add(area);


        play.addActionListener(this);
        pause.addActionListener(this);
        sd.addActionListener(this);

        p1.setBackground(Color.GRAY);
        p2.setBackground(Color.CYAN);
        p2.setBounds(200,200,200,200);

        frame.add(p1, BorderLayout.NORTH);
        frame.add(p2, BorderLayout.CENTER);
        frame.addMouseMotionListener(this);
        frame.setVisible(true);

        SerialPort port = SerialPort.getCommPort("COM3");
        port.openPort();
        port.setComPortParameters(9600, 8, 1, 0);
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        Scanner serialReader = new Scanner(port.getInputStream());
        String f = "";
        while (serialReader.hasNextLine()) {
            try {
                line = Double.parseDouble(serialReader.nextLine()) / 1000;
                f = String.format("%.2f", line);
            } catch (Exception ex) {
                ex.getCause();
            }
            area.setText(f + " m");
        }
    }

    private void displayDialog() {
        JOptionPane.showMessageDialog(null, getPanel(), "SD Hackathon Game", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel getPanel() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("MississaugaHacks presentation");
        ImageIcon image = new ImageIcon(("7WCrK5N2_400x400.jpg"));
        label.setIcon(image);
        panel.add(label);
        return panel;
    }


    public Dimension getPreferredSize() {
        return new Dimension(600, 650);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.CYAN);
        g2d.fillRect(0, 75, 600, 625);
        Color greyWhite = new Color(0xCCD4D0);

        // cloud 1
        g2d.setColor(Color.white);
        g2d.fillRect(cloudX[0][0],80,100,20);
        g2d.fillRect(cloudX[0][1],100,150,30);
        // cloud 2
        g2d.setColor(greyWhite);
        g2d.fillRect(cloudX[1][0],250,100,20);
        g2d.fillRect(cloudX[1][1],270,150,30);
        // cloud 3
        g2d.setColor(Color.WHITE);
        g2d.fillRect(cloudX[2][0],320,100,20);
        g2d.fillRect(cloudX[2][1],340,150,30);

        int[] grassRectX = {0, 100, 200, 300, 400, 500};
        Image land = new ImageIcon("PIXEL-Grass2.JPG").getImage();
        for (int i = 0; i < grassRectX.length; i++) {
            g2d.drawImage(land, grassRectX[i], 620, 100, 70, this);
        }

        Image virus = new ImageIcon("unnamed.png").getImage();
        Image guy = new ImageIcon("StickFigue5.png").getImage();

        for (int i = 0; i < xVirus.length; i++) {
            g2d.drawImage(virus, xVirus[i], yVirus[i], 50, 50, this);

        }

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("GB18030 Bitmap", Font.BOLD, 25));

        g2d.setColor(Color.orange);
        g2d.fill3DRect(415, 40, 185, 40, true);

        g2d.setColor(Color.black);
        g2d.drawString("Points:", 435, 70);
        g2d.drawString(String.valueOf(points), 530, 70);

        // Stick Figure
        g2d.drawImage(guy, xplayer, 379, 155, 240, this);


    }




    @Override
    public void actionPerformed(ActionEvent e) {
        int xSpead = 3;
        int ySpead = 5;

        if (e.getSource() == play) {
            t = new Timer(100, this);
            t.start();
        }

        if (e.getSource() == pause) {
            t.stop();
        }
        if (e.getSource() == sd) {
            JOptionPane.showMessageDialog(p1, "1. Stay at least 6 feet (about 2 arms length) " +
                    "from other people\n2.Wash you hands frequently\n3. Wear a mask when around others\n4. Maintain a positive mental well being");

        }

        if (xplayer <= 0) {
            xplayer = 0;
        }

        if (xplayer >= 600) {
            xplayer = 600;
        }


        for (int i = 0; i < xVirus.length; i++) {
            xVirus[i] += xSpead;
            yVirus[i] += ySpead;
            if (xVirus[i] >= xplayer && xVirus[i] <= xplayer + 155 && yVirus[i] >= 379) {
                xVirus[i] = (int) (Math.random() * 500) + 1;
                yVirus[i] = (int) (Math.random() * 500) + 1;
                points -= 10;
            }

            if (yVirus[i] >= 620) {
                xVirus[i] = (int) (Math.random() * 500) + 1;
                yVirus[i] = (int) (Math.random() * 500) + 1;
            }
        }
        for (int i = 0; i < xVirus.length; i++) {
            if (points <= 0) {
                t.start();
                int a = JOptionPane.showConfirmDialog(frame, "You lost! Always maintain proper social distancing.\n" +
                        "\t\tPlay Again?");
                if (a == JOptionPane.YES_OPTION) {
                    points = 1000;
                    t.start();
                    xVirus[i] = 10;
                    yVirus[i] = 400;
                } else if (a == JOptionPane.NO_OPTION) {
                    t.stop();
                }
            }
        }

            if (line <= 0.2 && line >= 0) {
               quantity += 6;
               points -= 2;
            } else if (line <= 1 && line > 0.2) {
                quantity += 4;
                points += 2;
            } else if(line <= 1.5 && line > 1) {
                quantity += 2;
                points += 4;
            } else if(line <= 2 && line > 1.5) {
                points += 6;
            } else if (line > 2) {
                points += 8;
            }

            for (int i= 0; i < 2; i++) {
                cloudX[0][i] = cloudX[0][i] + 5;
                if (cloudX[0][1] >= 810 ) {
                    cloudX[0][1] = - 155;
                    cloudX[0][0] = - 128;
                }
            }
            for (int i = 0; i < 2; i++) {
                cloudX[1][i] = cloudX[1][i] + 7;
                if (cloudX[1][1] >= 810 ) {
                    cloudX[1][1] = - 155;
                    cloudX[1][0] = - 128;
                }
            }

            for (int i = 0;  i < 2; i++) {
                cloudX[2][i] = cloudX[2][i] + 4;
                if (cloudX[2][1] >= 810 ) {
                    cloudX[2][1] = - 155;
                    cloudX[2][0] = - 128;
                }
            }

            repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        xplayer = e.getX() - 50;
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        xplayer = e.getX() - 50;
        repaint();
    }


    public static void main(String[] args) throws IOException {
        new Main();
    }
}

