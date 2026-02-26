package dragon.me.excellentDuels.commands;

import dragon.me.excellentDuels.ExcellentDuels;
import dragon.me.excellentDuels.api.command.ArenaCommandProvider;
import dragon.me.excellentDuels.controllers.ArenaController;
import dragon.me.excellentDuels.utils.ConfigProvider;
import dragon.me.excellentDuels.controllers.KitDataController;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ArenaCommand implements BasicCommand, ArenaCommandProvider {

    @Override
    public @Nullable String permission() {
        return  "excellentduels.arena";
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack stack, @NotNull String[] args) {
       return reimplementSuggestions(stack,args);
    }

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        reimplement(stack,args);
    }
    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        if (sender instanceof Player p){
            return true;
        }
        return  false;
    }

}