package com.github.offjaao.warps.database;

import lombok.Builder;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDatabase {

    private final Credentials credentials;

    private Connection connection;
    private boolean active;

    public MySQLDatabase(Credentials credentials) {
        this.credentials = credentials;
    }


    public void connect() {
        try {

            String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver);

            String url = "jdbc:mysql://<ip>:<port>/<database>";

            this.connection = DriverManager.getConnection(
                    url.replaceAll("<ip>", credentials.ip)
                            .replaceAll("<port>", String.valueOf(credentials.port))
                            .replaceAll("<database>", credentials.database),
                    credentials.user, credentials.password);

            DatabaseMetaData metaData = connection.getMetaData();
            System.out.println("Conectado à database " + metaData.getDatabaseProductName());
            System.out.println("Driver: " + metaData.getDriverName());

            this.active = true;

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection() {
        if (connection == null || !active) this.connect();

        return connection;
    }

    public void disconnect() {
        if (connection != null && active) {
            try {
                connection.close();
                active = false;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Builder
    public static class Credentials {
        private final String ip;
        private final int port;
        private final String user;
        private final String password;
        private final String database;
    }
}