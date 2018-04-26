package com.x0xp;

import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class SecurePlayer {

    private boolean isAuthed;
    private Player player;
    private boolean needsToRegister;
	private String IP;

    public SecurePlayer(Player player) {
		this.IP = player.getAddress().getHostString();
        File file = new File("Credentials.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        isAuthed = false;
        this.player = player;
        if (Main.getPasswordFromFile(this) == null) {
            //Player is new to the server and has never registered
            needsToRegister = true;
            return;
        }
        needsToRegister = false;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isAuthed() {
        return isAuthed;
    }

    public boolean isNeedsToRegister() {
        return this.needsToRegister;
    }

    public void setNeedsToRegister(boolean needsToRegister) {
        this.needsToRegister = needsToRegister;
    }

    public void setAuthed(boolean authed) {
        isAuthed = authed;
    }

    public static SecurePlayer getSecurePlayer(String name) {
        for (SecurePlayer sp : PlayerJoinListener.securePlayers) {
            if (sp.getPlayer().getName().equalsIgnoreCase(name)) {
                return sp;
            }
        }
        return null;
    }
	public String getIP(){
		return this.IP;
	}
}
