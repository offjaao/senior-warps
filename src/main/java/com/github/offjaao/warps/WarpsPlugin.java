package com.github.offjaao.warps;

import com.github.offjaao.warps.database.MySQLDatabase;
import com.github.offjaao.warps.database.Storage;
import com.github.offjaao.warps.entity.cache.UserCache;
import com.github.offjaao.warps.loader.ConfigurationLoader;
import com.github.offjaao.warps.manager.WarpManager;
import com.github.offjaao.warps.minecraft.commands.CreateWarpCommand;
import com.github.offjaao.warps.minecraft.commands.WarpCommand;
import com.github.offjaao.warps.minecraft.listeners.InventoryListener;
import com.github.offjaao.warps.minecraft.listeners.PlayerConnectionListener;
import com.github.offjaao.warps.modal.cache.WarpCache;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public final class WarpsPlugin extends JavaPlugin {

    private static WarpsPlugin instance;
    private UserCache userCache;
    private WarpCache warpCache;
    private Storage storage;
    private MySQLDatabase mySQLDatabase;
    private ConfigurationLoader configurationLoader;
    private WarpManager warpManager;

    @Override
    public void onLoad() {
        instance = this;
        saveDefaultConfig();
        setupConfig();
    }

    @Override
    public void onEnable() {
        mySQLDatabase = new MySQLDatabase(MySQLDatabase.Credentials.builder()
                .ip(configurationLoader.SQL_HOST)
                .port(configurationLoader.SQL_PORT)
                .database(configurationLoader.SQL_DATABASE)
                .password(configurationLoader.SQL_PASSWORD)
                .user(configurationLoader.SQL_USERNAME)
                .build());
        storage = new Storage(mySQLDatabase);

        warpCache = new WarpCache();
        userCache = new UserCache();

        warpManager = new WarpManager();
        warpManager.loadServerWarps();

        getCommand("createwarp").setExecutor(new CreateWarpCommand());
        getCommand("warp").setExecutor(new WarpCommand());
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerConnectionListener(), this);
    }

    @Override
    public void onDisable() {
        userCache.clear();
        warpCache.clear();
    }

    private void setupConfig() {
        configurationLoader = ConfigurationLoader.builder()
                .SQL_HOST(loadString("sql.host"))
                .SQL_DATABASE(loadString("sql.database"))
                .SQL_PASSWORD(loadString("sql.password"))
                .SQL_PORT(getConfig().getInt("sql.port"))
                .SQL_USERNAME(loadString("sql.user"))
                .CREATED(loadString("messages.created"))
                .NO_PERMISSION(loadString("messages.no_permission"))
                .ALREADY_EXISTS(loadString("messages.already_exists"))
                .UPDATED_ICON(loadString("messages.icon_updated"))
                .CANCELED(loadString("messages.canceled_operation"))
                .DELETED_WARP(loadString("messages.deleted_warp"))
                .PASSWORD(loadString("messages.password"))
                .TYPE_PASSWORD(loadString("messages.type_password"))
                .SET_LORE(loadString("messages.set_lore"))
                .SET_MATERIAL(loadString("messages.set_icon"))
                .SET_NAME(loadString("messages.set_name"))
                .SET_PASSWORD(loadString("messages.set_password"))
                .build();
    }

    protected String loadString(String path) {
        return getConfig().getString(path);
    }

    public static WarpsPlugin getInstance() {
        return instance;
    }
}
