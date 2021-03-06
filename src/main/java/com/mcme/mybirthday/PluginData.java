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

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.util.DiscordUtil;
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
import org.bukkit.plugin.Plugin;
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
                    String statement = "SELECT * FROM " + MyBirthday.getPluginInstance().database + ".b_data ;";

                    final ResultSet r = MyBirthday.getPluginInstance().con.prepareStatement(statement).executeQuery();

                    // do database stuff here
                    try {
                        if (r.first()) {

                            StringBuilder builder = new StringBuilder();
                            do {
                                UUID uuid = UUID.fromString(r.getString("uuid"));
                                OfflinePlayer pal = Bukkit.getOfflinePlayer(uuid);
                                if (MyBirthday.getPluginInstance().todaybirthday.contains(uuid)) {
                                    long delay = MyBirthday.getPluginInstance().broadcastlistevery * 60 * 1000;

                                    if (MyBirthday.getPluginInstance().playeragebool == true) {
                                        try {

                                            int now = Calendar.getInstance().get(Calendar.YEAR);

                                            int your = r.getInt("year");
                                            int year = now - your;
                                            int index = MyBirthday.getPluginInstance().todaybirthday.size() - 1;
                                            UUID val = MyBirthday.getPluginInstance().todaybirthday.get(index);

                                            if (uuid.equals(val) == false) {
                                                builder.append(pal.getName() + " (" + year + "), ");
                                            } else {
                                                builder.append(pal.getName() + " (" + year + ") !");
                                            }
                                        } catch (SQLException ex) {
                                            Logger.getLogger(MyBirthday.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                    } else {

                                        int index = MyBirthday.getPluginInstance().todaybirthday.size() - 1;
                                        UUID val = MyBirthday.getPluginInstance().todaybirthday.get(index);

                                        if (uuid.equals(val) == false) {
                                            builder.append(pal.getName() + " " + ", ");
                                        } else {
                                            builder.append(pal.getName() + " " + "!");
                                        }

                                    }

                                }

                            } while (r.next());
                            String text = builder.toString();
                            if (MyBirthday.getPluginInstance().liston == true) {
                                for (Player pl : Bukkit.getOnlinePlayers()) {

                                    if (!MyBirthday.getPluginInstance().todaybirthday.isEmpty()) {

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
        }.runTaskAsynchronously(MyBirthday.getPluginInstance());

    }

    public static synchronized void onJoinSQL(final PlayerJoinEvent e, final Boolean bool) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String statement = "SELECT * FROM " + MyBirthday.getPluginInstance().database + ".b_data WHERE uuid = '" + e.getPlayer().getUniqueId().toString() + "' ;";

                    final ResultSet r = MyBirthday.getPluginInstance().con.createStatement().executeQuery(statement);

                    // do database stuff here
                    try {
                        final Player pl = e.getPlayer();

                        final UUID uuid = pl.getUniqueId();
                        final String nameplayer = Bukkit.getOfflinePlayer(uuid).getName();
                        boolean listonjoin = MyBirthday.getPluginInstance().getConfig().getBoolean("listonjoin");
                        int now = Calendar.getInstance().get(Calendar.YEAR);

                        if (!r.first()) {

                            if (!bool) {
                                pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + " You haven't set your birthday yet! Use /birthday set");

                            }

                        } else {
                            if (MyBirthday.getPluginInstance().todaybirthday.contains(uuid)) {
                                int your = r.getInt("year");
                                final int year = now - your;
                                new BukkitRunnable() {

                                    @Override
                                    public void run() {

                                        pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW.BOLD + " Happy Birthday " + ChatColor.YELLOW.BOLD + nameplayer + ChatColor.YELLOW + " from all the Minecraft Middle Earth Community "
                                                + ChatColor.YELLOW + year + ChatColor.YELLOW + " years is a great achievement");
                                    }

                                }.runTaskLaterAsynchronously(MyBirthday.getPluginInstance(), 25L);

                                if (MyBirthday.getPluginInstance().todaybirthday.size() > 1) {
                                    MyBirthday.getPluginInstance().OtherPeopleBirthday(uuid, e);

                                }

                            } else if (listonjoin == true && !MyBirthday.getPluginInstance().todaybirthday.contains(uuid)) {
                                MyBirthday.getPluginInstance().ShowList(pl, e);
                            }
                            if (MyBirthday.getPluginInstance().particles == true && MyBirthday.getPluginInstance().todaybirthday.contains(uuid)) {

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {

                                                try {
                                                    String stat = "SELECT * FROM " + MyBirthday.getPluginInstance().database + ".b_data WHERE uuid = '" + e.getPlayer().getUniqueId().toString() + "' ;";

                                                    ResultSet s = MyBirthday.getPluginInstance().con.createStatement().executeQuery(stat);
                                                    s.first();
                                                    if (s.first()) {

                                                        if (s.getBoolean("particles") == true && s.getInt("year") != 1970) {
                                                            Location location = pl.getLocation().add(0, 2, 0);
                                                            pl.getWorld().spawnParticle(Particle.REDSTONE, location, 10, 1.0, 1.0, 1.0, new Particle.DustOptions(Color.RED, 1));
                                                            pl.getWorld().spawnParticle(Particle.REDSTONE, location, 10, 1.0, 1.0, 1.0, new Particle.DustOptions(Color.YELLOW, 1));
                                                            pl.getWorld().spawnParticle(Particle.REDSTONE, location, 10, 1.0, 1.0, 1.0, new Particle.DustOptions(Color.GREEN, 1));
                                                            pl.getWorld().spawnParticle(Particle.REDSTONE, location, 10, 1.0, 1.0, 1.0, new Particle.DustOptions(Color.FUCHSIA, 1));
                                                        } else {

                                                            cancel();

                                                        }
                                                    }

                                                } catch (SQLException ex) {
                                                    Logger.getLogger(MyBirthday.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            }
                                        }.runTaskAsynchronously(MyBirthday.getPluginInstance());

                                    }

                                }.runTaskTimer(MyBirthday.getPluginInstance(), 0, 15L);
                            }
                        }

                    } catch (SQLException e) {
                        Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, e);
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
                    String statement = "SELECT * FROM " + MyBirthday.getPluginInstance().database + ".b_data ;";

                    final ResultSet r = MyBirthday.getPluginInstance().con.createStatement().executeQuery(statement);

                    // do database stuff here
                    try {
                        if (r.first()) {
                            StringBuilder builder = new StringBuilder();
                            do {
                                UUID uuid = UUID.fromString(r.getString("uuid"));
                                OfflinePlayer pal = Bukkit.getOfflinePlayer(uuid);
                                if (MyBirthday.getPluginInstance().todaybirthday.contains(uuid)) {

                                    if (MyBirthday.getPluginInstance().playeragebool == true && !uuid.equals(uu)) {

                                        int now = Calendar.getInstance().get(Calendar.YEAR);
                                        int your = r.getInt("year");
                                        int year = now - your;
                                        int index = MyBirthday.getPluginInstance().todaybirthday.size() - 1;
                                        UUID val = MyBirthday.getPluginInstance().todaybirthday.get(index);

                                        if (uuid.equals(val) == false) {
                                            builder.append(pal.getName() + " (" + year + "), ");
                                        } else {
                                            builder.append(pal.getName() + " (" + year + ") !");
                                        }

                                    } else if (MyBirthday.getPluginInstance().playeragebool == false && !uuid.equals(uu)) {

                                        int index = MyBirthday.getPluginInstance().todaybirthday.size() - 1;
                                        UUID val = MyBirthday.getPluginInstance().todaybirthday.get(index);

                                        if (uuid.equals(val) == false) {
                                            builder.append(pal.getName() + " " + ", ");
                                        } else {
                                            builder.append(pal.getName() + " " + "!");
                                        }

                                    }

                                }
                            } while (r.next());
                            final String text = builder.toString();

                            new BukkitRunnable() {

                                @Override
                                public void run() {

                                    e.getPlayer().sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Today is also the Birthday of " + ChatColor.YELLOW + text);
                                }

                            }.runTaskLaterAsynchronously(MyBirthday.getPluginInstance(), 25L);

                        }
                    } catch (SQLException e) {
                        Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, e);
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
                    String statement = "SELECT * FROM " + MyBirthday.getPluginInstance().database + ".b_data ;";

                    final ResultSet r = MyBirthday.getPluginInstance().con.createStatement().executeQuery(statement);

                    try {
                        if (r.first()) {
                            StringBuilder builder = new StringBuilder();
                            do {

                                UUID uuid = UUID.fromString(r.getString("uuid"));
                                OfflinePlayer pal = Bukkit.getOfflinePlayer(uuid);
                                if (MyBirthday.getPluginInstance().todaybirthday.contains(UUID.fromString(r.getString("uuid")))) {
                                    if (MyBirthday.getPluginInstance().playeragebool == true) {

                                        int now = Calendar.getInstance().get(Calendar.YEAR);
                                        int your = r.getInt("year");
                                        int year = now - your;
                                        int index = MyBirthday.getPluginInstance().todaybirthday.size() - 1;

                                        UUID val = MyBirthday.getPluginInstance().todaybirthday.get(index);

                                        if (uuid.equals(val) == false) {
                                            builder.append(pal.getName() + " (" + year + "), ");
                                        } else {
                                            builder.append(pal.getName() + " (" + year + ") !");
                                        }

                                    } else {

                                        int index = MyBirthday.getPluginInstance().todaybirthday.size() - 1;
                                        UUID val = MyBirthday.getPluginInstance().todaybirthday.get(index);

                                        if (uuid.equals(val) == false) {
                                            builder.append(pal.getName() + " " + ", ");
                                        } else {
                                            builder.append(pal.getName() + " " + "!");
                                        }

                                    }

                                }
                            } while (r.next());
                            final String text = builder.toString();
                            if (!MyBirthday.getPluginInstance().todaybirthday.isEmpty()) {

                                new BukkitRunnable() {

                                    @Override
                                    public void run() {

                                        pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Today is the Birthday of " + ChatColor.YELLOW + text);
                                    }

                                }.runTaskLaterAsynchronously(MyBirthday.getPluginInstance(), 25L);

                            }
                        }
                    } catch (SQLException e) {
                        Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, e);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.runTaskAsynchronously(MyBirthday.getPluginInstance());

    }

    public static synchronized void today() {

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    MyBirthday.getPluginInstance().todaybirthday.clear();
                    int dayn = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    int month = Calendar.getInstance().get(Calendar.MONTH);
                    String statement = "SELECT * FROM " + MyBirthday.getPluginInstance().database + ".b_data;";

                    final ResultSet r = MyBirthday.getPluginInstance().con.createStatement().executeQuery(statement);

                    // do database stuff here
                    if (r.first()) {
                        do {

                            UUID uuid = UUID.fromString(r.getString("uuid"));

                            if (r.getInt("day") == 0 && r.getInt("month") == 0 && r.getInt("year") == 1970) {

                            } else {

                                if (r.getInt("day") == dayn && r.getInt("month") == month && Bukkit.getOfflinePlayer(uuid).getLastPlayed() > (System.currentTimeMillis() - (15552000000.00))) {

                                    if (!MyBirthday.getPluginInstance().todaybirthday.contains(uuid)) {

                                        MyBirthday.getPluginInstance().todaybirthday.add(uuid);
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

    public static synchronized void updateData(final UUID uuid, final Calendar date, final Boolean bol, final Long cooldown) throws SQLException {

        try {

            String statement = "SELECT * FROM " + MyBirthday.getPluginInstance().database + ".b_data WHERE uuid = '" + uuid.toString() + "' ;";
            ResultSet r = MyBirthday.getPluginInstance().con.prepareStatement(statement).executeQuery(statement);

            if (r.first()) {

                String stat = "UPDATE " + MyBirthday.getPluginInstance().database + ".b_data SET day = '" + date.get(Calendar.DAY_OF_MONTH) + "', month = '" + date.get(Calendar.MONTH) + "', year = '" + date.get(Calendar.YEAR) + "', particles = " + String.valueOf(bol) + ", cooldown = '" + cooldown.toString() + "' WHERE uuid = '" + uuid.toString() + "' ;";
                MyBirthday.getPluginInstance().con.prepareStatement(stat).executeUpdate(stat);

            } else {

                String stat = "INSERT INTO " + MyBirthday.getPluginInstance().database + ".b_data (uuid, day, particles, cooldown, month, year) VALUES ('" + uuid.toString() + "' , '" + date.get(Calendar.DAY_OF_MONTH) + "' , true , '" + cooldown.toString() + "','" + date.get(Calendar.MONTH) + "','" + date.get(Calendar.YEAR) + "') ;";
                MyBirthday.getPluginInstance().con.prepareStatement(stat).executeUpdate(stat);

            }

        } catch (SQLException ex) {
            Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static synchronized void insertData(final UUID uuid, final Calendar date, final Boolean bol, final Long cooldown) throws SQLException {

        String stat = "INSERT INTO " + MyBirthday.getPluginInstance().database + ".b_data (uuid, day, particles, cooldown, month, year) VALUES ('" + uuid.toString() + "' , '" + date.get(Calendar.DAY_OF_MONTH) + "' , true , '" + cooldown.toString() + "','" + date.get(Calendar.MONTH) + "','" + date.get(Calendar.YEAR) + "') ;";

        try {
            MyBirthday.getPluginInstance().con.prepareStatement(stat).executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*
     public static synchronized void deleteData(final UUID uuid) throws SQLException {
     if (birthday.getPluginInstance().isConnect()) {

     String stat = "DELETE "+birthday.getPluginInstance().database+".b_data WHERE uuid = '" + uuid.toString() + "' ;";
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
                    String statement = "SELECT * FROM " + MyBirthday.getPluginInstance().database + ".b_data WHERE uuid = '" + uuid.toString() + "' ;";

                    final ResultSet r = MyBirthday.getPluginInstance().con.prepareStatement(statement).executeQuery();

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
        }.runTaskAsynchronously(MyBirthday.getPluginInstance());

    }

    public static synchronized void setSQL(final UUID uuid, final String[] date, final Player pl) throws SQLException {
        new BukkitRunnable() {
            @Override
            public void run() {

                try {
                    String statement = "SELECT * FROM " + MyBirthday.getPluginInstance().database + ".b_data WHERE uuid = '" + uuid.toString() + "' ;";

                    final ResultSet r = MyBirthday.getPluginInstance().con.prepareStatement(statement).executeQuery();

                    // do database stuff here
                    try {
                        if (r.first()) {
                            if (r.getLong("cooldown") > System.currentTimeMillis()) {

                                long timerem = r.getLong("cooldown") - System.currentTimeMillis();
                                int remaining = (int) (timerem / 1000);
                                pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Sorry you can't set your birthday. You must wait " + ChatColor.YELLOW + remaining + ChatColor.YELLOW + " seconds");
                            } else {
                                String dd = date[0];
                                String mm = date[1];
                                String yyyy = date[2];

                                int d = Integer.parseInt(dd);
                                int m = (Integer.parseInt(mm) - 1);
                                int y = Integer.parseInt(yyyy);
                                Calendar cal = Calendar.getInstance();
                                cal.set(y, m, d);

                                if (MyBirthday.getPluginInstance().isMactive() == true) {
                                    pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + MyBirthday.getPluginInstance().getMessage()));
                                }
                                pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Birthday set correctly! Now you must wait " + ChatColor.YELLOW + MyBirthday.getPluginInstance().getCoold() + ChatColor.YELLOW + " hours to update it"));
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
                            PluginData.insertData(uuid, cal, Boolean.TRUE, l);

                            if (MyBirthday.getPluginInstance().isMactive() == true) {
                                pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] : " + ChatColor.YELLOW + MyBirthday.getPluginInstance().getMessage()));
                            }
                            pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Birthday set correctly! Now you must wait " + ChatColor.YELLOW + MyBirthday.getPluginInstance().getCoold() + ChatColor.YELLOW + " hours to update it"));
                        }

                    } catch (SQLException e) {
                        Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, e);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }.runTaskAsynchronously(MyBirthday.getPluginInstance());

    }

    public static synchronized void setSQLStaff(final UUID uuid, final String[] date, final Player pl) throws SQLException {
        new BukkitRunnable() {
            @Override
            public void run() {

                try {
                    String statement = "SELECT * FROM " + MyBirthday.getPluginInstance().database + ".b_data WHERE uuid = '" + uuid.toString() + "' ;";

                    final ResultSet r = MyBirthday.getPluginInstance().con.prepareStatement(statement).executeQuery();

                    try {
                        if (r.first()) {

                            String dd = date[0];
                            String mm = date[1];
                            String yyyy = date[2];

                            int d = Integer.parseInt(dd);
                            int m = (Integer.parseInt(mm) - 1);
                            int y = Integer.parseInt(yyyy);
                            Calendar cal = Calendar.getInstance();
                            cal.set(y, m, d);

                            if (MyBirthday.getPluginInstance().isMactive() == true) {
                                pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + MyBirthday.getPluginInstance().getMessage()));
                            }
                            pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Birthday set correctly!"));
                            Long l = (System.currentTimeMillis() + (MyBirthday.getPluginInstance().getCooldown1() * 3600) * 1000);

                            PluginData.updateData(uuid, cal, r.getBoolean("particles"), l);

                        } else {

                            pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + "Hey, " + pl.getName() + " the player needs to set his birthday before"));

                        }

                    } catch (SQLException e) {
                        Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, e);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }.runTaskAsynchronously(MyBirthday.getPluginInstance());

    }

    public static void createMessageandSend() {
        if (MyBirthday.getPluginInstance().discordFound) {
            TextChannel channel = DiscordUtil.getTextChannelById(MyBirthday.getPluginInstance().getDiscordChannel());

            final Guild guild = DiscordSRV.getPlugin().getMainGuild();
            final StringBuilder builder = new StringBuilder();
            builder.append(":gift:" + " ***" + "Today is the birthday of : *** ");

//
            try {

                String statement = "SELECT * FROM " + MyBirthday.getPluginInstance().database + ".b_data ;";
                final ResultSet r = MyBirthday.getPluginInstance().con.createStatement().executeQuery(statement);
                if (r.first()) {
                    do {
                        UUID uuid = UUID.fromString(r.getString("uuid"));
                        OfflinePlayer pal = Bukkit.getOfflinePlayer(uuid);

                        if (MyBirthday.getPluginInstance().todaybirthday.contains(uuid)) {

                            if (MyBirthday.getPluginInstance().playeragebool == true) {

                                int now = Calendar.getInstance().get(Calendar.YEAR);
                                int your = r.getInt("year");
                                int year = now - your;
                                int index = MyBirthday.getPluginInstance().todaybirthday.size() - 1;
                                UUID val = MyBirthday.getPluginInstance().todaybirthday.get(index);

                                if (uuid.equals(val) == false) {
                                    builder.append("\n" + " ***" + pal.getName() + " " + year + " years,  " + ":tada: ***");
                                } else {
                                    builder.append("\n" + " ***" + pal.getName() + " " + year + " years!  " + ":tada: ***");
                                }

                            } else if (MyBirthday.getPluginInstance().playeragebool == false) {

                                int index = MyBirthday.getPluginInstance().todaybirthday.size() - 1;
                                UUID val = MyBirthday.getPluginInstance().todaybirthday.get(index);

                                if (uuid.equals(val) == false) {
                                    builder.append(" ***" + " " + pal.getName() + ", " + ":tada: ***");
                                } else {
                                    builder.append(" ***" + " " + pal.getName() + "!" + ":tada: ***");
                                }

                            }

                        }
                    } while (r.next());

                }

            } catch (SQLException ex) {
                Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
            }

            builder.append("\n" + "Happy birthday wishes from the whole MCME community!");
            String s = builder.toString();

            sendDiscord(s);
        }
    }

    public static String sendCheck(String[] date) {
        StringBuilder builder = new StringBuilder();
        builder.append("Do you want to set your birthday as \n");
        if (date[0].equalsIgnoreCase("1") || date[0].equalsIgnoreCase("21") || date[0].equalsIgnoreCase("31")) {
            builder.append(" " + date[0] + "st");
        } else if (date[0].equalsIgnoreCase("2") || date[0].equalsIgnoreCase("22")) {
            builder.append(" " + date[0] + "nd");
        } else if (date[0].equalsIgnoreCase("3") || date[0].equalsIgnoreCase("23")) {
            builder.append(" " + date[0] + "rd");
        } else {
            builder.append(" " + date[0] + "th");
        }

        if (date[1].equals("01")) {
            builder.append(" " + " January");
        } else if (date[1].equals("02")) {
            builder.append(" " + " February");
        } else if (date[1].equals("03")) {
            builder.append(" " + " March");
        } else if (date[1].equals("04")) {
            builder.append(" " + " April");
        } else if (date[1].equals("05")) {
            builder.append(" " + " May");
        } else if (date[1].equals("06")) {
            builder.append(" " + " June");
        } else if (date[1].equals("07")) {
            builder.append(" " + " July");
        } else if (date[1].equals("08")) {
            builder.append(" " + " August");
        } else if (date[1].equals("09")) {
            builder.append(" " + " September");
        } else if (date[1].equals("10")) {
            builder.append(" " + " October");
        } else if (date[1].equals("11")) {
            builder.append(" " + " November");
        } else if (date[1].equals("12")) {
            builder.append(" " + " December");
        }

        builder.append(" " + date[2]);
        builder.append("\n Retype the same command to confirm");
        return builder.toString();
    }

    private static void sendDiscord(String message) {
        if ((MyBirthday.getPluginInstance().getDiscordChannel() != null) && (!MyBirthday.getPluginInstance().getDiscordChannel().equals(""))) {

            Plugin discordPlugin = Bukkit.getServer().getPluginManager().getPlugin("DiscordSRV");
            if (discordPlugin != null) {
                DiscordSRV discordPlugins = DiscordSRV.getPlugin();
                TextChannel channel = discordPlugins.getDestinationTextChannelForGameChannelName(MyBirthday.getPluginInstance().getDiscordChannel());
                if (channel != null) {
                    DiscordUtil.sendMessage(channel, message, 0, false);
                }
            }
        }

    }

}
