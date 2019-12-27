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
package com.mcme.mybirthday;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.command.TabExecutor;

/**
 *
 * @author fraspace5
 */
public class command implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            final Player pl = (Player) sender;
            final UUID uuid = pl.getUniqueId();
            Calendar call = Calendar.getInstance();

            if (args.length < 1) {

                pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Invalid Usage ! Type /birthday help for information");

            }

            if (args.length > 1) {
                if (args[0].equalsIgnoreCase("set")) {

                    if (args.length == 4) {

                        try {
                            PluginData.setSQL(uuid, args, pl);
                        } catch (SQLException ex) {
                            Logger.getLogger(command.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } else {

                        pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Not enough argouments! Type /birthday help");
                    }

                }
            } 
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("help")) {

                    pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " To set your birthday write /birthday set dd mm yyyy");

                }
            }

            if (args.length > 1) {
                if (args[0].equalsIgnoreCase("particles") == true) {
                    PluginData.particlesSQL(uuid, args, pl);
                }

            }

            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("removedatab") && args.length > 0) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            try {
                                String statement = "SELECT * FROM " + MyBirthday.getPluginInstance().database + ".b_data WHERE uuid = '" + uuid.toString() + "' ;";

                                final ResultSet r = MyBirthday.getPluginInstance().con.createStatement().executeQuery(statement);
                                if (r.first() && r.getInt("year") != 1970) {
                                    if (MyBirthday.getPluginInstance().getCoolsure().containsKey(uuid) && MyBirthday.getPluginInstance().getCoolsure().get(uuid) > System.currentTimeMillis()) {
                                        pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " All your data has been removed from our databases"));
                                        Calendar cal = Calendar.getInstance();
                                        cal.set(1970, 1, 1);
                                        try {
                                            Long l = (System.currentTimeMillis() + (MyBirthday.getPluginInstance().getCooldown1() * 3600) * 1000);
                                            PluginData.updateData(uuid, cal, Boolean.TRUE, l);
                                            MyBirthday.getPluginInstance().getCoolsure().remove(uuid);
                                        } catch (SQLException ex) {
                                            Logger.getLogger(command.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                    } else {
                                        long time = System.currentTimeMillis() + (30 * 1000);
                                        MyBirthday.getPluginInstance().getCoolsure().put(uuid, time);
                                        pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Are you sure? Retype /birthday removedatab to confirm"));
                                    }

                                } else {
                                    pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Nothing to remove"));
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(command.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    }.runTaskAsynchronously(MyBirthday.getPluginInstance());
                }
            }
        } else {
            System.out.println("You can't use this command in console");

        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Player pl = (Player) sender;
        List<String> arguments = new ArrayList<>();
        arguments.add("set");
        arguments.add("help");
        arguments.add("particles");
        arguments.add("removedatab");

        List<String> Flist = new ArrayList<>();
        List<String> particles = new ArrayList<>();
        List<String> day = new ArrayList<>();
        List<String> month = new ArrayList<>();
        List<String> year = new ArrayList<>();
        if (args.length == 1) {
            for (String s : arguments) {
                if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                    Flist.add(s);
                }
            }
            return Flist;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("particles")) {

                List<String> a = Arrays.asList("on", "off");
                for (String s : a) {
                    if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                        particles.add(s);
                    }
                }
                return particles;

            } else if (args[0].equalsIgnoreCase("set")) {

                List<String> a = Arrays.asList("day");
                for (String s : a) {
                    if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                        day.add(s);
                    }
                }
                return day;

            } else {
                return null;
            }

        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("set")) {
                List<String> a = Arrays.asList("month");
                for (String s : a) {
                    if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                        month.add(s);
                    }
                }
                return month;
            } else {
                return null;
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("set")) {
                List<String> a = Arrays.asList("year");
                for (String s : a) {
                    if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                        year.add(s);
                    }
                }
                return year;
            } else {
                return null;
            }
        } else {

            return null;
        }
    }

}
