package com.x0xp;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;
import java.util.ArrayList;

import static com.x0xp.Main.lol;

public class PlayerCommandListener extends PlayerListener {

    public static ArrayList<SecurePlayer> securePlayers = new ArrayList<>();

    public PlayerCommandListener() {
        Bukkit.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, this, Event.Priority.Highest, lol);
    }

    @Override
    public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent e) {
        if (e.getMessage().startsWith("/login") || e.getMessage().startsWith("/register")) {
            e.setCancelled(false);
            return;
        }
        if (!SecurePlayer.getSecurePlayer(e.getPlayer().getName()).isAuthed()) {
            e.setCancelled(true);
        }
        if (SecurePlayer.getSecurePlayer(e.getPlayer().getName()).isNeedsToRegister()) {
            e.setCancelled(true);
        }
    }
}
