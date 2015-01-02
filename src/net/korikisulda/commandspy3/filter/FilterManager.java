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

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.HashMap;
import java.util.Map.Entry;

import net.korikisulda.commandspy3.Main;
import net.korikisulda.commandspy3.util.Util;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class FilterManager {
    private HashMap<String,Filter> filters=new HashMap<String,Filter>();
    
    private Main main;
    
    public FilterManager(Main main) {
        this.main=main;
    }
    
    public void saveFilters(){
        File filterFile=new File(main.getDataFolder().getAbsolutePath(),"filters.yml");
        YamlConfiguration filterConfig=new YamlConfiguration();
        
        for(Entry<String,Filter> entry:filters.entrySet()){
            filterConfig.createSection(entry.getKey());
            entry.getValue().save(filterConfig.getConfigurationSection(entry.getKey()));
        }
        try {
            filterConfig.save(filterFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadFilters(){
            try {
                File filterFile=new File(main.getDataFolder().getAbsolutePath(),"filters.yml");
                if(!filterFile.exists()) filterFile.createNewFile();
                YamlConfiguration filterConfig=new YamlConfiguration();
                filterConfig.load(filterFile);
                for(String s:filterConfig.getKeys(true)){
                    filters.put(s,new Filter(filterConfig.getConfigurationSection(s)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    public boolean hasFilter(CommandSender sender) throws InvalidObjectException{
        return filters.containsKey(Util.getUUID(sender).toString());
    }
    
    public void removeFilter(CommandSender sender) throws InvalidObjectException{
        filters.remove(Util.getUUID(sender).toString());
    }
    
    public void setFilter(CommandSender sender,Filter filter) throws InvalidObjectException{
        filters.put(Util.getUUID(sender).toString(), filter);
    }
    
    public Filter getFilter(CommandSender sender) throws InvalidObjectException{
        return filters.get(Util.getUUID(sender).toString());
    }
}
