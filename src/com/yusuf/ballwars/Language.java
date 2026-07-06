package com.yusuf.ballwars;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class Language {
    private static HashMap<String, String> mevcutDilPaketi = new HashMap<>();

    // İlk açılışta varsayılan olarak Türkçe yükle
    static {
        dilYukle("tr");
    }

    public static void dilYukle(String dilKodu) {
        mevcutDilPaketi.clear();
        String dosyaYolu = "C:\\Users\\YUSUF\\Desktop\\java\\BallWars\\res\\" + dilKodu.toLowerCase() + ".json";
        
        try {
            File dosya = new File(dosyaYolu);
            if (!dosya.exists()) {
                System.out.println("Dil dosyasi bulunamadi: " + dosyaYolu);
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(dosya));
            String satir;
            StringBuilder jsonIcerik = new StringBuilder();
            
            while ((satir = reader.readLine()) != null) {
                jsonIcerik.append(satir);
            }
            reader.close();

            // JSON içerisindeki tırnak ve boşlukları ayıklayarak hassas temizlik
            String orjinalJson = jsonIcerik.toString();
            String[] satirlar = orjinalJson.split("\n");
            for (String s : satirlar) {
                if (s.contains(":") && !s.trim().startsWith("{") && !s.trim().startsWith("}")) {
                    int ikiNoktaIdx = s.indexOf(":");
                    String k = s.substring(0, ikiNoktaIdx).replaceAll("[\" ]", "").trim();
                    String v = s.substring(ikiNoktaIdx + 1).trim();
                    if (v.endsWith(",")) v = v.substring(0, v.length() - 1);
                    v = v.replaceAll("^\"|\"$", "").trim(); // Baştaki ve sondaki tırnakları sil
                    mevcutDilPaketi.put(k, v);
                }
            }

        } catch (Exception e) {
            System.out.println("JSON Dil dosyasi okunurken hata: " + e.getMessage());
        }
    }

    public static String get(String anahtar) {
        return mevcutDilPaketi.getOrDefault(anahtar, "[" + anahtar + "]");
    }
}
