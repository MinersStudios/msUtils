package github.minersStudios.msUtils.tabComplete;

import github.minersStudios.msUtils.classes.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AllPlayers implements TabCompleter {

	@Nullable
	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			if (!command.getName().equals("unban")) {
				for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
					if (offlinePlayer != null) {
						PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
						String ID = String.valueOf(playerInfo.getID());
						switch (command.getName()) {
							case "mute" -> {
								if (playerInfo.isMuted()) break;
								if (playerInfo.hasPlayerDataFile())
									completions.add(ID);
								if (playerInfo.hasPlayerDataFile())
									completions.add(offlinePlayer.getName());
							}
							case "unmute" -> {
								if (!playerInfo.isMuted()) break;
								if (playerInfo.hasPlayerDataFile())
									completions.add(ID);
								if (playerInfo.hasPlayerDataFile())
									completions.add(offlinePlayer.getName());
							}
							case "ban" -> {
								if (playerInfo.isBanned()) break;
								if (playerInfo.hasPlayerDataFile())
									completions.add(ID);
								completions.add(offlinePlayer.getName());
							}
							default -> {
								if (playerInfo.hasPlayerDataFile())
									completions.add(ID);
								if (playerInfo.hasPlayerDataFile())
									completions.add(offlinePlayer.getName());
							}
						}
					}
				}
			} else {
				for (OfflinePlayer offlinePlayer : Bukkit.getBannedPlayers()) {
					if (offlinePlayer != null) {
						PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
						String ID = String.valueOf(playerInfo.getID());
						if (playerInfo.hasPlayerDataFile())
							completions.add(ID);
						completions.add(offlinePlayer.getName());
					}
				}
			}
		}
		return completions;
	}
}
