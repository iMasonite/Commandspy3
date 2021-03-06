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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

    @Retention(RetentionPolicy.RUNTIME) public @interface CommandHandler{
        String[] permissions() default {};
        String[] alias() default {};
        String[] defaultArguments() default {};
        int minimumArgsLength() default 0;
        int maximumArgsLength() default 9001;
        String usage() default "";
        String description() default "";
        boolean playerOnly() default false;
    }