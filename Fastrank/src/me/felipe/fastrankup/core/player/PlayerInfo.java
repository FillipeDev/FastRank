package me.felipe.fastrankup.core.player;

import java.util.UUID;

import me.felipe.fastrankup.Main;
import me.felipe.fastrankup.core.Rank;
import me.felipe.fastrankup.core.RankManager;


public class PlayerInfo {

	private UUID player;

	public PlayerInfo(UUID player) {
		this.player = player;
	}

	public UUID getPlayer() {
		return this.player;
	}

	public void removeRank() {
		if (RankManager.PLAYER_RANKS.containsKey(player)) {
			RankManager.PLAYER_RANKS.remove(player);
		}
	
	}

	public Rank getPlayerRank() {
	
		return RankManager.PLAYER_RANKS.containsKey(player) ? RankManager.PLAYER_RANKS.get(player) : null;
	
	}

	public Rank getNextRank() {
	
		return Main.RANKS_ORDERED.get(RankManager.PLAYER_RANKS.get(player).getPosition() + 1);
	
	}
	
}
