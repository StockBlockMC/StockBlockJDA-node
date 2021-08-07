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

import org.bukkit.event.player.PlayerMoveEvent;
import java.util.ArrayList;
import java.util.List;

public final class StockBlockJDANode extends JavaPlugin implements Listener {
    final List<DetectionArea> detectionAreas = new ArrayList<>();

    @Override
    public void onEnable() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "stockblockjda:node");
        getServer().getPluginManager().registerEvents(this,this);

        detectionAreas.add(new DetectionArea(410,-404,105,87,81,-112,"world", new String[]{"4ee1cc2f-f517-4aee-8f74-7f3f36be22d8"},"house"));
        detectionAreas.add(new DetectionArea(53,33,88,67,-401,-421,"world",new String[]{"eac2f553-d4ac-412a-a581-0324b57463af","ae9c085a-a2f5-4f3b-947e-ece5172954ee","afd6dd77-5e95-40b3-8d40-0b40bbe87734"},"house"));
        detectionAreas.add(new DetectionArea(36,14,54,44,-393,-422,"world",new String[]{"eac2f553-d4ac-412a-a581-0324b57463af","ae9c085a-a2f5-4f3b-947e-ece5172954ee","afd6dd77-5e95-40b3-8d40-0b40bbe87734"},"vault"));
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

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        for (DetectionArea detectionArea : detectionAreas) {
            if (!detectionArea.ownersUUIDs.contains(event.getPlayer().getUniqueId()) && detectionArea.inHouse(event.getTo()) && !detectionArea.inHouse(event.getFrom())) {
                final Long cooldown = detectionArea.cooldowns.get(event.getPlayer());
                if (cooldown == null || cooldown < System.currentTimeMillis()) {
                    detectionArea.cooldowns.put(player,System.currentTimeMillis()+60000);
                    sendMessage("detectionArea|"+detectionArea.name+"|"+detectionArea.ownersUUIDs.get(0)+"|"+event.getPlayer().getDisplayName(),event.getPlayer());
                }
            }
        }
    }
}
