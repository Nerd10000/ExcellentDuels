package dragon.me.excellentDuels.commands;

import dragon.me.excellentDuels.ExcellentDuels;
import dragon.me.excellentDuels.api.command.DuelsCommandProvider;
import dragon.me.excellentDuels.utils.ConfigProvider;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;

public class DuelsCommand implements BasicCommand, DuelsCommandProvider {


    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        reimplementation( commandSourceStack,args);
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        return reimplementationSuggestion(commandSourceStack,args);
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        if (!(sender instanceof  Player)){
            return  false;
        }
        return true;
    }

    @Override
    public @Nullable String permission() {
        return BasicCommand.super.permission();
    }
}
