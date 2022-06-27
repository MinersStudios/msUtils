package github.minersStudios.msUtils.commands.other;

import github.minersStudios.msUtils.enums.ResourcePackType;
import github.minersStudios.msUtils.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import javax.annotation.Nonnull;

public class ResourcePackCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		if (!(sender instanceof Player player)) {
			return ChatUtils.sendError(sender, "Только игрок может использовать эту команду!");
		} else {
			player.openInventory(ResourcePackType.getInventory());
		}
		return true;
	}

}
