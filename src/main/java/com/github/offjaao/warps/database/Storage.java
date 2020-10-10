package com.github.offjaao.warps.database;

import com.github.offjaao.warps.enums.WarpCategory;
import com.github.offjaao.warps.modal.Warp;
import com.github.offjaao.warps.utils.LocationUtils;
import com.github.offjaao.warps.utils.ObjectString;
import com.github.offjaao.warps.utils.inventory.menu.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Storage {

    private final MySQLDatabase mySQLDatabase;

    private final ExecutorService executorService = new ThreadPoolExecutor(
            2, 4, 15, TimeUnit.SECONDS,
            new LinkedBlockingDeque<>()
    );

    public Storage(MySQLDatabase mySQLDatabase) {
        this.mySQLDatabase = mySQLDatabase;
        createTables();
    }

    private final String TABLE = "warps";
    private final String INSERT = "INSERT INTO " + TABLE + " (name, owner, password, category, location, icon) VALUES (?, ?, ?, ?, ?, ?);";
    private final String UPDATE = "UPDATE " + TABLE + " SET name = ?, owner = ?, password = ?, category = ?, location = ?, icon = ?;";
    private final String SELECT = "SELECT * FROM " + TABLE + " WHERE owner = ? AND category = ?;";
    private final String SELECT_BY_CATEGORY = "SELECT * FROM " + TABLE + " WHERE category = ?;";

    private void createTables() {
        try (PreparedStatement ps = connection().prepareStatement("CREATE TABLE IF NOT EXISTS " + TABLE + "("
                + "`id` INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT, "
                + "`name` VARCHAR(64), "
                + "`owner` VARCHAR(16), "
                + "`password` VARCHAR(64), "
                + "`category` TINYINT, "
                + "`location` VARCHAR(255), "
                + "`icon` TEXT);")) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveWarp(Warp warp) {
        executorService.submit(() -> {
            try (PreparedStatement ps = connection().prepareStatement(INSERT)) {
                ps.setString(1, warp.getName());
                ps.setString(2, warp.getOwner());
                ps.setString(3, warp.getPassword());
                ps.setInt(4, warp.getCategory().getId());
                ps.setString(5, LocationUtils.serialize(warp.getLocation()));
                ps.setString(6, new ObjectString(warp.getIcon()).getCode());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateWarp(Warp warp) {
        executorService.submit(() -> {
            try (PreparedStatement ps = connection().prepareStatement(UPDATE)) {
                ps.setString(1, warp.getName());
                ps.setString(2, warp.getOwner());
                ps.setString(3, warp.getPassword());
                ps.setInt(4, warp.getCategory().getId());
                ps.setString(5, LocationUtils.serialize(warp.getLocation()));
                ps.setString(6, new ObjectString(warp.getIcon()).getCode());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void deleteWarp(Warp warp) {
        executorService.submit(() -> {
            try (PreparedStatement ps = connection().prepareStatement("DELETE FROM " + TABLE + " WHERE owner = ? AND name = ?;")) {
                ps.setString(1, warp.getOwner());
                ps.setString(2, warp.getName());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

    public List<Warp> loadOwner(String owner, WarpCategory category) {
        List<Warp> warps = new ArrayList<>();
            try {
                PreparedStatement statement = connection().prepareStatement(SELECT);
                statement.setString(1, owner);
                statement.setInt(2, category.getId());
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    Warp warp = new Warp(
                            result.getString("name"),
                            owner,
                            result.getString("password"),
                            WarpCategory.byId(result.getInt("category")),
                            LocationUtils.deserialize(result.getString("location")),
                            (ItemStack) new ObjectString(result.getString("icon")).getObject()
                    );
                    warps.add(warp);
                }
                result.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return warps;
    }

    public List<Warp> loadCategory(WarpCategory category) {
        List<Warp> warps = new ArrayList<>();
            try {
                PreparedStatement statement = connection().prepareStatement(SELECT_BY_CATEGORY);
                statement.setInt(1, category.getId());
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    Warp warp = new Warp(
                            result.getString("name"),
                            result.getString("owner"),
                            result.getString("password"),
                            WarpCategory.byId(result.getInt("category")),
                            LocationUtils.deserialize(result.getString("location")),
                            (ItemStack) new ObjectString(result.getString("icon")).getObject()
                    );
                    warps.add(warp);
                }
                result.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return warps;
    }

    private Connection connection() {
        return mySQLDatabase.getConnection();
    }
}
