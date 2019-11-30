/*
 * Copyright (C) 2019 MCME (Fraspace5)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mcme.mybirthday;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import lombok.Getter;

/**
 *
 * @author Fraspace5
 */
public class BirthDayData {

    @Getter
    public UUID uuid;
    public Calendar calendar;
    public boolean particles;
    public Long cooldown;

    public BirthDayData(String data) {
        String[] dataArray = data.split(";");
        uuid = UUID.fromString(dataArray[0]);
        calendar = Calendar.getInstance();
        calendar.set(parseInt(dataArray[1]), parseInt(dataArray[2]), parseInt(dataArray[3]));
        particles = Boolean.getBoolean(dataArray[4]);
        cooldown = Long.getLong(dataArray[5]);
    }

    public String serialize() {
        return uuid + ";" + calendar.get(Calendar.YEAR) + ";" + calendar.get(Calendar.MONTH) + ";" + calendar.get(Calendar.DAY_OF_MONTH) + ";" + particles + ";" + cooldown;
    }

    public static String dserialize(UUID uuid, Calendar cal, Long l) {
        return uuid + ";" + cal.get(Calendar.YEAR) + ";" + cal.get(Calendar.MONTH) + ";" + cal.get(Calendar.DAY_OF_MONTH) + ";" + "true" + ";" + l.toString();
    }

    public static String serializeDatab(UUID uuid, Date date, Long l, Boolean bol) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return uuid + ";" + cal.get(Calendar.YEAR) + ";" + cal.get(Calendar.MONTH) + ";" + cal.get(Calendar.DAY_OF_MONTH) + ";" + bol.toString() + ";" + l.toString();
    }
}
