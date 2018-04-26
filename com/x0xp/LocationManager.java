package com.x0xp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.*;

public class LocationManager {

    public static void storeLogOffLocation(SecurePlayer sp) throws IOException {
        File file = new File("XPAuth_" + sp.getPlayer().getName() + ".txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        Writer w = new FileWriter(file, false);
        w.write(sp.getPlayer().getWorld().getName() + ":" + sp.getPlayer().getLocation().getX() + ":" + sp.getPlayer().getLocation().getY() + ":" + sp.getPlayer().getLocation().getZ());
        w.close();
    }

    public static void storeLogOffLocation(Player sp) throws IOException {
        File file = new File("XPAuth_" + sp.getName() + ".txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        Writer w = new FileWriter(file, false);
        w.write(sp.getWorld().getName() + ":" + sp.getLocation().getX() + ":" + sp.getLocation().getY() + ":" + sp.getLocation().getZ());
        w.close();
    }

    public static Location getLocation(Player sp) throws IOException {
        File file = new File("XPAuth_" + sp.getName() + ".txt");
        if (!file.exists()) {	
            file.createNewFile();
            sp.setHealth(0);
            return null;
        }
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
        BufferedReader reader = new BufferedReader(isr);
        String line = reader.readLine();
        if (line == null) {
            return sp.getLocation();
        }
        String[] locArray = line.split(":");
        return new Location(Bukkit.getServer().getWorld(locArray[0]), Double.parseDouble(locArray[1]), Double.parseDouble(locArray[2]), Double.parseDouble(locArray[3]));
    }
}
