package dragon.me.excellentDuels.controllers;

import dragon.me.excellentDuels.utils.ConfigProvider;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class InviteController {

    public  static final  List<Invite> inviteList = new ArrayList<>();

    public InviteController(){

    }
    public void addInvite(Invite invite){
        inviteList.add(invite);
    }

    public static void tick() {
        if (inviteList == null || inviteList.isEmpty()) {
            return;
        }
        if ((System.currentTimeMillis() - inviteList.get(0).timestamp) <= 1000L * ConfigProvider.INVITE_EXPIRE_TIME) return;

        Invite invite = inviteList.remove(0); // ATOMIC: cannot go out of bounds

        String message = ConfigProvider.INVITE_EXPIRED_MESSAGE
                .replace("%player1%", invite.getPlayer1().getName());

        invite.getPlayer1().sendRichMessage(message);
        invite.getPlayer2().sendRichMessage(message);
    }
    public  void remove(Invite invite){
        inviteList.remove(invite);
    }

    public static class Invite {
        @Getter
        private final Player player1;
        @Getter
        private final Player player2;
        @Getter
        private final long timestamp;
        @Getter
        private final String kitName;

        public Invite(Player player1, Player player2,String kitName){
            this.timestamp = System.currentTimeMillis();
            this.player1= player1;
            this.player2 = player2;
            this.kitName = kitName;
        }
    }
}
