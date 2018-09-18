package vsiege.common.addon;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import siege.common.siege.Siege;
import siege.common.siege.SiegeDatabase;
import vsiege.common.mode.Mode;

public class CommandSiegeMode extends CommandBase {

	@Override
	public String getCommandName() {
		return "siege_setmode";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/siege_setmode <siege> <mode> [<points needed>]";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(args.length < 2) throw new CommandException("Not enough args!");
		Siege siege = SiegeDatabase.getSiege(args[0]);
		if(siege == null) throw new CommandException("No such siege '" + args[0] + "'!");
		if(siege.isActive()) throw new CommandException("Siege is already running!");
		if(siege.isDeleted()) throw new CommandException("Siege is deletedd!");
		Mode mode = Mode.of(args[1]);
		if(mode == null) throw new CommandException("No such mode '" + args[1] + "'!");
		if(mode.pointsNeededToWin != -1 && args.length < 3) throw new CommandException("This mode requires a target winning score!");
		mode.pointsNeededToWin = parseIntWithMin(sender, args[2], 0);
		mode.setSiege(siege);
		sender.addChatMessage(new ChatComponentText("Set the mode of siege '" + args[0] + "' to '" + args[1] + "'."));
	}

	@Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
		if(args.length == 1) {
			return getListOfStringsMatchingLastWord(args, SiegeDatabase.getAllSiegeNames().toArray(new String[0]));
		}
		if(args.length == 2) {
			return getListOfStringsMatchingLastWord(args, Mode.values());
		}
		List ret = new ArrayList(); return ret;
	}

}
