package dragon.me.excellentDuels.listener;

import dragon.me.excellentDuels.controllers.GameController;
import dragon.me.excellentDuels.hooks.PlaceholderAPIHook;
import dragon.me.excellentDuels.utils.ConfigProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

public class PlayerCommandDispatchListener implements Listener {

    @EventHandler
    public void onEvent(PlayerCommandPreprocessEvent e){
        String command = e.getMessage();
        if (!GameController.isPlayerInDuel(e.getPlayer().getName())){
            return;
        }

        if (ConfigProvider.WHITELISTED_COMMANDS.contains(command)){
            return;
        }

        e.getPlayer().sendRichMessage(PlaceholderAPIHook.format(e.getPlayer(), ConfigProvider.BLACKLISTED_COMMAND_WHERE_RAN));
        e.setCancelled(true);
    }
}
