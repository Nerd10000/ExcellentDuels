package dragon.me.excellentDuels.listener;

import dragon.me.excellentDuels.ExcellentDuels;
import dragon.me.excellentDuels.api.models.Match;
import dragon.me.excellentDuels.api.models.Team;
import dragon.me.excellentDuels.controllers.ArenaController;
import dragon.me.excellentDuels.controllers.GameController;
import dragon.me.excellentDuels.controllers.InventoryController;
import dragon.me.excellentDuels.controllers.enums.PlayerStatus;
import dragon.me.excellentDuels.hooks.PlaceholderAPIHook;
import dragon.me.excellentDuels.utils.ConfigProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.time.Duration;
import java.util.Optional;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onEvent(PlayerDeathEvent e) {

        Player loser = e.getPlayer();

        if (!GameController.isPlayerInDuel(loser.getName())) {
            return;
        }

        Optional<Match> gameOptional = GameController.gameList.stream()
                .filter(game -> {
                    return game.getTeams().stream()
                            .anyMatch(team -> team.getPlayers().stream()
                                    .anyMatch(player -> player.getName() == e.getPlayer().getName()));

                })
                .findFirst();

        if (gameOptional.isEmpty()) {
            return;
        }

        Match game = gameOptional.get();

        game.getParticipantStatuses().put(loser.getName(), PlayerStatus.DEAD);


        e.getDrops().clear();
        e.setDroppedExp(0);
        e.deathMessage(Component.text(""));

        Bukkit.getScheduler().runTaskLater(ExcellentDuels.getPlugin(), () -> {
            loser.spigot().respawn();
            loser. teleport(ArenaController.arenaList.values().stream()
                    .filter(arena -> arena.getName() == game.getArenaName()).findFirst().get().getSpawnPositions().get(0));

        }, 1L);

        loser.setGameMode(GameMode.SPECTATOR);

        game.getParticipantStatuses().put(loser.getName(), PlayerStatus.DEAD);


        if (GameController.isMatchOver(game)) {
            Team winningTeam = GameController.getWinningTeam(game);
            Team losingTeam = game.getTeams().stream()
                    .filter(team -> !team.equals(winningTeam))
                    .findFirst()
                    .orElse(null);

            if (winningTeam != null) {
                for (Team team : game.getTeams()) {
                    for (Player participant : team.getPlayers()) {
                        if (winningTeam.getPlayers().contains(participant)) {
                            participant.showTitle(Title.title(
                                    MiniMessage.miniMessage().deserialize(ConfigProvider.VICTORY_TITLE),
                                    MiniMessage.miniMessage().deserialize(ConfigProvider.VICTORY_SUBTITLE),
                                    Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1))
                            ));
                            participant.sendRichMessage(
                                    PlaceholderAPIHook.format(participant,ConfigProvider.DUEL_VICTORY
                                            .replace("%winner_team%", "Your Team")
                                            .replace("%loser_team%",
                                                    losingTeam != null
                                                            ? losingTeam.getPlayers().get(0).getName() + "'s Team"
                                                            : "Unknown Team"))
                            );

                        } else {
                            participant.showTitle(Title.title(
                                    MiniMessage.miniMessage().deserialize(ConfigProvider.DEATH_TITLE),
                                    MiniMessage.miniMessage().deserialize(ConfigProvider.DEATH_SUBTITLE),
                                    Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1))
                            ));
                            participant.sendRichMessage(PlaceholderAPIHook.format(participant,ConfigProvider.DUEL_DEFEAT.replace("%winner_team%", winningTeam.getPlayers().get(0).getName() + "'s Team")));
                        }
                    }
                }
            } else {

                for (Team team : game.getTeams()) {
                    for (Player participant : team.getPlayers()) {
                        participant.sendRichMessage("The duel ended in a draw!");
                    }
                }
            }

            Bukkit.getScheduler().runTaskLater(ExcellentDuels.getPlugin(), () -> {
                for (Team team : game.getTeams()) {
                    for (Player participant : team.getPlayers()) {
                        participant.setGameMode(GameMode.SURVIVAL);
                        if (ConfigProvider.SPAWN_LOCATION == null) {
                            ExcellentDuels.getPlugin().getLogger().warning("HEY! It looks like you forgot to set the spawn location! Please set it via \033[38;5;220m/exduels setspawn\u001B[0m in game.");
                        } else {
                            participant.teleport(ConfigProvider.SPAWN_LOCATION);
                        }
                        participant.getInventory().setContents(InventoryController.inventoryMap.get(participant.getUniqueId()));
                        participant.getActivePotionEffects().clear();
                        participant.setSaturation(20);
                        participant.setHealth(20);
                    }
                }

                for (String command : ConfigProvider.DUEL_END_COMMANDS) {
                    String formatted = command
                            .replace("%arena%", game.getArenaName())
                            .replace("%winner_team%", (winningTeam != null ? winningTeam.getPlayers().get(0).getName() + "'s Team" : "N/A"))
                            .replace("%kit%", game.getKitName());

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formatted);
                }

                ExcellentDuels.getGameController().removeGame(game);
            }, 20L * ConfigProvider.DUEL_END_DELAY);

        } else {

            for (Team team : game.getTeams()) {
                for (Player participant : team.getPlayers()) {
                    if (participant != loser) {
                        participant.sendRichMessage(PlaceholderAPIHook.format(loser,ConfigProvider.PLAYER_ELIMINATED.replace("%player%", loser.getName())));
                    }
                }
            }

        }
    }
}
