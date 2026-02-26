package dragon.me.excellentDuels.hooks.placeholderapi;

import dragon.me.excellentDuels.ExcellentDuels;
import dragon.me.excellentDuels.api.models.Match;
import dragon.me.excellentDuels.api.models.Team;
import dragon.me.excellentDuels.controllers.GameController;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderExpansion extends me.clip.placeholderapi.expansion.PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "exduels";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Dragon";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.2.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equalsIgnoreCase("kit")){
            if (GameController.isPlayerInDuel(player.getName())){
                Match match = ExcellentDuels.getGameController().get(player);
                return match.getKitName();
            }
        }else if (params.equalsIgnoreCase("arena")){
            if (GameController.isPlayerInDuel(player.getName())){
                Match match = ExcellentDuels.getGameController().get(player);
                return  match.getArenaName();
            }

        }else if (params.startsWith("teams_detailed_")) {

            if (!GameController.isPlayerInDuel(player.getName())) {
                return "";
            }

            Match match = ExcellentDuels.getGameController().get(player);

            String numberPart = params.replace("teams_detailed_", "");

            int index;
            try {
                index = Integer.parseInt(numberPart) - 1;
            } catch (NumberFormatException e) {
                return "";
            }

            List<String> lines = new ArrayList<>();

            for (Team team : match.getTeams()) {

                String teamName = team.getPlayers().get(0).getName() + "'s Team";
                lines.add(teamName);

                for (Player p : team.getPlayers()) {
                    lines.add( " • " + match.getParticipantStatuses().get(p.getName()) + " " + p.getName());
                }
            }

            if (index >= 0 && index < lines.size()) {
                return lines.get(index);
            }

            return "";
        }

        return "";
    }
}
