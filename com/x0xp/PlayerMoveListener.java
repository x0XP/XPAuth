package com.x0xp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

import static com.x0xp.Main.lol;

public class PlayerMoveListener extends PlayerListener {
	
    public PlayerMoveListener() {
        Bukkit.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_MOVE, this, Event.Priority.Highest, lol);
    }
    
    @Override
    public void onPlayerMove(final PlayerMoveEvent e) {
        Location location = new Location(Bukkit.getServer().getWorld("world"), 7, 80, -34);
        if (SecurePlayer.getSecurePlayer(e.getPlayer().getName()).isNeedsToRegister()) {
            e.setCancelled(true);
            return;
        }
        if (!SecurePlayer.getSecurePlayer(e.getPlayer().getName()).isAuthed()) {
            e.getPlayer().teleport(location);
        }
    }
           
}
