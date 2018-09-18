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
import vsiege.common.rule.Rule;

public class CommandSiegeRule extends CommandBase {

	@Override
	public String getCommandName() {
		return "siege_rules";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/siege_setmode <siege> <rule> <add|set-value|remove> [<value>]";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(args.length < 3) throw new CommandException("Not enough args!");
		Siege siege = SiegeDatabase.getSiege(args[0]);
		if(siege == null) throw new CommandException("No such siege '" + args[0] + "'!");
		if(siege.isActive()) throw new CommandException("Siege is already running!");
		if(siege.isDeleted()) throw new CommandException("Siege is already finished!");
		Rule rule = Rule.of(args[1]);
		if(rule == null) throw new CommandException("No such rule '" + args[1] + "'!");
		String op = args[2];
		Rule prev = null;
		for(Rule r : siege.mode.rules) {
			if(r.getClass().equals(rule.getClass())) {
				prev = r;
				break;
			}
		}
		if(op.equals("add")) {
			if(prev == null) {
				siege.mode.rules.add(rule);
				sender.addChatMessage(new ChatComponentText("Added rule '" + args[1] + "'."));
			} else {
				sender.addChatMessage(new ChatComponentText("That rule is already in effect!"));
			}
		} else if(op.equals("set-value")) {
			if(args.length < 4) throw new CommandException("Not enough args! Missing a value.");
			if(prev == null) {
				sender.addChatMessage(new ChatComponentText("That rule is not in effect! Add it first!"));
			} else {
				prev.setValue(sender, args[3]);
			}
		} else if(op.equals("remove")) {
			if(prev == null) {
				sender.addChatMessage(new ChatComponentText("That rule is not in effect!"));
			} else {
				siege.mode.rules.remove(prev);
				sender.addChatMessage(new ChatComponentText("Removed rule '" + args[1] + "'."));
			}
		} else {
			throw new CommandException("Invalid argument '" + op + "'!");
		}
	}

	@Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
		if(args.length == 1) {
			return getListOfStringsMatchingLastWord(args, SiegeDatabase.getAllSiegeNames().toArray(new String[0]));
		}
		if(args.length == 2) {
			return getListOfStringsMatchingLastWord(args, Rule.values());
		}
		if(args.length == 3) {
			return getListOfStringsMatchingLastWord(args, "add", "set-value", "remove");
		}
		List ret = new ArrayList(); return ret;
	}

}
