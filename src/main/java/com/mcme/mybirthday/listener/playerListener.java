/*
 *Copyright (C) 2020 MCME (Fraspace5)
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
package com.mcme.mybirthday.listener;

import com.mcme.mybirthday.MyBirthday;
import com.mcme.mybirthday.data.PluginData;
import java.io.FileNotFoundException;
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
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Fraspace5
 */
public class playerListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) throws FileNotFoundException, SQLException {

        final Player pl = e.getPlayer();

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    UUID uuid = pl.getUniqueId();

                    MyBirthday.getSelect_bool().setString(1, uuid.toString());
                    final ResultSet r = MyBirthday.getSelect_bool().executeQuery();

                    if (r.first()) {
                        if (r.getBoolean("bool")) {
                            onJoinSQL(e, true);
                        } else {
                            onJoinSQL(e, false);
                        }
                    } else {
                        MyBirthday.getInsert_bool().setString(1, uuid.toString());
                        MyBirthday.getInsert_bool().setBoolean(2, false);
                        MyBirthday.getInsert_bool().executeUpdate();

                        onJoinSQL(e, false);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(playerListener.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }.runTaskAsynchronously(MyBirthday.getPluginInstance());

    }

    private static synchronized void onJoinSQL(final PlayerJoinEvent e, final Boolean bool) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    final Player pl = e.getPlayer();

                    final UUID uuid = pl.getUniqueId();
                    final String nameplayer = Bukkit.getOfflinePlayer(uuid).getName();
                    boolean listonjoin = MyBirthday.getPluginInstance().getConfig().getBoolean("listonjoin");
                    int now = Calendar.getInstance().get(Calendar.YEAR);

                    MyBirthday.getSelect_pdata().setString(1, e.getPlayer().getUniqueId().toString());
                    final ResultSet r = MyBirthday.getSelect_pdata().executeQuery();

                    if (!r.first()) {

                        if (!bool) {
                            pl.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "[MyBirthday] :" + " You haven't set your birthday yet! Use /birthday set");

                        }

                    } else {
                        if (MyBirthday.getPluginInstance().getTodaybirthday().contains(uuid)) {
                            int your = r.getInt("year");
                            final int year = now - your;
                            new BukkitRunnable() {

                                @Override
                                public void run() {

                                    pl.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "[MyBirthday] :" + ChatColor.YELLOW + " Happy Birthday " + nameplayer + " from all the Minecraft Middle Earth Community "
                                            + year + " years is a great achievement");
                                }

                            }.runTaskLaterAsynchronously(MyBirthday.getPluginInstance(), 25L);

                            if (MyBirthday.getPluginInstance().getTodaybirthday().size() > 1) {
                                PluginData.otherpeopleSQL(uuid, e);
                            }

                        } else if (listonjoin && !MyBirthday.getPluginInstance().getTodaybirthday().contains(uuid)) {
                            PluginData.showListSQL(pl, e);
                        }
                        if (MyBirthday.getPluginInstance().isParticles() && MyBirthday.getPluginInstance().getTodaybirthday().contains(uuid)) {

                            new BukkitRunnable() {
                                @Override
                                public void run() {

                                    try {
                                        MyBirthday.getSelect_pdata().setString(1, uuid.toString());
                                        final ResultSet s = MyBirthday.getSelect_pdata().executeQuery();

                                        if (s.first()) {

                                            if (s.getBoolean("particles") && s.getInt("year") != 1970) {
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
                                        Logger.getLogger(playerListener.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                }

                            }.runTaskTimer(MyBirthday.getPluginInstance(), 0, 15L);
                        }
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.runTaskAsynchronously(MyBirthday.getPluginInstance());

    }

}
