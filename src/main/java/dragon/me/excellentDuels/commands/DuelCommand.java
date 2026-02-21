package dragon.me.excellentDuels.commands;

import dragon.me.excellentDuels.ExcellentDuels;
import dragon.me.excellentDuels.utils.ConfigProvider;
import dragon.me.excellentDuels.controllers.InviteController;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DuelCommand implements BasicCommand {

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        if (!(commandSourceStack.getSender() instanceof  Player p)){
            commandSourceStack.getSender().sendRichMessage("⏵<yellow><u>ExcellentDuels</u> </yellow>» <red>Only players can execute this command.</red>");
            return;

        }else {
            if (args.length != 2){
                p.sendRichMessage(ConfigProvider.INCORRECT_USAGE);
                return;
            }
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == p){
            p.sendRichMessage(ConfigProvider.SELF_ERROR);

            return;
        }

        if (target == null){
            p.sendRichMessage(ConfigProvider.PLAYER_IS_OFFLINE);
            return;
        }
        String kit = args[1];

        ExcellentDuels.getInviteController().addInvite(new InviteController.Invite(p,target,kit));
        target.sendRichMessage(ConfigProvider.INVITATION_MESSAGE
                .replace("%player1%",p.getName())
                .replace("%kit%",kit));
        p.sendRichMessage(ConfigProvider.INVITATION_CONFIRMATION.replace("%player2%", target.getName())
                .replace("%kit%", kit));
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        if (args.length <= 1 && args.length <= 2){
            return Bukkit.getOnlinePlayers().stream().map( player -> player.getName()).collect(Collectors.toList());
        }else if ( args.length >= 1){
            return ExcellentDuels.getKitDataController().getAllKits().stream().map(kit -> kit.getName()).toList();
        }
        return List.of();
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return BasicCommand.super.canUse(sender);
    }

    @Override
    public @Nullable String permission() {
        return BasicCommand.super.permission();
    }
}


