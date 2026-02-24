package dragon.me.excellentDuels.api.abstractions;

import dragon.me.excellentDuels.api.command.AcceptDuelProvider;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Team implements AcceptDuelProvider {

    private List<Player> players = new ArrayList<>();

     public Team(List<Player> players){
         this.players = players;

     }

}
