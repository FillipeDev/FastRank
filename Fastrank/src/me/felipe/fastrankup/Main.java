package me.felipe.fastrankup;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.felipe.fastrankup.comandos.Admin;
import me.felipe.fastrankup.comandos.Rankup;
import me.felipe.fastrankup.core.Flatfile;
import me.felipe.fastrankup.core.LocaleManager;
import me.felipe.fastrankup.core.Metrics;
import me.felipe.fastrankup.core.MySQL;
import me.felipe.fastrankup.core.Rank;
import me.felipe.fastrankup.hook.Legendchat;
import me.felipe.fastrankup.hook.UltimateChat;
import me.felipe.fastrankup.listener.GUIListener;
import me.felipe.fastrankup.listener.JoinQuitListener;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	public static Map<Integer, Rank> RANKS_ORDERED = new HashMap<>();
	public static Economy economy = null;
	public static final String PLUGIN_PREFIX = "§b[FastRankup]§f ";
	public static final String PLUGIN_ERROR_PREFIX = "§4[FastRankup]§c ";
	public static String PLUGIN_VERSION = "";
	public static boolean CHAT_HOOK = false;
	public static boolean MYSQL_USE = false;
	public static boolean USE_UUIDS = false;
	public static boolean USE_UPGUI = false;	
	
	public void onEnable() {
	
		plugin = this;
		PLUGIN_VERSION = getDescription().getVersion();
		ConsoleCommandSender b = Bukkit.getConsoleSender();
		b.sendMessage(PLUGIN_PREFIX + "Inicializando FastRankup v" + PLUGIN_VERSION);
		
		Metrics metrics = new Metrics(this);
		
		setupConfig();
		if(getConfig().getBoolean("MySQL.Usar")) {
			
			MYSQL_USE = true;
			MySQL.setupMySQL(this);
			metrics.addCustomChart(new Metrics.SimplePie("using_mysql", () -> "yes"));
			
		} else {
			
			Flatfile.setupDataFile(this);
			metrics.addCustomChart(new Metrics.SimplePie("using_mysql", () -> "no"));
			
		}
				
		if(!setupDependencies()) {
			
			return;
			
		}
		
		LocaleManager.setupMensagens(this);
		registerListeners();
		getCommand("rankup").setExecutor(new Rankup());
		getCommand("fastrankup").setExecutor(new Admin(this));
		setupHooks();
		setupEconomy();
		setupRanks();
		
		if(getConfig().getBoolean("Usar-UUIDs")) {
			
			USE_UUIDS = true;
			
		}
			
		
		if(getConfig().getBoolean("Rankup-GUI.Usar")) {
			
			USE_UPGUI = true;
			
		}
		
		
		
	}
	
	public void onDisable() {
		
		try {
			MySQL.mysqlConn().close();
		} catch (SQLException e) {}
		
	}
		
	private void registerListeners() {
		
		Bukkit.getPluginManager().registerEvents(new JoinQuitListener(this), this);
		Bukkit.getPluginManager().registerEvents(new GUIListener(this), this);
	}
	
	private void setupConfig() {
		
		File file = new File(getDataFolder(), "config.yml");
	    if (!file.exists()) {
		      try
		      {
		        saveResource("config.yml", false);
		      }
		      catch (Exception localException1) {}
	    }
		
	}
 
	private boolean setupDependencies() {
		ConsoleCommandSender b = Bukkit.getConsoleSender();
		if(Bukkit.getPluginManager().isPluginEnabled("Vault")) {
			
			b.sendMessage(PLUGIN_PREFIX + "Vault encontrado! Dependencias §aOK§f!");
			return true;
			
		} else {
			
			b.sendMessage(PLUGIN_ERROR_PREFIX + "§Vault não encontrado! Desativando plugin...");
			Bukkit.getPluginManager().disablePlugin(this);
			return false;
		}
		
		
	}
	
	protected static void setupHooks() {

		ConsoleCommandSender b = Bukkit.getConsoleSender();
		if(Bukkit.getPluginManager().isPluginEnabled("Legendchat")) {
			
			b.sendMessage(PLUGIN_PREFIX + "Legendchat encontrado! Hook ativado.");
			Bukkit.getPluginManager().registerEvents(new Legendchat(plugin), plugin);
			CHAT_HOOK = true;
			
		} else if(Bukkit.getPluginManager().isPluginEnabled("UltimateChat")) {
			
			Bukkit.getPluginManager().registerEvents(new UltimateChat(plugin), plugin);
			b.sendMessage(PLUGIN_PREFIX + "uChat encontrado! Hook ativado.");
			CHAT_HOOK = true;
		}
		
		
	}

	private static void setupRanks() {
		
		int i = 1;
		for(String s : Main.plugin.getConfig().getConfigurationSection("Ranks").getKeys(false)) {
			FileConfiguration conf = Main.plugin.getConfig();
			Rank rank = new Rank(s, conf.getString("Ranks." + s + ".Nome").replaceAll("&", "§"), conf.getString("Ranks." + s + ".Tag"), conf.getDouble("Ranks." + s + ".Preco"), conf.getStringList("Ranks." + s + ".Comandos"), i);
			
			RANKS_ORDERED.put(i, rank);
			i++;
			
		}
		
		Bukkit.getConsoleSender().sendMessage("§b[FastRankup] §fForam encontrados e armazenados §a" + (i - 1) + " §franks!");
		
	}
	
	public static void setupRanks(CommandSender sender) {
		
		int i = 1;
		for(String s : Main.plugin.getConfig().getConfigurationSection("Ranks").getKeys(false)) {
			FileConfiguration conf = Main.plugin.getConfig();
			Rank rank = new Rank(s, conf.getString("Ranks." + s + ".Nome").replaceAll("&", "§"), conf.getString("Ranks." + s + ".Tag"), conf.getDouble("Ranks." + s + ".Preco"), conf.getStringList("Ranks." + s + ".Comandos"), i);
			
			RANKS_ORDERED.put(i, rank);
			i++;
		}
		
		sender.sendMessage("§b[FastRankup] §fForam encontrados e armazenados §a" + (i - 1) + " §franks!");
		
	}

	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
}