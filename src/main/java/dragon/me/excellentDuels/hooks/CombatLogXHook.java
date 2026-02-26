package dragon.me.excellentDuels.hooks;

import com.github.sirblobman.combatlogx.api.event.PlayerPreTagEvent;
import dragon.me.excellentDuels.controllers.GameController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CombatLogXHook implements Listener {

    @EventHandler
    public void onEvent(PlayerPreTagEvent e){
        if (GameController.isPlayerInDuel(e.getPlayer().getName())){
            e.setCancelled(true);
        }
    }



}
