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

import static java.lang.Integer.parseInt;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.command.TabExecutor;

/**
 *
 * @author Fraspace5
 */
public class command implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            final Player pl = (Player) sender;
            final UUID uuid = pl.getUniqueId();
            Calendar call = Calendar.getInstance();

            if (args.length < 1) {

                pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.RED + " Invalid Usage ! Type /birthday help for information");

            } else if (args[0].equalsIgnoreCase("set")) {
                if (pl.hasPermission("mybirthday.*") || pl.hasPermission("mybirthday.set")) {
                    if (args.length == 2) {
                        if (validateDate(args[1])) {
                            String[] fir = unserialize(args[1]);

                            Calendar cal = Calendar.getInstance();
                            int year = cal.get(Calendar.YEAR) - 99;
                            int yourY = parseInt(fir[2]);
                            if (yourY > year) {
                                try {
                                    if (MyBirthday.getPluginInstance().coolsure2.containsKey(pl.getUniqueId())) {
                                        if (MyBirthday.getPluginInstance().coolsure2.get(pl.getUniqueId()).equals(args[1])) {
                                            PluginData.setSQL(uuid, fir, pl);
                                            MyBirthday.getPluginInstance().coolsure2.remove(pl.getUniqueId());
                                        } else {
                                            String s = PluginData.sendCheck(fir);
                                            pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.DARK_GREEN + s);
                                            MyBirthday.getPluginInstance().coolsure2.remove(pl.getUniqueId());
                                            MyBirthday.getPluginInstance().coolsure2.put(pl.getUniqueId(), args[1]);
                                        }
                                    } else {
                                        String s = PluginData.sendCheck(fir);
                                        pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.DARK_GREEN + s);
                                        MyBirthday.getPluginInstance().coolsure2.put(pl.getUniqueId(), args[1]);
                                    }

                                } catch (SQLException ex) {
                                    Logger.getLogger(command.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.RED + " Year should be major of " + year);
                            }

                        } else {
                            pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.RED + "Wrong date format... It should be dd/mm/yyyy");
                        }

                    } else {

                        pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.RED + " Not enough argouments! Type /birthday help");
                    }

                } else {
                    pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.RED + " You don't have enough permissions to use this command");

                }

            } else if (args[0].equalsIgnoreCase("help")) {

                pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " To set your birthday write /birthday set dd/mm/yyyy");

            } else if (args[0].equalsIgnoreCase("change")) {
                if (pl.hasPermission("mybirthday.change")) {
                    if (args.length > 2) {
                        if (validateDate(args[2])) {
                            String[] fir = unserialize(args[2]);
                            OfflinePlayer p = Bukkit.getOfflinePlayer(args[1]);
                            Calendar cal = Calendar.getInstance();
                            int year = cal.get(Calendar.YEAR) - 99;
                            int yourY = parseInt(fir[2]);
                            if (yourY > year) {
                                try {
                                    if (MyBirthday.getPluginInstance().coolsure2.containsKey(pl.getUniqueId())) {
                                        if (MyBirthday.getPluginInstance().coolsure2.get(pl.getUniqueId()).equals(args[2])) {
                                            PluginData.setSQLStaff(p.getUniqueId(), fir, pl);
                                            MyBirthday.getPluginInstance().coolsure2.remove(pl.getUniqueId());
                                        } else {
                                            String s = PluginData.sendCheck(fir);
                                            pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.DARK_GREEN + s);
                                            MyBirthday.getPluginInstance().coolsure2.remove(pl.getUniqueId());
                                            MyBirthday.getPluginInstance().coolsure2.put(pl.getUniqueId(), args[2]);
                                        }
                                    } else {
                                        String s = PluginData.sendCheck(fir);
                                        pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.DARK_GREEN + s);
                                        MyBirthday.getPluginInstance().coolsure2.put(pl.getUniqueId(), args[2]);
                                    }

                                } catch (SQLException ex) {
                                    Logger.getLogger(command.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.RED + " Year should be major of " + year);
                            }
                        } else {
                            pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.RED + "Wrong date format... It should be dd/mm/yyyy");
                        }

                    }
                } else {
                    pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.RED + " You don't have enough permissions to use this command");
                }

            } else if (args[0].equalsIgnoreCase("particles")) {
                if (pl.hasPermission("mybirthday.*") || pl.hasPermission("mybirthday.particles")) {
                    if (args.length == 2) {
                        PluginData.particlesSQL(uuid, args, pl);
                    } else {
                        pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.RED + " Not enough argouments! Type /birthday help");
                    }
                } else {
                    pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.RED + " You don't have enough permissions to use this command");
                }

            } else if (args[0].equalsIgnoreCase("removedatab")) {
                if (pl.hasPermission("mybirthday.*") || pl.hasPermission("mybirthday.delete")) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            try {
                                String statement = "SELECT * FROM " + MyBirthday.getPluginInstance().database + ".b_data WHERE uuid = '" + uuid.toString() + "' ;";

                                Statement statm = MyBirthday.getPluginInstance().con.prepareStatement(statement);
                                statm.setQueryTimeout(10);
                                final ResultSet r = statm.executeQuery(statement);

                                if (r.first() && r.getInt("year") != 1970) {
                                    if (MyBirthday.getPluginInstance().getCoolsure().containsKey(uuid) && MyBirthday.getPluginInstance().getCoolsure().get(uuid) > System.currentTimeMillis()) {
                                        pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " All your data has been removed from our databases"));
                                        Calendar cal = Calendar.getInstance();
                                        cal.set(1970, 1, 1);
                                        try {
                                            Long l = (System.currentTimeMillis() + (MyBirthday.getPluginInstance().getCooldown1() * 3600) * 1000);
                                            PluginData.updateData(uuid, cal, Boolean.FALSE, l);
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

                } else {
                    pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.RED + " You don't have enough permissions to use this command");
                }
            } else if (args[0].equalsIgnoreCase("ignore")) {

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            String statement = "SELECT * FROM " + MyBirthday.getPluginInstance().database + ".player_data WHERE uuid = '" + uuid.toString() + "' ;";

                            Statement statm = MyBirthday.getPluginInstance().con.prepareStatement(statement);
                            statm.setQueryTimeout(10);
                            final ResultSet r = statm.executeQuery(statement);
                            
                            if (r.first()) {
                                if (r.getBoolean("bool")) {
                                    String stat = "UPDATE " + MyBirthday.getPluginInstance().database + ".player_data SET bool = 0 ;";
                                    pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.GREEN + "Now you aren't ignoring my plugin, thanks");
                                    MyBirthday.getPluginInstance().con.createStatement().executeUpdate(stat);
                                } else {
                                    String stat = "UPDATE " + MyBirthday.getPluginInstance().database + ".player_data SET bool = 1 ;";
                                    pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.GREEN + "You are ignoring MyBirthday plugin");
                                    MyBirthday.getPluginInstance().con.createStatement().executeUpdate(stat);
                                }
                            } else {
                                String s = "INSERT INTO " + MyBirthday.getPluginInstance().database + ".player_data (uuid, bool) VALUES ('" + uuid.toString() + "', 1); ";
                                pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.GREEN + "You are ignoring MyBirthday plugin");
                                MyBirthday.getPluginInstance().con.createStatement().executeUpdate(s);
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(command.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }.runTaskAsynchronously(MyBirthday.getPluginInstance());

            } else {

                pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.RED + " Version " + MyBirthday.getPluginInstance().getDescription().getVersion() + " for " + MyBirthday.getPluginInstance().getDescription().getAPIVersion());

            }

        } else {
            System.out.println("You can't use this command in console");

        }

        return false;
    }

    public boolean validateDate(String date) {
        String regex = "^([0-2][0-9]||3[0-1])/(0[0-9]||1[0-2])/([0-9][0-9])?[0-9][0-9]$";

        if (date.matches(regex)) {

            return true;

        } else {
            return false;
        }
    }

    public static String[] unserialize(String line) {
        String[] dataArray = line.split("/");

        return dataArray;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Player pl = (Player) sender;
        List<String> arguments = new ArrayList<>();
        arguments.add("set");
        arguments.add("help");
        arguments.add("particles");
        arguments.add("ignore");
        arguments.add("removedatab");
        if (pl.hasPermission("mybirthday.*") || pl.hasPermission("mybirthday.staff")) {
            arguments.add("change");
        }
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

                List<String> a = Arrays.asList("dd/mm/yyyy");
                for (String s : a) {
                    if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                        day.add(s);
                    }
                }
                return day;

            } else {
                return null;
            }

        } else {

            return null;
        }
    }

}
