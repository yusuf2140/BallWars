package com.yusuf.ballwars;

import java.awt.*;
import java.util.Random;

public class Ball {
    public double x, y, vx, vy;
    public int cap;
    public Color renk;
    public String numara;
    private Random rastgele = new Random();

    public Ball(double x, double y, int cap, Color renk, String numara) {
        this.x = x;
        this.y = y;
        this.cap = cap;
        this.renk = renk;
        this.numara = numara;
    }

    public void hareketEt(double surtunme) {
        x += vx;
        y += vy;
        vx *= surtunme;
        vy *= surtunme;
    }

    // Duvarlara çarpınca falso/sapma alan gerçekçi fizik
    public void duvarCarpmasiKontrol(int maxW, int maxH) {
        if (x < 50) { 
            x = 50; vx = -vx; 
            vy += (rastgele.nextDouble() * 2 - 1) * Math.abs(vx) * 0.15; 
        }
        if (x > maxW - cap) { 
            x = maxW - cap; vx = -vx; 
            vy += (rastgele.nextDouble() * 2 - 1) * Math.abs(vx) * 0.15; 
        }
        if (y < 50) { 
            y = 50; vy = -vy; 
            vx += (rastgele.nextDouble() * 2 - 1) * Math.abs(vy) * 0.15; 
        }
        if (y > maxH - cap) { 
            y = maxH - cap; vy = -vy; 
            vx += (rastgele.nextDouble() * 2 - 1) * Math.abs(vy) * 0.15; 
        }
    }

    public void ciz(Graphics2D g2d) {
        g2d.setColor(renk);
        g2d.fillOval((int)x, (int)y, cap, cap);
        if (!numara.isEmpty()) {
            g2d.setColor(renk == Color.YELLOW ? Color.BLACK : Color.WHITE);
            g2d.drawString(numara, (int)x + 11, (int)y + 20);
        }
    }
}
