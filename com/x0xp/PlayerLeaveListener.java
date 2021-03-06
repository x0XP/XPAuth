package com.x0xp;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.util.ArrayList;

import static com.x0xp.Main.lol;

public class PlayerLeaveListener extends PlayerListener {

    public static ArrayList<SecurePlayer> securePlayers = new ArrayList<>();

    public PlayerLeaveListener() {
        Bukkit.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_QUIT, this, Event.Priority.Highest, lol);
    }
    @Override
    public void onPlayerQuit(final PlayerQuitEvent e) {
        if (SecurePlayer.getSecurePlayer(e.getPlayer().getName()).isAuthed()) {
            try {
                LocationManager.storeLogOffLocation(e.getPlayer());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        PlayerJoinListener.securePlayers.remove(SecurePlayer.getSecurePlayer(e.getPlayer().getName()));
    }
}
