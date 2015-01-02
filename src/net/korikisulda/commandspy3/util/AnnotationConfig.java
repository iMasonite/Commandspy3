/*
    Commandspy - A Minecraft server plugin to facilitate real-time usage of commands, and sign-changes
    Copyright (C) 2013,2014,2015 korikisulda

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

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class AnnotationConfig extends CommandManager{

	/*
	 * Saving and loading methods
	 */
	public ConfigurationSection save(ConfigurationSection c){
		for(Field f:this.getClass().getFields()){
			if(f.isAnnotationPresent(config.class)){
				try{
    				if(f.get(this) instanceof Location){
    					c.set(f.getName() + ".world", ((Location)f.get(this)).getWorld().getName());				
    					c.set(f.getName() + ".x", ((Location)f.get(this)).getX());
    					c.set(f.getName() + ".y", ((Location)f.get(this)).getY());
    					c.set(f.getName() + ".z", ((Location)f.get(this)).getZ());
    				}else if(f.get(this) instanceof List<?>){
    					c.set(f.getName(),f.get(this));
    				}else if(f.get(this) instanceof AnnotationConfig){
    					c.createSection(f.getName());
    					((AnnotationConfig)f.get(this)).applyTo(c.getConfigurationSection(f.getName()));
    				}else{
    					c.set(f.getName(), f.get(this));
    				}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			}
		return c;
	}

	public void load(ConfigurationSection c){
		for(Field f:this.getClass().getFields()){
			if(f.isAnnotationPresent(config.class)&&c.isSet(f.getName())){
				try{
					if(f.get(this) instanceof String){
						f.set(this, c.getString(f.getName()));
					}else if(f.get(this) instanceof Long){
						f.set(this, c.getLong(f.getName()));
					}else if(f.get(this) instanceof Integer){
						f.set(this, c.getInt(f.getName()));
					}else if(f.get(this) instanceof Double){
						f.set(this, c.getDouble(f.getName()));
					}else if(f.get(this) instanceof Boolean){
						f.set(this, c.getBoolean(f.getName()));
					}else if(f.getType()==Location.class){
						f.set(this, new Location(
    						Bukkit.getServer().getWorld(c.getString(f.getName() + ".world")),
    						c.getDouble(f.getName() + ".x"),
    						c.getDouble(f.getName() + ".y"),
    						c.getDouble(f.getName() + ".z")
						));
					}else if(f.getType()==List.class){
					    f.set(this, c.getList(f.getName()));
    				}else if(f.get(this) instanceof AnnotationConfig){
    					((AnnotationConfig)f.get(this)).loadFrom(c.getConfigurationSection(f.getName()));
    				}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			}
	}
	
	@Override
	@command(
        maximumArgsLength=0,
        description="null command"
        )
	public void _null(CommandSender sender,String[] args) throws IllegalArgumentException, IllegalAccessException{
	    for(Field f:this.getClass().getFields()){
	        if(f.isAnnotationPresent(config.class)){
	            if(f.getType()==Location.class){
	                if(f.get(this)!=null){
	                    sender.sendMessage(f.getName() + ": " + 
                            ((Location)f.get(this)).getWorld().getName() + "," + 
                            ((Location)f.get(this)).getBlockX() + "," + 
                            ((Location)f.get(this)).getBlockY() + "," + 
                            ((Location)f.get(this)).getBlockZ());
	                }else{
	                    sender.sendMessage(ChatColor.GRAY + f.getName() + ": " + "null");
	                }
	            }else if(f.get(this) instanceof List){
	                String s="";
	                for(Object o:((List<?>)f.get(this))){
	                    s+=","+o.toString();
	                }
                    sender.sendMessage(ChatColor.GRAY + f.getName() + ": " + s);
	            }else{
	                if(f.get(this).toString()==""||f.get(this).toString()=="-1"){
	                    sender.sendMessage(ChatColor.GRAY + f.getName() + ": " + f.get(this).toString());           
	                }else{
	                    sender.sendMessage(f.getName() + ": " + f.get(this).toString());
	                }
	            }
	            
	        }
        }  
	}

	@command(
	        maximumArgsLength=1000,
	        minimumArgsLength=2,
	        usage="<name> <value>",
	        description="Changes settings."
	        )
	public void set(CommandSender sender,String[] args) throws NumberFormatException, IllegalArgumentException, IllegalAccessException{
        Field f=getFieldCaseInsensitive(args[0]);

        if(f.isAnnotationPresent(config.class)){
            if(f.getAnnotation(config.class).settable()==false) return;
            
                if(f.get(this) instanceof String){
                    f.set(this, Util.join(args," ",1));
                }else if(f.get(this) instanceof Long){
                    f.set(this, Long.parseLong(args[1]));
                }else if(f.get(this) instanceof Integer){
                    f.set(this, Integer.parseInt(args[1]));
                }else if(f.get(this) instanceof Double){
                    f.set(this, Double.parseDouble(args[1]));
                }else if(f.get(this) instanceof Boolean){
                    f.set(this, Boolean.parseBoolean(args[1]));
                }else if(f.getType()==Location.class){
                    if(args[1].equalsIgnoreCase("me")){
                        f.set(this, ((Player)sender).getLocation());
                    }else if(args[1].equalsIgnoreCase("null")){
                        f.set(this, null);
                    }else{
                        sender.sendMessage("fail.");
                    }
                }
                sender.sendMessage(ChatColor.GREEN + "Set " + f.getName() + " to " + Util.join(args," ",1));
        }
	}
	
	@SuppressWarnings("unchecked")//Everything in Java extends Object. I don't need to check. Go away, javac.
    @command(minimumArgsLength=2)
	public void add(CommandSender sender,String[] args) throws IllegalArgumentException, IllegalAccessException{
	    if(getFieldCaseInsensitive(args[0]).get(this) instanceof List){
	        ((List<Object>)getFieldCaseInsensitive(args[0]).get(this)).add(Util.join(args," ",1));
	    }else{
	        sender.sendMessage(ChatColor.RED + "That's not a list. You can only add to lists.");
	    }
	}
	
	public Field getFieldCaseInsensitive(String name){
	       for(Field f:this.getClass().getFields()){
	            if(f.getName().equalsIgnoreCase(name)){
	                return f;
	            }
	       }
	       return null;
	}

	public void applyTo(ConfigurationSection s) {
		save(s);
	}

	public void loadFrom(ConfigurationSection s) {
		load(s);
	}
}
