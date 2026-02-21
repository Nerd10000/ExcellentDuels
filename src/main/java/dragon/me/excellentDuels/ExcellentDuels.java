package dragon.me.excellentDuels;

import dragon.me.excellentDuels.commands.*;
import dragon.me.excellentDuels.controllers.*;
import dragon.me.excellentDuels.listener.PlayerCommandDispatchListener;
import dragon.me.excellentDuels.listener.PlayerDeathListener;
import dragon.me.excellentDuels.listener.PlayerMoveListener;
import dragon.me.excellentDuels.utils.ConfigProvider;
import dragon.me.excellentDuels.utils.GeneralScheduler;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class ExcellentDuels extends JavaPlugin {
    private  final String banner = "  _____          _ _            _   ____             _     \n" +
            "                                   | ____|_  _____| | | ___ _ __ | |_|  _ \\ _   _  ___| |___ \n" +
            "                                   |  _| \\ \\/ / _ \\ | |/ _ \\ '_ \\| __| | | | | | |/ _ \\ / __|\n" +
            "                                   | |___ >  <  __/ | |  __/ | | | |_| |_| | |_| |  __/ \\__ \\\n" +
            "                                   |_____/_/\\_\\___|_|_|\\___|_| |_|\\__|____/ \\__,_|\\___|_|___/";
    private static Plugin plugin;
    private static InviteController inviteController;
    private  static KitDataController kitDataController;
    private static ArenaController arenaController;
    private static GameController gameController;
    private  static InventoryController inventoryController;
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        this.getLogger().info(
                banner
        );

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }


        File kitsFile = new File(getDataFolder(), "kits.yml");
        if (!kitsFile.exists()) {
            try {
                kitsFile.createNewFile();
            } catch (IOException e) {
                getLogger().severe("Could not create kits.yml!");
                e.printStackTrace();
            }
        }

        saveDefaultConfig();
        this.getLogger().info("Loading internals...");

        this.getLogger().info("Loading Event listeners...Done!");
        registerListeners();
        this.getLogger().info("Loading MessageProvider... Done!");
        new ConfigProvider().onInit();
        inviteController = new InviteController();
        this.getLogger().info("Loading InviteController...Done!");
        Bukkit.getScheduler().runTaskTimer(this,new GeneralScheduler(),0,20);
        this.getLogger().info("Starting up the scheduler... Done!");
        this.getLogger().info("Starting up the KitDataController... Done!");
        kitDataController = new KitDataController();

        this.getLogger().info("Starting ArenaDataController... Done!");
        arenaController = new ArenaController();
        this.getLogger().info("Starting GameController... Done!");
        gameController = new GameController();
        this.getLogger().info("Starting up the InventoryController...");
        inventoryController = new InventoryController();
        this.getLogger().info("Loading commands...");


        ArenaController.arenaList.forEach((s, arena) ->
                this.getLogger().info("DE: Arena: " + s + "; Kits:" + arena.getAvailableKits().stream()
                        .map(kit -> kit.getName()).collect(java.util.stream.Collectors.joining(", ")) + " has been loaded!"));

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register("duel",new DuelCommand());
            commands.registrar().register("exkits",new KitsCommand());
            commands.registrar().register("acceptduel", new AcceptDuelCommand());
            commands.registrar().register("exarenas", new ArenaCommand());
            commands.registrar().register("exduels", new DuelsCommand());
        });

    }
    public void registerListeners(){
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerCommandDispatchListener(),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static Plugin getPlugin() {
        return plugin;
    }

    public static InviteController getInviteController() {
        return inviteController;
    }

    public static KitDataController getKitDataController() {
        return kitDataController;
    }

    public static ArenaController getArenaController(){
        return arenaController;
    }

    public static GameController getGameController() {
        return gameController;
    }

    public static InventoryController getInventoryController() {
        return inventoryController;
    }
}
