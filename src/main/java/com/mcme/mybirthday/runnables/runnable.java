/*
 * Copyright (C) 2021 MCME (Fraspace5)
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
package com.mcme.mybirthday.runnables;

import com.mcme.mybirthday.MyBirthday;
import com.mcme.mybirthday.data.PluginData;
import com.mcme.mybirthday.discord.discord;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Fraspace5
 */
public class runnable {

    public static void setListRunnable() {

        new BukkitRunnable() {

            @Override
            public void run() {

                PluginData.setTodayBirthdays();

            }

        }.runTaskTimer(MyBirthday.getPluginInstance(), 0L, 600L);

    }

    public static void showListRunnable() {

        new BukkitRunnable() {

            @Override
            public void run() {

                PluginData.showListAll();

            }

        }.runTaskTimer(MyBirthday.getPluginInstance(), MyBirthday.getPluginInstance().getBroadcastlistevery() * 60 * 20 * 1000000, MyBirthday.getPluginInstance().getBroadcastlistevery() * 60 * 20);

    }

    public static void connectionRunnable() {

        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    if (!MyBirthday.getPluginInstance().getConnection().isValid(5)) {
                        MyBirthday.getPluginInstance().getConnection().close();
                        MyBirthday.getPluginInstance().openConnection();

                    }

                } catch (SQLException ex) {

                }

            }

        }.runTaskTimer(MyBirthday.getPluginInstance(), 60L, 1000L);

    }

    public static void discordRunnable() {

        new BukkitRunnable() {

            @Override
            public void run() {

                TimeZone tim = TimeZone.getTimeZone("Europe/London");
                Calendar cal = Calendar.getInstance(tim);

                if (cal.get(Calendar.HOUR_OF_DAY) == MyBirthday.getPluginInstance().getConfig().getInt("time.hours")
                        && cal.get(Calendar.MINUTE) == MyBirthday.getPluginInstance().getConfig().getInt("time.minutes")) {
                    if (!MyBirthday.getPluginInstance().getTodaybirthday().isEmpty()) {
                        discord.createMessageandSend();
                    }

                }

            }

        }.runTaskTimer(MyBirthday.getPluginInstance(), 0L, 1200L);

    }

}
