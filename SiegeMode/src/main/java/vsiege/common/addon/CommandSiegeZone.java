package vsiege.common.addon;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import siege.common.siege.Siege;
import siege.common.siege.SiegeDatabase;
import siege.common.siege.SiegeTeam;
import vsiege.common.game.ZoneControl;
import vsiege.common.game.ZoneFlag;
import vsiege.common.mode.Mode;
import vsiege.common.mode.ModeCTF;
import vsiege.common.mode.ModeDomination;

public class CommandSiegeZone extends CommandBase {

	@Override
	public String getCommandName() {
		return "siege_zone";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/siege_zone <siege> <add|set-value|remove> <team|name> [<x> <y> <z> <size>]|[<value>]";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(args.length < 3) throw new CommandException("Not enough args!");
		Siege siege = SiegeDatabase.getSiege(args[0]);
		if(siege == null) throw new CommandException("No such siege '" + args[0] + "'!");
		if(siege.isActive()) throw new CommandException("Siege is already running!");
		if(siege.isDeleted()) throw new CommandException("Siege is deleted!");
		String op = args[1];
		String name = args[2];
		Mode mode = siege.mode;
		if(mode instanceof ModeCTF) {
			ModeCTF ctf = (ModeCTF)mode;
			SiegeTeam team = siege.getTeam(name);
			if(team == null) throw new CommandException("No such team '" + name + "'!");
			if(op.equals("remove")) {
				boolean removed = false;
				for(ZoneFlag zf : ctf.zones.toArray(new ZoneFlag[ctf.zones.size()])) {
					if(zf.owner.getTeamName().equals(team.getTeamName())) {
						ctf.owners.remove(zf.owner);
						ctf.zones.remove(zf);
						removed = true;
					}
				}
				if(removed) {
					func_152373_a(sender, this, "Removed the home zone for team %s.", name);
				} else {
					throw new CommandException("That team does not have a home zone set!");
				}
			} else if(op.equals("add")) {
				if(args.length < 7) throw new CommandException("Not enough arguments! Missing coordinates and size.");
				for(ZoneFlag zf : ctf.zones.toArray(new ZoneFlag[ctf.zones.size()])) {
					if(zf.owner.getTeamName().equals(team.getTeamName())) {
						ctf.owners.remove(zf.owner);
						ctf.zones.remove(zf);
					}
				}
				int x = parseInt(sender, args[3]);
				int y = parseInt(sender, args[4]);
				int z = parseInt(sender, args[5]);
				int size = parseInt(sender, args[6]);
				ZoneFlag zf = new ZoneFlag(team, x, y, z, size);
				zf.siege = siege;
				ctf.owners.put(zf.owner, zf);
				ctf.zones.add(zf);
				func_152373_a(sender, this, "Set the home zone for team %s.", name);
			} else if(op.equals("set-value")) {
				throw new CommandException("This mode's zones do not have values to set!");
			} else {
				throw new CommandException("Invalid operation '" + op + "'!");
			}
		} else if(mode instanceof ModeDomination) {
			ModeDomination dom = (ModeDomination)mode;
			if(op.equals("remove")) {
				boolean removed = false;
				for(ZoneControl c : dom.zones.toArray(new ZoneControl[dom.zones.size()])) {
					if(c.name.equals(name)) {
						dom.zones.remove(c);
						removed = true;
					}
				}
				if(removed) {
					func_152373_a(sender, this, "Removed the zone %s.", name);
				} else {
					throw new CommandException("That zone does not exist!");
				}
			} else if(op.equals("add")) {
				if(args.length < 7) throw new CommandException("Not enough arguments! Missing coordinates and size.");
				int x = parseInt(sender, args[3]);
				int y = parseInt(sender, args[4]);
				int z = parseInt(sender, args[5]);
				int size = parseInt(sender, args[6]);
				ZoneControl zc = new ZoneControl(name, x, y, z, size);
				zc.siege = siege;
				dom.zones.add(zc);
				func_152373_a(sender, this, "Added the zone %s.", name);
			} else if(op.equals("set-value")) {
				if(args.length < 4) throw new CommandException("Not enough arguments! Missing value.");
				boolean did = false;
				for(ZoneControl c : dom.zones.toArray(new ZoneControl[dom.zones.size()])) {
					if(c.name.equals(name)) {
						c.setValue(sender, args[3]);
						did = true;
					}
				}
				if(did) {
					func_152373_a(sender, this, "Set zone's value to '" + args[3] + "'.");
				} else {
					throw new CommandException("That zone does not exist!");
				}
			} else {
				throw new CommandException("Invalid operation '" + op + "'!");
			}
		} else {
			throw new CommandException("This siege's mode does not use zones!");
		}
	}

	@Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
		if(args.length == 1) {
			return getListOfStringsMatchingLastWord(args, SiegeDatabase.getAllSiegeNames().toArray(new String[0]));
		}
		if(args.length == 2) {
			return getListOfStringsMatchingLastWord(args, "add", "set-value", "remove");
		}
		if(args.length == 3) {
			return getListOfStringsFromIterableMatchingLastWord(args, SiegeDatabase.getSiege(args[0]).listTeamNames());
		}
		if(args.length == 4) {
			return getListOfStringsMatchingLastWord(args, String.valueOf(sender.getPlayerCoordinates().posX));
		}
		if(args.length == 5) {
			return getListOfStringsMatchingLastWord(args, String.valueOf(sender.getPlayerCoordinates().posY));
		}
		if(args.length == 6) {
			return getListOfStringsMatchingLastWord(args, String.valueOf(sender.getPlayerCoordinates().posZ));
		}
		List ret = new ArrayList(); return ret;
	}

}
