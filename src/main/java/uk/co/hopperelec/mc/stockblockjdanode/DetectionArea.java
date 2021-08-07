package uk.co.hopperelec.mc.stockblockjdanode;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DetectionArea {
    int xa;
    int xb;
    int ya;
    int yb;
    int za;
    int zb;
    String worldname;
    List<UUID> ownersUUIDs = new ArrayList<>();
    HashMap<Player,Long> cooldowns = new HashMap<>();
    String name;

    public DetectionArea(int xa, int xb, int ya, int yb, int za, int zb, String worldname, String[] ownersUUIDs, String name) {
        this.xa = xa;
        this.xb = xb;
        this.ya = ya;
        this.yb = yb;
        this.za = za;
        this.zb = zb;
        this.worldname = worldname;
        for (String ownerUUID : ownersUUIDs) this.ownersUUIDs.add(UUID.fromString(ownerUUID));
        this.name = name;
    }

    public boolean inHouse(Location loc) {
        if (loc == null) return false;
        if (loc.getWorld() == null) return false;
        return loc.getWorld().getName().equals(worldname) && loc.getBlockX() < xa && loc.getBlockX() > xb && loc.getBlockY() < ya && loc.getBlockY() > yb && loc.getBlockZ() < za && loc.getBlockZ() > zb;
    }
}
