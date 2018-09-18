package vsiege.common.addon;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import siege.common.siege.Siege;
import siege.common.siege.SiegeDatabase;
import vsiege.common.mode.Mode;

public class CommandSiegeList extends CommandBase {

	@Override
	public String getCommandName() {
		return "siege_list";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/siege_list <active|inavtive|deleted> [<mode>]";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(args.length < 1) throw new CommandException("Not enough args!");
		String activity = args[0];
		if(!(activity.equals("active") || activity.equals("inactive") || activity.equals("deleted"))) throw new CommandException("That is not a state a siege can be in!");
		String mode = args.length >= 2 ? args[1] : null;
		if(!(mode == null ? true : Lists.newArrayList(Mode.values()).contains(mode))) throw new CommandException("That is not a gameplay mode a siege can have!");
		List<String> allOfType = Lists.newArrayList();
		for(Siege siege : SiegeDatabase.getAllSieges()) {
			if(siege.isActive() && activity.equals("active")) {
				allOfType.add(siege.getSiegeName());
			} else if(siege.isDeleted() && activity.equals("deleted")) {
				allOfType.add(siege.getSiegeName());
			} else if(!siege.isActive() && activity.equals("inactive")) {
				allOfType.add(siege.getSiegeName());
			}
		}
		if(allOfType.size() == 0) {
			sender.addChatMessage(new ChatComponentText("No '" + activity + (mode == null ? "" : (", " + mode)) + "' sieges."));
		} else {
			sender.addChatMessage(new ChatComponentText("Sieges of type '" + activity + (mode == null ? "" : (", " + mode)) + "': " + joinNiceStringFromCollection(allOfType)));
		}
	}

	@Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
		if(args.length == 1) {
			return getListOfStringsFromIterableMatchingLastWord(args, SiegeDatabase.getAllSiegeNames());
		}
		if(args.length == 2) {
			return getListOfStringsMatchingLastWord(args, Mode.values());
		}
		List ret = new ArrayList(); return ret;
	}

}
