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

import net.korikisulda.commandspy3.util.Util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class EventListener implements Listener{
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoinEvent(PlayerJoinEvent event){
        Main.getInstance().getUpdateNotifyManager().testNotifySender(event.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onSignChangeEvent(SignChangeEvent event) throws InvalidObjectException{
        for(Player p:Bukkit.getServer().getOnlinePlayers()){
            if(Main.getInstance().getFilterManager().hasFilter(p)){
                Main.getInstance().getFilterManager().getFilter(p).onSignChange(p,event);
            }
        }
        if(Main.getInstance().getFilterManager().hasFilter(Bukkit.getConsoleSender())) Main.getInstance().getFilterManager().getFilter(Bukkit.getConsoleSender()).onSignChange(Bukkit.getConsoleSender(), event);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onServerCommandEvent(ServerCommandEvent event) throws InvalidObjectException{
          if(shouldIgnore(event.getCommand())) return;
          for(Player p:Bukkit.getServer().getOnlinePlayers()){
                if(Main.getInstance().getFilterManager().hasFilter(p)){
                    Main.getInstance().getFilterManager().getFilter(p).onServerCommand(p,event);
                }
            }
          if(Main.getInstance().getFilterManager().hasFilter(Bukkit.getConsoleSender())) Main.getInstance().getFilterManager().getFilter(Bukkit.getConsoleSender()).onServerCommand(Bukkit.getConsoleSender(), event);
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) throws InvalidObjectException{
         if(shouldIgnore(event.getMessage())) return;
        for(Player p:Bukkit.getServer().getOnlinePlayers()){
              if(Main.getInstance().getFilterManager().hasFilter(p)){
                  Main.getInstance().getFilterManager().getFilter(p).onPlayerCommand(p,event);
              }
            }
        //Yes. I will change these. It makes me sad. 
        if(Main.getInstance().getFilterManager().hasFilter(Bukkit.getConsoleSender())) Main.getInstance().getFilterManager().getFilter(Bukkit.getConsoleSender()).onPlayerCommand(Bukkit.getConsoleSender(), event);
    }
    
    private boolean shouldIgnore(String message){
        return Main.getInstance().getConfigManager().ignore.contains(Util.sit(message,' ',0));
    }
}
