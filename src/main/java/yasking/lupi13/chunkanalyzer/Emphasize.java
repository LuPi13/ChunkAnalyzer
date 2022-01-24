package yasking.lupi13.chunkanalyzer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Emphasize implements CommandExecutor {

    private ChunkAnalyzer plugin;
    public Emphasize(ChunkAnalyzer plugin) {
        this.plugin = plugin;
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender);
            if (player.isOp()) {
                if (args.length == 1) {
                    if (BlockException.blocklist.contains(args[0].toUpperCase())) {
                        plugin.getConfig().set("emphasize", args[0]);
                        plugin.saveConfig();
                        player.sendMessage(ChatColor.GREEN + args[0] + " set to emphasize.");
                    }
                    else if (args[0].equalsIgnoreCase("remove")) {
                        plugin.getConfig().set("emphasize", null);
                        plugin.saveConfig();
                        player.sendMessage(ChatColor.GREEN + "Emphasize block removed.");
                    }
                    else {
                        player.sendMessage(ChatColor.YELLOW + args[0] + ChatColor.RED + " doesn't exist.");
                    }
                }
                else {
                    player.sendMessage(ChatColor.RED + "/emphasize [blockcode/remove]");
                }
            }
            else {
                player.sendMessage(ChatColor.RED + "You don't have permission to execute this command.");
            }
        }
        else {
            sender.sendMessage(ChatColor.RED + "You are not a Player!");
        }
        return true;
    }
}
