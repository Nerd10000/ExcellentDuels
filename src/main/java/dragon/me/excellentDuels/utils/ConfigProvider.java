package dragon.me.excellentDuels.utils;

import dragon.me.excellentDuels.ExcellentDuels;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ConfigProvider {

    public static String PREFIX;
    public static String INCORRECT_USAGE;
    public static int INVITE_EXPIRE_TIME;
    public static String INVITE_EXPIRED_MESSAGE;
    public static String PLAYER_IS_OFFLINE;
    public static String INVITATION_MESSAGE;
    public static String SELF_ERROR;
    public static String INVITATION_CONFIRMATION;
    public static String KIT_CREATED;
    public static String NO_SUCH_KIT;
    public static String DUEL_ACCEPTED;
    public static String DUEL_VICTORY;
    public static String DUEL_DEFEAT;
    public static String PLAYER_ELIMINATED;

    // Arenas
    public static String ARENA_CREATED;
    public static String CORNER1_SET;
    public static String CORNER2_SET;
    public static String NO_SUCH_ARENA;
    public static String ARENA_ICON_SET;
    public static String KIT_ADDED_TO_ARENA;
    public static String KIT_REMOVED_FROM_ARENA;
    public static String ARENA_REMOVED;
    public static String DEATH_TITLE;
    public static String DEATH_SUBTITLE;
    public  static  String SPAWN_HAS_BEEN_SET;
    public static int DUEL_END_DELAY;
    public static int COUNTDOWN_DELAY;
    public static String VICTORY_TITLE;
    public static String VICTORY_SUBTITLE;
    public static String COUNTDOWN_DEFAULT;
    public static String COUNTDOWN_GO;
    public static String COUNTDOWN_SUBTITLE;
    public static String BLACKLISTED_COMMAND_WHERE_RAN;
    public static List<String> WHITELISTED_COMMANDS;
    public static List<String> DUEL_START_COMMANDS;
    public static List<String> DUEL_END_COMMANDS;
    public static List<String> HELP_MESSAGE;
    public static Location SPAWN_LOCATION;

    public static  String CANCELLED_TITLE;
    public static  String CANCELLED_SUBTITLE;
    public static String NO_INVITES;
    public static String ADD_SPAWN;
    public static  String REMOVE_SPAWN;
    public static String INDEX_ERROR;
    public static String CLEAR_SPAWNS;
    public static String DRAW;

    public void onInit() {
        var config = ExcellentDuels.getPlugin().getConfig();

        PREFIX = config.getString("messages.prefix", "⏵<yellow><u>ExcellentDuels</u> </yellow>»");

        INCORRECT_USAGE = getMsg(config, "messages.incorrect_usage");
        INVITE_EXPIRE_TIME = config.getInt("invite_expire_time", 120);
        INVITE_EXPIRED_MESSAGE = getMsg(config, "messages.invite_expired");
        PLAYER_IS_OFFLINE = getMsg(config, "messages.player_is_offline");
        INVITATION_MESSAGE = getMsg(config, "messages.invitation");
        SELF_ERROR = getMsg(config, "messages.self_error");
        INVITATION_CONFIRMATION = getMsg(config, "messages.invitation_confirmation");
        KIT_CREATED = getMsg(config, "messages.kit_created");
        NO_SUCH_KIT = getMsg(config, "messages.no_such_kit");
        DUEL_ACCEPTED = getMsg(config, "messages.duel_accept");
        DUEL_VICTORY = getMsg(config, "messages.duel_victory");
        DUEL_DEFEAT = getMsg(config, "messages.duel_defeat");
        PLAYER_ELIMINATED = getMsg(config, "messages.player_eliminated");

        ARENA_CREATED = getMsg(config, "messages.duel_arena_created");
        CORNER1_SET = getMsg(config, "messages.1st_corner_set");
        CORNER2_SET = getMsg(config, "messages.2nd_corner_set");
        NO_SUCH_ARENA = getMsg(config, "messages.no_such_arena");
        ARENA_ICON_SET = getMsg(config, "messages.arena_icon_set");
        KIT_ADDED_TO_ARENA = getMsg(config, "messages.kit_added_to_arena");
        KIT_REMOVED_FROM_ARENA = getMsg(config, "messages.kit_removed_from_arena");
        ARENA_REMOVED = getMsg(config, "messages.arena_removed");
        SPAWN_HAS_BEEN_SET = getMsg(config, "messages.spawn_has_been_set");

        DEATH_TITLE = getMsg(config, "messages.death_title");

        DEATH_SUBTITLE = getMsg(config, "messages.death_subtitle");

        VICTORY_TITLE = getMsg(config, "messages.victory_title");

        VICTORY_SUBTITLE = getMsg(config, "messages.victory_subtitle");

        DUEL_END_DELAY = config.getInt("delays.duel_end", 5);
        SPAWN_LOCATION = config.getLocation("spawn_location");

        COUNTDOWN_DELAY = config.getInt("delays.countdown", 60);

        COUNTDOWN_DEFAULT = config.getString("messages.countdown.default", "<yellow>%number%");
        COUNTDOWN_GO = config.getString("messages.countdown.go", "<gold>ɢᴏ!");
        COUNTDOWN_SUBTITLE = config.getString("messages.countdown_subtitle", "<grey>Prepare for the fight!");

        WHITELISTED_COMMANDS = config.getStringList("whitelisted_commands");

        BLACKLISTED_COMMAND_WHERE_RAN = getMsg(config, "messages.blacklisted_commands_while_in_match");

        DUEL_START_COMMANDS = config.getStringList("duel_start_commands");

        DUEL_END_COMMANDS = config.getStringList("duel_end_commands");

        List<String> formattedHelp = new ArrayList<>();
        for (String line : config.getStringList("messages.help")){
            line = line.replace("%prefix%",PREFIX);
            formattedHelp.add(line);
        }
        HELP_MESSAGE = formattedHelp;

        CANCELLED_TITLE = config.getString("messages.cancelled_title", "<red>ᴍᴀᴛᴄʜ ᴄᴀɴᴄᴇʟʟᴇᴅ!");
        CANCELLED_SUBTITLE = config.getString("messages.cancelled_subtitle", "<grey>Looks like your opponent left the game!");

        NO_INVITES = getMsg(config,"messages.no_invitation");
        ADD_SPAWN = getMsg(config, "messages.add_spawnpoint");
        REMOVE_SPAWN = getMsg(config,"messages.remove_spawnpoint");
        INDEX_ERROR = getMsg(config,"messages.index_error");

        CLEAR_SPAWNS = getMsg(config,"messages.clear_spawnpoints");
        DRAW = getMsg(config,"messages.duel_draw");
    }

    private String getMsg(FileConfiguration config, String path) {
        String msg = config.getString(path);
        if (msg == null) {
            return "Missing config: " + path;
        }

        return msg.replace("%prefix%", PREFIX);
    }

    public void reload() {
        ExcellentDuels.getPlugin().reloadConfig();
        onInit();
    }
}