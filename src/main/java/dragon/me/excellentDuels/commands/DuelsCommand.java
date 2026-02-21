package dragon.me.excellentDuels.commands;

import dragon.me.excellentDuels.ExcellentDuels;
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

public class DuelsCommand implements BasicCommand {


    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        Player player = (Player) commandSourceStack.getSender();
        if (args.length < 1){
            player.sendRichMessage(ConfigProvider.INCORRECT_USAGE);
            return;
        }

        switch (args[0]){

            case "setspawn":

                ExcellentDuels.getPlugin().getConfig()
                                .set("spawn_location",player.getLocation());
                ExcellentDuels.getPlugin().saveConfig();
                ExcellentDuels.getPlugin().reloadConfig();
                new ConfigProvider().reload();
                player.sendRichMessage(ConfigProvider.SPAWN_HAS_BEEN_SET.replace("%location%","X: " + player.getLocation().x()
                + " Y: " + player.getLocation().y() + " Z: " + player.getLocation().z()+ " YAW: " + player.getLocation().getYaw() + " PITCH: " + player.getLocation().getPitch()));

        }
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        return List.of();
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
