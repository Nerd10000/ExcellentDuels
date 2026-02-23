package dragon.me.excellentDuels.utils.inventory;

import dragon.me.excellentDuels.ExcellentDuels;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersistentInventoryManager {

    private File file;
    private YamlConfiguration yamlConfiguration;

    public PersistentInventoryManager() throws IOException {
        file = new File(ExcellentDuels.getPlugin().getDataFolder() + "/presistent_cache.yml");
        if (file.exists()){

        }else {
            file.createNewFile();
        }

        yamlConfiguration = YamlConfiguration.loadConfiguration(file);

    }

    public void save(){
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reload(){
        if (file.exists()){
            yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        }else {
            ExcellentDuels.getPlugin().getLogger().warning("Failed to load PRESISTENT_CACHE.YML to memory!");
        }
    }

    public void add(UUID uuid, ItemStack[] content) {

        yamlConfiguration.set("cache." + uuid,content);
        save();
        reload();
    }

    public boolean needsToRestoreInventory(UUID uuid){

        if (!yamlConfiguration.contains("cache")) {
            return false;
        }

        for (String key : yamlConfiguration.getConfigurationSection("cache").getKeys(false)){
            if (UUID.fromString(key).equals(uuid)){
                return  true;
            }
        }
        return  false;
    }

    public ItemStack[] get(UUID uuid) {

        if (!needsToRestoreInventory(uuid)) {
            return new ItemStack[0];
        }

        List<?> list = yamlConfiguration.getList("cache." + uuid.toString());
        if (list == null) {
            return new ItemStack[0];
        }

        ItemStack[] items = new ItemStack[list.size()];

        for (int i = 0; i < list.size(); i++) {
            items[i] = (ItemStack) list.get(i);
        }

        return items;
    }
    public void remove(UUID uuid) {
        yamlConfiguration.set("cache." + uuid.toString(), null);
        save();
    }
}
