package net.korikisulda.commandspy3.net.update;

import org.bukkit.configuration.ConfigurationSection;

import net.korikisulda.commandspy3.util.AnnotationConfig;
import net.korikisulda.commandspy3.util.ConfigInclude;

public class VersionEntry extends AnnotationConfig{
    public VersionEntry(ConfigurationSection s){
        this.load(s);
    }
    
    @ConfigInclude public long version=-1l;
    @ConfigInclude public String versionString="";
    
    @ConfigInclude public String bugsDescription="";
    @ConfigInclude public boolean hasBugs=false;
    @ConfigInclude public boolean hasUpdate=false;
    @ConfigInclude public boolean isAhead=false;
    
    @ConfigInclude public String dateTimeReleased="";
    
    @ConfigInclude public long update=-1l;
}
