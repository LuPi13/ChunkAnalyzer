package yasking.lupi13.chunkanalyzer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExceptionTab implements TabCompleter {
    public static List<String> ar;
    public static List<String> blocks;
    public static void makeExceptionTab() {
        List<String> list1 = new ArrayList<>();
        list1.add("add");
        list1.add("remove");
        ar = list1;

        List<String> list2 = new ArrayList<>();
        for (String block : BlockException.blocklist) {
            list2.add(block.toLowerCase());
        }
        list2.add("all");
        blocks = list2;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], ar, completions);
        }
        else if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], blocks, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
