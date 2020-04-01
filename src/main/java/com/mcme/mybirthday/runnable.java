/*
 * Copyright (C) 2020 MCME (Fraspace5)
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

import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author fraspace5
 */
public class runnable {

    public static void SetListRunnable() {

        new BukkitRunnable() {

            @Override
            public void run() {

                try {
                    MyBirthday.getPluginInstance().SetTodayBirthdays();
                } catch (SQLException ex) {
                    MyBirthday.getPluginInstance().Logger.getLogger(MyBirthday.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }.runTaskTimer(MyBirthday.getPluginInstance(), 0L, 600L);

    }

    public static void ShowListRunnable() {

        new BukkitRunnable() {

            @Override
            public void run() {
                PluginData.showlist();

            }

        }.runTaskTimer(MyBirthday.getPluginInstance(), MyBirthday.getPluginInstance().broadcastlistevery * 60 * 20 * 1000000, MyBirthday.getPluginInstance().broadcastlistevery * 60 * 20);

    }

    public static void ConnectionRunnable() {

        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    if (!MyBirthday.getPluginInstance().con.isValid(2)) {

                        MyBirthday.getPluginInstance().openConnection();

                    }
                } catch (SQLException ex) {
                    MyBirthday.getPluginInstance().Logger.getLogger(MyBirthday.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }.runTaskTimer(MyBirthday.getPluginInstance(), 60L, 100L);

    }

    public static void DiscordRunnable() {

        new BukkitRunnable() {

            @Override
            public void run() {

                TimeZone tim = TimeZone.getTimeZone("Europe/London");
                Calendar cal = Calendar.getInstance(tim);

                if (cal.get(Calendar.HOUR_OF_DAY) == MyBirthday.getPluginInstance().getConfig().getInt("time.hours")
                        && cal.get(Calendar.MINUTE) == MyBirthday.getPluginInstance().getConfig().getInt("time.minutes")) {
                    if (!MyBirthday.getPluginInstance().todaybirthday.isEmpty()) {
                        PluginData.createMessageandSend();
                    }

                }

            }

        }.runTaskTimer(MyBirthday.getPluginInstance(), 0L, 1200L);

    }

}
