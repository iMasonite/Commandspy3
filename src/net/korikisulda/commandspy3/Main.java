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

import net.korikisulda.commandspy3.filter.FilterManager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
    private Config config;
    private FilterManager filterManager;
    private static Main instance;
    
    public Main(){
        config=new Config();
        filterManager=new FilterManager(this);
        instance=this;
    }
    
    @Override
    public void onEnable(){
        load();
        Bukkit.getServer().getPluginManager().registerEvents(new EventListener(),this);
        Bukkit.getServer().getPluginCommand("commandspy").setExecutor(new Commands(this));
    }
    
    @Override
    public void onDisable(){
        save();
    }
    
    public static Main getInstance(){
        return instance;
    }
    
    public Config getConfigManager(){
        return config;
    }
    
    public FilterManager getFilterManager(){
        return filterManager;
    }
    
    public void save(){
        config.save(getConfig());
        saveConfig();
        getFilterManager().saveFilters();
    }
    
    public void load(){
        config.load(getConfig());
        getFilterManager().loadFilters();
    }


}
