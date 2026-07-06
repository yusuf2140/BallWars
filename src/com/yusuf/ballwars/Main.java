package com.yusuf.ballwars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;

public class Main extends JPanel implements ActionListener, KeyListener {
    private Table masa;
    private UI arayuz;
    private Player p1, p2;
    private Ball beyazTop;
    private LAN agMotoru;
    
    private double aci = 0; 
    private double guc = 5;  
    public int sira = 1;   
    private double surtunme = 0.98;

    private int benimOyuncuId = 1; 
    private String p1Isim = "Player 1";
    private String p2Isim = "Player 2";
    
    private static boolean upnpAktif = false;
    private static int kullanılanPort = 5555;

    public Main(String kullaniciAdi, boolean hostMu, String ip, int port) {
        masa = new Table();
        arayuz = new UI();
        agMotoru = new LAN(this);
        
        if (hostMu) {
            this.p1Isim = kullaniciAdi;
            this.benimOyuncuId = 1;
            agMotoru.hostBaslat(port);
        } else {
            this.p2Isim = kullaniciAdi;
            this.benimOyuncuId = 2;
            agMotoru.baglan(ip, port);
        }

        Ball b1 = new Ball(200, 200, 30, Color.YELLOW, "1");
        Ball b2 = new Ball(200, 400, 30, Color.BLUE, "2");
        beyazTop = new Ball(500, 300, 30, Color.WHITE, "");

        p1 = new Player(1, p1Isim, b1);
        p2 = new Player(2, p2Isim, b2);

        Timer timer = new Timer(16, this);
        timer.start();
        setFocusable(true); 
        addKeyListener(this);
    }

    public void atisYap() {
        if (!agMotoru.bagliMi || sira != benimOyuncuId) return;

        Ball aktifTop = (sira == 1) ? p1.top : p2.top;
        if (agMotoru.hostMu) {
            if (Math.abs(aktifTop.vx) < 0.1 && Math.abs(aktifTop.vy) < 0.1) {
                aktifTop.vx = Math.cos(aci) * guc;
                aktifTop.vy = Math.sin(aci) * guc;
                agMotoru.hostAtisSenkronizeEt(aci, guc);
                sira = (sira == 1) ? 2 : 1;
            }
        } else {
            agMotoru.atisKomutuGonder(aci, guc);
        }
        this.requestFocusInWindow();
    }

    public void lanAtisYap(double gelenAci, double gelenGuc) {
        Ball aktifTop = (sira == 1) ? p1.top : p2.top;
        aktifTop.vx = Math.cos(gelenAci) * gelenGuc;
        aktifTop.vy = Math.sin(gelenAci) * gelenGuc;
        sira = (sira == 1) ? 2 : 1;
    }

    public void pozisyonlariGuncelle(double p1x, double p1y, double p2x, double p2y, double bx, double by, int gSira) {
        p1.top.x = p1x; p1.top.y = p1y;
        p2.top.x = p2x; p2.top.y = p2y;
        beyazTop.x = bx; beyazTop.y = by;
        this.sira = gSira;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g; 
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        masa.ciz(g2d);
        p1.top.ciz(g2d);
        p2.top.ciz(g2d);
        
        if (beyazTop.x != -100) {
            beyazTop.ciz(g2d);
        }

        arayuz.ciz(g2d, sira, guc, agMotoru.bagliMi, agMotoru.hostMu, p1Isim, p2Isim);
        
        if (sira == benimOyuncuId && agMotoru.bagliMi) { 
            Ball aktifTop = (sira == 1) ? p1.top : p2.top;
            arayuz.nisanCizgisiCiz(g2d, aktifTop, aci, guc);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (agMotoru.hostMu) {
            p1.top.hareketEt(surtunme);
            p2.top.hareketEt(surtunme);
            beyazTop.hareketEt(surtunme);

            p1.top.duvarCarpmasiKontrol(masa.maxW, masa.maxH);
            p2.top.duvarCarpmasiKontrol(masa.maxW, masa.maxH);
            beyazTop.duvarCarpmasiKontrol(masa.maxW, masa.maxH);

            masa.ikiTopCarpismaKontrol(p1.top, beyazTop);
            masa.ikiTopCarpismaKontrol(p2.top, beyazTop);
            masa.ikiTopCarpismaKontrol(p1.top, p2.top);

            if (masa.deligeDustuMu(beyazTop)) {
                beyazTop.x = -100; beyazTop.y = -100;
                beyazTop.vx = 0; beyazTop.vy = 0;
                int kazanan = (sira == 1) ? 2 : 1;
                String kazananIsim = (kazanan == 1) ? p1Isim : p2Isim;
                JOptionPane.showMessageDialog(this, "GAME OVER! " + kazananIsim.toUpperCase() + " Won!");
            }

            agMotoru.pozisyonlariGonder(p1.top.x, p1.top.y, p2.top.x, p2.top.y, beyazTop.x, beyazTop.y, sira);
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (sira != benimOyuncuId || !agMotoru.bagliMi) return;

        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) aci -= 0.05;
        if (key == KeyEvent.VK_DOWN) aci += 0.05;
        if (key == KeyEvent.VK_RIGHT) { if (guc < 15) guc += 0.5; }
        if (key == KeyEvent.VK_LEFT) { if (guc > 1) guc -= 0.5; }
        if (key == KeyEvent.VK_SPACE) atisYap();
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame lobiFrame = new JFrame("Ball Wars - Login Screen");
        lobiFrame.setSize(380, 250);
        lobiFrame.setLayout(new GridLayout(6, 2, 10, 10)); // UPnP satırı için 6 satır yapıldı
        lobiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblAd = new JLabel(" Username:");
        JTextField txtAd = new JTextField("Yusuf");
        JLabel lblIp = new JLabel(" Server IP:");
        JTextField txtIp = new JTextField("server");
        JLabel lblPort = new JLabel(" Port:");
        JTextField txtPort = new JTextField("5555");
        
        // UPnP Seçim Elemanları
        JLabel lblUpnp = new JLabel(" UPnP (Auto Port):");
        JCheckBox chkUpnp = new JCheckBox("Enable UPnP", true);

        JButton btnHost = new JButton("Create Room (Host)");
        JButton btnJoin = new JButton("Join Room (Client)");

        lobiFrame.add(lblAd); lobiFrame.add(txtAd);
        lobiFrame.add(lblIp); lobiFrame.add(txtIp);
        lobiFrame.add(lblPort); lobiFrame.add(txtPort);
        lobiFrame.add(lblUpnp); lobiFrame.add(chkUpnp);
        lobiFrame.add(btnHost); lobiFrame.add(btnJoin);
        lobiFrame.setLocationRelativeTo(null);
        lobiFrame.setVisible(true);

        btnHost.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int port = Integer.parseInt(txtPort.getText());
                kullanılanPort = port;
                
                // Eğer UPnP işaretliyse otomatik port açmayı dene
                if (chkUpnp.isSelected()) {
                    try {
                        String localIp = InetAddress.getLocalHost().getHostAddress();
                        System.out.println("Local IP: " + localIp);
                        boolean sonuc = UPnP.openPort(port, localIp);
                        if (sonuc) {
                            upnpAktif = true;
                            System.out.println("UPnP: Port " + port + " başarıyla açıldı!");
                        } else {
                            System.out.println("UPnP: Port açılamadı, modem desteklemiyor olabilir.");
                        }
                    } catch (Exception ex) {
                        System.out.println("UPnP Hatası: " + ex.getMessage());
                    }
                }
                
                baslatOyun(txtAd.getText(), true, "", port);
                lobiFrame.dispose();
            }
        });

        btnJoin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String girilenIp = txtIp.getText().trim();
                if (girilenIp.equalsIgnoreCase("server")) {
                    girilenIp = "127.0.0.1";
                }
                baslatOyun(txtAd.getText(), false, girilenIp, Integer.parseInt(txtPort.getText()));
                lobiFrame.dispose();
            }
        });
    }

    private static void baslatOyun(String ad, boolean hostMu, String ip, int port) {
        JFrame frame = new JFrame("Ball Wars - Multiplayer");
        
        // ÖNEMLİ: Oyun penceresi kapandığında portu arkasından kapatma kuralı
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (upnpAktif) {
                    UPnP.closePort(kullanılanPort);
                    System.out.println("UPnP: Port güvenli şekilde kapatıldı.");
                }
                System.exit(0);
            }
        });

        frame.setResizable(false);

        final Main oyunPanel = new Main(ad, hostMu, ip, port);
        oyunPanel.setLayout(null);
        oyunPanel.setPreferredSize(new Dimension(800, 600));

        JButton yollaButon = new JButton("FIRE 🚀");
        yollaButon.setBounds(650, 15, 110, 35);
        yollaButon.setFont(new Font("Arial", Font.BOLD, 12));
        yollaButon.setFocusable(false);

        yollaButon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oyunPanel.atisYap();
            }
        });

        oyunPanel.add(yollaButon);
        frame.add(oyunPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        oyunPanel.requestFocusInWindow();
    }
}
