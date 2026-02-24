package dragon.me.excellentDuels.commands;

import dragon.me.excellentDuels.ExcellentDuels;
import dragon.me.excellentDuels.api.command.KitsCommandProvider;
import dragon.me.excellentDuels.utils.ConfigProvider;
import dragon.me.excellentDuels.controllers.KitDataController;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class KitsCommand implements BasicCommand, KitsCommandProvider {

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        reimplementation(stack,args);
    }

    @Override
    public @NotNull Collection<String> suggest(
            @NotNull CommandSourceStack stack,
            @NotNull String[] args) {
        return reimplementationSuggestion(stack,args);
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return sender instanceof Player;
    }

    @Override
    public @Nullable String permission() {
        return "excellentduels.kits";
    }
}