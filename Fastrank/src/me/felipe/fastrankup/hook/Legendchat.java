package me.felipe.fastrankup.hook;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import me.felipe.fastrankup.Main;
import me.felipe.fastrankup.core.RankManager;
import me.felipe.fastrankup.core.player.PlayerInfo;

public class Legendchat implements Listener {
	
	Main plugin;
	public Legendchat(Main m) {
	
		plugin = m;
		
	}
	
	@EventHandler
	public void onChat(ChatMessageEvent e) {
		
		Player p = e.getSender();
		if(!RankManager.PLAYER_RANKS.containsKey(p.getUniqueId())) {
			
			p.sendMessage("§cNão foi possível encontrar seu rank, por favor, relogue!");
			
		} else {
			
			PlayerInfo pInfo = new PlayerInfo(p.getUniqueId());
			
			if(!(p.hasPermission("fastrankup.hidetag"))) {
			
				e.setTagValue("rank", pInfo.getPlayerRank().getTag());
				
			}
			
			
			
		}
		
		
	}

}
