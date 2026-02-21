package dragon.me.excellentDuels.controllers;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class InventoryController {

    public static HashMap<UUID, ItemStack[]> inventoryMap = new HashMap<>();

    public InventoryController() {

    }


    public void addInventory(UUID uuid, ItemStack[] inventory){
        inventoryMap.put(uuid,inventory);
    }

    public void removeInv(UUID uuid){
        inventoryMap.remove(uuid);
    }

    public ItemStack[] getInventory(UUID uuid){
        return inventoryMap.getOrDefault(uuid,new ItemStack[0]);
    }
}
