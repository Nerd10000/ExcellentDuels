package dragon.me.excellentDuels.api;

import dragon.me.excellentDuels.controllers.*;
import dragon.me.excellentDuels.utils.ConfigProvider;
import dragon.me.excellentDuels.utils.inventory.PersistentInventoryManager;

public interface DuelsApi {

    ArenaController getArenaController();
    GameController getGameController();
    InventoryController getInventoryController();
    InviteController getInviteController();
    KitDataController getKitDataController();
    PersistentInventoryManager getPersistentInventoryController();


}
