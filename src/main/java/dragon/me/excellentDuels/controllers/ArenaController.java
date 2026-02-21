package dragon.me.excellentDuels.controllers;

import dragon.me.excellentDuels.ExcellentDuels;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Getter
public class ArenaController {
    public static final Map<String,Arena>  arenaList = new HashMap<>();
    private final File file;
    private final FileConfiguration config;

    public ArenaController(){
        file = new File(ExcellentDuels.getPlugin().getDataFolder(), "arenas.yml");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Failed to create arenas.yml", e);
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
        loadArena();
    }





    @Getter
    @Setter
    public static class Arena {
        String name;
        List<KitDataController.Kit> availableKits;
        ItemStack arenaIcon;
        Location corner1;
        Location corner2;

        Location spawnPos1;
        Location spawnPos2;

        public Arena(String name, List<KitDataController.Kit> availableKits, ItemStack arenaIcon, Location corner1, Location corner2, Location spawnPos1, Location spawnPos2) {
            this.name = name;
            this.availableKits = (availableKits != null) ? new ArrayList<>(availableKits) : new ArrayList<>();
            this.arenaIcon = arenaIcon;
            this.corner1 = corner1;
            this.corner2 = corner2;
            this.spawnPos1 = spawnPos1;
            this.spawnPos2 = spawnPos2;
        }
    }
    private void loadArena() {
        arenaList.clear();

        if (!config.contains("arenas")) return;

        for (String key : config.getConfigurationSection("arenas").getKeys(false)) {
            String path = "arenas." + key;

            Location corner1 = config.getLocation(path + ".corner1");
            Location corner2 = config.getLocation(path + ".corner2");
            Location spawn1 = config.getLocation(path + ".spawn1");
            Location spawn2 = config.getLocation(path + ".spawn2");
            ItemStack icon = config.getItemStack(path + ".icon");

            // Load kits safely as names
            List<String> kitNames = config.getStringList(path + ".usable_kits");
            List<KitDataController.Kit> kits = new ArrayList<>();
            for (String kitName : kitNames) {
                KitDataController.Kit kit = ExcellentDuels.getKitDataController().getKitByName(kitName);
                if (kit != null) kits.add(kit);
            }

            Arena arena = new Arena(key, kits, icon, corner1, corner2, spawn1, spawn2);
            arenaList.put(key.toLowerCase(), arena);
        }
    }

    public void saveArena(Arena arena) {
        String path = "arenas." + arena.getName().toLowerCase();

        config.set(path + ".corner1", arena.getCorner1());
        config.set(path + ".corner2", arena.getCorner2());
        config.set(path + ".spawn1", arena.getSpawnPos1());
        config.set(path + ".spawn2", arena.getSpawnPos2());
        config.set(path + ".icon", arena.getArenaIcon());

        List<String> kitNames = new ArrayList<>();
        for (KitDataController.Kit kit : arena.getAvailableKits()) {
            kitNames.add(kit.getName());
        }
        config.set(path + ".usable_kits", kitNames);

        arenaList.put(arena.getName().toLowerCase(), arena);
        saveFile();
    }

    private void saveFile() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save arenas.yml", e);
        }
    }



    public void removeArena(String arenaName) {
        if (!config.contains("arenas")) return;

        String key = arenaName.toLowerCase();
        String path = "arenas." + key;

        arenaList.remove(key);

        if (config.contains(path)) {
            config.set(path, null);
            try {
                config.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
