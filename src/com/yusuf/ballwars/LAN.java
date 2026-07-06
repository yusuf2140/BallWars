package com.yusuf.ballwars;

import java.io.*;
import java.net.*;

public class LAN implements Runnable {
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Main oyunPanel;
    
    public boolean bagliMi = false;
    public boolean hostMu = false;

    public LAN(Main oyunPanel) {
        this.oyunPanel = oyunPanel;
    }

    public void hostBaslat(int port) {
        this.hostMu = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(port);
                    socket = serverSocket.accept();
                    baglantiyiKur();
                } catch (Exception e) {
                    System.out.println("Host error: " + e.getMessage());
                }
            }
        }).start();
    }

    public void baglan(String ip, int port) {
        this.hostMu = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(ip, port);
                    baglantiyiKur();
                } catch (Exception e) {
                    System.out.println("Client error: " + e.getMessage());
                }
            }
        }).start();
    }

    private void baglantiyiKur() throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        bagliMi = true;
        new Thread(this).start();
    }

    // Host için: Tüm topların pozisyonlarını Client'a gönderir
    public void pozisyonlariGonder(double p1x, double p1y, double p2x, double p2y, double bx, double by, int sira) {
        if (bagliMi && out != null && hostMu) {
            out.println("POS:" + p1x + "," + p1y + "," + p2x + "," + p2y + "," + bx + "," + by + "," + sira);
        }
    }

    // Client için: Atış komutunu Host'a gönderir
    public void atisKomutuGonder(double aci, double guc) {
        if (bagliMi && out != null && !hostMu) {
            out.println("SHOT:" + aci + "," + guc);
        }
    }

    // Host için: Atış komutunu Client'a senkronize etmek amacıyla gönderir
    public void hostAtisSenkronizeEt(double aci, double guc) {
        if (bagliMi && out != null && hostMu) {
            out.println("SHOT:" + aci + "," + guc);
        }
    }

    @Override
    public void run() {
        try {
            String gelenVeri;
            while (bagliMi && (gelenVeri = in.readLine()) != null) {
                if (hostMu && gelenVeri.startsWith("SHOT:")) {
                    // Client'tan gelen atış komutunu Host işler
                    String[] parcalar = gelenVeri.substring(5).split(",");
                    double gelenAci = Double.parseDouble(parcalar[0]);
                    double gelenGuc = Double.parseDouble(parcalar[1]);
                    oyunPanel.lanAtisYap(gelenAci, gelenGuc);
                } 
                else if (!hostMu && gelenVeri.startsWith("POS:")) {
                    // Host'tan gelen pozisyonları Client doğrudan toplara eşitler
                    String[] parcalar = gelenVeri.substring(4).split(",");
                    double p1x = Double.parseDouble(parcalar[0]);
                    double p1y = Double.parseDouble(parcalar[1]);
                    double p2x = Double.parseDouble(parcalar[2]);
                    double p2y = Double.parseDouble(parcalar[3]);
                    double bx = Double.parseDouble(parcalar[4]);
                    double by = Double.parseDouble(parcalar[5]);
                    int gSira = Integer.parseInt(parcalar[6]);
                    oyunPanel.pozisyonlariGuncelle(p1x, p1y, p2x, p2y, bx, by, gSira);
                }
                else if (!hostMu && gelenVeri.startsWith("SHOT:")) {
                    // Host atış yaptığında Client'ın nişan çizgisini gizlemek/göstermek için sıra takibi
                    String[] parcalar = gelenVeri.substring(5).split(",");
                    double gelenAci = Double.parseDouble(parcalar[0]);
                    double gelenGuc = Double.parseDouble(parcalar[1]);
                    oyunPanel.lanAtisYap(gelenAci, gelenGuc);
                }
            }
        } catch (Exception e) {
            bagliMi = false;
        }
    }
}
