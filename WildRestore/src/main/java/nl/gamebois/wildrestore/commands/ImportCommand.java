package nl.gamebois.wildrestore.commands;

import java.util.List;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.command.*;
import org.bukkit.command.CommandExecutor;
import nl.gamebois.wildrestore.*;
import nl.gamebois.wildrestore.data.repositories.ChunkRepository;
import nl.gamebois.wildrestore.world.*;
import nl.gamebois.wildrestore.world.models.SerialChunk;

public class ImportCommand implements CommandExecutor, Runnable
{
    private ChunkRepository chunkRepository;

    public ImportCommand()
    {
        this.chunkRepository = new ChunkRepository(App.db);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        List<SerialChunk> chunks = ChunckHelper.GetChuncks(player.getWorld());
        chunkRepository.Insert(chunks);
        player.sendMessage(ChatColor.GREEN + "Succes: ");

        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(App.app, this, 1);
        player.sendMessage(ChatColor.GREEN + "Succes: ");
        return true;
    }

    @Override
    public void run() {
        List<SerialChunk> chunks = ChunckHelper.GetChuncks(Bukkit.getWorld("world"));
        chunkRepository.Insert(chunks);
        Bukkit.getLogger().info("Done");
    }
}