package yasking.lupi13.chunkanalyzer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmphasizeTab implements TabCompleter {
    public static List<String> blocks2;
    public static void makeEmphasizeTab() {
        List<String> list = new ArrayList<>();
        for (String block : BlockException.blocklist) {
            list.add(block.toLowerCase());
        }
        list.add("remove");
        blocks2 = list;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], blocks2, completions);
            Collections.sort(completions);
            return completions;
        }
        else {
            return null;
        }
    }
}
