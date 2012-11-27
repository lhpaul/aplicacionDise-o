package usermanager;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import usermanager.SHA1;

public class ComputerDevice extends Device implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String TAG = ComputerDevice.class.getName();

    /**
     * Constructor of ComputerDevice, sets the ip address and mac address from system resources.
     */
    public ComputerDevice(){
        super();
        
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            System.out.println(TAG + ": " + "no network device found");
            e.printStackTrace();
        }
        System.out.println("Current IP address : " + ip.getHostAddress());
        
        ipAddress = ip.getHostAddress();
        
        NetworkInterface network = null;
        try {
            network = NetworkInterface.getByInetAddress(ip);
        } catch (SocketException e) {
            System.out.println(TAG + ": " + "failed to access network interface");
            e.printStackTrace();
        }
        
        byte[] mac = null;
        try {
            mac = network.getHardwareAddress();
        } catch (SocketException e) {
            System.out.println(TAG + ": " + "failed to access mac address");
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));        
        }
        
        macAddress = sb.toString();
        
        uid = new SHA1().getHash(macAddress);
    }
}
