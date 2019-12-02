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

import java.sql.Date;
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
                    String statement = "SELECT * FROM BDATA ";

                    final ResultSet r = birthday.getPluginInstance().con.createStatement().executeQuery(statement);

                    // do database stuff here
                    new BukkitRunnable() {
                        @Override
                        public void run() {
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

                                                    int your = PluginData.convert(r.getDate("date")).get(Calendar.YEAR);
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
                        }

                    }.runTaskAsynchronously(birthday.getPluginInstance());
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
                    String statement = "SELECT * FROM BDATA WHERE = " + e.getPlayer().getName();

                    final ResultSet r = birthday.getPluginInstance().con.createStatement().executeQuery(statement);

                    // do database stuff here
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            try {
                                final Player pl = e.getPlayer();
                                final UUID uuid = pl.getUniqueId();
                                String nameplayer = Bukkit.getOfflinePlayer(uuid).getName();
                                boolean listonjoin = birthday.getPluginInstance().getConfig().getBoolean("listonjoin");
                                int now = Calendar.getInstance().get(Calendar.YEAR);

                                if (!r.first()) {

                                    pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + " You haven't set your birthday yet! Use /birthday set");

                                }
                                if (birthday.getPluginInstance().todaybirthday.contains(nameplayer)) {
                                    int your = PluginData.convert(r.getDate("date")).get(Calendar.YEAR);
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

                            } catch (SQLException e) {
                                Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, e);
                            }
                        }

                    }.runTaskAsynchronously(birthday.getPluginInstance());
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
                    String statement = "SELECT * FROM BDATA ";

                    final ResultSet r = birthday.getPluginInstance().con.createStatement().executeQuery(statement);

                    // do database stuff here
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            try {
                                if (r.first()) {
                                    StringBuilder builder = new StringBuilder();
                                    do {
                                        OfflinePlayer pal = Bukkit.getOfflinePlayer(r.getString("uuid"));
                                        if (birthday.getPluginInstance().todaybirthday.contains(pal.getName())) {
                                            if (birthday.getPluginInstance().playeragebool == true && !pal.getName().equalsIgnoreCase(nameplayer)) {

                                                UUID uuid = pal.getUniqueId();
                                                int now = Calendar.getInstance().get(Calendar.YEAR);
                                                int your = PluginData.convert(r.getDate("date")).get(Calendar.YEAR);
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
                        }

                    }.runTaskAsynchronously(birthday.getPluginInstance());
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
                    String statement = "SELECT * FROM BDATA ";

                    final ResultSet r = birthday.getPluginInstance().con.createStatement().executeQuery(statement);

                    // do database stuff here
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            try {
                                if (r.first()) {
                                    StringBuilder builder = new StringBuilder();
                                    OfflinePlayer pal = Bukkit.getOfflinePlayer(r.getString("uuid"));
                                    do {
                                        if (birthday.getPluginInstance().todaybirthday.contains(pal.getName())) {
                                            if (birthday.getPluginInstance().playeragebool == true) {

                                                UUID uuid = pal.getUniqueId();
                                                int now = Calendar.getInstance().get(Calendar.YEAR);
                                                int your = PluginData.convert(r.getDate("date")).get(Calendar.YEAR);
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
                        }

                    }.runTaskAsynchronously(birthday.getPluginInstance());
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
                    String statement = "SELECT * FROM BDATA";

                    final ResultSet r = birthday.getPluginInstance().con.createStatement().executeQuery(statement);

                    // do database stuff here
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            try {

                                if (r.first()) {
                                    do {
                                        UUID uuid = UUID.fromString(r.getString("uuid"));
                                        Calendar cale = PluginData.convert(r.getDate("date"));
                                        String name = Bukkit.getOfflinePlayer(uuid).getName();
                                        int dc = cale.get(Calendar.DAY_OF_MONTH);
                                        int mc = cale.get(Calendar.MONTH);
                                        int dayn = birthday.getPluginInstance().call.get(Calendar.DAY_OF_MONTH);
                                        int monthn = birthday.getPluginInstance().call.get(Calendar.MONTH);
                                        int moonthn1 = monthn + 1;

                                        if (dc == dayn && mc == moonthn1) {

                                            if (birthday.getPluginInstance().todaybirthday.contains(name) == true) {

                                            } else {

                                                birthday.getPluginInstance().todaybirthday.add(name);
                                            }

                                        }

                                    } while (r.next());
                                }

                            } catch (SQLException e) {
                                Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, e);
                            }
                        }

                    }.runTaskAsynchronously(birthday.getPluginInstance());
                } catch (SQLException ex) {
                    Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.runTaskAsynchronously(birthday.getPluginInstance());

    }

    public static synchronized void updateData(final UUID uuid, final Calendar date, final Boolean bol, final Long cooldown) throws SQLException {

        if (birthday.getPluginInstance().isConnect()) {

            new BukkitRunnable() {

                @Override
                public void run() {

                    String statement = "SELECT * FROM BDATA WHERE uuid = " + uuid.toString();
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

    public static synchronized boolean exist(UUID uuid) throws SQLException {
        if (birthday.getPluginInstance().isConnect()) {
            String statement = "SELECT * FROM BDATA WHERE uuid = " + uuid.toString();
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

    public static synchronized void particlesSQL(final UUID uuid, final String[] args, final Player pl) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String statement = "SELECT * FROM BDATA WHERE = " + uuid;

                    final ResultSet r = birthday.getPluginInstance().con.createStatement().executeQuery(statement);

                    // do database stuff here
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            try {
                                if (r.first()) {
                                    if (args[1].equalsIgnoreCase("on")) {
                                        try {
                                            pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Particles on");

                                            PluginData.insertData(uuid, PluginData.convert(r.getDate("date")), Boolean.TRUE, r.getLong("cooldown"));
                                        } catch (SQLException ex) {
                                            Logger.getLogger(command.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                    } else if (args[1].equalsIgnoreCase("off") == true) {
                                        try {
                                            pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Particles off");

                                            PluginData.insertData(uuid, PluginData.convert(r.getDate("date")), Boolean.FALSE, r.getLong("cooldown"));
                                        } catch (SQLException ex) {
                                            Logger.getLogger(command.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                    }
                                }

                            } catch (SQLException e) {
                                Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, e);
                            }
                        }

                    }.runTaskAsynchronously(birthday.getPluginInstance());
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
                    String statement = "SELECT * FROM BDATA WHERE = " + uuid;

                    final ResultSet r = birthday.getPluginInstance().con.createStatement().executeQuery(statement);

                    // do database stuff here
                    new BukkitRunnable() {
                        @Override
                        public void run() {
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

                                        ResultSet ob = r;
                                        PluginData.updateData(uuid, cal, ob.getBoolean("particles"), ob.getLong("cooldown"));

                                        if (birthday.getPluginInstance().isMactive() == true) {
                                            pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + birthday.getPluginInstance().getMessage()));
                                        }
                                        pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Birthday set correctly! Now you must wait " + ChatColor.YELLOW + birthday.getPluginInstance().getCoold() + ChatColor.YELLOW + " hours to update it"));
                                        PluginData.updateData(uuid, cal, ob.getBoolean("particles"), (System.currentTimeMillis() + (birthday.getPluginInstance().getCooldown1() * 3600) * 1000));

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

                                    PluginData.insertData(uuid, cal, Boolean.TRUE, System.currentTimeMillis() + (birthday.getPluginInstance().getCooldown1() * 3600) * 1000);

                                    if (birthday.getPluginInstance().isMactive() == true) {
                                        pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] : " + ChatColor.YELLOW + birthday.getPluginInstance().getMessage()));
                                    }
                                    pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Birthday set correctly! Now you must wait " + ChatColor.YELLOW + birthday.getPluginInstance().getCoold() + ChatColor.YELLOW + " hours to update it"));
                                }

                            } catch (SQLException e) {
                                Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, e);
                            }
                        }

                    }.runTaskAsynchronously(birthday.getPluginInstance());
                } catch (SQLException ex) {
                    Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }.runTaskAsynchronously(birthday.getPluginInstance());

    }

}
