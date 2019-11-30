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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Fraspace5
 */
public class PluginData {

    public static synchronized ResultSet loadData(UUID uuid) throws SQLException {
        new BukkitRunnable() {
            @Override
            public void run() {
                String statement = "SELECT uuid FROM BDATA WHERE uuid = " + uuid.toString();
                ResultSet r = birthday.getPluginInstance().con.createStatement().executeQuery(statement);
                // do database stuff here
                new BukkitRunnable() {
                    @Override
                    public ResultSet run() {
                        return r;
                    }
                }.runTaskAsynchronously(birthday.getPluginInstance());
            }
        }.runTaskAsynchronously(birthday.getPluginInstance());

    }

    public static synchronized ResultSet loadAll() throws SQLException {
        new BukkitRunnable() {
            @Override
            public void run() {
                String statement = "SELECT uuid FROM BDATA";
                ResultSet r = birthday.getPluginInstance().con.createStatement().executeQuery(statement);
                // do database stuff here
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        return r;
                    }
                }.runTaskAsynchronously(birthday.getPluginInstance());
            }
        }.runTaskAsynchronously(birthday.getPluginInstance());

    }

    public static synchronized void updateData(final UUID uuid, final Calendar date, final Boolean bol, final Long cooldown) throws SQLException {

        if (birthday.getPluginInstance().isConnect()) {

            new BukkitRunnable() {

                @Override
                public void run() {

                    String statement = "SELECT uuid FROM BDATA WHERE uuid = " + uuid.toString();
                    ResultSet r = null;
                    try {
                        r = birthday.getPluginInstance().con.createStatement().executeQuery(statement);
                    } catch (SQLException ex) {
                        Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    try {
                        if (r.first()) {
                            Date d = new Date(date.getTimeInMillis());
                            String stat = "UPDATE INTO BDATA (uuid, date, particles, cooldown) VALUES  (" + uuid + ", " + d + ", " + bol + ", " + cooldown + ") WHERE uuid = " + uuid;
                            birthday.getPluginInstance().con.createStatement().execute(stat);

                        } else {
                            Date d = new Date(date.getTimeInMillis());
                            String stat = "INSERT INTO BDATA (uuid, date, particles, cooldown) VALUES  (" + uuid + ", " + d + ", " + bol + ", " + cooldown + ")";
                            birthday.getPluginInstance().con.createStatement().execute(stat);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }.runTaskAsynchronously(birthday.getPluginInstance());

        } else {
            birthday.getPluginInstance().openConnection();
        }
    }

    public static synchronized void insertData(final UUID uuid, final Calendar date, final Boolean bol, final Long cooldown) throws SQLException {
        if (birthday.getPluginInstance().isConnect()) {
            new BukkitRunnable() {

                @Override
                public void run() {

                    Date d = new Date(date.getTimeInMillis());
                    String stat = "INSERT INTO BDATA (uuid, date, particles, cooldown) VALUES  (" + uuid + ", " + d + ", " + bol + ", " + cooldown + ")";
                    try {
                        birthday.getPluginInstance().con.createStatement().executeQuery(stat);
                    } catch (SQLException ex) {
                        Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }.runTaskAsynchronously(birthday.getPluginInstance());

        } else {
            birthday.getPluginInstance().openConnection();
        }
    }

    public static synchronized void deleteData(final UUID uuid) throws SQLException {
        if (birthday.getPluginInstance().isConnect()) {
            new BukkitRunnable() {

                @Override
                public void run() {

                    String stat = "DELETE BDATA WHERE " + uuid;
                    try {
                        birthday.getPluginInstance().con.createStatement().execute(stat);
                    } catch (SQLException ex) {
                        Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }.runTaskAsynchronously(birthday.getPluginInstance());

        } else {
            birthday.getPluginInstance().openConnection();
        }
    }

    public static synchronized boolean exists(UUID uuid) throws SQLException {
        if (birthday.getPluginInstance().isConnect()) {
            String statement = "SELECT uuid FROM BDATA WHERE uuid = " + uuid.toString();
            ResultSet r = birthday.getPluginInstance().con.createStatement().executeQuery(statement);

            if (r.first()) {

                return true;

            } else {

                return false;

            }

        } else {
            birthday.getPluginInstance().openConnection();
            return false;
        }
    }

    public static Calendar convert(Date date) {

        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        return cal;

    }

}
