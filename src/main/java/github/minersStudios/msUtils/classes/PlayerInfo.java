package github.minersStudios.msUtils.classes;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.enums.Pronouns;
import github.minersStudios.msUtils.enums.ResourcePackType;
import github.minersStudios.msUtils.utils.ChatUtils;
import github.minersStudios.msUtils.utils.PlayerUtils;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PlayerInfo {
	private final File dataFile;
	@Getter private final YamlConfiguration yamlConfiguration;
	@Getter @Nonnull private final UUID uuid;
	private Player onlinePlayer;
	private String nickname, firstname, lastname, patronymic;

	public PlayerInfo(@Nonnull UUID uuid) {
		this.dataFile = new File(Main.plugin.getDataFolder(), "players/" + uuid + ".yml");
		this.yamlConfiguration = YamlConfiguration.loadConfiguration(this.dataFile);
		this.uuid = uuid;
	}

	public PlayerInfo(@Nonnull UUID uuid, @Nonnull String nickname) {
		this.dataFile = new File(Main.plugin.getDataFolder(), "players/" + uuid + ".yml");
		this.yamlConfiguration = YamlConfiguration.loadConfiguration(this.dataFile);
		this.uuid = uuid;
		this.nickname = nickname;
	}

	@Nonnull
	public String getDefaultName() {
		return "[" + this.getID() + "] " + this.getFirstname() + " " + this.getLastname();
	}

	@Nonnull
	public String getGoldenName() {
		return net.md_5.bungee.api.ChatColor.of("#fcf5c7") + "[" + this.getID() + "] " + net.md_5.bungee.api.ChatColor.of("#ffee93") + this.getFirstname() + " " + this.getLastname();
	}

	@Nonnull
	public String getGrayIDGoldName() {
		return ChatColor.GRAY + "[" + this.getID() + "] " + ChatColor.GOLD + this.getFirstname() + " " + this.getLastname();
	}

	@Nonnull
	public String getGrayIDGreenName() {
		return ChatColor.GRAY + "[" + this.getID() + "] " + ChatColor.GREEN + this.getFirstname() + " " + this.getLastname();
	}

	/**
	 * Sets player ip in data file
	 *
	 * @param ip player ip
	 */
	public void setIP(String ip) {
		if (this.getOnlinePlayer() == null) return;
		List<String> ips = this.yamlConfiguration.getStringList("ip");
		ips.add(ip);
		this.yamlConfiguration.set("ip", ips);
		this.savePlayerDataFile();
	}

	/**
	 * Gets player ip from data file
	 *
	 * @return player ip
	 */
	@Nullable
	public List<String> getIP() {
		return this.yamlConfiguration.getStringList("ip");
	}

	/**
	 * Gets offline player by UUID
	 *
	 * @return offline player
	 */
	@Nonnull
	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(uuid);
	}

	/**
	 * Gets online player by offline player
	 *
	 * @return online player if it's online, else null
	 */
	@Nullable
	public Player getOnlinePlayer() {
		return this.onlinePlayer == null ? this.onlinePlayer = this.getOfflinePlayer().getPlayer() != null && this.getOfflinePlayer().isOnline()
					? this.getOfflinePlayer().getPlayer()
					: null
				: this.onlinePlayer;
	}

	/**
	 * Gets player ID by UUID
	 *
	 * @return player ID
	 */
	public int getID() {
		return this.hasPlayerDataFile() ? new PlayerID().getPlayerID(this.uuid) : 0;
	}

	/**
	 * Gets player nickname by offline player
	 *
	 * @return player nickname
	 */
	@Nullable
	public String getNickname() {
		return this.nickname == null ? this.nickname = this.getOfflinePlayer().getName() : this.nickname;
	}


	/**
	 * Gets player firstname from data file
	 *
	 * @return player firstname if it's != null, else returns default firstname
	 */
	@Nonnull
	public String getFirstname() {
		return this.yamlConfiguration == null ? "????????"
				: this.firstname == null ? this.firstname = this.yamlConfiguration.getString("name.firstname", "????????")
				: this.firstname;
	}

	/**
	 * Sets player firstname in data file
	 *
	 * @param firstname player's first name
	 */
	public void setFirstname(@Nonnull String firstname) {
		if (!this.hasPlayerDataFile()) return;
		this.firstname = firstname;
		this.yamlConfiguration.set("name.firstname", firstname);
		this.savePlayerDataFile();
	}

	/**
	 * Gets player lastname from data file
	 *
	 * @return player lastname if it's != null, else returns default lastname
	 */
	@Nonnull
	public String getLastname() {
		return this.yamlConfiguration == null ? "????????????"
				: this.lastname == null ? this.lastname = this.yamlConfiguration.getString("name.lastname", "????????????")
				: this.lastname;
	}

	/**
	 * Sets player lastname in data file
	 *
	 * @param lastname player's last name
	 */
	public void setLastName(@Nonnull String lastname) {
		if (!this.hasPlayerDataFile()) return;
		this.lastname = lastname;
		this.yamlConfiguration.set("name.lastname", lastname);
		this.savePlayerDataFile();
	}


	/**
	 * Gets player patronymic from data file
	 *
	 * @return player patronymic if it's != null, else returns default patronymic
	 */
	@Nonnull
	public String getPatronymic() {
		return this.yamlConfiguration == null ? "????????????"
				: this.patronymic == null ? this.patronymic = this.yamlConfiguration.getString("name.patronymic", "????????????")
				: this.patronymic;
	}

	/**
	 * Sets patronymic of player in data file
	 *
	 * @param patronymic player's patronymic
	 */
	public void setPatronymic(@Nonnull String patronymic) {
		if (!this.hasPlayerDataFile()) return;
		this.patronymic = patronymic;
		this.yamlConfiguration.set("name.patronymic", patronymic);
		this.savePlayerDataFile();
	}

	/**
	 * Gets player pronouns from data file
	 *
	 * @return player pronouns if it's != null, else returns null
	 */
	@Nonnull
	public Pronouns getPronouns() {
		return Pronouns.valueOf(this.yamlConfiguration.getString("pronouns", "HE"));
	}

	/**
	 * Sets player pronouns in data file
	 *
	 * @param pronouns player pronouns
	 */
	public void setPronouns(@Nonnull Pronouns pronouns) {
		if (!this.hasPlayerDataFile()) return;
		this.yamlConfiguration.set("pronouns", pronouns.name());
		this.savePlayerDataFile();
	}

	/**
	 * Gets player resource pack type from data file
	 *
	 * @return player resource pack type if it's != null, else returns null
	 */
	@Nullable
	public ResourcePackType getResourcePackType() {
		return ResourcePackType.getResourcePackByString(this.yamlConfiguration.getString("resource-pack", "NULL"));
	}

	/**
	 * Sets resource pack type in data file
	 *
	 * @param resourcePackType resource pack type : "FULL/LITE/NONE"
	 */
	public void setResourcePackType(@Nonnull ResourcePackType resourcePackType) {
		if (!this.hasPlayerDataFile()) return;
		this.yamlConfiguration.set("resource-pack", resourcePackType.name());
		savePlayerDataFile();
	}

	/**
	 * Gets player disk type from data file
	 *
	 * @return player disk type if it's != null, else returns default disk type
	 */
	@Nonnull
	public ResourcePackType.DiskType getDiskType() {
		return ResourcePackType.DiskType.valueOf(this.yamlConfiguration.getString("disk-type", "DROPBOX"));
	}

	/**
	 * Sets disk type in data file
	 *
	 * @param diskType disk type : "DROPBOX/YANDEX_DISK"
	 */
	public void setDiskType(@Nullable ResourcePackType.DiskType diskType) {
		if (!this.hasPlayerDataFile()) return;
		this.yamlConfiguration.set("disk-type", diskType != null ? diskType.name() : null);
		savePlayerDataFile();
	}

	/**
	 * @return True if player is muted
	 */
	public boolean isMuted() {
		return this.yamlConfiguration.getBoolean("bans.muted", false);
	}

	public boolean setMuted(boolean value, CommandSender commandSender) {
		return this.setMuted(value, 0, null, commandSender);
	}

	/**
	 * Mutes/unmutes player
	 *
	 * @param value mute value
	 * @param time mutes for time
	 * @param reason mute reason
	 */
	public boolean setMuted(boolean value, long time, @Nullable String reason, CommandSender sender) {
		if (this.getNickname() == null || !this.hasPlayerDataFile())
			return ChatUtils.sendWarning(sender, "???????????? ?????????? ?????? ???? ???????? ???? ?????????? ???? ??????????????");
		this.yamlConfiguration.set("bans.muted", value);
		this.yamlConfiguration.set("time.muted-to", time);
		this.yamlConfiguration.set("bans.mute-reason", reason);
		savePlayerDataFile();
		Player player = this.getOnlinePlayer();
		if (value && player != null && player.getAddress() != null) {
			ChatUtils.sendFine(
					sender,
					"?????????? : \"" + this.getGrayIDGreenName() + " (" + this.getNickname() + ")\" ?????? ?????????????? : "
					+ "\n    - ?????????????? : \""
					+ reason
					+ "\"\n    - ???? : "
					+ Instant.ofEpochMilli(time).atZone(
					sender instanceof Player senderPlayer && senderPlayer.getAddress() != null
							? ZoneId.of(PlayerUtils.getTimezone(senderPlayer.getAddress()))
							: ZoneId.systemDefault()
					).format(DateTimeFormatter.ofPattern("EEE, yyyy-MM-dd hh:mm z"))
			);
			ChatUtils.sendWarning(
					player,
					"???? ???????? ???????????????? : "
					+ "\n    - ?????????????? : \""
					+ reason
					+ "\"\n    - ???? : "
					+ Instant.ofEpochMilli(time).atZone(ZoneId.of(PlayerUtils.getTimezone(player.getAddress()))).format(DateTimeFormatter.ofPattern("EEE, yyyy-MM-dd hh:mm z"))
			);
		} else {
			ChatUtils.sendFine(sender, "?????????? : \"" + this.getGrayIDGreenName() + " (" + this.getNickname() + ")\" ?????? ????????????????");
			ChatUtils.sendWarning(player, "???? ???????? ??????????????????");
		}
		return true;
	}

	/**
	 * Gets player mute reason if it's != null, else returns default reason
	 *
	 * @return player mute reason
	 */
	@Nonnull
	public String getMuteReason() {
		return this.yamlConfiguration.getString("bans.mute-reason", "????????????????????");
	}

	/**
	 * @return True if player is banned
	 */
	public boolean isBanned() {
		return this.getNickname() != null && (Bukkit.getBanList(BanList.Type.NAME).isBanned(this.getNickname()) || this.yamlConfiguration.getBoolean("bans.banned", false));
	}

	public boolean setBanned(boolean value, CommandSender commandSender) {
		return this.setBanned(value, 0, null, commandSender);
	}

	/**
	 * Bans/unbans player
	 *
	 * @param value ban value
	 * @param time bans for time
	 * @param reason ban reason
	 */
	public boolean setBanned(boolean value, long time, @Nullable String reason, @Nullable CommandSender sender) {
		if (this.getNickname() == null)
			return ChatUtils.sendWarning(sender, "???????????? ?????????? ?????? ???? ???????? ???? ?????????? ???? ??????????????, ?????????????????????? ???????????????????? ????????????");
		if (this.hasPlayerDataFile()) {
			this.yamlConfiguration.set("bans.banned", value);
			this.yamlConfiguration.set("time.banned-to", time);
			this.yamlConfiguration.set("bans.ban-reason", reason);
			savePlayerDataFile();
		}
		if (value && sender != null) {
			Player player = this.getOnlinePlayer();
			Bukkit.getBanList(BanList.Type.NAME).addBan(this.getNickname(), reason, new Date(time), sender.getName());
			if (player != null && player.getAddress() != null) {
				this.setLastLeaveLocation(player);
				player.kickPlayer(
						ChatColor.RED + "\n??l???? ???????? ????????????????"
						+ ChatColor.DARK_GRAY + "\n\n<---====+====--->"
						+ ChatColor.GRAY + "\n?????????????? :\n\""
						+ reason
						+ "\"\n ???? : \n"
						+ Instant.ofEpochMilli(time).atZone(ZoneId.of(PlayerUtils.getTimezone(player.getAddress()))).format(DateTimeFormatter.ofPattern("EEE, yyyy-MM-dd hh:mm z"))
						+ ChatColor.DARK_GRAY + "\n<---====+====--->\n"
				);
			}
			ChatUtils.sendFine(sender,
					"?????????? : \"" + this.getGrayIDGreenName() + " (" + this.getNickname() + ")\" ?????? ?????????????? : "
					+ "\n    - ?????????????? : \""
					+ reason
					+ "\"\n    - ???? : "
					+ Instant.ofEpochMilli(time).atZone(
							sender instanceof Player senderPlayer && senderPlayer.getAddress() != null
							? ZoneId.of(PlayerUtils.getTimezone(senderPlayer.getAddress()))
							: ZoneId.systemDefault()
					).format(DateTimeFormatter.ofPattern("EEE, yyyy-MM-dd hh:mm z"))
			);
		} else {
			Bukkit.getBanList(BanList.Type.NAME).pardon(this.getNickname());
			ChatUtils.sendFine(sender, "?????????? : \"" + this.getGrayIDGreenName() + " (" + this.getNickname() + ")\" ?????? ????????????????");
		}
		return true;
	}

	/**
	 * Gets player ban reason if it's != null, else returns default reason
	 *
	 * @return player ban reason
	 */
	@Nonnull
	public String getBanReason() {
		return this.yamlConfiguration.getString("bans.ban-reason", "????????????????????");
	}

	/**
	 * Gets player first join time in milliseconds
	 *
	 * @return player first join time
	 */
	public long getFirstJoin() {
		return this.yamlConfiguration.getLong("time.first-join", 0);
	}

	/**
	 * Gets player banned to time in milliseconds
	 *
	 * @return player banned to time
	 */
	public long getBannedTo() {
		return this.yamlConfiguration.getLong("time.banned-to", 0);
	}

	/**
	 * Gets player muted to time in milliseconds
	 *
	 * @return player muted to time
	 */
	public long getMutedTo() {
		return this.yamlConfiguration.getLong("time.muted-to", 0);
	}

	/**
	 * Gets player last gamemode
	 *
	 * @return gamemode
	 */
	@Nonnull
	public GameMode getGameMode() {
		return GameMode.valueOf(this.yamlConfiguration.getString("gamemode", "SURVIVAL"));
	}

	/**
	 * Teleport player to dark world and adds some effects
	 */
	public void teleportToDarkWorld() {
		Player player = this.getOnlinePlayer();
		if (player == null) return;
		if (player.isDead())
			player.spigot().respawn();
		player.setGameMode(GameMode.SPECTATOR);
		player.teleport(new Location(Main.worldDark,  0.0d, 0.0d, 0.0d), PlayerTeleportEvent.TeleportCause.PLUGIN);
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, -1, true, false));
	}

	/**
	 * Teleport player to their last leave location and removes some effects
	 */
	public void teleportToLastLeaveLocation() {
		Player player = this.getOnlinePlayer();
		if (player == null) return;
		player.setGameMode(this.getGameMode());
		player.teleport(this.getLastLeaveLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
		player.removePotionEffect(PotionEffectType.INVISIBILITY);
		ChatUtils.sendJoinMessage(this, this.getOnlinePlayer());
	}

	/**
	 * Gets player last leave location from data file
	 *
	 * @return player last leave location
	 */
	@Nonnull
	public Location getLastLeaveLocation() {
		Location spawnLocation = Main.overworld.getSpawnLocation();
		return new Location(
				Bukkit.getWorld(this.yamlConfiguration.getString("locations.last-leave-location.world", spawnLocation.getBlock().getWorld().getName())),
				this.yamlConfiguration.getDouble("locations.last-leave-location.x", spawnLocation.getX()),
				this.yamlConfiguration.getDouble("locations.last-leave-location.y", spawnLocation.getY()),
				this.yamlConfiguration.getDouble("locations.last-leave-location.z", spawnLocation.getZ()),
				(float) this.yamlConfiguration.getDouble("locations.last-leave-location.yaw", spawnLocation.getYaw()),
				(float) this.yamlConfiguration.getDouble("locations.last-leave-location.pitch", spawnLocation.getPitch())
		);
	}

	/**
	 * Sets last leave location of player
	 *
	 * @param player online player
	 */
	public void setLastLeaveLocation(@Nonnull Player player) {
		if (!this.hasPlayerDataFile() || player.getWorld() == Main.worldDark) return;
		Location leaveLocation = player.getLocation(), respawnLocation = player.getBedSpawnLocation() != null ? player.getBedSpawnLocation() : Main.overworld.getSpawnLocation();
		this.yamlConfiguration.set("gamemode", player.getGameMode().toString());
		if (player.isDead()) {
			this.yamlConfiguration.set("locations.last-leave-location.world", respawnLocation.getBlock().getWorld().getName());
			this.yamlConfiguration.set("locations.last-leave-location.x", respawnLocation.getX());
			this.yamlConfiguration.set("locations.last-leave-location.y", respawnLocation.getY());
			this.yamlConfiguration.set("locations.last-leave-location.z", respawnLocation.getZ());
			this.yamlConfiguration.set("locations.last-leave-location.yaw", respawnLocation.getYaw());
			this.yamlConfiguration.set("locations.last-leave-location.pitch", respawnLocation.getPitch());
		} else {
			this.yamlConfiguration.set("locations.last-leave-location.world", leaveLocation.getBlock().getWorld().getName());
			this.yamlConfiguration.set("locations.last-leave-location.x", leaveLocation.getX());
			this.yamlConfiguration.set("locations.last-leave-location.y", leaveLocation.getY());
			this.yamlConfiguration.set("locations.last-leave-location.z", leaveLocation.getZ());
			this.yamlConfiguration.set("locations.last-leave-location.yaw", leaveLocation.getYaw());
			this.yamlConfiguration.set("locations.last-leave-location.pitch", leaveLocation.getPitch());
		}
		this.savePlayerDataFile();
	}

	/**
	 * Gets player last death location from data file
	 *
	 * @return player last death location
	 */
	@Nonnull
	public Location getLastDeathLocation() {
		return new Location(
				Bukkit.getWorld(this.yamlConfiguration.getString("locations.last-death-location.world", Main.overworld.getName())),
				this.yamlConfiguration.getDouble("locations.last-death-location.x"),
				this.yamlConfiguration.getDouble("locations.last-death-location.y"),
				this.yamlConfiguration.getDouble("locations.last-death-location.z"),
				(float) this.yamlConfiguration.getDouble("locations.last-death-location.yaw"),
				(float) this.yamlConfiguration.getDouble("locations.last-death-location.pitch")
		);
	}

	/**
	 * Sets last death location of player
	 *
	 * @param player player
	 */
	public void setLastDeathLocation(@Nonnull Player player) {
		if (!this.hasPlayerDataFile() && player.getWorld() == Main.worldDark) return;
		Location location = player.getLocation();
		this.yamlConfiguration.set("locations.last-death-location.world", location.getBlock().getWorld().getName());
		this.yamlConfiguration.set("locations.last-death-location.x", location.getX());
		this.yamlConfiguration.set("locations.last-death-location.y", location.getY());
		this.yamlConfiguration.set("locations.last-death-location.z", location.getZ());
		this.yamlConfiguration.set("locations.last-death-location.yaw", location.getYaw());
		this.yamlConfiguration.set("locations.last-death-location.pitch", location.getPitch());
		this.savePlayerDataFile();
	}

	/**
	 * Creates new player data file in "plugins/msUtils/players/uuid.yml"
	 */
	public void createPlayerDataFile() {
		if (this.hasPlayerDataFile()) return;
		this.yamlConfiguration.set("nickname", this.getNickname());
		if (this.getOnlinePlayer() != null) {
			this.yamlConfiguration.set("ip", this.getOnlinePlayer().getAddress() == null ? null : this.getOnlinePlayer().getAddress().getHostName());
			this.yamlConfiguration.set("time.first-join", System.currentTimeMillis());
		}
		this.savePlayerDataFile();
		ChatUtils.sendFine(null, "???????????? ???????? ?? ?????????????? ???????????? : \"" + this.getNickname() + "\" ?? ?????????????????? : \"" + this.uuid + ".yml\"");
	}

	/**
	 * Saves player data file in "plugins/msUtils/players/uuid.yml"
	 */
	public void savePlayerDataFile() {
		try {
			this.yamlConfiguration.save(this.dataFile);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * @return True if player data file exists
	 */
	public boolean hasPlayerDataFile() {
		return this.dataFile.exists();
	}

	/**
	 * @return True if player has no name
	 */
	public boolean hasNoName() {
		return this.yamlConfiguration.getString("name.firstname") == null
				|| this.yamlConfiguration.getString("name.lastname") == null
				|| this.yamlConfiguration.getString("name.patronymic") == null;
	}
}