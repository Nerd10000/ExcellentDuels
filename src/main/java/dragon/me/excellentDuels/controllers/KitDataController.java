package dragon.me.excellentDuels.controllers;

import dragon.me.excellentDuels.ExcellentDuels;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class KitDataController {

    private static final Map<String, Kit> kits = new HashMap<>();
    private final File file;
    private final FileConfiguration config;

    public KitDataController() {
        file = new File(ExcellentDuels.getPlugin().getDataFolder(), "kits.yml");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Failed to create kits.yml", e);
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
        loadKits();
    }



    public static class Kit {
        @Getter
        private final String name;
        private final List<ItemStack> items;
        private final List<ItemStack> armorItems;
        private final ItemStack offhand;

        public Kit(String name, List<ItemStack> items, List<ItemStack> armorItems, ItemStack offhand) {
            this.name = name;
            this.items = items;
            this.armorItems = armorItems;
            this.offhand = offhand;
        }

        public List<ItemStack> getItems() {
            return items;
        }

        public List<ItemStack> getArmorItems() {
            return armorItems;
        }

        public ItemStack getOffhand() {
            return offhand;
        }
    }


    public void saveKit(Kit kit) {
        String path = "kits." + kit.getName().toLowerCase();

        config.set(path + ".items", kit.getItems());
        config.set(path + ".armor", kit.getArmorItems());
        config.set(path + ".offhand", kit.getOffhand());

        kits.put(kit.getName().toLowerCase(), kit);
        saveFile();
    }

    private void saveFile() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save kits.yml", e);
        }
    }


    private void loadKits() {
        kits.clear();

        if (!config.contains("kits")) return;

        for (String key : config.getConfigurationSection("kits").getKeys(false)) {
            String path = "kits." + key;

            List<ItemStack> items =
                    (List<ItemStack>) config.getList(path + ".items", new ArrayList<>());

            List<ItemStack> armor =
                    (List<ItemStack>) config.getList(path + ".armor", new ArrayList<>());

            ItemStack offhand = config.getItemStack(path + ".offhand");

            Kit kit = new Kit(key, items, armor, offhand);
            kits.put(key.toLowerCase(), kit);
        }
    }

    // ---------------- GETTERS ----------------

    public Kit getKitByName(String name) {
        return kits.get(name.toLowerCase());
    }

    public Collection<Kit> getAllKits() {
        return kits.values();
    }
}