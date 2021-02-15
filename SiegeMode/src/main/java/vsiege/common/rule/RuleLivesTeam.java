package vsiege.common.rule;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import siege.common.siege.Siege;
import siege.common.siege.SiegePlayerData;
import siege.common.siege.SiegeTeam;

public class RuleLivesTeam extends RuleLives {
	
	public RuleLivesTeam() {
		this.type = "VinyarionAddon_LivesTeam";
	}

	public RuleLivesTeam(int lives) {
		this();
		this.lives = lives;
	}
	
	public void playerDie(Siege siege, EntityPlayer player) {
		SiegeTeam team = siege.getPlayerTeam(player);
		if(this.lives <= team.getTeamDeaths()) {
			siege.leavePlayer((EntityPlayerMP) player, true);
		}
	}
	
	public void playerJoin(Siege siege, EntityPlayer player) {
		SiegeTeam team = siege.getPlayerTeam(player);
		if(this.lives <= team.getTeamDeaths()) {
			siege.leavePlayer((EntityPlayerMP) player, true);
			player.addChatMessage(new ChatComponentText("This team ran out of lives, so you can not join it!"));
		}
	}
	
}
