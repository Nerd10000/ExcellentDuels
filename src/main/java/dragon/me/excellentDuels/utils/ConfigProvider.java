package dragon.me.excellentDuels.utils;

import dragon.me.excellentDuels.ExcellentDuels;
import org.bukkit.Location;

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

    // Arenas
    public static String ARENA_CREATED;
    public static String CORNER1_SET;
    public static String CORNER2_SET;
    public static String FIRST_SPAWN_SET;
    public static String SECOND_SPAWN_SET;
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
    public void onInit() {
        var config = ExcellentDuels.getPlugin().getConfig();

        PREFIX = config.getString("messages.prefix");

        INCORRECT_USAGE = config.getString("messages.incorrect_usage").replace("%prefix%", PREFIX);
        INVITE_EXPIRE_TIME = config.getInt("invite_expire_time");
        INVITE_EXPIRED_MESSAGE = config.getString("messages.invite_expired").replace("%prefix%", PREFIX);
        PLAYER_IS_OFFLINE = config.getString("messages.player_is_offline").replace("%prefix%", PREFIX);
        INVITATION_MESSAGE = config.getString("messages.invitation").replace("%prefix%", PREFIX);
        SELF_ERROR = config.getString("messages.self_error").replace("%prefix%", PREFIX);
        INVITATION_CONFIRMATION = config.getString("messages.invitation_confirmation").replace("%prefix%", PREFIX);
        KIT_CREATED = config.getString("messages.kit_created").replace("%prefix%", PREFIX);
        NO_SUCH_KIT = config.getString("messages.no_such_kit").replace("%prefix%", PREFIX);
        DUEL_ACCEPTED = config.getString("messages.duel_accept").replace("%prefix%", PREFIX);
        DUEL_VICTORY = config.getString("messages.duel_victory").replace("%prefix%", PREFIX);
        DUEL_DEFEAT = config.getString("messages.duel_defeat").replace("%prefix%", PREFIX);

        ARENA_CREATED = config.getString("messages.duel_arena_created").replace("%prefix%", PREFIX);
        CORNER1_SET = config.getString("messages.1st_corner_set").replace("%prefix%", PREFIX);
        CORNER2_SET = config.getString("messages.2nd_corner_set").replace("%prefix%", PREFIX);
        FIRST_SPAWN_SET = config.getString("messages.1st_spawn_set").replace("%prefix%", PREFIX);
        SECOND_SPAWN_SET = config.getString("messages.2nd_spawn_set").replace("%prefix%", PREFIX);
        NO_SUCH_ARENA = config.getString("messages.no_such_arena").replace("%prefix%", PREFIX);
        ARENA_ICON_SET = config.getString("messages.arena_icon_set").replace("%prefix%", PREFIX);
        KIT_ADDED_TO_ARENA = config.getString("messages.kit_added_to_arena").replace("%prefix%", PREFIX);
        KIT_REMOVED_FROM_ARENA = config.getString("messages.kit_removed_from_arena").replace("%prefix%", PREFIX);
        ARENA_REMOVED = config.getString("messages.arena_removed").replace("%prefix%", PREFIX);
        SPAWN_HAS_BEEN_SET = config.getString("messages.spawn_has_been_set")
                .replace("%prefix%",PREFIX);

        DEATH_TITLE = config.getString("messages.death_title")
                .replace("%prefix%", PREFIX);

        DEATH_SUBTITLE = config.getString("messages.death_subtitle")
                .replace("%prefix%", PREFIX);

        VICTORY_TITLE = config.getString("messages.victory_title")
                .replace("%prefix%", PREFIX);

        VICTORY_SUBTITLE = config.getString("messages.victory_subtitle")
                .replace("%prefix%", PREFIX);

        DUEL_END_DELAY = config.getInt("delays.duel_end");
        SPAWN_LOCATION = config.getLocation("spawn_location");

        COUNTDOWN_DELAY = config.getInt("delays.countdown");

        COUNTDOWN_DEFAULT = config.getString("messages.countdown.default");
        COUNTDOWN_GO = config.getString("messages.countdown.go");
        COUNTDOWN_SUBTITLE =config.getString("messages.countdown_subtitle");

        WHITELISTED_COMMANDS = config.getStringList("whitelisted_commands");

        BLACKLISTED_COMMAND_WHERE_RAN = config.getString("messages.blacklisted_commands_while_in_match")
                .replace("%prefix%",PREFIX);

        DUEL_START_COMMANDS = config.getStringList("duel_start_commands");

        DUEL_END_COMMANDS = config.getStringList("duel_end_commands");

        List<String> formattedHelp = new ArrayList<>();
        for (String line : config.getStringList("messages.help")){
            line = line.replace("%prefix%",PREFIX);
            formattedHelp.add(line);
        }
        HELP_MESSAGE = formattedHelp;

        CANCELLED_TITLE = config.getString("messages.cancelled_title");
        CANCELLED_SUBTITLE = config.getString("messages.cancelled_subtitle");
    }

    public void reload() {
        ExcellentDuels.getPlugin().reloadConfig();
        onInit();
    }
}