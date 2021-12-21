package yasking.lupi13.chunkanalyzer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnalyzeTab implements TabCompleter {
    public static List<String> intex;
    public static List<String> booleanex;
    public static void makeAnalyzeTab() {
        List<String> list1 = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            list1.add(Integer.toString(i));
        }
        intex = list1;

        List<String> list2 = new ArrayList<>();
        list2.add("true");
        list2.add("false");
        booleanex = list2;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], intex, completions);
        }
        else if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], booleanex, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
