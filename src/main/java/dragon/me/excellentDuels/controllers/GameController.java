package dragon.me.excellentDuels.controllers;

import dragon.me.excellentDuels.api.abstractions.Match;
import dragon.me.excellentDuels.api.abstractions.Team;
import dragon.me.excellentDuels.controllers.enums.GameState;
import dragon.me.excellentDuels.controllers.enums.PlayerStatus; // Import PlayerStatus

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors; // Import Collectors

public class GameController {

    public  static List<Match> gameList = new ArrayList<>();

    public GameController(){

    }

    public void addGame(Match g){
        gameList.add(g);
    }

    public void removeGame(Match game) {
        gameList.remove(game);
    }
    public static  void setGameState(Match game,GameState state){
        game.setState(state);

    }
    public static List<Player> getOpponents(Player player, Match game) {
        if (player == null || game == null || game.getTeams() == null) {
            return Collections.emptyList();
        }

        List<Player> opponents = new ArrayList<>();

        for (Team team : game.getTeams()) {
            if (team == null || team.getPlayers() == null) continue;

            if (!team.getPlayers().contains(player)) {
                opponents.addAll(team.getPlayers());
            }
        }

        return opponents;
    }

    public static boolean isPlayerInDuel(String name) {
        if (name == null) return false;

        return gameList.stream()
                .flatMap(match -> match.getTeams().stream())
                .flatMap(team -> team.getPlayers().stream())
                .anyMatch(player -> player.getName().equalsIgnoreCase(name));
    }

    public static boolean isArenaInUsage(String arenaName){
        for (Match g : gameList){
            if (g.getArenaName().equals(arenaName)){
                return  true;
            }
        }
        return false;
    }

    public Match get(Player participant) {
        if (participant == null) return null;

        for (Match match : gameList) {
            if (match.getTeams() == null) continue;

            for (Team team : match.getTeams()) {
                if (team.getPlayers() == null) continue;
                if (team.getPlayers().contains(participant)) {
                    return match; // Found the match
                }
            }
        }

        return null;
    }

    public static boolean isTeamEliminated(Match match, Team team) {
        return team.getPlayers().stream()
                .allMatch(player -> match.getParticipantStatuses().get(player.getName()) == PlayerStatus.DEAD);
    }

    public static List<Team> getRemainingTeams(Match match) {
        return match.getTeams().stream()
                .filter(team -> team.getPlayers().stream()
                        .anyMatch(player -> match.getParticipantStatuses().get(player.getName()) == PlayerStatus.LIVING))
                .collect(Collectors.toList());
    }

    public static Team getWinningTeam(Match match) {
        List<Team> remainingTeams = getRemainingTeams(match);
        if (remainingTeams.size() == 1) {
            return remainingTeams.get(0);
        }
        return null;
    }

    public static boolean isMatchOver(Match match) {
        return getRemainingTeams(match).size() <= 1;
    }
}
