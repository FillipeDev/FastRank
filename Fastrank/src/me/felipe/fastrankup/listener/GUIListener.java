package me.felipe.fastrankup.listener;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.felipe.fastrankup.Main;
import me.felipe.fastrankup.comandos.Rankup;
import me.felipe.fastrankup.core.player.PlayerInfo;

public class GUIListener implements Listener {
	
	
	Main plugin;
	public GUIListener(Main m) {
		
		plugin = m;
		
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		
		String name = e.getInventory().getName();
		FileConfiguration config = plugin.getConfig();
		if(name.equalsIgnoreCase(config.getString("Rankup-GUI.Nome").replace("&", "�"))) {
			
			if(e.getSlot() == 9) {
				
				e.setCancelled(true);
				e.getWhoClicked().closeInventory();
				
			} 
			
			if(e.getSlot() == 17) {
				
				Player p = (Player) e.getWhoClicked();
				PlayerInfo pInfo = new PlayerInfo(p.getUniqueId());
				if(Rankup.upPlayer(p, pInfo, pInfo.getPlayerRank(), pInfo.getNextRank())) {
					
					p.closeInventory();
					
				}
				p.closeInventory();
				
			}
			
			e.setCancelled(true);
			
			
		}
		
	}

}
