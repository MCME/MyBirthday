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
package com.mcme.mybirthday.data;

import com.mcme.mybirthday.MyBirthday;
import com.mcmiddleearth.pluginutil.message.FancyMessage;
import com.mcmiddleearth.pluginutil.message.MessageType;
import com.mcmiddleearth.pluginutil.message.MessageUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Fraspace5
 */
public class PluginData {

    public static synchronized void showListAll() {

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    final ResultSet r = MyBirthday.getSelect_data().executeQuery();

                    if (r.first()) {

                        StringBuilder builder = new StringBuilder();
                        do {
                            UUID uuid = UUID.fromString(r.getString("uuid"));
                            OfflinePlayer pal = Bukkit.getOfflinePlayer(uuid);
                            if (MyBirthday.getPluginInstance().getTodaybirthday().contains(uuid)) {

                                if (MyBirthday.getPluginInstance().isPlayeragebool()) {
                                    try {

                                        int now = Calendar.getInstance().get(Calendar.YEAR);

                                        int your = r.getInt("year");
                                        int year = now - your;
                                        int index = MyBirthday.getPluginInstance().getTodaybirthday().size() - 1;
                                        UUID val = MyBirthday.getPluginInstance().getTodaybirthday().get(index);

                                        if (!uuid.equals(val)) {
                                            builder.append(pal.getName()).append(" (").append(year).append("), ");
                                        } else {
                                            builder.append(pal.getName()).append(" (").append(year).append(") !");
                                        }
                                    } catch (SQLException ex) {
                                        Logger.getLogger(MyBirthday.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                } else {

                                    int index = MyBirthday.getPluginInstance().getTodaybirthday().size() - 1;
                                    UUID val = MyBirthday.getPluginInstance().getTodaybirthday().get(index);

                                    if (!uuid.equals(val)) {
                                        builder.append(pal.getName()).append(" , ");
                                    } else {
                                        builder.append(pal.getName()).append(" !");
                                    }

                                }

                            }

                        } while (r.next());

                        String text = builder.toString();
                        if (MyBirthday.getPluginInstance().isListon()) {
                            for (Player pl : Bukkit.getOnlinePlayers()) {

                                if (!MyBirthday.getPluginInstance().getTodaybirthday().isEmpty()) {

                                    String text2 = ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "[MyBirthday] :" + ChatColor.YELLOW + " Today is the Birthday of ";
                                    if (MyBirthday.getPluginInstance().getTodaybirthday().size() < 5) {
                                        text2 += builder.toString();
                                        pl.sendMessage(text2);
                                    } else {

                                        FancyMessage message = new FancyMessage(MessageType.INFO_NO_PREFIX, new MessageUtil());

                                        message.addTooltipped(text2 + builder.toString().substring(0, 5) + "...", builder.toString());
                                        message.send(pl);
                                    }
                                }

                            }
                        }
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.runTaskAsynchronously(MyBirthday.getPluginInstance());

    }

    public static synchronized void otherpeopleSQL(final UUID uu, final PlayerJoinEvent e) {

        new BukkitRunnable() {
            @Override
            public void run() {

                try {
                    final ResultSet r = MyBirthday.getSelect_data().executeQuery();

                    if (r.first()) {
                        final StringBuilder builder = new StringBuilder();
                        do {

                            UUID uuid = UUID.fromString(r.getString("uuid"));

                            OfflinePlayer pal = Bukkit.getOfflinePlayer(uuid);
                            if (MyBirthday.getPluginInstance().getTodaybirthday().contains(uuid)) {

                                if (MyBirthday.getPluginInstance().isPlayeragebool() && !uuid.equals(uu)) {

                                    int now = Calendar.getInstance().get(Calendar.YEAR);
                                    int your = r.getInt("year");
                                    int year = now - your;
                                    int index = MyBirthday.getPluginInstance().getTodaybirthday().size() - 1;
                                    UUID val = MyBirthday.getPluginInstance().getTodaybirthday().get(index);

                                    if (!uuid.equals(val)) {
                                        builder.append(pal.getName()).append(" (").append(year).append("), ");
                                    } else {
                                        builder.append(pal.getName()).append(" (").append(year).append(") !");
                                    }

                                } else if (!MyBirthday.getPluginInstance().isPlayeragebool() && !uuid.equals(uu)) {

                                    int index = MyBirthday.getPluginInstance().getTodaybirthday().size() - 1;
                                    UUID val = MyBirthday.getPluginInstance().getTodaybirthday().get(index);

                                    if (!uuid.equals(val)) {
                                        builder.append(pal.getName()).append(" , ");
                                    } else {
                                        builder.append(pal.getName()).append(" !");
                                    }

                                }

                            }
                        } while (r.next());

                        new BukkitRunnable() {

                            @Override
                            public void run() {

                                String text = ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "[MyBirthday] :" + ChatColor.YELLOW + " Today is the Birthday of ";
                                if (MyBirthday.getPluginInstance().getTodaybirthday().size() < 5) {
                                    text += builder.toString();
                                    e.getPlayer().sendMessage(text);
                                } else {

                                    FancyMessage message = new FancyMessage(MessageType.INFO_NO_PREFIX, new MessageUtil());

                                    message.addTooltipped(text + builder.toString().substring(0, 5) + "...", builder.toString());
                                    message.send(e.getPlayer());
                                }
                            }

                        }.runTaskLater(MyBirthday.getPluginInstance(), 25L);

                    }

                } catch (SQLException ex) {
                    Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.runTaskAsynchronously(MyBirthday.getPluginInstance());

    }

    public static synchronized void showListSQL(final Player pl, final PlayerJoinEvent e) {

        new BukkitRunnable() {
            @Override
            public void run() {

                try {
                    final ResultSet r = MyBirthday.getSelect_data().executeQuery();
                    if (r.first()) {
                        final StringBuilder builder = new StringBuilder();

                        do {

                            UUID uuid = UUID.fromString(r.getString("uuid"));
                            OfflinePlayer pal = Bukkit.getOfflinePlayer(uuid);
                            if (MyBirthday.getPluginInstance().getTodaybirthday().contains(UUID.fromString(r.getString("uuid")))) {
                                if (MyBirthday.getPluginInstance().isPlayeragebool()) {

                                    int now = Calendar.getInstance().get(Calendar.YEAR);
                                    int your = r.getInt("year");
                                    int year = now - your;
                                    int index = MyBirthday.getPluginInstance().getTodaybirthday().size() - 1;

                                    UUID val = MyBirthday.getPluginInstance().getTodaybirthday().get(index);

                                    if (!uuid.equals(val)) {
                                        builder.append(pal.getName()).append(" (").append(year).append("), ");
                                    } else {
                                        builder.append(year).append(pal.getName()).append(" () !");
                                    }

                                } else {

                                    int index = MyBirthday.getPluginInstance().getTodaybirthday().size() - 1;
                                    UUID val = MyBirthday.getPluginInstance().getTodaybirthday().get(index);

                                    if (!uuid.equals(val)) {
                                        builder.append(pal.getName()).append(" , ");
                                    } else {
                                        builder.append(pal.getName()).append(" !");
                                    }

                                }

                            }
                        } while (r.next());

                        if (!MyBirthday.getPluginInstance().getTodaybirthday().isEmpty()) {

                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    String text = ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "[MyBirthday] :" + ChatColor.YELLOW + " Today is the Birthday of ";
                                    if (MyBirthday.getPluginInstance().getTodaybirthday().size() < 5) {
                                        text += builder.toString();
                                        pl.sendMessage(text);
                                    } else {

                                        FancyMessage message = new FancyMessage(MessageType.INFO_NO_PREFIX, new MessageUtil());

                                        message.addTooltipped(text + builder.toString().substring(0, 5) + "...", builder.toString());
                                        message.send(pl);
                                    }

                                }

                            }.runTaskLaterAsynchronously(MyBirthday.getPluginInstance(), 25L);

                        }
                    }
                } catch (SQLException e) {

                }

            }
        }.runTaskAsynchronously(MyBirthday.getPluginInstance());

    }

    public static synchronized void setTodayBirthdays() {

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    MyBirthday.getPluginInstance().getTodaybirthday().clear();
                    int dayn = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    int month = Calendar.getInstance().get(Calendar.MONTH);

                    final ResultSet r = MyBirthday.getSelect_data().executeQuery();

                    if (r.first()) {
                        do {

                            UUID uuid = UUID.fromString(r.getString("uuid"));
                            OfflinePlayer pl = Bukkit.getOfflinePlayer(uuid);
                            if (!(r.getInt("day") == 0 && r.getInt("month") == 0 && r.getInt("year") == 1970)) {

                                if (r.getInt("day") == dayn && r.getInt("month") == month && Bukkit.getOfflinePlayer(uuid).getLastPlayed() > (System.currentTimeMillis() - (15552000000.00)) && !MyBirthday.getPluginInstance().getTodaybirthday().contains(uuid)) {

                                    //player must have been online in the last 5 months
                                    if (pl.getLastPlayed() > (System.currentTimeMillis() - 13392000000L)) {
                                        MyBirthday.getPluginInstance().getTodaybirthday().add(uuid);
                                    }

                                }

                            }

                        } while (r.next());
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.runTaskAsynchronously(MyBirthday.getPluginInstance());

    }

    /**
     * Method to set your birthday
     *
     * @param uuid
     * @param date
     * @param pl
     * @throws SQLException
     */
    public static synchronized void setBirthday(final UUID uuid, final String[] date, final Player pl) throws SQLException {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    MyBirthday.getSelect_pdata().setString(1, uuid.toString());
                    final ResultSet r = MyBirthday.getSelect_pdata().executeQuery();

                    if (r.first()) {
                        if (r.getLong("cooldown") > System.currentTimeMillis()) {

                            long timerem = r.getLong("cooldown") - System.currentTimeMillis();
                            int remaining = (int) (timerem / 1000);
                            pl.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "[MyBirthday] :" + ChatColor.YELLOW + " Sorry you can't set your birthday. You must wait " + remaining + " seconds");
                        } else {
                            String dd = date[0];
                            String mm = date[1];
                            String yyyy = date[2];

                            int d = Integer.parseInt(dd);
                            int m = (Integer.parseInt(mm) - 1);
                            int y = Integer.parseInt(yyyy);
                            Calendar cal = Calendar.getInstance();
                            cal.set(y, m, d);

                            if (MyBirthday.getPluginInstance().isMactive()) {
                                pl.sendMessage((ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "[MyBirthday] :" + ChatColor.YELLOW + MyBirthday.getPluginInstance().getMessage()));
                            }
                            pl.sendMessage((ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "[MyBirthday] :" + ChatColor.YELLOW + " Birthday set correctly! Now you must wait " + MyBirthday.getPluginInstance().getCoold() + " hours to update it"));
                            Long l = (System.currentTimeMillis() + (MyBirthday.getPluginInstance().getCooldown1() * 3600) * 1000);

                            PluginData.updateData(uuid, cal, r.getBoolean("particles"), l);

                        }

                    } else {
                        String dd = date[0];
                        String mm = date[1];
                        String yyyy = date[2];

                        int d = Integer.parseInt(dd);
                        int m = (Integer.parseInt(mm) - 1);
                        int y = Integer.parseInt(yyyy);
                        Calendar cal = Calendar.getInstance();
                        cal.set(y, m, d);
                        Long l = (System.currentTimeMillis() + (MyBirthday.getPluginInstance().getCooldown1() * 3600) * 1000);
                        updateData(uuid, cal, Boolean.TRUE, l);

                        if (MyBirthday.getPluginInstance().isMactive()) {
                            pl.sendMessage((ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "[MyBirthday] : " + ChatColor.YELLOW + MyBirthday.getPluginInstance().getMessage()));
                        }

                        pl.sendMessage((ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "[MyBirthday] :" + ChatColor.YELLOW + " Birthday set correctly! Now you must wait " + MyBirthday.getPluginInstance().getCoold() + " hours to update it"));
                    }

                } catch (SQLException e) {
                }

            }
        }.runTaskAsynchronously(MyBirthday.getPluginInstance());

    }

    /**
     * Method for staff command
     *
     * @param uuid
     * @param date
     * @param pl
     * @throws SQLException
     */
    public static synchronized void setBirthdayStaff(final UUID uuid, final String[] date, final Player pl) throws SQLException {
        new BukkitRunnable() {
            @Override
            public void run() {

                try {
                    MyBirthday.getSelect_pdata().setString(1, uuid.toString());
                    final ResultSet r = MyBirthday.getSelect_pdata().executeQuery();

                    if (r.first()) {

                        String dd = date[0];
                        String mm = date[1];
                        String yyyy = date[2];

                        int d = Integer.parseInt(dd);
                        int m = (Integer.parseInt(mm) - 1);
                        int y = Integer.parseInt(yyyy);
                        Calendar cal = Calendar.getInstance();
                        cal.set(y, m, d);
                        Long l = (System.currentTimeMillis() + (MyBirthday.getPluginInstance().getCooldown1() * 3600) * 1000);

                        if (MyBirthday.getPluginInstance().isMactive()) {
                            pl.sendMessage((ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "[MyBirthday] :" + ChatColor.YELLOW + MyBirthday.getPluginInstance().getMessage()));
                        }

                        pl.sendMessage((ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "[MyBirthday] :" + ChatColor.YELLOW + " Birthday set correctly!"));

                        updateData(uuid, cal, r.getBoolean("particles"), l);

                    } else {

                        pl.sendMessage((ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "[MyBirthday] :" + ChatColor.YELLOW + "Hey, " + pl.getName() + " the player needs to set his birthday before"));

                    }

                } catch (SQLException e) {
                    Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, e);
                }

            }
        }.runTaskAsynchronously(MyBirthday.getPluginInstance());

    }

    public static synchronized void updateData(final UUID uuid, final Calendar date, final Boolean bol, final Long cooldown) throws SQLException {

        MyBirthday.getSelect_pdata().setString(1, uuid.toString());
        final ResultSet r = MyBirthday.getSelect_pdata().executeQuery();

        if (r.first()) {

            MyBirthday.getUpdate_pdata().setInt(1, date.get(Calendar.DAY_OF_MONTH));
            MyBirthday.getUpdate_pdata().setInt(2, date.get(Calendar.MONTH));
            MyBirthday.getUpdate_pdata().setInt(3, date.get(Calendar.YEAR));
            MyBirthday.getUpdate_pdata().setBoolean(4, bol);
            MyBirthday.getUpdate_pdata().setString(5, cooldown.toString());
            MyBirthday.getUpdate_pdata().setString(6, uuid.toString());

            MyBirthday.getUpdate_pdata().executeUpdate();

        } else {

            MyBirthday.getUpdate_pdata().setString(1, uuid.toString());
            MyBirthday.getUpdate_pdata().setInt(2, date.get(Calendar.DAY_OF_MONTH));
            MyBirthday.getUpdate_pdata().setString(3, cooldown.toString());
            MyBirthday.getUpdate_pdata().setInt(4, date.get(Calendar.MONTH));
            MyBirthday.getUpdate_pdata().setInt(5, date.get(Calendar.YEAR));

            MyBirthday.getUpdate_pdata().executeUpdate();

        }

    }

}
