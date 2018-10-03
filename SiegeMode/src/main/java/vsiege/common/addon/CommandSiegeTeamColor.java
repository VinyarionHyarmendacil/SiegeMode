package vsiege.common.addon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import siege.common.siege.Siege;
import siege.common.siege.SiegeDatabase;
import siege.common.siege.SiegeTeam;
import vsiege.common.mode.Mode;
import vsiege.common.rule.Rule;

public class CommandSiegeTeamColor extends CommandBase {

	@Override
	public String getCommandName() {
		return "siege_teamcolor";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/siege_teamcolor <siege> <team> <color>";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(args.length < 3) throw new CommandException("Not enough args!");
		Siege siege = SiegeDatabase.getSiege(args[0]);
		if(siege == null) throw new CommandException("No such siege '" + args[0] + "'!");
		SiegeTeam team = siege.getTeam(args[1]);
		if(team == null) throw new CommandException("No such team '" + args[1] + "'!");
		EnumChatFormatting color = EnumChatFormatting.getValueByName(args[2]);
		if(color == null) throw new CommandException("No such color '" + args[2] + "'!");
		if(!color.isColor()) throw new CommandException("That formatting code is not a color!");
		team.color = color;
		func_152373_a(sender, this, "Set color for team %s to %s", team.getTeamName(), color.getFriendlyName());
	}

	@Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
		if(args.length == 1) {
			return getListOfStringsMatchingLastWord(args, SiegeDatabase.getAllSiegeNames().toArray(new String[0]));
		}
		if(args.length == 2) {
			Siege siege = SiegeDatabase.getSiege(args[0]);
			return getListOfStringsFromIterableMatchingLastWord(args, siege == null ? Collections.EMPTY_LIST : siege.listTeamNames());
		}
		if(args.length == 3) {
			return getListOfStringsMatchingLastWord(args, "black", "dark_blue", "dark_green", "dark_aqua", "dark_red", "dark_purple", 
					"gold", "gray", "dark_gray", "blue", "green", "aqua", "red", "light_purple", "yellow", "white");
		}
		List ret = new ArrayList(); return ret;
	}

}
