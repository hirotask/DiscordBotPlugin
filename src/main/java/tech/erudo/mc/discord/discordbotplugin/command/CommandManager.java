package tech.erudo.mc.discord.discordbotplugin.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import tech.erudo.mc.discord.discordbotplugin.command.subcommand.SubCommand;
import tech.erudo.mc.discord.discordbotplugin.command.subcommand.subcommands.Auth;
import tech.erudo.mc.discord.discordbotplugin.command.subcommand.subcommands.Help;
import tech.erudo.mc.discord.discordbotplugin.command.subcommand.subcommands.Mute;

import java.util.*;

public class CommandManager implements CommandExecutor {
    private final JavaPlugin plugin;

    private final String mainCommand = "discord";
    private final List<SubCommand> commands = new ArrayList<>();

    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        plugin.getCommand(mainCommand).setExecutor(this);

        this.commands.add(new Help());
        this.commands.add(new Auth());
        this.commands.add(new Mute());

        if(plugin.getCommand(mainCommand) == null) {
            System.out.println("Command null");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            Bukkit.getLogger().info("Only players can run this command!");
            return true;
        }

        Player player = (Player) sender;

        if(command.getName().equalsIgnoreCase(mainCommand)) {
            if(args.length == 0) {
                player.sendMessage("Command length equals ZERO");
                return true;
            }

            //<command> args[0] args[1] args[2]...

            SubCommand target = this.get(args[0]);

            if(Objects.isNull(target)) {
                player.sendMessage("Invalid Command");
                return true;
            }

            ArrayList<String> arrayList = new ArrayList<>();

            //<command> <subcommand> args[0] args[1]...

            arrayList.addAll(Arrays.asList(args));
            arrayList.remove(0); //index 0 はサブコマンド本体

            try {
                target.onCommand(player, arrayList.toArray(new String[arrayList.size()]));
            } catch(Exception e) {
                player.sendMessage("Command Error");

                e.printStackTrace();
            }

        }

        return true;
    }

    private SubCommand get(String name) {
        Iterator<SubCommand> subcommands = this.commands.iterator();

        while(subcommands.hasNext()) {
            SubCommand sc = (SubCommand) subcommands.next();

            if(sc.name().equalsIgnoreCase(name)) {
                return sc;
            }

            String[] aliases;
            int var6 = (aliases = sc.aliases()).length;

            for(int var5 = 0; var5 < var6; var5++) {
                String alias = aliases[var5];
                if(name.equalsIgnoreCase(alias)) {
                    return sc;
                }
            }
        }

        return null;
    }
}