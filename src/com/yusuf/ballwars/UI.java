package com.yusuf.ballwars;

import java.awt.*;

public class UI {
    public void ciz(Graphics2D g2d, int sira, double guc, double aci, boolean bagliMi, boolean hostMu, String p1Isim, String p2Isim) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        
        String aktifOyuncu = (sira == 1) ? p1Isim : p2Isim;
        g2d.drawString("TURN: " + aktifOyuncu.toUpperCase(), 20, 30);
        g2d.drawString("POWER: " + (int)(guc * 10) + " / 150", 20, 55);
        g2d.drawString("ANGLE: " + (double)(aci), 20, 80);
        
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        if (bagliMi) {
            g2d.setColor(Color.GREEN);
            g2d.drawString("NETWORK: CONNECTED (" + (hostMu ? "HOST" : "CLIENT") + ")", 20, 80);
        } else {
            g2d.setColor(Color.ORANGE);
            g2d.drawString("NETWORK: WAITING FOR PLAYER...", 20, 80);
        }
    }

    public void nisanCizgisiCiz(Graphics2D g2d, Ball aktifTop, double aci, double guc) {
        int merkezX = (int)(aktifTop.x + aktifTop.cap / 2);
        int merkezY = (int)(aktifTop.y + aktifTop.cap / 2);
        int cizgiUzunluk = (int)(guc * 6);
        
        int hedefX = merkezX + (int) (Math.cos(aci) * cizgiUzunluk);
        int hedefY = merkezY + (int) (Math.sin(aci) * cizgiUzunluk);

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2)); 
        g2d.drawLine(merkezX, merkezY, hedefX, hedefY);
    }
}
