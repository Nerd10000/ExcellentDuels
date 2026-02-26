package dragon.me.excellentDuels.commands;

import dragon.me.excellentDuels.ExcellentDuels;
import dragon.me.excellentDuels.api.command.DuelCommandProvider;
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

public class DuelCommand implements BasicCommand, DuelCommandProvider {

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        reimplementation(commandSourceStack,args);
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        return reimplementSuggestion(commandSourceStack,args);
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        if (sender instanceof Player p){
            return true;
        }
        return  false;
    }
    @Override
    public @Nullable String permission() {
        return BasicCommand.super.permission();
    }
}


