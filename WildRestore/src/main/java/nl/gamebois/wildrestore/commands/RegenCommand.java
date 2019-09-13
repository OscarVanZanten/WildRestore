package nl.gamebois.wildrestore.commands;
 
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.command.*;
import org.bukkit.command.CommandExecutor;
import nl.gamebois.wildrestore.*;
import nl.gamebois.wildrestore.world.*;

public class RegenCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        //ChunckHelper.SetChuncks(App.chunks, player.getWorld());
        player.sendMessage(ChatColor.GREEN + "Succes");

        return true;
    }
}