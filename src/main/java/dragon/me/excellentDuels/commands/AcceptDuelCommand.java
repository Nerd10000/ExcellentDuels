package dragon.me.excellentDuels.commands;

import dragon.me.excellentDuels.api.command.AcceptDuelProvider;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.*;


public class AcceptDuelCommand implements BasicCommand,AcceptDuelProvider{

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        reimplement(commandSourceStack,args);

    }



    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        return reimplementSuggestions(commandSourceStack,args);
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        if (sender instanceof Player p){
            return true;
        }
        return  false;
    }
}