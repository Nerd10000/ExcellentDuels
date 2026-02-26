package dragon.me.excellentDuels.api.command;

import dragon.me.excellentDuels.ExcellentDuels;
import dragon.me.excellentDuels.controllers.KitDataController;
import dragon.me.excellentDuels.hooks.PlaceholderAPIHook;
import dragon.me.excellentDuels.utils.ConfigProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public interface KitsCommandProvider {

    default void reimplementation(CommandSourceStack stack,String[] args){
        if (!(stack.getSender() instanceof Player player)) {
            stack.getSender().sendRichMessage(
                    "⏵<yellow><u>ExcellentDuels</u></yellow> » <red>Only players can execute this command.</red>"
            );
            return;
        }

        if (args.length < 2) {
            player.sendRichMessage(PlaceholderAPIHook.format(player,ConfigProvider.INCORRECT_USAGE));
            return;
        }

        String sub = args[0].toLowerCase();
        String kitName = args[1];

        KitDataController controller = ExcellentDuels.getKitDataController();

        switch (sub) {

            case "create" -> {


                List<ItemStack> items = new ArrayList<>();
                for (ItemStack item : player.getInventory().getStorageContents()) {
                    if (item != null) {
                        items.add(item.clone());
                    }
                }

                List<ItemStack> armor = new ArrayList<>();
                for (ItemStack item : player.getInventory().getArmorContents()) {
                    if (item != null) {
                        armor.add(item.clone());
                    }
                }

                ItemStack offhand = player.getInventory().getItemInOffHand();
                if (offhand != null) offhand = offhand.clone();

                KitDataController.Kit kit =
                        new KitDataController.Kit(kitName, items, armor, offhand);

                controller.saveKit(kit);

                player.sendRichMessage(
                        PlaceholderAPIHook.format(player,ConfigProvider.KIT_CREATED.replace("%kit%", kitName))
                );
            }
            case "give" -> {
                KitDataController.Kit kit = controller.getKitByName(kitName);

                if (kit == null) {
                    player.sendRichMessage(PlaceholderAPIHook.format(player,ConfigProvider.NO_SUCH_KIT));
                    return;
                }

                player.getInventory().clear();
                player.getInventory().setArmorContents(null);

                player.getInventory().setContents(
                        kit.getItems().stream()
                                .map(ItemStack::clone)
                                .toArray(ItemStack[]::new)
                );

                List<ItemStack> armor = kit.getArmorItems();
                if (armor != null && armor.size() == 4) {
                    player.getInventory().setArmorContents(
                            armor.stream()
                                    .map(ItemStack::clone)
                                    .toArray(ItemStack[]::new)
                    );
                }

                if (kit.getOffhand() != null) {
                    player.getInventory().setItemInOffHand(kit.getOffhand().clone());
                }

                player.updateInventory();
            }
        }
    }
    default List<String> reimplementationSuggestion(CommandSourceStack stack,String[] args){
        if (args.length == 0) {
            return List.of("create", "give");
        }

        if (args.length == 1) {
            return List.of("create", "give").stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .toList();
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            return ExcellentDuels.getKitDataController().getAllKits().stream()
                    .map(KitDataController.Kit::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }

        return List.of();
    }
}
