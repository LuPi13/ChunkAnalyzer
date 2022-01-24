package yasking.lupi13.chunkanalyzer;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
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
                        int cx2 = cx;
                        int cz2 = cz;

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
                        Map<String, Integer> BlockCount = new TreeMap<>();
                        Map<Integer, Integer> EmphasizeCount = new TreeMap<>();
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
                                                if (!BlockCount.containsKey(block.getType().toString())) {
                                                    BlockCount.put(block.getType().toString(), 1);
                                                }
                                                else {
                                                    BlockCount.put(block.getType().toString(), BlockCount.get(block.getType().toString()) + 1);
                                                }

                                                if (plugin.getConfig().getString("emphasize") != null && BlockException.blocklist.contains(plugin.getConfig().getString("emphasize").toUpperCase())) {
                                                    if (block.getType().toString().equalsIgnoreCase(plugin.getConfig().getString("emphasize"))) {
                                                        if (EmphasizeCount.containsKey(y)) {
                                                            EmphasizeCount.put(y, EmphasizeCount.get(y) + 1);
                                                        }
                                                        else {
                                                            EmphasizeCount.put(y, 1);
                                                        }
                                                    }
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
                                    List<Map.Entry<String, Integer>> CountSort = new ArrayList<>(BlockCount.entrySet());
                                    Collections.sort(CountSort, new Comparator<Map.Entry<String, Integer>>() {
                                        @Override
                                        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                            return o2.getValue().compareTo(o1.getValue());
                                        }
                                    });

                                    List<String> pages = new ArrayList<>();
                                    StringBuilder page = new StringBuilder();
                                    pages.add(ChatColor.GOLD + "\n\n\nAlphabetical order");
                                    int line = 1;
                                    for (String key : BlockCount.keySet()) {
                                        if (line == BlockCount.keySet().size()) {
                                            page.append(key.toLowerCase()).append(": ").append(BlockCount.get(key)).append("\n");
                                            pages.add(page.toString());
                                            page = new StringBuilder();
                                            break;
                                        }
                                        if (line % 14 != 0) {
                                            page.append(key.toLowerCase()).append(": ").append(BlockCount.get(key)).append("\n");
                                        }
                                        if (line % 14 == 0){
                                            page.append(key.toLowerCase()).append(": ").append(BlockCount.get(key)).append("\n");
                                            pages.add(page.toString());
                                            page = new StringBuilder();
                                        }
                                        line += 1;
                                    }

                                    line = 1;
                                    pages.add(ChatColor.GOLD + "\n\n\nNumerical order");
                                    for (Map.Entry<String, Integer> sorting : CountSort) {
                                        if (line == CountSort.size()) {
                                            page.append(sorting.getKey().toLowerCase()).append(": ").append(sorting.getValue()).append("\n");
                                            pages.add(page.toString());
                                            page =new StringBuilder();
                                            break;
                                        }
                                        if (line % 14 != 0) {
                                            page.append(sorting.getKey().toLowerCase()).append(": ").append(sorting.getValue()).append("\n");
                                        }
                                        if (line % 14 == 0) {
                                            page.append(sorting.getKey().toLowerCase()).append(": ").append(sorting.getValue()).append("\n");
                                            pages.add(page.toString());
                                            page = new StringBuilder();
                                        }
                                        line += 1;
                                    }
                                    if (plugin.getConfig().getString("emphasize") != null && BlockException.blocklist.contains(plugin.getConfig().getString("emphasize").toUpperCase())) {
                                        //Object[] mapkey = EmphasizeCount.keySet().toArray();
                                        //Arrays.sort(mapkey);
                                        //List<Map.Entry<Integer, Integer>> EmphasizeSort = new ArrayList<>(EmphasizeCount.entrySet());
                                        line = 1;
                                        pages.add(ChatColor.GOLD + "\n\n\nEmphasized Block\n    " + ChatColor.AQUA + plugin.getConfig().getString("emphasize"));
                                        for (Integer key : EmphasizeCount.keySet()) {
                                            if (line == EmphasizeCount.size()) {
                                                page.append(key).append(": ").append(EmphasizeCount.get(key)).append("\n");
                                                pages.add(page.toString());
                                                break;
                                            }
                                            if (line % 14 != 0) {
                                                page.append(key).append(": ").append(EmphasizeCount.get(key)).append("\n");
                                            }
                                            if (line % 14 == 0) {
                                                page.append(key).append(": ").append(EmphasizeCount.get(key)).append("\n");
                                                pages.add(page.toString());
                                                page = new StringBuilder();
                                            }
                                            line += 1;
                                        }
                                    }

                                    ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
                                    BookMeta meta = (BookMeta) book.getItemMeta();
                                    meta.setTitle(ChatColor.GREEN + "Analyze Result");
                                    meta.setAuthor(ChatColor.AQUA + "ChunkAnalyzer");
                                    List<String> lore = new ArrayList<>();
                                    lore.add(ChatColor.LIGHT_PURPLE + "(x: " + cx2 + ", z: " + cz2 + ", size: " + args[0] + ")");
                                    if (plugin.getConfig().getString("emphasize") != null && BlockException.blocklist.contains(plugin.getConfig().getString("emphasize").toUpperCase())) {
                                        lore.add(ChatColor.GOLD + "Emphasized " + plugin.getConfig().getString("emphasize"));
                                    }
                                    meta.setLore(lore);
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
