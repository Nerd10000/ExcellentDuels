package dragon.me.excellentDuels.commands;

import dragon.me.excellentDuels.ExcellentDuels;
import dragon.me.excellentDuels.controllers.*;
import dragon.me.excellentDuels.controllers.enums.GameState;
import dragon.me.excellentDuels.utils.ConfigProvider;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AcceptDuelCommand implements BasicCommand {

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        if (!(commandSourceStack.getSender() instanceof Player p)) return;

        if (args.length < 1) {
            p.sendRichMessage(ConfigProvider.INCORRECT_USAGE);
            return;
        }

        String targetName = args[0];
        Player targetPlayer = Bukkit.getPlayer(targetName);

        if (targetPlayer == null) {
            p.sendRichMessage(ConfigProvider.PLAYER_IS_OFFLINE);
            return;
        }

        // Find the invite safely
        Optional<InviteController.Invite> optionalInvite = InviteController.inviteList.stream()
                .filter(invite -> invite.getPlayer2() == p && invite.getPlayer1() == targetPlayer)
                .findFirst();

        if (optionalInvite.isEmpty()) {
            p.sendRichMessage("You have no duel invite from " + targetPlayer.getName() + "!");
            return;
        }

        InviteController.Invite invite = optionalInvite.get();

        // Notify both players
        String acceptMsg = ConfigProvider.DUEL_ACCEPTED.replace("%player2%", targetPlayer.getName());
        p.sendRichMessage(acceptMsg);
        targetPlayer.sendRichMessage(acceptMsg);


        Optional<ArenaController.Arena> optionalArena = ArenaController.arenaList.values().stream()
                .filter(arena -> !GameController.isArenaInUsage(arena.getName()))
                .filter(arena -> arena.getAvailableKits().stream()
                        .anyMatch(kit -> kit.getName().equals(invite.getKitName())))
                .findFirst();

        if (optionalArena.isEmpty()) {
            ExcellentDuels.getPlugin().getLogger().warning(
                    "No arena has the kit '" + invite.getKitName() + "'. Match cannot start!"
            );
            p.sendRichMessage("No available arena has the kit '" + invite.getKitName() + "'.");
            return;
        }
        ExcellentDuels.getInventoryController().addInventory(p.getUniqueId(),p.getInventory().getContents().clone());
        ExcellentDuels.getInventoryController().addInventory(targetPlayer.getUniqueId(),targetPlayer.getInventory().getContents().clone());

        ArenaController.Arena arena = optionalArena.get();

        Optional<KitDataController.Kit> optionalKit = arena.getAvailableKits().stream()
                .filter(kit -> kit.getName().equals(invite.getKitName()))
                .findFirst();

        if (optionalKit.isEmpty()) {
            ExcellentDuels.getPlugin().getLogger().warning(
                    "Kit '" + invite.getKitName() + "' is missing in arena '" + arena.getName() + "'."
            );
            p.sendRichMessage("The kit '" + invite.getKitName() + "' is unavailable.");
            return;
        }



        KitDataController.Kit kit = optionalKit.get();

        // Apply kit to both players
        setPlayerInventory(p, kit);
        setPlayerInventory(targetPlayer, kit);

        // Teleport players
        p.teleport(arena.getSpawnPos1());
        targetPlayer.teleport(arena.getSpawnPos2());

        // Game Start Logic
        List<Player> teamBlue = new ArrayList<>();
        teamBlue.add(p);
        List<Player> teamRed = new ArrayList<>();
        teamRed.add(targetPlayer);

        GameController.Game game = new GameController.Game(teamBlue, teamRed, arena.getName(),invite.getKitName());
        ExcellentDuels.getGameController().addGame(game);
        InviteController.inviteList.remove(invite); // Remove the invite after duel starts

        GameController.setGameState(game, GameState.STARTING);
        AtomicInteger countdown = new AtomicInteger(5); // or whatever your start value is


        for (String command : ConfigProvider.DUEL_START_COMMANDS){
            String formated =  command
                    .replace("%arena%",game.getArenaName())
                    .replace("%player1%",game.getTeamRed().get(0).getName())
                    .replace("%player2%",game.getTeamBlue().get(0).getName())
                    .replace("%kit%",game.getKitName());

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),formated);
        }
        Bukkit.getScheduler().runTaskTimer(ExcellentDuels.getPlugin(), task -> {

            int timeLeft = countdown.get();

            Component subtitle = MiniMessage.miniMessage()
                    .deserialize(ConfigProvider.COUNTDOWN_SUBTITLE);

            if (timeLeft > 0) {

                String path = "messages.countdown." + timeLeft;

                String message = ExcellentDuels.getPlugin().getConfig().getString(
                        path,
                        ConfigProvider.COUNTDOWN_DEFAULT.replace("%number%", String.valueOf(timeLeft))
                );

                Component title = MiniMessage.miniMessage().deserialize(message);

                Title.Times times = Title.Times.times(
                        Duration.ofSeconds(0),
                        Duration.ofSeconds(1),
                        Duration.ofSeconds(0)
                );

                p.showTitle(Title.title(title, subtitle, times));
                targetPlayer.showTitle(Title.title(title, subtitle, times));

            } else {

                Component goTitle = MiniMessage.miniMessage()
                        .deserialize(ConfigProvider.COUNTDOWN_GO);

                Title.Times times = Title.Times.times(
                        Duration.ofSeconds(0),
                        Duration.ofSeconds(2),
                        Duration.ofSeconds(0)
                );

                p.showTitle(Title.title(goTitle, subtitle, times));
                targetPlayer.showTitle(Title.title(goTitle, subtitle, times));

                GameController.setGameState(game, GameState.ON_GOING);
                task.cancel();
                return;
            }

            countdown.decrementAndGet();

        }, 0L, 20L);


    }

    private void setPlayerInventory(Player player, KitDataController.Kit kit) {

        player.getInventory().setContents(kit.getItems().toArray(new ItemStack[0]));
        player.getInventory().setItemInOffHand(kit.getOffhand());
        player.getInventory().setArmorContents(kit.getArmorItems().toArray(new ItemStack[0]));
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        if (!(commandSourceStack.getSender() instanceof Player sender)) {
            return java.util.Collections.emptyList();
        }

        if (args.length != 1) return java.util.Collections.emptyList();

        String partial = args[0].toLowerCase();

        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> !name.equalsIgnoreCase(sender.getName()))
                .filter(name -> name.toLowerCase().startsWith(partial))
                .toList();
    }
}