package com.x0xp;

import static com.x0xp.Main.lol;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

public class PlayerChatListener extends PlayerListener {

    public PlayerChatListener() {
        Bukkit.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_CHAT, this, Event.Priority.Highest, lol);
    }
    
    @Override
    public void onPlayerChat(PlayerChatEvent e) {
    	if (!SecurePlayer.getSecurePlayer(e.getPlayer().getName()).isAuthed() && !e.getMessage().startsWith("/")) {
    		e.setCancelled(true);
    		return;
    	}
    }
}