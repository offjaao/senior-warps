package com.github.offjaao.warps.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtils {

    public static String serialize(Location location) {
        if (location == null) return null;

        return location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();
    }

    public static Location deserialize(String location) {
        if (location == null) return null;

        String[] parts = location.split(":");

        World w = Bukkit.getServer().getWorld(parts[0]);
        double x = Double.parseDouble(parts[1]);
        double y = Double.parseDouble(parts[2]);
        double z = Double.parseDouble(parts[3]);
        float yaw = Float.parseFloat(parts[4]);
        float pitch = Float.parseFloat(parts[5]);
        return new Location(w, x, y, z, yaw, pitch);
    }

}
