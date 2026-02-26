package dragon.me.excellentDuels.hooks;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlaceholderAPIHook {

    public static String format(Player p, String text){
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            return PlaceholderAPI.setPlaceholders(p,text);
        }
        return text;
    }
}
