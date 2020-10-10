package com.github.offjaao.warps.entity;

import com.github.offjaao.warps.enums.WarpCategory;
import com.github.offjaao.warps.modal.Warp;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class UserWarp {

    private final String name;
    private final UUID uniqueId;
    private List<Warp> warps;
    private int warpLimit;

    public void validateWarpLimit() {
        for (PermissionAttachmentInfo info : getPlayer().getEffectivePermissions()) {
            if (info.getPermission().startsWith("warp.create")) {
                String[] perm = info.getPermission().split("\\.");
                setWarpLimit(Integer.parseInt(perm[2]));
                break;
            }
        }
    }

    public List<Warp> getWarpsByCategory(WarpCategory category) {
        return warps.stream().filter(warp -> warp.getCategory() == category).collect(Collectors.toList());
    }

    public void addWarp(Warp warp) {
        warps.add(warp);
    }

    public void remove(Warp warp) {
        warps.remove(warp);
    }

    private Player getPlayer() {
        return Bukkit.getPlayerExact(name);
    }

}
