package dragon.me.excellentDuels.listener;

import dragon.me.excellentDuels.ExcellentDuels;
import dragon.me.excellentDuels.controllers.GameController;
import dragon.me.excellentDuels.controllers.InventoryController;
import dragon.me.excellentDuels.utils.ConfigProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameEvent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import javax.management.monitor.CounterMonitor;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Optional;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onEvent(PlayerDeathEvent e) {

        Player loser = e.getPlayer();

        if (!GameController.isPlayerInDuel(loser.getName())) {
            return;
        }

        Optional<GameController.Game> gameOptional = GameController.gameList.stream()
                .filter(game -> game.getTeamBlue().contains(loser)
                        || game.getTeamRed().contains(loser))
                .findFirst();

        if (gameOptional.isEmpty()) {
            return;
        }


        GameController.Game game = gameOptional.get();

        Player winner = ExcellentDuels.getGameController().getOpponent(loser, game);
        if (winner == null) {
            return;
        }
        e.getDrops().clear();
        e.setDroppedExp(0);
        e.deathMessage(Component.text(""));
        // Titles + messages
        loser.showTitle(Title.title(
                MiniMessage.miniMessage().deserialize(ConfigProvider.DEATH_TITLE),
                MiniMessage.miniMessage().deserialize(ConfigProvider.DEATH_SUBTITLE),
                Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1))
        ));

        loser.sendRichMessage(
                ConfigProvider.DUEL_DEFEAT.replace("%winner%", winner.getName())
        );

        winner.showTitle(Title.title(
                MiniMessage.miniMessage().deserialize(ConfigProvider.VICTORY_TITLE),
                MiniMessage.miniMessage().deserialize(ConfigProvider.VICTORY_SUBTITLE),
                Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1))
        ));

        winner.sendRichMessage(
                ConfigProvider.DUEL_VICTORY.replace("%loser%", loser.getName())
        );

        // Force respawn next tick
        Bukkit.getScheduler().runTaskLater(ExcellentDuels.getPlugin(), () -> {
            loser.spigot().respawn();
            loser.teleport(winner.getLocation().add(-1, 4, -1));
        },1L);

        loser.setGameMode(GameMode.SPECTATOR);


        Bukkit.getScheduler().runTaskLater(ExcellentDuels.getPlugin(), () -> {

            loser.setGameMode(GameMode.SURVIVAL);
            winner.setGameMode(GameMode.SURVIVAL);

            loser.teleport(ConfigProvider.SPAWN_LOCATION);
            winner.teleport(ConfigProvider.SPAWN_LOCATION);

            loser.getInventory().setContents(InventoryController.inventoryMap.get(loser.getUniqueId()));
            winner.getInventory().setContents(InventoryController.inventoryMap.get(winner.getUniqueId()));

            for (String command : ConfigProvider.DUEL_END_COMMANDS){
                String formated =  command
                        .replace("%arena%",game.getArenaName())
                        .replace("%winner%",winner.getName())
                        .replace("%loser%",loser.getName())
                        .replace("%kit%", game.getKitName());

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),formated);
            }

            ExcellentDuels.getGameController().removeGame(game);

        }, 20L * ConfigProvider.DUEL_END_DELAY);
    }
}
