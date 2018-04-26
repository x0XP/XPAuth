package com.x0xp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;

public class Main extends JavaPlugin implements Listener {

    public static JavaPlugin lol;
	
    public static HashMap<String, Integer> ipAbuseMap = new HashMap<>();

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        lol = this;
        System.out.println("Enabling XPAuth");
        this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_CHAT, new PlayerChatListener(), Event.Priority.Highest, this);
        this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_MOVE, new PlayerMoveListener(), Event.Priority.Highest, this);
        this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, new PlayerJoinListener(), Event.Priority.Highest, this);
        this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_QUIT, new PlayerLeaveListener(), Event.Priority.Highest, this);
        this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_KICK, new PlayerKickListener(), Event.Priority.Highest, this);
        this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, new PlayerCommandListener(), Event.Priority.Highest, this);
    }

    public static String getPasswordFromFile(SecurePlayer sp) {
        String name = sp.getPlayer().getName();
        File file = new File("Credentials.txt");
        if (!file.exists()) {
            return null;
        }
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
            BufferedReader reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().startsWith(name.toLowerCase())) {
                    String[] arr = line.split(":");
                    return arr[1];
                }
            }
            reader.close();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void registerUser(SecurePlayer sp, String password) throws IOException {
        File file = new File("Credentials.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        Writer writer = new FileWriter(file, true);
        writer.write(sp.getPlayer().getName() + ":" + password + "\n\r");
        writer.close();
        sp.getPlayer().setHealth(20);
        sp.getPlayer().setFallDistance(0f);
    }
	
    public static boolean isAbusing(String IP, int threshold){
        for (HashMap.Entry<String, Integer> entry : ipAbuseMap.entrySet()){
            if (entry.getKey().equals(IP)){
		if (entry.getValue() >= threshold){
		return true;
		}
            }	
	}
	return false;
    }
    
    public void onPlayerMove(final PlayerMoveEvent e) {
        Location location = new Location(Bukkit.getServer().getWorld("world"), 7, 80, -34);
        if (SecurePlayer.getSecurePlayer(e.getPlayer().getName()).isNeedsToRegister()) {
            //e.getPlayer().teleport(location);
            e.setCancelled(true);
            return;
        }
        if (!SecurePlayer.getSecurePlayer(e.getPlayer().getName()).isAuthed()) {
            e.getPlayer().teleport(location);
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        Player player = (Player) sender;
        if (commandLabel.equalsIgnoreCase("register")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to exec this!");
                return true;
            }
            SecurePlayer p = SecurePlayer.getSecurePlayer(player.getName());
            if (p == null) {
                sender.sendMessage("\2474A fatal error occurred. Try relogging.");
                return true;
            }
            if (!p.isNeedsToRegister() || p.isAuthed()) {
                p.getPlayer().sendMessage("\2474You are already registered!");
                return true;
            }
            if (args.length != 1) {
                p.getPlayer().sendMessage("\247cIncorrect usage. Use /register <password>");
                return true;
            }
            if(isAbusing(p.getIP(), 5)){
                 p.getPlayer().kickPlayer("Possible account abuse detected.");
                     return true;
            }
            String psswd = args[0];
            try {
                registerUser(p, psswd);
                p.getPlayer().sendMessage("\247aRegistered successfully!");
                p.setNeedsToRegister(false);
                p.setAuthed(true);
                if (new File("XPAuth" + p.getPlayer().getName() + ".txt").exists()) {
                    p.getPlayer().teleport(LocationManager.getLocation(p.getPlayer()));
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                p.getPlayer().sendMessage("\2474A server-sided logic error occurred, please relog!");
                return true;
            }
        }
        if (commandLabel.equalsIgnoreCase("login")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to exec this!");
                return true;
            }
            SecurePlayer p = SecurePlayer.getSecurePlayer(player.getName());
            if (p == null) {
                sender.sendMessage("\2474A fatal error occurred. Try relogging.");
                return true;
            }
            if (p.isNeedsToRegister()) {
                p.getPlayer().sendMessage("\2474You need to register first!");
                return true;
            }
            if (p.isAuthed()) {
                p.getPlayer().sendMessage("\2474You are already authenticated!");
                return true;
            }
            if (args.length != 1) {
                p.getPlayer().sendMessage("\247cIncorrect usage. Use /login <password>");
                return true;
            }
            String passAttempt = args[0];
            if (getPasswordFromFile(p).equals(passAttempt)) {
                p.setAuthed(true);
                p.getPlayer().sendMessage("\247aLogged in successfully!");
                try {
                    p.getPlayer().teleport(LocationManager.getLocation(p.getPlayer()));
                } catch (IOException e) {
                    e.printStackTrace();
                    p.getPlayer().sendMessage("\2474Error whilst attempting teleportation to logout location!");
                }
                return true;
            }
            p.getPlayer().kickPlayer("Incorrect Password!!!");
        }
        return false;
    }
}
