package dragon.me.excellentDuels.listener;

import dragon.me.excellentDuels.ExcellentDuels;
import dragon.me.excellentDuels.utils.ConfigProvider;
import dragon.me.excellentDuels.utils.inventory.PersistentInventoryManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void  onEvent(PlayerJoinEvent e){
        if (ExcellentDuels.getPersistentInventoryManager().needsToRestoreInventory(e.getPlayer().getUniqueId())){

            Player p = e.getPlayer();

            if (ConfigProvider.SPAWN_LOCATION == null){
                ExcellentDuels.getPlugin().getLogger().warning("HEY! It looks like you forgot to set the spawn location! Please set it via \033[38;5;220m/exduels setspawn\u001B[0m in game.");

            }else {
                p.teleport(ConfigProvider.SPAWN_LOCATION);
            }


            p.getInventory().setContents(ExcellentDuels.getPersistentInventoryManager().get(p.getUniqueId()));
            ExcellentDuels.getPersistentInventoryManager().remove(p.getUniqueId());
            ExcellentDuels.getInventoryController().removeInv(p.getUniqueId());

            ExcellentDuels.getPlugin().getLogger().info("DE: Successfully restored the inventory of " + p.getName() + " as he left in the middle of the match!");

        }
    }
}
