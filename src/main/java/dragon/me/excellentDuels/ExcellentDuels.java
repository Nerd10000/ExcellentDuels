package dragon.me.excellentDuels;

import dragon.me.excellentDuels.api.DuelsApi;
import dragon.me.excellentDuels.commands.*;
import dragon.me.excellentDuels.controllers.*;
import dragon.me.excellentDuels.hooks.CombatLogXHook;
import dragon.me.excellentDuels.hooks.placeholderapi.PlaceholderExpansion;
import dragon.me.excellentDuels.listener.*;
import dragon.me.excellentDuels.utils.ConfigProvider;
import dragon.me.excellentDuels.utils.GeneralScheduler;
import dragon.me.excellentDuels.utils.inventory.PersistentInventoryManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class ExcellentDuels extends JavaPlugin {
    private  final String banner = "\u001B[33m  _____          _ _            _   ____             _     \n" +
            "                                   | ____|_  _____| | | ___ _ __ | |_|  _ \\ _   _  ___| |___ \n" +
            "                                   |  _| \\ \\/ / _ \\ | |/ _ \\ '_ \\| __| | | | | | |/ _ \\ / __|\n" +
            "                                   | |___ >  <  __/ | |  __/ | | | |_| |_| | |_| |  __/ \\__ \\\n" +
            "                                   |_____/_/\\_\\___|_|_|\\___|_| |_|\\__|____/ \\__,_|\\___|_|___/\u001B[0m";
    private static Plugin plugin;
    private static InviteController inviteController;
    private  static KitDataController kitDataController;
    private static ArenaController arenaController;
    private static GameController gameController;
    private  static InventoryController inventoryController;
    private  static PersistentInventoryManager persistentInventoryManager;
    @SneakyThrows
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
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){

            this.getLogger().info("\u001B[33m  | Hooked into PlaceholderAPI!\u001B[0m");
            new PlaceholderExpansion().register();
        }
        if (Bukkit.getPluginManager().isPluginEnabled("CombatLogX")){
            this.getLogger().info("\u001B[33m  | Hooked into CombatLogX!\u001B[0m");
            getServer().getPluginManager().registerEvents(new CombatLogXHook(),this);
        }
        this.getLogger().info("\u001B[33m  | Loading Event listeners...Done!\u001B[0m");
        registerListeners();
        this.getLogger().info("\u001B[33m  | Loading MessageProvider... Done!\u001B[0m");
        new ConfigProvider().onInit();
        inviteController = new InviteController();
        this.getLogger().info("\u001B[33m  | Loading InviteController...Done!\u001B[0m");

        persistentInventoryManager = new PersistentInventoryManager();
        this.getLogger().info("\u001B[33m  | Loading PersistentInventoryManager...Done!\u001B[0m");

        Bukkit.getScheduler().runTaskTimer(this,new GeneralScheduler(),0,20);
        this.getLogger().info("\u001B[33m  | Starting up the scheduler... Done!\u001B[0m");
        this.getLogger().info("\u001B[33m  | Starting up the KitDataController... Done!\u001B[0m");
        kitDataController = new KitDataController();

        this.getLogger().info("\u001B[33m  | Starting ArenaDataController... Done!\u001B[0m");
        arenaController = new ArenaController();
        this.getLogger().info("\u001B[33m  | Starting GameController... Done!\u001B[0m");
        gameController = new GameController();
        this.getLogger().info("\u001B[33m  | Starting up the InventoryController...\u001B[0m");
        inventoryController = new InventoryController();
        this.getLogger().info("\u001B[33m  | Loading commands...\u001B[0m");


        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register("duel",new DuelCommand());
            commands.registrar().register("exkits",new KitsCommand());
            commands.registrar().register("acceptduel", new AcceptDuelCommand());
            commands.registrar().register("exarenas", new ArenaCommand());
            commands.registrar().register("exduels", new DuelsCommand());
        });

        Bukkit.getServicesManager().register(DuelsApi.class,new ExternalDuelsApi(),this, ServicePriority.Normal);

    }
    public void registerListeners(){
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerCommandDispatchListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(),this);
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

    public static PersistentInventoryManager getPersistentInventoryManager() {
        return persistentInventoryManager;
    }

}
