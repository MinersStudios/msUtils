package github.minersStudios.msUtils.classes;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.enums.Pronouns;
import github.minersStudios.msUtils.enums.ResourcePackType;
import github.minersStudios.msUtils.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class RegistrationProcess {
    private Player player;
    private PlayerInfo playerInfo;

    public void registerPlayer(@Nonnull PlayerInfo playerInfo){
        this.playerInfo = playerInfo;
        this.player = playerInfo.getOnlinePlayer();

        if(this.player == null) return;
        player.playSound(player.getLocation(), Sound.MUSIC_DISC_FAR, 0.15f, 1.25f);

        if(!playerInfo.hasPlayerDataFile()){
            playerInfo.createPlayerDataFile();
        }
        ChatUtils.sendDialogueMessage(player, "Оу...", 100L);
        ChatUtils.sendDialogueMessage(player, "Крайне странное местечко.", 150L);
        ChatUtils.sendDialogueMessage(player, "Ничего не напоминает?", 225L);
        ChatUtils.sendDialogueMessage(player, "Ну ладно...", 300L);
        ChatUtils.sendDialogueMessage(player, "Где-то я уже тебя видел.", 350L);
        ChatUtils.sendDialogueMessage(player, "Напомни-ка своё имя.", 400L);
        ChatUtils.sendDialogueMessage(player, "Только говори честно, иначе буду тебя ошибочно називать до конца дней своих.", 450L);
        Bukkit.getScheduler().runTaskLater(Main.plugin, this::setFirstname, 550L);
    }

    private void setFirstname(){
        SignMenu menu = new SignMenu(Arrays.asList("", "---===+===---", "Введите выше", "имя"))
                .reopenIfFail(true)
                .response((player, strings) -> {
                    if (strings[0].matches("[А-ЯҐІЁ][-А-яҐґІіЁё]+")) {
                        this.playerInfo.setFirstname(strings[0]);
                        ChatUtils.sendDialogueMessage(this.player, "Интересно...", 25L);
                        ChatUtils.sendDialogueMessage(this.player, "За свою жизнь я многих повидал с таким именем", 100L);
                        ChatUtils.sendDialogueMessage(this.player, "Но тебя вижу впервые", 225L);
                        ChatUtils.sendDialogueMessage(this.player, "Можешь, пожалуйста, уточнить свою фамилию и отчество?", 300L);
                        Bukkit.getScheduler().runTaskLater(Main.plugin, this::setLastname, 375L);
                    } else {
                        this.player.sendMessage(ChatColor.GOLD + " Используйте только кириллицу");
                        return false;
                    }
                    return true;
                });
        menu.open(this.player);
    }

    private void setLastname(){
        SignMenu menu = new SignMenu(Arrays.asList("", "---===+===---", "Введите выше", "фамилию"))
                .reopenIfFail(true)
                .response((player, strings) -> {
                    if (strings[0].matches("[А-ЯҐІЁ][-А-яҐґІіЁё]+")) {
                        this.playerInfo.setLastName(strings[0]);
                        this.setPatronymic();
                    } else {
                        this.player.sendMessage(ChatColor.GOLD + " Используйте только кириллицу");
                        return false;
                    }
                    return true;
                });
        menu.open(this.player);
    }

    private void setPatronymic() {
        SignMenu menu = new SignMenu(Arrays.asList("", "---===+===---", "Введите выше", "отчество"))
                .reopenIfFail(true)
                .response((player, strings) -> {
                    if (strings[0].matches("[А-ЯҐІЁ][-А-яҐґІіЁё]+")) {
                        this.playerInfo.setPatronymic(strings[0]);
                        new PlayerID().addPlayer(this.player.getUniqueId());
                        ChatUtils.sendDialogueMessage(player,
                                "Ну вот и отлично, "
                                        + ChatColor.GRAY + "[" + this.playerInfo.getID() + "] "
                                        + ChatColor.WHITE + this.playerInfo.getFirstname() + " "
                                        + this.playerInfo.getLastname() + " "
                                        + this.playerInfo.getPatronymic(), 25L);
                        ChatUtils.sendDialogueMessage(this.player, "Слушай...", 100L);
                        ChatUtils.sendDialogueMessage(this.player, "А как мне к тебе обращаться?", 150L);
                        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> this.player.openInventory(Pronouns.getInventory()), 225L);
                    } else {
                        this.player.sendMessage(ChatColor.GOLD + " Используйте только кириллицу");
                        return false;
                    }
                    return true;
                });
        menu.open(this.player);
    }

    public void setPronouns(@Nonnull PlayerInfo playerInfo){
        this.playerInfo = playerInfo;
        this.player = playerInfo.getOnlinePlayer();
        if(playerInfo.getPronouns() == null) return;
        ChatUtils.sendDialogueMessage(this.player, "Славно", 25L);
        ChatUtils.sendDialogueMessage(this.player, "Ну что же,", 75L);
        ChatUtils.sendDialogueMessage(this.player, "мне уже пора", 125L);
        ChatUtils.sendDialogueMessage(this.player, "Хорошей " + playerInfo.getPronouns().getPronouns() + " дороги, " + playerInfo.getPronouns().getTraveler(), 175L);
        Bukkit.getScheduler().runTaskLater(Main.plugin, this::setOther, 225L);
    }

    private void setOther(){
        this.player.setDisplayName("[" + this.playerInfo.getID() + "] " + this.playerInfo.getFirstname() + " " + this.playerInfo.getLastname());
        if(this.playerInfo.getResourcePackType() == null){
            Bukkit.getScheduler().runTask(Main.plugin, () -> this.player.openInventory(ResourcePackType.getInventory()));
        } else if (this.playerInfo.getResourcePackType() == ResourcePackType.NONE){
            Bukkit.getScheduler().runTask(Main.plugin, this.playerInfo::teleportToLastLeaveLocation);
        } else {
            ResourcePackType.setResourcePack(this.playerInfo);
        }
    }
}
