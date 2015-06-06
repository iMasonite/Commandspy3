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

import java.util.ArrayList;
import java.util.List;

import net.korikisulda.commandspy3.util.AnnotationConfig;
import net.korikisulda.commandspy3.util.ConfigInclude;

public class Config extends AnnotationConfig{
@ConfigInclude public List<String> ignore=new ArrayList<String>();
@ConfigInclude public String updateURL="https://eagle.korikisulda.net/commandspy/update.yml";
@ConfigInclude public boolean updateCheck=true;
@ConfigInclude public boolean updateNotify=true;
@ConfigInclude public boolean updateNotifyOnlyIfBug=false;


@ConfigInclude public boolean databaseEnabled=false;
@ConfigInclude public String databaseDriver="com.mysql.jdbc.Driver";
@ConfigInclude public String databaseAddress="jdbc:mysql://localhost/commandspy?useUnicode=true&characterEncoding=utf-8";
@ConfigInclude public String databaseUsername="commandspy";
@ConfigInclude public String databasePassword="";

@ConfigInclude public boolean databaseSigns=true;
@ConfigInclude public boolean databaseCommands=true;
@ConfigInclude public boolean databaseServerCommands=true;

}
