package com.github._josephcw.loginbypassplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class JoinFull extends JavaPlugin implements Listener {

	private PluginDescriptionFile pdf = this.getDescription();
	
	private boolean getStatusEnabled() {
		return Boolean.valueOf(
				getConfig().getString("STATUS"));
	}
	
	private String setStatusEnabled(boolean status) {
		getConfig().set("STATUS", Boolean.toString(status));
		this.saveConfig();
		return "LoginBypass is now set to: " + Boolean.toString(status);
		
	}
	
	@Override
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(args.length != 1) {
			sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " <on/off/status/info>");
		} else {
			switch(args[0]) {
			case "status":
				sender.sendMessage(ChatColor.AQUA + "LoginBypass status: " + getStatusEnabled());
				break;
			case "on":
				sender.sendMessage(ChatColor.DARK_GREEN + "LoginBypass has been enabled!");
				setStatusEnabled(true);
				break;
			case "off":
				sender.sendMessage(ChatColor.RED + "LoginBypass has been disabled!");
				setStatusEnabled(false);
				break;
			case "info":
				sender.sendMessage(ChatColor.DARK_GREEN + "LoginBypass: " + pdf.getDescription());
				sender.sendMessage(ChatColor.DARK_GREEN + "Version: " + pdf.getVersion());
				sender.sendMessage(ChatColor.DARK_GREEN + "Made By: " + pdf.getAuthors());
				// No break, when users send info, also send command usage
			default:
				sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " <on/off/status/info>");
				break;
			}
		}
		return true;
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e) {
		if(getStatusEnabled()) {
			Player pLogin = e.getPlayer();
			if(e.getResult() == Result.KICK_FULL && pLogin.hasPermission("loginbypass.joinfull")) {
				e.allow();
			}
		}
	}
}
