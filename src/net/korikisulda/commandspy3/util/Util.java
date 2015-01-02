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

import java.io.InvalidObjectException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Util {
    private Util(){}
    
    public static UUID getUUID(CommandSender sender) throws InvalidObjectException{
        UUID UID0=UUID.fromString("00000000-0000-0000-0000-000000000000");
        if(sender instanceof ConsoleCommandSender) return UID0;
        if(sender instanceof Player){
            Player player=(Player)sender;
            if(player.getUniqueId().equals(UID0)) throw new DireSecurityException("It's... It's a player with UUID 0! Run for the hills!");
            return player.getUniqueId();
        }
        throw new InvalidObjectException("Neither player nor console");
    }

    public static String join(String[] a, String delimiter, Integer startIndex) {
        try {
            Collection<String> s = Arrays.asList(a);
            StringBuffer buffer = new StringBuffer();
            Iterator<String> iter = s.iterator();

            while (iter.hasNext()) {
                if (startIndex == 0) {
                    buffer.append(iter.next());
                    if (iter.hasNext()) {
                        buffer.append(delimiter);
                    }
                } else {
                    startIndex--;
                    iter.next();
                }
            }

            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    public static String sit(String iStr, char delimiter, int part) {
        if (part == 0) {
            if (!iStr.contains(String.valueOf(delimiter)))
                return iStr;
        } else {
            if (!iStr.contains(String.valueOf(delimiter)))
                return "";
        }
        if (part == 0)
            return iStr.substring(0, (iStr.indexOf(delimiter, 0)));
        return iStr.substring(iStr.indexOf(delimiter, 0) + 1, iStr.length());
    }
}
