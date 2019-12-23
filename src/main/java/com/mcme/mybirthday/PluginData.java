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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Fraspace5
 */
public class PluginData {

    public static synchronized void showlist() {

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String statement = "SELECT * FROM birthday_data.b_data ;";

                    final ResultSet r = birthday.getPluginInstance().con.prepareStatement(statement).executeQuery();

                    // do database stuff here
                    try {
                        if (r.first()) {

                            StringBuilder builder = new StringBuilder();
                            do {
                                OfflinePlayer pal = Bukkit.getOfflinePlayer(r.getString("uuid"));
                                if (birthday.getPluginInstance().todaybirthday.contains(pal.getName())) {
                                    long delay = birthday.getPluginInstance().broadcastlistevery * 60 * 1000;

                                    if (birthday.getPluginInstance().playeragebool == true) {
                                        try {

                                            UUID uuid = pal.getUniqueId();
                                            int now = Calendar.getInstance().get(Calendar.YEAR);

                                            int your = r.getInt("year");
                                            int year = now - your;
                                            int index = birthday.getPluginInstance().todaybirthday.size() - 1;
                                            String val = birthday.getPluginInstance().todaybirthday.get(index);

                                            if (pal.getName().equalsIgnoreCase(val) == false) {
                                                builder.append(pal.getName() + " [" + year + "], ");
                                            } else {
                                                builder.append(pal.getName() + " [" + year + "] !");
                                            }
                                        } catch (SQLException ex) {
                                            Logger.getLogger(birthday.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                    } else {

                                        int index = birthday.getPluginInstance().todaybirthday.size() - 1;
                                        String val = birthday.getPluginInstance().todaybirthday.get(index);

                                        if (pal.getName().equalsIgnoreCase(val) == false) {
                                            builder.append(pal.getName() + " " + ", ");
                                        } else {
                                            builder.append(pal.getName() + " " + "!");
                                        }

                                    }

                                }

                            } while (r.next());
                            String text = builder.toString();
                            if (birthday.getPluginInstance().liston == true) {
                                for (Player pl : Bukkit.getOnlinePlayers()) {
                                    String si = birthday.getPluginInstance().todaybirthday.toString();
                                    if (!birthday.getPluginInstance().todaybirthday.isEmpty()) {
                                        pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Today is the Birthday of " + ChatColor.YELLOW + text);
                                    }

                                }
                            }
                        }
                    } catch (SQLException e) {
                        Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, e);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.runTaskAsynchronously(birthday.getPluginInstance());

    }

    public static synchronized void onJoinSQL(final PlayerJoinEvent e) {

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String statement = "SELECT * FROM birthday_data.b_data WHERE uuid = '" + e.getPlayer().getUniqueId().toString() + "' ;";

                    final ResultSet r = birthday.getPluginInstance().con.createStatement().executeQuery(statement);

                    // do database stuff here
                    try {
                        final Player pl = e.getPlayer();
                        final UUID uuid = pl.getUniqueId();
                        String nameplayer = Bukkit.getOfflinePlayer(uuid).getName();
                        boolean listonjoin = birthday.getPluginInstance().getConfig().getBoolean("listonjoin");
                        int now = Calendar.getInstance().get(Calendar.YEAR);

                        if (!r.first()) {

                            pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + " You haven't set your birthday yet! Use /birthday set");

                        } else {
                            if (birthday.getPluginInstance().todaybirthday.contains(nameplayer)) {
                                int your = r.getInt("year");
                                int year = now - your;
                                e.setJoinMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW.BOLD + " Happy Birthday " + ChatColor.YELLOW.BOLD + nameplayer + ChatColor.YELLOW + " from all the Minecraft Middle Earth Community "
                                        + ChatColor.YELLOW + year + ChatColor.YELLOW + " years is a great achievement");
                                if (birthday.getPluginInstance().todaybirthday.size() > 1) {
                                    birthday.getPluginInstance().OtherPeopleBirthday(nameplayer, e);

                                }

                            } else if (listonjoin == true && !birthday.getPluginInstance().todaybirthday.contains(nameplayer)) {
                                birthday.getPluginInstance().ShowList(pl, e);
                            }
                            if (birthday.getPluginInstance().particles == true && birthday.getPluginInstance().todaybirthday.contains(nameplayer)) {

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {

                                        try {
                                            if (r.getBoolean("particles") == true) {
                                                Location location = pl.getLocation().add(0, 2, 0);
                                                pl.getWorld().spawnParticle(Particle.REDSTONE, location, 10, 1.0, 1.0, 1.0, new Particle.DustOptions(Color.RED, 1));
                                                pl.getWorld().spawnParticle(Particle.REDSTONE, location, 10, 1.0, 1.0, 1.0, new Particle.DustOptions(Color.YELLOW, 1));
                                                pl.getWorld().spawnParticle(Particle.REDSTONE, location, 10, 1.0, 1.0, 1.0, new Particle.DustOptions(Color.GREEN, 1));
                                                pl.getWorld().spawnParticle(Particle.REDSTONE, location, 10, 1.0, 1.0, 1.0, new Particle.DustOptions(Color.FUCHSIA, 1));
                                            } else {
                                                try {
                                                    if (r.getBoolean("particles") == false) {

                                                    }
                                                } catch (SQLException ex) {
                                                    Logger.getLogger(birthday.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            }
                                        } catch (SQLException ex) {
                                            Logger.getLogger(birthday.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                    }

                                }.runTaskTimer(birthday.getPluginInstance(), 0, 10L);
                            }
                        }

                    } catch (SQLException e) {
                        Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, e);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.runTaskAsynchronously(birthday.getPluginInstance());

    }

    public static synchronized void otherpeopleSQL(final String nameplayer, final PlayerJoinEvent e) {

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String statement = "SELECT * FROM birthday_data.b_data ;";

                    final ResultSet r = birthday.getPluginInstance().con.createStatement().executeQuery(statement);

                    // do database stuff here
                    try {
                        if (r.first()) {
                            StringBuilder builder = new StringBuilder();
                            do {
                                OfflinePlayer pal = Bukkit.getOfflinePlayer(r.getString("uuid"));
                                if (birthday.getPluginInstance().todaybirthday.contains(pal.getName())) {
                                    if (birthday.getPluginInstance().playeragebool == true && !pal.getName().equalsIgnoreCase(nameplayer)) {

                                        UUID uuid = pal.getUniqueId();
                                        int now = Calendar.getInstance().get(Calendar.YEAR);
                                        int your = r.getInt("year");
                                        int year = now - your;
                                        int index = birthday.getPluginInstance().todaybirthday.size() - 1;
                                        String val = birthday.getPluginInstance().todaybirthday.get(index);

                                        if (pal.getName().equalsIgnoreCase(val) == false) {
                                            builder.append(pal.getName() + " [" + year + "], ");
                                        } else {
                                            builder.append(pal.getName() + " [" + year + "] !");
                                        }

                                    } else if (birthday.getPluginInstance().playeragebool == false && !pal.getName().equalsIgnoreCase(nameplayer)) {

                                        int index = birthday.getPluginInstance().todaybirthday.size() - 1;
                                        String val = birthday.getPluginInstance().todaybirthday.get(index);

                                        if (pal.getName().equalsIgnoreCase(val) == false) {
                                            builder.append(pal.getName() + " " + ", ");
                                        } else {
                                            builder.append(pal.getName() + " " + "!");
                                        }

                                    } else {

                                    }

                                }
                            } while (r.next());
                            String text = builder.toString();

                            e.setJoinMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Today is also the Birthday of " + ChatColor.YELLOW + text);
                        }
                    } catch (SQLException e) {
                        Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, e);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.runTaskAsynchronously(birthday.getPluginInstance());

    }

    public static synchronized void showListSQL(final Player pl, final PlayerJoinEvent e) {

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String statement = "SELECT * FROM birthday_data.b_data ;";

                    final ResultSet r = birthday.getPluginInstance().con.createStatement().executeQuery(statement);

                    // do database stuff here
                    try {
                        if (r.first()) {
                            StringBuilder builder = new StringBuilder();
                            OfflinePlayer pal = Bukkit.getOfflinePlayer(r.getString("uuid"));
                            do {
                                if (birthday.getPluginInstance().todaybirthday.contains(pal.getName())) {
                                    if (birthday.getPluginInstance().playeragebool == true) {

                                        UUID uuid = pal.getUniqueId();
                                        int now = Calendar.getInstance().get(Calendar.YEAR);
                                        int your = r.getInt("year");
                                        int year = now - your;
                                        int index = birthday.getPluginInstance().todaybirthday.size() - 1;
                                        String val = birthday.getPluginInstance().todaybirthday.get(index);

                                        if (pal.getName().equalsIgnoreCase(val) == false) {
                                            builder.append(pal.getName() + " [" + year + "], ");
                                        } else {
                                            builder.append(pal.getName() + " [" + year + "] !");
                                        }

                                    } else {

                                        int index = birthday.getPluginInstance().todaybirthday.size() - 1;
                                        String val = birthday.getPluginInstance().todaybirthday.get(index);

                                        if (pal.getName().equalsIgnoreCase(val) == false) {
                                            builder.append(pal.getName() + " " + ", ");
                                        } else {
                                            builder.append(pal.getName() + " " + "!");
                                        }

                                    }

                                }
                            } while (r.next());
                            String text = builder.toString();
                            if (birthday.getPluginInstance().todaybirthday.isEmpty() == false) {

                                e.setJoinMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Today is the Birthday of " + ChatColor.YELLOW + text);
                            }
                        }
                    } catch (SQLException e) {
                        Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, e);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.runTaskAsynchronously(birthday.getPluginInstance());

    }

    public static synchronized void today() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String statement = "SELECT * FROM birthday_data.b_data ;";

                    final ResultSet r = birthday.getPluginInstance().con.createStatement().executeQuery(statement);

                    // do database stuff here
                    if (r.first()) {
                        do {
                            UUID uuid = UUID.fromString(r.getString("uuid"));
                            Calendar cale = Calendar.getInstance();
                            cale.set(r.getInt("year"), r.getInt("month"), r.getInt("day"));
                            String name = Bukkit.getOfflinePlayer(uuid).getName();
                            int dc = cale.get(Calendar.DAY_OF_MONTH);

                            int mc = cale.get(Calendar.MONTH);
                            int dayn = birthday.getPluginInstance().call.get(Calendar.DAY_OF_MONTH);
                            int month = birthday.getPluginInstance().call.get(Calendar.MONTH);

                            System.out.println(mc + " " + dc + " " + dayn + " " + month);
                            if (dc == dayn && mc == month) {

                                if (birthday.getPluginInstance().todaybirthday.contains(name) == true) {

                                } else {

                                    birthday.getPluginInstance().todaybirthday.add(name);
                                }

                            }

                        } while (r.next());
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.runTaskAsynchronously(birthday.getPluginInstance());

    }

    public static synchronized void updateData(final UUID uuid, final Calendar date, final Boolean bol, final Long cooldown) throws SQLException {

        try {

            String statement = "SELECT * FROM birthday_data.b_data WHERE uuid = '" + uuid.toString() + "' ;";
            ResultSet r = birthday.getPluginInstance().con.prepareStatement(statement).executeQuery(statement);

            if (r.first()) {

                String stat = "UPDATE birthday_data.b_data SET day = '" + date.get(Calendar.DAY_OF_MONTH) + "', month = '" + date.get(Calendar.MONTH) + "', year = '" + date.get(Calendar.YEAR) + "', particles = " + String.valueOf(bol) + ", cooldown = '" + cooldown.toString() + "' WHERE uuid = '" + uuid.toString() + "' ;";
                birthday.getPluginInstance().con.prepareStatement(stat).executeUpdate(stat);
                System.out.println(stat);
            } else {

                String stat = "INSERT INTO birthday_data.b_data (uuid, day, particles, cooldown, month, year) VALUES ('" + uuid.toString() + "' , '" + date.get(Calendar.DAY_OF_MONTH) + "' , true , '" + cooldown.toString() + "','" + date.get(Calendar.MONTH) + "','" + date.get(Calendar.YEAR) + "') ;";
                birthday.getPluginInstance().con.prepareStatement(stat).executeUpdate(stat);
                System.out.println(stat);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static synchronized void insertData(final UUID uuid, final Calendar date, final Boolean bol, final Long cooldown) throws SQLException {

        String stat = "INSERT INTO birthday_data.b_data (uuid, day, particles, cooldown, month, year) VALUES ('" + uuid.toString() + "' , '" + date.get(Calendar.DAY_OF_MONTH) + "' , true , '" + cooldown.toString() + "','" + date.get(Calendar.MONTH) + "','" + date.get(Calendar.YEAR) + "') ;";
        System.out.println(stat);
        try {
            birthday.getPluginInstance().con.prepareStatement(stat).executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*
     public static synchronized void deleteData(final UUID uuid) throws SQLException {
     if (birthday.getPluginInstance().isConnect()) {

     String stat = "DELETE birthday_data.b_data WHERE uuid = '" + uuid.toString() + "' ;";
     try {
     birthday.getPluginInstance().con.prepareStatement(stat).executeUpdate();
     } catch (SQLException ex) {
     Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
     }

     } else {
     birthday.getPluginInstance().openConnection();
     }
     }
     */
    public static synchronized void particlesSQL(final UUID uuid, final String[] args, final Player pl) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String statement = "SELECT * FROM birthday_data.b_data WHERE uuid = '" + uuid.toString() + "' ;";

                    final ResultSet r = birthday.getPluginInstance().con.prepareStatement(statement).executeQuery();

                    // do database stuff here
                    try {
                        if (r.first()) {
                            if (args[1].equalsIgnoreCase("on")) {
                                try {
                                    pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Particles on");
                                    Calendar cal = Calendar.getInstance();
                                    cal.set(r.getInt("year"), r.getInt("month"), r.getInt("day"));
                                    PluginData.updateData(uuid, cal, Boolean.TRUE, r.getLong("cooldown"));
                                } catch (SQLException ex) {
                                    Logger.getLogger(command.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            } else if (args[1].equalsIgnoreCase("off")) {
                                try {
                                    pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Particles off");
                                    Calendar cal = Calendar.getInstance();
                                    cal.set(r.getInt("year"), r.getInt("month"), r.getInt("day"));
                                    PluginData.updateData(uuid, cal, Boolean.FALSE, r.getLong("cooldown"));
                                } catch (SQLException ex) {
                                    Logger.getLogger(command.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }
                        }

                    } catch (SQLException e) {
                        Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, e);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.runTaskAsynchronously(birthday.getPluginInstance());

    }

    public static synchronized void setSQL(final UUID uuid, final String[] args, final Player pl) throws SQLException {
        new BukkitRunnable() {
            @Override
            public void run() {

                try {
                    String statement = "SELECT * FROM birthday_data.b_data WHERE uuid = '" + uuid.toString() + "' ;";

                    final ResultSet r = birthday.getPluginInstance().con.prepareStatement(statement).executeQuery();

                    // do database stuff here
                    try {
                        if (r.first()) {
                            if (r.getLong("cooldown") > System.currentTimeMillis()) {

                                long timerem = r.getLong("cooldown") - System.currentTimeMillis();
                                int remaining = (int) (timerem / 1000);
                                pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Sorry you can't set your birthday. You must wait " + ChatColor.YELLOW + remaining + ChatColor.YELLOW + " seconds");
                            } else {
                                String dd = args[1];
                                String mm = args[2];
                                String yyyy = args[3];

                                int d = Integer.parseInt(dd);
                                int m = Integer.parseInt(mm);
                                int y = Integer.parseInt(yyyy);
                                Calendar cal = Calendar.getInstance();
                                cal.set(y, m, d);

                                if (birthday.getPluginInstance().isMactive() == true) {
                                    pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + birthday.getPluginInstance().getMessage()));
                                }
                                pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Birthday set correctly! Now you must wait " + ChatColor.YELLOW + birthday.getPluginInstance().getCoold() + ChatColor.YELLOW + " hours to update it"));
                                Long l = (System.currentTimeMillis() + (birthday.getPluginInstance().getCooldown1() * 3600) * 1000);
                                System.out.println("yes");
                                PluginData.updateData(uuid, cal, r.getBoolean("particles"), l);

                            }

                        } else {
                            String dd = args[1];
                            String mm = args[2];
                            String yyyy = args[3];

                            int d = Integer.parseInt(dd);
                            int m = Integer.parseInt(mm);
                            int y = Integer.parseInt(yyyy);
                            Calendar cal = Calendar.getInstance();
                            cal.set(y, m, d);
                            Long l = (System.currentTimeMillis() + (birthday.getPluginInstance().getCooldown1() * 3600) * 1000);
                            PluginData.insertData(uuid, cal, Boolean.TRUE, l);
                            System.out.println("no");
                            if (birthday.getPluginInstance().isMactive() == true) {
                                pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] : " + ChatColor.YELLOW + birthday.getPluginInstance().getMessage()));
                            }
                            pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Birthday set correctly! Now you must wait " + ChatColor.YELLOW + birthday.getPluginInstance().getCoold() + ChatColor.YELLOW + " hours to update it"));
                        }

                    } catch (SQLException e) {
                        Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, e);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }.runTaskAsynchronously(birthday.getPluginInstance());

    }

}
