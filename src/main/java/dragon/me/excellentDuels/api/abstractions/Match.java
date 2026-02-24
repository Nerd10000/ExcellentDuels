package dragon.me.excellentDuels.api.abstractions;

import dragon.me.excellentDuels.ExcellentDuels;
import dragon.me.excellentDuels.controllers.ArenaController;
import dragon.me.excellentDuels.controllers.GameController;
import dragon.me.excellentDuels.controllers.KitDataController;
import dragon.me.excellentDuels.controllers.enums.GameState;
import dragon.me.excellentDuels.controllers.enums.PlayerStatus;
import dragon.me.excellentDuels.utils.ConfigProvider;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
@Getter
public class Match {

    private List<Team> teams;
    @Setter
    private GameState state;
    private String arenaName;
    private  String kitName;
    private final HashMap<String, PlayerStatus> participantStatuses = new HashMap<>();

    public Match(List<Team> teams, String arenaName, String kitName){
        this.teams = teams;
        this.arenaName = arenaName;
        this.kitName = kitName;
        for (Team m : teams){
            for (Player player : m.getPlayers()){
                participantStatuses.put(player.getName(),PlayerStatus.LIVING);
            }
        }
    }



    public void startCountdown(int startSeconds){

        AtomicInteger countdown = new AtomicInteger(startSeconds);

        Bukkit.getScheduler().runTaskTimer(ExcellentDuels.getPlugin(), task -> {

            int timeLeft = countdown.get();
            Component subtitle = MiniMessage.miniMessage().deserialize(ConfigProvider.COUNTDOWN_SUBTITLE);

            if (timeLeft > 0) {
                sendCountdownTitle(this, timeLeft, subtitle, 1);
            } else {
                sendCountdownTitle(this, 0, subtitle, 2);
                GameController.setGameState(this, GameState.ON_GOING);
                task.cancel();
            }

            countdown.decrementAndGet();

        }, 0L, 20L);
    }

    private void sendCountdownTitle(Match match, int seconds, Component subtitle, int durationSeconds){
        String path = seconds > 0 ? "messages.countdown." + seconds : null;
        String message = path != null
                ? ExcellentDuels.getPlugin().getConfig().getString(path,
                ConfigProvider.COUNTDOWN_DEFAULT.replace("%number%", String.valueOf(seconds)))
                : ConfigProvider.COUNTDOWN_GO;

        Component title = MiniMessage.miniMessage().deserialize(message);
        Title.Times times = Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(durationSeconds), Duration.ofSeconds(0));

        for (Team team : match.getTeams()) {
            for (Player player : team.getPlayers()) {
                player.showTitle(Title.title(title, subtitle, times));
            }
        }
    }

    public void applyKit(Player player, KitDataController.Kit kit){
        setPlayerInventory(player, kit); // Keep the old helper for inventory
    }

    public void teleportTeams(ArenaController.Arena arena){
        for (int i = 0; i < teams.size(); i++) {
            if (i >= arena.getSpawnPositions().size()) {
                // Handle the case where there are more teams than spawn points
                break;
            }
            for (Player player : teams.get(i).getPlayers()) {
                player.teleport(arena.getSpawnPositions().get(i));
            }
        }
    }
    private void setPlayerInventory(Player player, KitDataController.Kit kit) {

        player.getInventory().setContents(kit.getItems().toArray(new ItemStack[0]));
        player.getInventory().setItemInOffHand(kit.getOffhand());
        player.getInventory().setArmorContents(kit.getArmorItems().toArray(new ItemStack[0]));
    }

}