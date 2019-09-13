package nl.gamebois.wildrestore;

import java.io.*;
import org.bukkit.plugin.java.JavaPlugin;

import nl.gamebois.wildrestore.commands.ImportCommand;
import nl.gamebois.wildrestore.commands.RegenCommand;
import nl.gamebois.wildrestore.data.Database;
import nl.gamebois.wildrestore.helpers.ResourceHelper;

public class App extends JavaPlugin {
    public ResourceHelper ResourceHelper = new ResourceHelper();
    public static App app;
    public static Database db;

    @Override
    public void onEnable() {
        app = this;
        String location = getDataFolder().getAbsolutePath() + "\\" + AppConstants.DB_NAME;
        
        if (!new File(location).exists()) {
            db = CreateDB(location);
        } else {
            db = new Database(location);
        }
        db.Connect();
        
        this.getCommand("save").setExecutor(new ImportCommand());
        this.getCommand("regen").setExecutor(new RegenCommand());
    }

    @Override
    public void onDisable() {
        
    }

    private Database CreateDB(String location) {
        getDataFolder().mkdirs();
        Database db = new Database(location);
        db.Connect();
        String createScript = ResourceHelper.GetTextFromResource(AppConstants.DB_CREATE_SQL);
        db.ExecuteUpdate(createScript);
        db.Disconnect();
        return db;
    }

}
