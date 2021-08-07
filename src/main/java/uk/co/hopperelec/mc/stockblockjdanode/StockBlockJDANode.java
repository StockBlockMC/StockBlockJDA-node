package uk.co.hopperelec.mc.stockblockjdanode;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class StockBlockJDANode extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "stockblockjda:node");
        getServer().getPluginManager().registerEvents(this,this);
    }

    public void sendMessage(String message, Player player) {
        try {
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            new DataOutputStream(stream).writeUTF(message);
            if (player == null || !player.isOnline()) {
                try {
                    player = (Player) getServer().getOnlinePlayers().toArray()[0];
                } catch (IndexOutOfBoundsException e) {
                    getLogger().warning("Could not send message `"+message+"` because there is no player online who's connection it can be sent through");
                    return;
                }
            }
            player.sendPluginMessage(this, "stockblockjda:node", stream.toByteArray());
        } catch (IOException e) {
            getLogger().warning("Could not send message `"+message+"` due to an IO error");
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        sendMessage("death|"+event.getDeathMessage(),event.getEntity());
    }

    @EventHandler
    public void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        sendMessage("advancement|"+event.getPlayer().getDisplayName()+"|"+event.getAdvancement().getKey().getKey(),event.getPlayer());
    }
}
