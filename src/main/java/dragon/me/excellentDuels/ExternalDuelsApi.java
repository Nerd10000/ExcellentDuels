package dragon.me.excellentDuels;

import dragon.me.excellentDuels.api.DuelsApi;
import dragon.me.excellentDuels.controllers.*;
import dragon.me.excellentDuels.utils.inventory.PersistentInventoryManager;

public class ExternalDuelsApi implements DuelsApi {

    @Override
    public ArenaController getArenaController() {
        return ExcellentDuels.getArenaController();
    }

    @Override
    public GameController getGameController() {
        return ExcellentDuels.getGameController();
    }

    @Override
    public InventoryController getInventoryController() {
        return ExcellentDuels.getInventoryController();
    }

    @Override
    public InviteController getInviteController() {
        return ExcellentDuels.getInviteController();
    }

    @Override
    public KitDataController getKitDataController() {
        return ExcellentDuels.getKitDataController();
    }

    @Override
    public PersistentInventoryManager getPersistentInventoryController() {
        return ExcellentDuels.getPersistentInventoryManager();
    }

}
