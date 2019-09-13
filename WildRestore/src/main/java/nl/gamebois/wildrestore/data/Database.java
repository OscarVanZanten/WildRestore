package nl.gamebois.wildrestore.data;

import java.sql.*;

import org.bukkit.Bukkit;

public class Database {
    private String filename;
    private Connection connection = null;

    public Database(String filename) {
        this.filename = filename;
    }

    public boolean Connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + filename);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean Disconnect() {
        if (connection == null) {
            return true;
        }

        try {
            if (connection.isClosed()) {
                return true;
            } else {
                connection.close();
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Connection GetConnection() {
        return connection;
    }

    public Boolean ExecuteUpdate(String sql) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        } catch (SQLException e) {
            Bukkit.getLogger().warning("SQL ERROR: " + e.getSQLState());
            return false;
        }
    }
}