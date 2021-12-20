package yasking.lupi13.chunkanalyzer;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Analyze implements CommandExecutor {

    private ChunkAnalyzer plugin;
    public Analyze(ChunkAnalyzer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()) {
                if (args.length == 2) {
                    try {
                        int count = Integer.parseInt(args[0]);
                        boolean toggle = Boolean.parseBoolean(args[1]);
                        if (count < 0) {
                            player.sendMessage(ChatColor.RED + "Input more than 0");
                            return true;
                        }
                        int rotate = 0;
                        if (!args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false")) {
                            player.sendMessage(ChatColor.RED + "Input true/false");
                            return true;
                        }
                        int cx = player.getLocation().getChunk().getX();
                        int cz = player.getLocation().getChunk().getZ();

                        List<Chunk> chunklist = new ArrayList<>(count);
                        for (int i = 1; i <= count; i++) {
                            chunklist.add(player.getWorld().getChunkAt(cx, cz));
                            switch (rotate % 4) {
                                case 0:
                                    cx++;
                                    break;
                                case 1:
                                    cz++;
                                    break;
                                case 2:
                                    cx--;
                                    break;
                                case 3:
                                    cz--;
                                    break;
                            }
                            double sqrt = Math.sqrt(i);
                            if ((sqrt % 1 == 0) || (((i % 2) == 0) && isTriangleNumber(i / 2))) {
                                rotate += 1;
                            }
                        }
                        Map<String, Integer> blockcount = new TreeMap<>();
                        final int[] timer = {0};
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Chunk chunk = chunklist.get(timer[0]);

                                for (int x = 0; x <= 15; x++) {
                                    for (int z = 0; z <= 15; z++) {
                                        for (int y = -64; y <=320; y++) {
                                            Block block = chunk.getBlock(x, y, z);
                                            if (!block.getType().isAir()) {
                                                if (!blockcount.containsKey(block.getType().toString())) {
                                                    blockcount.put(block.getType().toString(), 1);
                                                }
                                                else {
                                                    blockcount.put(block.getType().toString(), blockcount.get(block.getType().toString()) + 1);
                                                }

                                                if (toggle) {
                                                    List<String> exception = plugin.getConfig().getStringList("exception");
                                                    if (!exception.contains(block.getType().toString())) {
                                                        block.setType(Material.AIR, false);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                timer[0] += 1;
                                if (timer[0] >= count) {
                                    List<String> pages = new ArrayList<>();
                                    StringBuilder page = new StringBuilder();
                                    int line = 1;
                                    for (String key : blockcount.keySet()) {
                                        if (line == blockcount.keySet().size()) {
                                            page.append(key.toLowerCase()).append(": ").append(blockcount.get(key)).append("\n");
                                            pages.add(page.toString());
                                            break;
                                        }
                                        if (line % 14 != 0) {
                                            page.append(key.toLowerCase()).append(": ").append(blockcount.get(key)).append("\n");
                                        }
                                        if (line % 14 == 0){
                                            page.append(key.toLowerCase()).append(": ").append(blockcount.get(key)).append("\n");
                                            pages.add(page.toString());
                                            page = new StringBuilder();
                                        }
                                        line += 1;
                                    }

                                    ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
                                    BookMeta meta = (BookMeta) book.getItemMeta();
                                    meta.setTitle(ChatColor.GREEN + "Analyze Result");
                                    meta.setAuthor(ChatColor.AQUA + "ChunkAnalyzer");
                                    meta.setPages(pages);
                                    book.setItemMeta(meta);
                                    player.getInventory().addItem(book);
                                    cancel();
                                }
                            }
                        }.runTaskTimer(plugin, 0L, 1L);
                    }
                    catch (NumberFormatException exception) {
                        player.sendMessage(ChatColor.RED + "/analyze [(int) number of chunk] [(boolean) destroy]");
                        return true;
                    }
                }
                else {
                    player.sendMessage(ChatColor.RED + "/analyze [(int) number of chunk] [(boolean) destroy]");
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

    public static boolean isTriangleNumber(int number) {
        int sum = 0;
        int num = 1;
        while (true) {
            if (number > sum) {
                sum += num;
            }
            if (number == sum) {
                return true;
            }
            if (number < sum) {
                return false;
            }
            num += 1;
        }
    }
}
