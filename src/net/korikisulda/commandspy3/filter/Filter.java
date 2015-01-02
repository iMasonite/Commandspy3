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
package net.korikisulda.commandspy3.filter;

import java.util.ArrayList;
import java.util.List;

import net.korikisulda.commandspy3.util.ConfigInclude;
import net.korikisulda.commandspy3.util.Util;
import net.korikisulda.commandspy3.util.AnnotationConfig;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class Filter extends AnnotationConfig{
    @ConfigInclude public String signFlag="";
    @ConfigInclude public String commandFlag="";
    
    @ConfigInclude public boolean considerFlags=false;
    
    @ConfigInclude public String signNotifyFormat="&8[Sign]&7 %s&8:&e %s";
    @ConfigInclude public String signNotifyLineDelimiter="・";
    
    @ConfigInclude public String playerCommandNotifyFormat="&7%s&8: &b%s";
    @ConfigInclude public String serverCommandNotifyFormat="&d%s";
    
    @ConfigInclude public List<String> ignore=new ArrayList<String>();
    
    
    @ConfigInclude public boolean disable=false;
    public Filter() {
        
    }
    
    public Filter(String... params){
        for(String s:params){
            if(s.toCharArray()[0]=='s') signFlag=s.substring(1);
            if(s.toCharArray()[0]=='c') commandFlag=s.substring(1);
            considerFlags=true;
        }
    }
    
    public Filter(ConfigurationSection s){
        load(s);
    }
    
    private String format(String s,Object... format){
        return ChatColor.translateAlternateColorCodes('&', String.format(s, format));
    }

    public void onSignChange(CommandSender s, SignChangeEvent event) {
        if(disable) return;
        if(considerFlags&&signFlag.equals("*")) s.sendMessage(format(signNotifyFormat,event.getPlayer().getName(),Util.join(event.getLines(), signNotifyLineDelimiter, 0)));
    }

    public void onServerCommand(CommandSender s, ServerCommandEvent event) {
        if(disable) return;
        if(ignore.contains(Util.sit(event.getCommand(), ' ', 0))) return;
        if(considerFlags&&commandFlag.equals("*")) s.sendMessage(format(serverCommandNotifyFormat,event.getCommand()));
    }

    public void onPlayerCommand(CommandSender s, PlayerCommandPreprocessEvent event) {
        if(disable) return;
        if(ignore.contains(Util.sit(event.getMessage(), ' ', 0))) return;
        if(considerFlags&&commandFlag.equals("*")) s.sendMessage(format(playerCommandNotifyFormat,event.getPlayer().getName(),event.getMessage()));
    }
    
}
