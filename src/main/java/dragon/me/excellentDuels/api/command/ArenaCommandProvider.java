package dragon.me.excellentDuels.api.command;

import dragon.me.excellentDuels.ExcellentDuels;
import dragon.me.excellentDuels.controllers.ArenaController;
import dragon.me.excellentDuels.controllers.KitDataController;
import dragon.me.excellentDuels.hooks.PlaceholderAPIHook;
import dragon.me.excellentDuels.utils.ConfigProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface ArenaCommandProvider {

    default void  reimplement(@NotNull CommandSourceStack stack, @NotNull String[] args){
        if (!(stack.getSender() instanceof Player p)) return;
        if (args.length == 0) {
            p.sendRichMessage(ConfigProvider.INCORRECT_USAGE);
            return;
        }

        ArenaController arenaController = ExcellentDuels.getArenaController();
        String sub = args[0].toLowerCase();

        switch (sub) {
            case "create" -> {
                if (!checkArgs(p, args, 2)) return;
                String name = args[1].toLowerCase();
                ArenaController.Arena arena = new ArenaController.Arena(name, List.of(), null, null, null, new java.util.ArrayList<>());
                arenaController.saveArena(arena);
                sendMessage(p, PlaceholderAPIHook.format(p,ConfigProvider.ARENA_CREATED), name);
            }

            case "corner1", "corner2" -> {
                if (!checkArgs(p, args, 2)) return;
                ArenaController.Arena arena = getArena(p, args[1]);
                if (arena == null) return;

                if (sub.equals("corner1")) {
                    arena.setCorner1(p.getLocation());
                    sendMessage(p, PlaceholderAPIHook.format(p,ConfigProvider.CORNER1_SET), arena.getName());
                } else {
                    arena.setCorner2(p.getLocation());
                    sendMessage(p, PlaceholderAPIHook.format(p,ConfigProvider.CORNER2_SET), arena.getName());
                }

                saveArena(arena);
            }

            case "addspawn" -> {
                if (!checkArgs(p, args, 2)) return;
                ArenaController.Arena arena = getArena(p, args[1]);
                if (arena == null) return;

                arena.getSpawnPositions().add(p.getLocation());
                saveArena(arena);
                sendMessage(p, PlaceholderAPIHook.format(p,ConfigProvider.ADD_SPAWN), arena.getName());
            }

            case "removespawn" -> {
                if (!checkArgs(p, args, 3)) return;
                ArenaController.Arena arena = getArena(p, args[1]);
                if (arena == null) return;

                int index;
                try {
                    index = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sendMessage(p,PlaceholderAPIHook.format(p,ConfigProvider.INDEX_ERROR),arena.getName());
                    return;
                }

                if (index < 0 || index >= arena.getSpawnPositions().size()) {
                    sendMessage(p,PlaceholderAPIHook.format(p,ConfigProvider.INDEX_ERROR),arena.getName());
                    return;
                }

                arena.getSpawnPositions().remove(index);
                saveArena(arena);
                sendMessage(p, PlaceholderAPIHook.format(p,ConfigProvider.REMOVE_SPAWN.replace("%index%",String.valueOf(index))), arena.getName());
            }

            case "clearspawns" -> {
                if (!checkArgs(p, args, 2)) return;
                ArenaController.Arena arena = getArena(p, args[1]);
                if (arena == null) return;

                arena.getSpawnPositions().clear();
                saveArena(arena);
                sendMessage(p, PlaceholderAPIHook.format(p,ConfigProvider.CLEAR_SPAWNS), arena.getName());
            }

            case "seticon" -> {
                if (!checkArgs(p, args, 2)) return;
                ArenaController.Arena arena = getArena(p, args[1]);
                if (arena == null) return;

                ItemStack item = p.getInventory().getItemInMainHand();
                if (item.getType().isAir()) {
                    p.sendMessage("%prefix% <red> Hold an item in your hand.");
                    return;
                }

                arena.setArenaIcon(item.clone());
                saveArena(arena);
                sendMessage(p, ConfigProvider.ARENA_ICON_SET, arena.getName());
            }

            case "addkit" -> {
                if (!checkArgs(p, args, 3)) return;
                ArenaController.Arena arena = getArena(p, args[1]);
                if (arena == null) return;

                KitDataController.Kit kit = ExcellentDuels.getKitDataController().getKitByName(args[2]);
                if (kit == null) {
                    p.sendRichMessage(PlaceholderAPIHook.format(p,ConfigProvider.NO_SUCH_KIT));
                    return;
                }

                if (!arena.getAvailableKits().contains(kit)) {
                    arena.getAvailableKits().add(kit);
                    saveArena(arena);
                }

                sendMessage(p,PlaceholderAPIHook.format(p, ConfigProvider.KIT_ADDED_TO_ARENA),arena.getName(), kit.getName());
            }

            case "removekit" -> {
                if (!checkArgs(p, args, 3)) return;
                ArenaController.Arena arena = getArena(p, args[1]);
                if (arena == null) return;

                arena.getAvailableKits().removeIf(k -> k.getName().equalsIgnoreCase(args[2]));
                saveArena(arena);
                sendMessage(p, PlaceholderAPIHook.format(p,ConfigProvider.KIT_REMOVED_FROM_ARENA), arena.getName(), args[2]);
            }

            default -> p.sendRichMessage(ConfigProvider.INCORRECT_USAGE);
        }
    }

    default List<String> reimplementSuggestions(@NotNull CommandSourceStack stack, @NotNull String[] args){
        if (!(stack.getSender() instanceof Player)) return List.of();

        if (args.length < 1 && args.length != 1){
            return  List.of("create", "corner1", "corner2","addspawn", "removespawn", "clearspawns", "seticon","addkit","removekit");
        }
        if (args.length < 2 && args[0].equalsIgnoreCase("create")) {
            return List.of("<name>");
        }else if (args.length < 2 && (args[0].equalsIgnoreCase("addspawn") || args[0].equalsIgnoreCase("removespawn") || args[0].equalsIgnoreCase("clearspawns"))){
            return  ArenaController.arenaList.values().stream().map(arena -> arena.getName()).toList();

        }else if (args.length < 2 && (args[0].equalsIgnoreCase("corner1") || args[0].equalsIgnoreCase("corner2"))){
            return  ArenaController.arenaList.values().stream().map(arena -> arena.getName()).toList();
        }
        else if (args.length < 2 && (args[0].equalsIgnoreCase("seticon"))){
            return  ArenaController.arenaList.values().stream().map(arena -> arena.getName()).toList();
        }

        else if (args.length < 2 && (args[0].equalsIgnoreCase("addkit"))){
            return  ArenaController.arenaList.values().stream().map(arena -> arena.getName()).toList();
        }
        else if (args.length < 3  && (args[0].equalsIgnoreCase("addkit"))){
            return  ExcellentDuels.getKitDataController().getAllKits().stream().map(KitDataController.Kit::getName).toList();
        }

        else if (args.length < 2 && (args[0].equalsIgnoreCase("removekit"))){
            return  ArenaController.arenaList.values().stream().map(arena -> arena.getName()).toList();
        }
        else if (args.length < 3  && (args[0].equalsIgnoreCase("removekit"))){
            Optional<ArenaController.Arena> arenaOptional = ArenaController.arenaList.values().stream().filter(arena -> arena.getName().equalsIgnoreCase(args[1])).findFirst();
            if (arenaOptional.isEmpty()) return  List.of();
            return  arenaOptional.get().getAvailableKits().stream().map(kit -> kit.getName()).toList();
        }



        return List.of(); // fallback
    }
    default boolean checkArgs(Player p, String[] args, int required) {
        if (args.length < required) {
            p.sendRichMessage(ConfigProvider.INCORRECT_USAGE);
            return false;
        }
        return true;
    }

    default ArenaController.Arena getArena(Player p, String name) {
        ArenaController.Arena arena = ArenaController.arenaList.get(name.toLowerCase());
        if (arena == null) {
            p.sendRichMessage(ConfigProvider.NO_SUCH_ARENA);
        }
        return arena;
    }

    default void saveArena(ArenaController.Arena arena) {
        ExcellentDuels.getArenaController().saveArena(arena);
    }

    default void sendMessage(Player p, String template, String arenaName) {
        p.sendRichMessage(template.replace("%arena%", arenaName));
    }

    default void sendMessage(Player p, String template, String arenaName, String kitName) {
        p.sendRichMessage(template.replace("%arena%", arenaName).replace("%kit%", kitName));
    }
}


