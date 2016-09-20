package com.simonorj.mc.EventfulDynmap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;

public class EventfulDynmap extends JavaPlugin {
	private static DynmapAPI dynmap;
	
	@Override
	public void onEnable() {
		dynmap = (DynmapAPI) Bukkit.getPluginManager().getPlugin("dynmap");
		if (dynmap == null) {
			getServer().getLogger().warning("Dynmap not found.  This plugin will work whenever Dynmap is loaded.");
		}
		getServer().getPluginManager().registerEvents(new EventfulDiscordListner(), this);
		getServer().getLogger().info("No commands available.");
	}
	
	private void msgDynmap(String m) {
		// Check it
		if (dynmap == null) 
			dynmap = (DynmapAPI) Bukkit.getPluginManager().getPlugin("dynmap");
		// Check if dynmap has changed
		if (dynmap != null)
			// send message to dynmap
			dynmap.sendBroadcastToWeb(null, m);
	}
	
	@Override
	public void onDisable() {
		getServer().getLogger().info("EventfulDiscord has been disabled.");
	}
	
	public final class EventfulDiscordListner implements Listener {
		// Commands - /me and /say
		@EventHandler (priority=EventPriority.MONITOR)
		public void catchSlashTalk(PlayerCommandPreprocessEvent e) {
			// Check if it's cancelled.
			if (e.isCancelled())
				return;
			String[] s = e.getMessage().split(" ", 2);

			if (s[0].equalsIgnoreCase("/me")) { // Check for /me
				msgDynmap("* " + e.getPlayer().getName() + " " + s[1]);
				return;
			}
			if (s[0].equalsIgnoreCase("/say")) { // Check for broadcast /say
				msgDynmap("[" + e.getPlayer().getName() + "] " + s[1]);
				return;
			}
		}
		
		// Server Commands
		@EventHandler (priority=EventPriority.MONITOR)
		public void serverSlashTalk(ServerCommandEvent e) {
			// Check if it's cancelled.
			if (e.isCancelled())
				return;
			String[] s = e.getCommand().split(" ", 2);

			if (s[0].equalsIgnoreCase("/me")) { // Check for /me
				msgDynmap("* Server " + s[1]);
				return;
			}
			if (s[0].equalsIgnoreCase("/say")) { // Check for broadcast /say
				msgDynmap("[Server] " + s[1]);
				return;
			}
		}
		
		// Player dying
		@EventHandler (priority=EventPriority.MONITOR)
		public void killed(PlayerDeathEvent e) {
			msgDynmap(ChatColor.stripColor(e.getDeathMessage()));
		}

	}
}
