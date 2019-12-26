package me.felipe.fastrankup.hook;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import br.net.fabiozumbi12.UltimateChat.Bukkit.API.SendChannelMessageEvent;
import me.felipe.fastrankup.Main;
import me.felipe.fastrankup.core.RankManager;
import me.felipe.fastrankup.core.player.PlayerInfo;

public class UltimateChat implements Listener {
	
	Main plugin;
	public UltimateChat(Main m) {
	
		plugin = m;
		
	}
	
	@EventHandler
	public void onChat(SendChannelMessageEvent e) {
		
		Player p = (Player) e.getSender();
		
		if(!RankManager.PLAYER_RANKS.containsKey(p.getUniqueId())) {
			
			p.sendMessage("§cNão foi possível encontrar seu rank, por favor, relogue!");
			
		} else {
			
			PlayerInfo pInfo = new PlayerInfo(p.getUniqueId());
			if(!p.hasPermission("fastrankup.hidetag")) {
				
				e.addTag("{rank}", pInfo.getPlayerRank().getTag());
				e.addTag("{rank_name}", pInfo.getPlayerRank().getRankName());
				e.addTag("{rank_id}", pInfo.getPlayerRank().getRankId());
				
			}
			
		}
		
		
		
	}

}
