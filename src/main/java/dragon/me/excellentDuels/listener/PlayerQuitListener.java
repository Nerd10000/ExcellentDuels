package dragon.me.excellentDuels.listener;

import dragon.me.excellentDuels.ExcellentDuels;
import dragon.me.excellentDuels.controllers.GameController;
import dragon.me.excellentDuels.controllers.InventoryController;
import dragon.me.excellentDuels.utils.ConfigProvider;
import dragon.me.excellentDuels.utils.inventory.PersistentInventoryManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onEvent(PlayerQuitEvent e){
        if (GameController.isPlayerInDuel(e.getPlayer().getName())){

            Player p = e.getPlayer();
            Optional<GameController.Game>
                    game = GameController.gameList
                    .stream().filter(game1 -> game1.getTeamBlue().contains(p) || game1.getTeamRed().contains(p)).findFirst();

            if (game.isEmpty()) return;

            Player opponent = GameController.getOpponent(p,game.get());
            ExcellentDuels.getGameController().removeGame(game.get());
            opponent.showTitle(Title.title(
                    MiniMessage.miniMessage().deserialize(ConfigProvider.CANCELLED_TITLE),
                    MiniMessage.miniMessage().deserialize(ConfigProvider.COUNTDOWN_SUBTITLE),
                    Title.DEFAULT_TIMES
            ));

            ExcellentDuels.getPersistentInventoryManager().add(
                    p.getUniqueId(),ExcellentDuels.getInventoryController().getInventory(p.getUniqueId()));

            Bukkit.getScheduler().runTaskLater(ExcellentDuels.getPlugin(), () -> {
                if (ConfigProvider.SPAWN_LOCATION == null){
                    ExcellentDuels.getPlugin().getLogger().warning("HEY! It looks like you forgot to set the spawn location! Please set it via \033[38;5;220m/exduels setspawn\u001B[0m in game.");
                }else {
                    opponent.teleport(ConfigProvider.SPAWN_LOCATION);
                    p.teleport(ConfigProvider.SPAWN_LOCATION);
                }

                p.getInventory().setContents(ExcellentDuels.getInventoryController().getInventory(p.getUniqueId()));
                opponent.getInventory().setContents(ExcellentDuels.getInventoryController().getInventory(opponent.getUniqueId()));


                for (String command : ConfigProvider.DUEL_END_COMMANDS){
                    String formated =  command
                            .replace("%arena%",game.get().getArenaName())
                            .replace("%winner%",opponent.getName())
                            .replace("%loser%",p.getName())
                            .replace("%kit%", game.get().getKitName());

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),formated);
                }
                },ConfigProvider.DUEL_END_DELAY);
        }
    }
}
