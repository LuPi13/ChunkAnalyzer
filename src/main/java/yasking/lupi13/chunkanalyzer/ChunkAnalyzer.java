package yasking.lupi13.chunkanalyzer;

import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChunkAnalyzer extends JavaPlugin {

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        getCommand("analyze").setExecutor(new Analyze(this));
        getCommand("analyze").setTabCompleter(new AnalyzeTab());
        getCommand("exception").setExecutor(new BlockException(this));
        getCommand("exception").setTabCompleter(new ExceptionTab());
        getCommand("emphasize").setExecutor(new Emphasize(this));
        getCommand("emphasize").setTabCompleter(new EmphasizeTab());
        BlockException.createBlockList();
        AnalyzeTab.makeAnalyzeTab();
        ExceptionTab.makeExceptionTab();
        EmphasizeTab.makeEmphasizeTab();

        System.out.println("Chunk Analyzer started.");
    }

    @Override
    public void onDisable() {
        System.out.println("Chunk Analyzer stopped.");
    }
}
