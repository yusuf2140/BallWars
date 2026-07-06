package com.yusuf.ballwars;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;

public class Table {
    public int maxW = 751;
    public int maxH = 520;
    public int delikX = 700;
    public int delikY = 450;
    public int delikCap = 60;
    private Image arkaPlanResmi;

    public Table() {
        try {
            File resimDosyasi = new File("C:\\Users\\YUSUF\\Desktop\\java\\BallWars\\res\\table_prototype.png");
            if (resimDosyasi.exists()) {
                arkaPlanResmi = ImageIO.read(resimDosyasi);
            }
        } catch (Exception e) {
            System.out.println("Image loading error: " + e.getMessage());
        }
    }

    public void ciz(Graphics2D g2d) {
        if (arkaPlanResmi != null) {
            g2d.drawImage(arkaPlanResmi, 0, 0, 800, 600, null);
        } else {
            g2d.setColor(new Color(34, 139, 34));
            g2d.fillRect(0, 0, 800, 600);
        }
        g2d.setColor(Color.BLACK);
        g2d.fillOval(delikX, delikY, delikCap, delikCap);
    }

    public void ikiTopCarpismaKontrol(Ball b1, Ball b2) {
        if (b1.x == -100 || b2.x == -100) return;
        
        double dx = b2.x - b1.x;
        double dy = b2.y - b1.y;
        double mesafe = Math.sqrt(dx * dx + dy * dy);

        if (mesafe < b1.cap) {
            double nx = dx / mesafe;
            double ny = dy / mesafe;
            
            // Üst üste yapışmayı önlemek için topları hafifçe geri itiyoruz
            double overlap = b1.cap - mesafe;
            b1.x -= nx * overlap * 0.5;
            b1.y -= ny * overlap * 0.5;
            b2.x += nx * overlap * 0.5;
            b2.y += ny * overlap * 0.5;

            // Hız değişimi (Momentum aktarımı)
            double kx = b1.vx - b2.vx;
            double ky = b1.vy - b2.vy;
            double p = nx * kx + ny * ky;

            if (p > 0) {
                b1.vx -= nx * p;
                b1.vy -= ny * p;
                b2.vx += nx * p;
                b2.vy += ny * p;
            }
        }
    }

    public boolean deligeDustuMu(Ball beyazTop) {
        if (beyazTop.x == -100) return false;
        double delikDx = (delikX + delikCap / 2) - (beyazTop.x + beyazTop.cap / 2);
        double delikDy = (delikY + delikCap / 2) - (beyazTop.y + beyazTop.cap / 2);
        return Math.sqrt(delikDx * delikDx + delikDy * delikDy) < delikCap / 2;
    }
}
