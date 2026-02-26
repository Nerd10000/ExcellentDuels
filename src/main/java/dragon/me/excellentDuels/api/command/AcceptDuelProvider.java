package dragon.me.excellentDuels.api.command;

import dragon.me.excellentDuels.ExcellentDuels;
import dragon.me.excellentDuels.api.models.Match;
import dragon.me.excellentDuels.api.models.Team;
import dragon.me.excellentDuels.controllers.ArenaController;
import dragon.me.excellentDuels.controllers.GameController;
import dragon.me.excellentDuels.controllers.InviteController;
import dragon.me.excellentDuels.controllers.KitDataController;
import dragon.me.excellentDuels.controllers.enums.GameState;
import dragon.me.excellentDuels.hooks.PlaceholderAPIHook;
import dragon.me.excellentDuels.utils.ConfigProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface AcceptDuelProvider  {

    default void reimplement(@NotNull CommandSourceStack source, @NotNull String[] args){

        if (!(source.getSender() instanceof Player p)) return;

        if (args.length < 1) {
            p.sendRichMessage(PlaceholderAPIHook.format(p,ConfigProvider.INCORRECT_USAGE));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            p.sendRichMessage(PlaceholderAPIHook.format(p,ConfigProvider.PLAYER_IS_OFFLINE));
            return;
        }

        Optional<InviteController.Invite> inviteOpt = findInvite(target, p);
        if (inviteOpt.isEmpty()) {
            p.sendRichMessage(PlaceholderAPIHook.format(p,ConfigProvider.NO_INVITES.replace("%player%",target.getName())));
            return;
        }

        InviteController.Invite invite = inviteOpt.get();
        notifyPlayersDuelAccepted(p, target);

        ArenaController.Arena arena = findAvailableArena(invite);
        if (arena == null) return;


        backupInventory(p);
        backupInventory(target);


        KitDataController.Kit kit = findKit(arena, invite.getKitName());
        if (kit == null) return;

        Match match = createMatch(p, target, arena,invite.getKitName());
        match.applyKit(p, kit);
        match.applyKit(target, kit);
        match.teleportTeams(arena);

        ExcellentDuels.getGameController().addGame(match);
        InviteController.inviteList.remove(invite);

        GameController.setGameState(match, GameState.STARTING);

        executeStartCommands(match, p, target);

        // Start countdown
        match.startCountdown(5);
    }

    default List<String> reimplementSuggestions(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args){
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


    //==== Helper methods ====
    default Optional<InviteController.Invite> findInvite(Player from, Player to){
        return InviteController.inviteList.stream()
                .filter(inv -> inv.getPlayer2() == to && inv.getPlayer1() == from)
                .findFirst();
    }

    default void notifyPlayersDuelAccepted(Player p1, Player p2){
        String msg = ConfigProvider.DUEL_ACCEPTED.replace("%player2%", p2.getName());
        p1.sendRichMessage(msg);
        p2.sendRichMessage(msg);
    }

    default ArenaController.Arena findAvailableArena(InviteController.Invite invite){
        return ArenaController.arenaList.values().stream()
                .filter(arena -> !GameController.isArenaInUsage(arena.getName()))
                .filter(arena -> arena.getAvailableKits().stream()
                        .anyMatch(kit -> kit.getName().equals(invite.getKitName())))
                .findFirst()
                .orElseGet(() -> {
                    Bukkit.getLogger().warning("No arena with kit " + invite.getKitName());
                    return null;
                });
    }

    default KitDataController.Kit findKit(ArenaController.Arena arena, String kitName){
        return arena.getAvailableKits().stream()
                .filter(kit -> kit.getName().equals(kitName))
                .findFirst()
                .orElseGet(() -> {
                    Bukkit.getLogger().warning("Kit " + kitName + " missing in arena " + arena.getName());
                    return null;
                });
    }

    default void backupInventory(Player p){
        ExcellentDuels.getInventoryController().addInventory(p.getUniqueId(),
                p.getInventory().getContents().clone());
    }

    default Match createMatch(Player p1, Player p2, ArenaController.Arena arena,String kitName){
        List<Team> teams = List.of(new Team(List.of(p1)), new Team(List.of(p2)));
        return new Match(teams, arena.getName(),kitName);
    }

    default void executeStartCommands(Match match, Player p1, Player p2){
        List<Player> players = new ArrayList<>();
        for (Team team : match.getTeams()) {
            players.addAll(team.getPlayers());
        }

        for (String cmd : ConfigProvider.DUEL_START_COMMANDS){
            String formatted = cmd
                    .replace("%arena%", match.getArenaName())
                    .replace("%players%", players.stream().map(Player::getName).reduce((a, b) -> a + ", " + b).orElse(""));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formatted);
        }
    }
}
