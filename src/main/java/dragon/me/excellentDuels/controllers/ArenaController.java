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
import java.util.*;

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

        List<Location> spawnPositions;

        public Arena(String name, List<KitDataController.Kit> availableKits, ItemStack arenaIcon, Location corner1, Location corner2, List<Location> spawnPositions) {
            this.name = name;
            this.availableKits = (availableKits != null) ? new ArrayList<>(availableKits) : new ArrayList<>();
            this.arenaIcon = arenaIcon;
            this.corner1 = corner1;
            this.corner2 = corner2;
            this.spawnPositions = spawnPositions;
        }
    }
    private void loadArena() {
        arenaList.clear();

        if (!config.contains("arenas")) return;

        for (String key : config.getConfigurationSection("arenas").getKeys(false)) {
            String path = "arenas." + key;

            Location corner1 = config.getLocation(path + ".corner1");
            Location corner2 = config.getLocation(path + ".corner2");
            ItemStack icon = config.getItemStack(path + ".icon");

            List<Location> spawnPositions = new ArrayList<>();
            if (config.isList(path + ".spawn_positions")) {
                spawnPositions.addAll((Collection<? extends Location>) config.getList(path + ".spawn_positions", new ArrayList<Location>()));
            } else {
                Location spawn1 = config.getLocation(path + ".spawn1");
                Location spawn2 = config.getLocation(path + ".spawn2");
                if (spawn1 != null) {
                    spawnPositions.add(spawn1);
                }
                if (spawn2 != null) {
                    spawnPositions.add(spawn2);
                }
            }

            List<String> kitNames = config.getStringList(path + ".usable_kits");
            List<KitDataController.Kit> kits = new ArrayList<>();
            for (String kitName : kitNames) {
                KitDataController.Kit kit = ExcellentDuels.getKitDataController().getKitByName(kitName);
                if (kit != null) kits.add(kit);
            }

            Arena arena = new Arena(key, kits, icon, corner1, corner2, spawnPositions);
            arenaList.put(key.toLowerCase(), arena);
        }
    }

    public void saveArena(Arena arena) {
        String path = "arenas." + arena.getName().toLowerCase();

        config.set(path + ".corner1", arena.getCorner1());
        config.set(path + ".corner2", arena.getCorner2());
        config.set(path + ".spawn_positions", arena.getSpawnPositions());
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
