package com.yusuf.ballwars;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class UPnP {
    private static String gatewayUrl = null;
    private static String serviceType = "urn:schemas-upnp-org:service:WANIPConnection:1";

    // Modemi ağda arayan ve bulan metot (SSDP Protokolü)
    public static boolean discover() {
        try {
            String discoverMessage = "M-SEARCH * HTTP/1.1\r\n" +
                    "HOST: 239.255.255.250:1900\r\n" +
                    "ST: urn:schemas-upnp-org:device:InternetGatewayDevice:1\r\n" +
                    "MAN: \"ssdp:discover\"\r\n" +
                    "MX: 2\r\n\r\n";

            InetAddress multicastAddr = InetAddress.getByName("239.255.255.250");
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(2500);

            byte[] txData = discoverMessage.getBytes();
            DatagramPacket txPacket = new DatagramPacket(txData, txData.length, multicastAddr, 1900);
            socket.send(txPacket);

            byte[] rxData = new byte[2048];
            DatagramPacket rxPacket = new DatagramPacket(rxData, rxData.length);
            socket.receive(rxPacket);

            String response = new String(rxPacket.getData(), 0, rxPacket.getLength());
            socket.close();

            StringTokenizer st = new StringTokenizer(response, "\r\n");
            while (st.hasMoreTokens()) {
                String line = st.nextToken();
                if (line.toUpperCase().startsWith("LOCATION:")) {
                    gatewayUrl = line.substring(9).trim();
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("UPnP Modem bulunamadı: " + e.getMessage());
        }
        return false;
    }

    // Port Açma Komutu (SOAP XML Gönderimi)
    public static boolean openPort(int port, String localIp) {
        if (gatewayUrl == null && !discover()) return false;
        try {
            String soapBody = "<u:AddPortMapping xmlns:u=\"" + serviceType + "\">" +
                    "<NewRemoteHost></NewRemoteHost>" +
                    "<NewExternalPort>" + port + "</NewExternalPort>" +
                    "<NewProtocol>TCP</NewProtocol>" +
                    "<NewInternalPort>" + port + "</NewInternalPort>" +
                    "<NewInternalClient>" + localIp + "</NewInternalClient>" +
                    "<NewEnabled>1</NewEnabled>" +
                    "<NewPortMappingDescription>BallWars</NewPortMappingDescription>" +
                    "<NewLeaseDuration>0</NewLeaseDuration>" +
                    "</u:AddPortMapping>";

            return sendSoapAction("AddPortMapping", soapBody);
        } catch (Exception e) {
            return false;
        }
    }

    // Oyun kapandığında portu kapatma komutu
    public static boolean closePort(int port) {
        if (gatewayUrl == null) return false;
        try {
            String soapBody = "<u:DeletePortMapping xmlns:u=\"" + serviceType + "\">" +
                    "<NewRemoteHost></NewRemoteHost>" +
                    "<NewExternalPort>" + port + "</NewExternalPort>" +
                    "<NewProtocol>TCP</NewProtocol>" +
                    "</u:DeletePortMapping>";

            return sendSoapAction("DeletePortMapping", soapBody);
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean sendSoapAction(String action, String body) throws Exception {
        URL url = new URL(gatewayUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "text/xml; charset=\"utf-8\"");
        conn.setRequestProperty("SOAPACTION", "\"" + serviceType + "#" + action + "\"");

        String soapRequest = "<?xml version=\"1.0\"?><s:Envelope xmlns:s=\"http://xmlsoap.org\" " +
                "s:encodingStyle=\"http://xmlsoap.org\"><s:Body>" + body + "</s:Body></s:Envelope>";

        OutputStream os = conn.getOutputStream();
        os.write(soapRequest.getBytes("UTF-8"));
        os.flush();
        os.close();

        int rc = conn.getResponseCode();
        conn.disconnect();
        return rc == 200;
    }
}
