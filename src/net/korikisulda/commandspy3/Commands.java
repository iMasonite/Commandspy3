/*
    Commandspy - A Minecraft server plugin to facilitate real-time usage of commands, and sign-changes
    Copyright (C) 2014,2015 korikisulda

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.korikisulda.commandspy3;

import java.io.InvalidObjectException;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.korikisulda.commandspy3.util.CommandManager;
import net.korikisulda.commandspy3.util.CommandHandler;

public class Commands extends CommandManager{
    private Main main;
    public Commands(Main main) {
        this.main=main;
    }

    @CommandHandler(maximumArgsLength=0,permissions="")
    public void False(CommandSender sender,String[] args) throws InvalidObjectException{
        if(!main.getFilterManager().hasFilter(sender)){
            sender.sendMessage(ChatColor.RED + "(╯°□°）╯︵ ┻━ɹoɹɹǝ ɹǝs∩━┻");
        }else{
            main.getFilterManager().removeFilter(sender);
            sender.sendMessage(ChatColor.GREEN + "Removed parameters.");
        }
    }
    @CommandHandler(maximumArgsLength=0,permissions="")
    public void off(CommandSender sender,String[] args) throws InvalidObjectException{
        False(sender,args);
    }
    
    @CommandHandler(maximumArgsLength=0,permissions="commandspy.set")
    public void True(CommandSender sender,String[] args) throws InvalidObjectException{
        main.getFilterManager().setFilter(sender, new Filter("c*"));
        sender.sendMessage(ChatColor.GREEN + "Set commandspying on.");
    }
    
    @CommandHandler(maximumArgsLength=0,permissions="commandspy.set")
    public void on(CommandSender sender,String[] args) throws InvalidObjectException{
        True(sender,args);
    }
    
    @CommandHandler(maximumArgsLength=0,permissions="commandspy.set")
    public void set(CommandSender sender,String[] args) throws InvalidObjectException{
        main.getFilterManager().setFilter(sender, new Filter(args));
        sender.sendMessage(ChatColor.GREEN + "Set parameters.");
    }
    
    @CommandHandler(maximumArgsLength=0)
    public void version(CommandSender sender,String[] args){
        sender.sendMessage(ChatColor.GOLD + "Commandspy v" + main.getDescription().getVersion());
        sender.sendMessage(ChatColor.GOLD + "This is free software licensed under the GNU GPL.");
        sender.sendMessage(ChatColor.GRAY + "https://github.com/korikisulda/commandspy3/");
    }
    
    @CommandHandler(permissions="commandspy.configure")
    public void config(CommandSender sender,String[] args){
        main.getConfigManager().onCommand(sender, args);
    }
    
    @CommandHandler(permissions="commandspy.save")
    public void save(CommandSender sender,String[] args){
        main.save();
        sender.sendMessage(ChatColor.GREEN + "Saved.");
    }
    
    @CommandHandler(permissions="commandspy.filter")
    public void filter(CommandSender sender,String[] args) throws InvalidObjectException{
        if(!main.getFilterManager().hasFilter(sender)){
            sender.sendMessage(ChatColor.RED + "(╯°□°）╯︵ ┻━ɹoɹɹǝ ɹǝs∩━┻");
            sender.sendMessage(ChatColor.RED + "You don't have a filter assigned. Use /commandspy on for a new one.");
        }else{
            main.getFilterManager().getFilter(sender).onCommand(sender, args);
            main.getFilterManager().saveFilters();
        }
    }
    
}
