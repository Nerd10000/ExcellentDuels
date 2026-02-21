package dragon.me.excellentDuels.controllers;

import dragon.me.excellentDuels.controllers.enums.GameState;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    public  static List<Game> gameList = new ArrayList<>();

    public GameController(){

    }

    public void addGame(Game g){
        gameList.add(g);
    }

    public void removeGame(Game game) {
        gameList.remove(game);
    }
    public static  void setGameState(Game game,GameState state){
        game.setState(state);

    }
    public static Player getOpponent(Player player, Game game) {
        if (game.getTeamRed().contains(player)) {
            return game.getTeamBlue().get(0); // Assuming 1v1 duel
        } else if (game.getTeamBlue().contains(player)) {
            return game.getTeamRed().get(0); // Assuming 1v1 duel
        }
        return null; // Should not happen in a valid duel context
    }

    public static boolean isPlayerInDuel(String name) {
        return gameList.stream()
                .anyMatch(game -> game.getTeamRed().stream().anyMatch(player -> player.getName().equals(name)) ||
                                   game.getTeamBlue().stream().anyMatch(player -> player.getName().equals(name)));
    }

    public static boolean isArenaInUsage(String arenaName){
        for (Game g : gameList){
            if (g.getArenaName().equals(arenaName)){
                return  true;
            }
        }
        return false;
    }

    @Getter
    public static  class Game {
        private final List<Player> teamBlue,teamRed;
        private  final String arenaName;
        @Setter
        private GameState state;
        private String kitName;
        public Game(List<Player> teamBlue, List<Player> teamRed, String arenaName,String kitName){
            this.teamBlue = (teamBlue != null) ? new ArrayList<>(teamBlue) : new ArrayList<>();
            this.teamRed = (teamRed != null) ? new ArrayList<>(teamRed) : new ArrayList<>();
            this.arenaName = arenaName;
            this.kitName = kitName;
        }

    }

}
