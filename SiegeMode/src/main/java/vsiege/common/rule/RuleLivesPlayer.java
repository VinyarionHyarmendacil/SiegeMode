package vsiege.common.rule;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import siege.common.siege.Siege;
import siege.common.siege.SiegePlayerData;

public class RuleLivesPlayer extends RuleLives {
	
	public RuleLivesPlayer() {
		this.type = "VinyarionAddon_LivesPlayer";
	}
	
	public RuleLivesPlayer(int lives) {
		this();
		this.lives = lives;
	}
	
	public void playerDie(Siege siege, EntityPlayer player) {
		SiegePlayerData pd = siege.getPlayerData(player);
		if(this.lives <= pd.getDeaths()) {
			siege.leavePlayer((EntityPlayerMP) player, true);
		}
	}
	
	public void playerJoin(Siege siege, EntityPlayer player) {
		SiegePlayerData pd = siege.getPlayerData(player);
		if(this.lives <= pd.getDeaths()) {
			siege.leavePlayer((EntityPlayerMP) player, true);
			player.addChatMessage(new ChatComponentText("You have run out of lives, so you can not join this siege again!"));
		}
	}
	
}
