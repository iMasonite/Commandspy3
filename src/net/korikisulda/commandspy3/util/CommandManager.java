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
package net.korikisulda.commandspy3.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Stack;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public abstract class CommandManager implements CommandExecutor{
    private HashMap<String,Method> commands=new HashMap<String,Method>();
    private Stack<Exception> exceptions=new Stack<Exception>();
    
    {
        for(Method m : this.getClass().getMethods()) {
            if(m.getAnnotation(CommandHandler.class)!=null) {
                commands.put(m.getName().toLowerCase(), m);
            }
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2,String[] args) {
        return onCommand(sender,args);
    }
    
    public boolean onCommand(CommandSender sender,String[] args) {
        if(args.length==0) args=new String[]{"_null"};
        try{
            onSubCommand(sender,stripArray(args),args[0].toLowerCase());
        }catch(Exception e){
            exceptions.push(e);
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "An error occurred executing this command.");
        }
        return true;
    }
    
    public void onSubCommand(CommandSender sender,String[] arguments,String commandName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        if(!commands.containsKey(commandName)) commandName="_notfound";
        Method m=commands.get(commandName);
        CommandHandler c=commands.get(commandName).getAnnotation(CommandHandler.class);
        if(arguments==null) arguments=c.defaultArguments();
        
        if(!(sender instanceof Player)&&c.playerOnly()){
            sender.sendMessage(ChatColor.RED + "This command can only be executed as a player.");
        }else if(arguments.length>c.maximumArgsLength()){
            sender.sendMessage(c.usage());
        }else if(arguments.length<c.minimumArgsLength()){
            sender.sendMessage(c.usage());
        }else if(!hasPerms(sender,c.permissions())){
            sender.sendMessage(ChatColor.RED + "You do not have permission do do that!");
            for(String p:c.permissions()){
                sender.sendMessage("- " + p);
            }
        }else{
            //well..... if they're here...
            m.invoke(this, new Object[]{sender,arguments});
        }
        return;
    }
    
    @CommandHandler
    public void _null(CommandSender sender,String[] arguments) throws Exception{
        sender.sendMessage(ChatColor.RED + "You haven't specified a subcommand.");
    }
    @CommandHandler
    public void _notfound(CommandSender sender,String[] arguments) throws Exception{
        sender.sendMessage(ChatColor.RED + "That subcommand was not found.");
    }
    
    @CommandHandler(
        maximumArgsLength=1,
        usage="[command]",
        description="displays help"
        )
    public void help(CommandSender sender,String[] args){
        for(Entry<String,Method> command:commands.entrySet()){
            if(command.getKey().startsWith("_")) continue;
            CommandHandler commandMeta=command.getValue().getAnnotation(CommandHandler.class);
            String colour=hasPerms(sender,commandMeta.permissions())?ChatColor.GREEN.toString():ChatColor.RED.toString();
            sender.sendMessage(colour + command.getKey() + " " + commandMeta.usage() + ChatColor.GRAY + " - " + commandMeta.description());
        }
    }
    
    @CommandHandler(permissions="korikisulda.stack")
    public void _stack(CommandSender sender,String[] arguments){
        Exception e=exceptions.pop();
        sender.sendMessage(ChatColor.RED + e.getMessage());
        sender.sendMessage(ChatColor.RED + e.getClass().getCanonicalName());
        for(StackTraceElement s:e.getStackTrace()){
            sender.sendMessage(ChatColor.GOLD + s.toString());
        }
    }
    
    
    /**
     * Perform a check for player permissions
     * @param sender The CommandSender this permission check is to be performed on
     * @param perms Permissions to check for. Note that all must be present to return true
     * @return Returns true if the player has the specified permissions
     */
    private boolean hasPerms(CommandSender sender,String... perms){
        for(String p:perms){
            if(!sender.hasPermission(p)) return false;
        }
        return true;
    }
    
    /**
     * Removes the first element of an array
     * @param array Array to remove the first element of
     * @return Stripped array
     */
    private String[] stripArray(String[] array){
        if(array.length==0) return new String[0];
        String[] result=new String[array.length-1];
        for(int i=1;i<array.length;i++){
            result[i-1]=array[i];
        }
        return result;
    }

}
