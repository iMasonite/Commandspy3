package net.korikisulda.commandspy3.net.update;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import net.korikisulda.commandspy3.Main;

public class UpdateNotifyManager {
    private List<VersionEntry> versions=new ArrayList<VersionEntry>();
    
    private Main main ;
    public UpdateNotifyManager(Main main) {
        this.main=main;
    }

    public void testNotifySender(CommandSender sender){
        if(!sender.hasPermission("commandspy.notify")) return;
        if(versions.size()==0) return;
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
