package yasking.lupi13.chunkanalyzer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BlockException implements CommandExecutor {

    private ChunkAnalyzer plugin;
    public BlockException(ChunkAnalyzer plugin) {
        this.plugin = plugin;
    }

    public static List<String> blocklist = new ArrayList<>();
    public static void createBlockList() {
        List<Material> list = new ArrayList<>();
        for (Material block : Material.values()) {
            if (block.isBlock()) {
                list.add(block);
            }
        }
        for (Material comp : list) {
            blocklist.add(comp.toString());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()) {
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("add")) {
                        if (blocklist.contains(args[1].toUpperCase())) {
                            List<String> list = plugin.getConfig().getStringList("exception");
                            if (!list.contains(args[1].toUpperCase())) {
                                list.add(args[1].toUpperCase());
                                plugin.getConfig().set("exception", list);
                                plugin.saveConfig();
                                player.sendMessage(ChatColor.GREEN + args[1] + " added.");
                            }
                            else {
                                player.sendMessage(ChatColor.YELLOW + args[1] + ChatColor.RED + " already exist.");
                            }
                        }
                        else {
                            player.sendMessage(ChatColor.YELLOW + args[1] + ChatColor.RED + " is not valid block.");
                        }
                    }
                    else if (args[0].equalsIgnoreCase("remove")) {
                        if (plugin.getConfig().getStringList("exception").contains(args[1].toUpperCase())) {
                            List<String> list = plugin.getConfig().getStringList("exception");
                            list.remove(args[1].toUpperCase());
                            plugin.getConfig().set("exception", list);
                            plugin.saveConfig();
                            player.sendMessage(ChatColor.GREEN + args[1] + " removed.");
                        }
                        else if (args[1].equalsIgnoreCase("all")) {
                            List<String> list = plugin.getConfig().getStringList("exception");
                            list.clear();
                            plugin.getConfig().set("exception", list);
                            plugin.saveConfig();
                            player.sendMessage(ChatColor.GREEN + "All exceptions removed.");
                        }
                        else {
                            player.sendMessage(ChatColor.YELLOW + args[1] + ChatColor.RED + " doesn't exist.");
                        }
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "Input add/remove");
                    }
                }
                else {
                    player.sendMessage(ChatColor.RED + "/exception [add/remove] [blockcode/all]");
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
