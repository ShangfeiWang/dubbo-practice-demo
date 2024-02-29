package com.wsf.component.utils;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * @author wangsf
 */
public class InetIpUtil {

    public static String getLocalAddress() {
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            List<String> ipv4Result = new ArrayList<>();
            List<String> ipv6Result = new ArrayList<>();

            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = enumeration.nextElement();
                Enumeration<InetAddress> en = networkInterface.getInetAddresses();

                while (en.hasMoreElements()) {
                    InetAddress address = en.nextElement();
                    if (!address.isLoopbackAddress()) {
                        if (address instanceof Inet6Address) {
                            ipv6Result.add(normalizeHostAddress(address));
                        } else {
                            ipv4Result.add(normalizeHostAddress(address));
                        }
                    }
                }
            }

            if (!ipv4Result.isEmpty()) {
                Iterator<String> var8 = ipv4Result.iterator();

                String ip;
                do {
                    if (!var8.hasNext()) {
                        return ipv4Result.get(ipv4Result.size() - 1);
                    }

                    ip = var8.next();
                } while (ip.startsWith("127.0") || ip.startsWith("192.168"));

                return ip;
            } else if (!ipv6Result.isEmpty()) {
                return ipv6Result.get(0);
            } else {
                InetAddress localHost = InetAddress.getLocalHost();
                return normalizeHostAddress(localHost);
            }
        } catch (Exception var6) {
            return "127.0.0.1";
        }
    }

    private static String normalizeHostAddress(InetAddress localHost) {
        return localHost instanceof Inet6Address ? "[" + localHost.getHostAddress() + "]" : localHost.getHostAddress();
    }

}
