package dragon.me.excellentDuels.utils;

import dragon.me.excellentDuels.ExcellentDuels;
import dragon.me.excellentDuels.controllers.InviteController;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Consumer;

public final class GeneralScheduler implements Consumer<BukkitTask> {

    @Override
    public void accept(final BukkitTask task) {
        if (InviteController.inviteList == null || InviteController.inviteList.isEmpty()) {
            return;
        }

        ExcellentDuels.getInviteController().tick();
    }
}