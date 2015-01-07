package net.korikisulda.commandspy3.net.update;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import net.korikisulda.commandspy3.Main;
import net.korikisulda.commandspy3.util.Util;

public class UpdateNotifyManager {
    private List<VersionEntry> versions=new ArrayList<VersionEntry>();
    
    private Main main ;
    public UpdateNotifyManager(Main main) {
        this.main=main;
    }

    public synchronized void testNotifySender(CommandSender sender){
        //If we have no updates, return
        if(versions.size()==0) return;
        //If notify is turned off completely, return
        if(!main.getConfigManager().updateNotify&&!main.getConfigManager().updateNotifyOnlyIfBug) return;
        //If the sender shouldn't receive the message, return
        if(!sender.hasPermission("commandspy.notify")) return;
        //If the current version isn't even in the version list, return
        if(!hasVersion(Util.getNumericVersion(main.getDescription().getVersion()))) return;
        //get the current version entry
        VersionEntry currentVersion=getVersion(Util.getNumericVersion(main.getDescription().getVersion()));
        //If we're set to only notify on a buggy current build, and there are no bugs, return
        if(main.getConfigManager().updateNotifyOnlyIfBug&&!currentVersion.hasBugs) return;
        //If there is no update available, the build isn't ahead, and doesn't have bugs:
        if(!currentVersion.hasUpdate&&!currentVersion.isAhead&&!currentVersion.hasBugs) return;
        
        if(currentVersion.hasBugs) sender.sendMessage(ChatColor.GRAY + "[Commandspy] " + ChatColor.RED + currentVersion.bugsDescription);
        if(currentVersion.hasUpdate) sender.sendMessage(ChatColor.GRAY + "[Commandspy] " + ChatColor.GOLD + "An update is available.");
        if(currentVersion.isAhead) sender.sendMessage(ChatColor.GRAY + "[Commandspy] " + ChatColor.GOLD + "This version is marked as ahead of current release.");
    }
    
    public boolean hasVersion(long version){
        for(VersionEntry e:versions){
            if(e.version==version) return true;
        }
        return false;
    }
    
    public VersionEntry getVersion(long version){
        for(VersionEntry e:versions){
            if(e.version==version) return e;
        }
        return null;
    }
    
    public void updateVersionInformation(){
        if(!main.getConfigManager().updateCheck) return;
        
        new Thread(
            new Runnable(){
                @Override
                public synchronized void run(){
                    HttpURLConnection connection = null;  
                    try {
                        URL url = new URL(main.getConfigManager().updateURL);
                        connection = (HttpURLConnection)url.openConnection();
                        connection.setReadTimeout(1000);
                        connection.setRequestMethod("GET");
                        connection.setUseCaches (false);
                        connection.setDoInput(true);
                        YamlConfiguration cs=new YamlConfiguration();
                        cs.load(connection.getInputStream());
          
                        versions.clear();
                        for(String s:cs.getKeys(false)){
                           versions.add(new VersionEntry(cs.getConfigurationSection(s)));
                        }
          
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
        }).start();
    }
}
