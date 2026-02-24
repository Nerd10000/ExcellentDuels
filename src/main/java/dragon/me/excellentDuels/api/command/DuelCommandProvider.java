package dragon.me.excellentDuels.api.command;

import dragon.me.excellentDuels.ExcellentDuels;
import dragon.me.excellentDuels.api.abstractions.Team;
import dragon.me.excellentDuels.controllers.InviteController;
import dragon.me.excellentDuels.utils.ConfigProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public interface DuelCommandProvider {

    default  void reimplementation(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args){
        if (!(commandSourceStack.getSender() instanceof  Player p)){
            commandSourceStack.getSender().sendRichMessage("⏵<yellow><u>ExcellentDuels</u> </yellow>» <red>Only players can execute this command.</red>");
            return;

        }
        if (args.length < 2) {
            p.sendRichMessage(ConfigProvider.INCORRECT_USAGE);
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        String kit = args[args.length - 1];

        Team team1 = new Team(List.of(p));
        Team team2 = new Team(List.of(target));


        ExcellentDuels.getInviteController().addInvite(new InviteController.Invite(p, target, kit));
        target.sendRichMessage(ConfigProvider.INVITATION_MESSAGE
                .replace("%player1%", p.getName())
                .replace("%kit%", kit));

        p.sendRichMessage(ConfigProvider.INVITATION_CONFIRMATION
                .replace("%player2%", target.getName())
                .replace("%kit%", kit));
    }

    default List<String> reimplementSuggestion(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args){
        if (args.length == 0) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        }
        if (args.length > 0) {
            String lastArg = args[args.length - 1];
            List<String> playerNames = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            List<String> kitNames = ExcellentDuels.getKitDataController().getAllKits().stream().map(kit -> kit.getName()).toList();

            List<String> suggestions = new java.util.ArrayList<>();
            suggestions.addAll(playerNames);
            suggestions.addAll(kitNames);

            return suggestions.stream().filter(s -> s.toLowerCase().startsWith(lastArg.toLowerCase())).collect(Collectors.toList());
        }
        return List.of();
    }
}
