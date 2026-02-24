package dragon.me.excellentDuels.listener;

import dragon.me.excellentDuels.ExcellentDuels;
import dragon.me.excellentDuels.api.abstractions.Match;
import dragon.me.excellentDuels.controllers.GameController;
import dragon.me.excellentDuels.controllers.enums.GameState;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Optional;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onEvent(PlayerMoveEvent e){
        Player p = e.getPlayer();
        Optional<Match> gameOptional = GameController.gameList.stream()
                .filter(game ->game.getTeams().stream()
                        .anyMatch(team -> team.getPlayers().contains(p))).findFirst();
        if (gameOptional.isEmpty()){
            return;
        }

        Match game = gameOptional.get();


        if (game.getState() == GameState.STARTING){

            Location finalLoc = e.getFrom();
            finalLoc.setYaw(e.getTo().getYaw());
            finalLoc.setPitch(e.getTo().getPitch());

            p.teleport(finalLoc);

        }
    }
}
