package dragon.me.excellentDuels.api.command;

import dragon.me.excellentDuels.ExcellentDuels;
import dragon.me.excellentDuels.hooks.PlaceholderAPIHook;
import dragon.me.excellentDuels.utils.ConfigProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

import java.util.List;

public interface DuelsCommandProvider {

    default void reimplementation(CommandSourceStack stack,String[] args){
        Player player = (Player) stack.getSender();
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
                player.sendRichMessage(PlaceholderAPIHook.format(player,ConfigProvider.SPAWN_HAS_BEEN_SET.replace("%location%","X: " + player.getLocation().x()
                        + " Y: " + player.getLocation().y() + " Z: " + player.getLocation().z()+ " YAW: " + player.getLocation().getYaw() + " PITCH: " + player.getLocation().getPitch())));
                break;
            case "help":
                for (String line : ConfigProvider.HELP_MESSAGE){
                    player.sendRichMessage(line);
                }
                break;

        }
    }
    default List<String> reimplementationSuggestion(CommandSourceStack stack,String[] args){
        if (args.length < 1){
            return  List.of("setspawn","reload","info","help");
        }

        return List.of();
    }
}
