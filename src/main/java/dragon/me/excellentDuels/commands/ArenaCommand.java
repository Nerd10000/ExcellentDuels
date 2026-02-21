package dragon.me.excellentDuels.commands;

import dragon.me.excellentDuels.ExcellentDuels;
import dragon.me.excellentDuels.controllers.ArenaController;
import dragon.me.excellentDuels.utils.ConfigProvider;
import dragon.me.excellentDuels.controllers.KitDataController;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class ArenaCommand implements BasicCommand {

    @Override
    public @Nullable String permission() {
        return  "excellentduels.arena";
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (!(stack.getSender() instanceof Player)) return List.of();

        // All subcommands
        String[] subcommands = {
                "create", "corner1", "corner2",
                "spawn1", "spawn2", "seticon",
                "addkit", "removekit", "remove"
        };

        // === FIRST ARGUMENT ===
        if (args.length == 0 || args.length == 1) {
            String partial = args.length == 0 ? "" : args[0].toLowerCase();
            return java.util.Arrays.stream(subcommands)
                    .filter(cmd -> cmd.toLowerCase().startsWith(partial))
                    .sorted()
                    .toList();
        }

        // === SECOND ARGUMENT ===
        if (args.length >= 2) {
            String firstArg = args[0].toLowerCase();
            String partialSecond = args[1].toLowerCase();

            switch (firstArg) {
                case "create": // no suggestions for new arena name
                    return List.of();

                case "corner1":
                case "corner2":
                case "spawn1":
                case "spawn2":
                case "seticon":
                case "remove": // suggest arena names
                    return ArenaController.arenaList.keySet().stream()
                            .filter(name -> name.toLowerCase().startsWith(partialSecond))
                            .sorted()
                            .toList();

                case "addkit":
                case "removekit": // suggest kit names
                    return ExcellentDuels.getKitDataController().getAllKits().stream()
                            .map(kit -> kit.getName())
                            .filter(name -> name.toLowerCase().startsWith(partialSecond))
                            .sorted()
                            .toList();

                default:
                    return List.of();
            }
        }

        return List.of(); // fallback
    }

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (!(stack.getSender() instanceof Player p)) {
            return;
        }

        if (args.length == 0) {
            p.sendRichMessage(ConfigProvider.INCORRECT_USAGE);
            return;
        }

        ArenaController arenaController = ExcellentDuels.getArenaController();
        String sub = args[0].toLowerCase();

        switch (sub) {

            case "create": {
                if (args.length < 2) {
                    p.sendRichMessage(ConfigProvider.INCORRECT_USAGE);
                    return;
                }

                String name = args[1].toLowerCase();

                ArenaController.Arena arena =
                        new ArenaController.Arena(name, List.of(), null, null, null, null, null);

                arenaController.saveArena(arena);
                p.sendRichMessage(ConfigProvider.ARENA_CREATED.replace("%arena%", name));
                break;
            }

            case "corner1":
            case "corner2": {
                if (args.length < 2) {
                    p.sendRichMessage(ConfigProvider.INCORRECT_USAGE);
                    return;
                }

                ArenaController.Arena arena = ArenaController.arenaList.get(args[1].toLowerCase());
                if (arena == null) {
                    p.sendRichMessage(ConfigProvider.NO_SUCH_ARENA);
                    return;
                }

                if (sub.equals("corner1")) {
                    arena.setCorner1(p.getLocation());
                    p.sendRichMessage(ConfigProvider.CORNER1_SET.replace("%arena%", arena.getName()));
                } else {
                    arena.setCorner2(p.getLocation());
                    p.sendRichMessage(ConfigProvider.CORNER2_SET.replace("%arena%", arena.getName()));
                }

                arenaController.saveArena(arena);
                break;
            }

            case "spawn1":
            case "spawn2": {
                if (args.length < 2) {
                    p.sendRichMessage(ConfigProvider.INCORRECT_USAGE);
                    return;
                }

                ArenaController.Arena arena = ArenaController.arenaList.get(args[1].toLowerCase());
                if (arena == null) {
                    p.sendRichMessage(ConfigProvider.NO_SUCH_ARENA);
                    return;
                }

                if (sub.equals("spawn1")) {
                    arena.setSpawnPos1(p.getLocation());
                    p.sendRichMessage(ConfigProvider.FIRST_SPAWN_SET.replace("%arena%", arena.getName()));
                } else {
                    arena.setSpawnPos2(p.getLocation());
                    p.sendRichMessage(ConfigProvider.SECOND_SPAWN_SET.replace("%arena%", arena.getName()));
                }

                arenaController.saveArena(arena);
                break;
            }

            case "seticon": {
                if (args.length < 2) {
                    p.sendRichMessage(ConfigProvider.INCORRECT_USAGE);
                    return;
                }

                ArenaController.Arena arena = ArenaController.arenaList.get(args[1].toLowerCase());
                if (arena == null) {
                    p.sendRichMessage(ConfigProvider.NO_SUCH_ARENA);
                    return;
                }

                ItemStack item = p.getInventory().getItemInMainHand();
                if (item.getType().isAir()) {
                    p.sendMessage("§cHold an item in your hand.");
                    return;
                }

                arena.setArenaIcon(item.clone());
                arenaController.saveArena(arena);

                p.sendRichMessage(ConfigProvider.ARENA_ICON_SET.replace("%arena%", arena.getName()));
                break;
            }

            case "addkit": {
                if (args.length < 3) {
                    p.sendRichMessage(ConfigProvider.INCORRECT_USAGE);
                    return;
                }

                ArenaController.Arena arena = ArenaController.arenaList.get(args[1].toLowerCase());
                if (arena == null) {
                    p.sendRichMessage(ConfigProvider.NO_SUCH_ARENA);
                    return;
                }

                KitDataController.Kit kit =
                        ExcellentDuels.getKitDataController().getKitByName(args[2]);

                if (kit == null) {
                    p.sendRichMessage(ConfigProvider.NO_SUCH_KIT);
                    return;
                }

                if (!arena.getAvailableKits().contains(kit)) {
                    arena.getAvailableKits().add(kit);
                    arenaController.saveArena(arena);
                }

                p.sendRichMessage(ConfigProvider.KIT_ADDED_TO_ARENA
                        .replace("%kit%", kit.getName())
                        .replace("%arena%", arena.getName()));
                break;
            }

            case "removekit": {
                if (args.length < 3) {
                    p.sendRichMessage(ConfigProvider.INCORRECT_USAGE);
                    return;
                }

                ArenaController.Arena arena = ArenaController.arenaList.get(args[1].toLowerCase());
                if (arena == null) {
                    p.sendRichMessage(ConfigProvider.NO_SUCH_ARENA);
                    return;
                }

                arena.getAvailableKits()
                        .removeIf(k -> k.getName().equalsIgnoreCase(args[2]));

                arenaController.saveArena(arena);

                p.sendRichMessage(ConfigProvider.KIT_REMOVED_FROM_ARENA
                        .replace("%kit%", args[2])
                        .replace("%arena%", arena.getName()));
                break;
            }

            default:
                p.sendRichMessage(ConfigProvider.INCORRECT_USAGE);
        }
    }
}