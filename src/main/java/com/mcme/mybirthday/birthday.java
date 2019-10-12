/*
 *Copyright (C) 2019 MCME (Fraspace5)
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import com.mcme.mybirthday.PluginData;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;

/**
 *
 * @author Fraspace5
 */
public class birthday extends JavaPlugin implements Listener {

    Logger Logger = Bukkit.getLogger();
    ConsoleCommandSender clogger = this.getServer().getConsoleSender();
    private File data;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults();
        clogger.sendMessage(ChatColor.GREEN + "---------------------------------------");
        clogger.sendMessage(ChatColor.YELLOW + "MyBirthday Plugin 2.0 Enabled");
        clogger.sendMessage(ChatColor.GREEN + "---------------------------------------");
        getCommand("birthday").setExecutor(new Commands());
        Bukkit.getPluginManager().registerEvents(this, this);
        try {
            InitiateFile();
        } catch (IOException ex) {
            Logger.getLogger(birthday.class.getName()).log(Level.SEVERE, null, ex);
        }
        SetListRunnable();
        ShowListRunnable();
        try {
            PluginData.loadData(data);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(birthday.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void InitiateFile() throws IOException {

        data = new File(Bukkit.getServer().getPluginManager().getPlugin("MyBirthday").getDataFolder(), "data.yml");

        if (!data.exists()) {

            data.createNewFile();
        }

    }

    @Override
    public void onDisable() {
        try {
            PluginData.saveData(data);
        } catch (FileNotFoundException ex) {

        }
        clogger.sendMessage(ChatColor.RED + "---------------------------------------");
        clogger.sendMessage(ChatColor.YELLOW + "Environment Plugin 2.0 Disabled");
        clogger.sendMessage(ChatColor.RED + "---------------------------------------");

    }

    List<String> todaybirthday = new ArrayList<>();

    HashMap<UUID, Long> coolsure = new HashMap<>();

    Calendar call = Calendar.getInstance();

    boolean liston = this.getConfig().getBoolean("broadcastlist");

    int broadcastlistevery = this.getConfig().getInt("broadcastlistevery");

    boolean particles = this.getConfig().getBoolean("particles");

    String message = this.getConfig().getString("privacymessage");

    boolean mactive = this.getConfig().getBoolean("messageactive");

    String coold = this.getConfig().getString("wait-time");

    long cooldown1 = this.getConfig().getLong("wait-time");

    boolean playeragebool = this.getConfig().getBoolean("showplayerage");

    public void SetListRunnable() {

        new BukkitRunnable() {

            @Override
            public void run() {

                SetTodayBirthdays();
            }

        }.runTaskTimer(this, 0L, 600L);

    }

    public String serialize(UUID uuid, Calendar calendar, Long cooldown) {
        return uuid + ";" + calendar.get(Calendar.YEAR) + ";" + calendar.get(Calendar.MONTH) + ";" + calendar.get(Calendar.DAY_OF_MONTH) + ";true;" + cooldown;
    }

    public void ShowListRunnable() {

        new BukkitRunnable() {

            @Override
            public void run() {
                long delay = broadcastlistevery * 60 * 1000;

                StringBuilder builder = new StringBuilder();
                for (String value : todaybirthday) {
                    if (playeragebool == true) {
                        OfflinePlayer pal = Bukkit.getOfflinePlayer(value);
                        UUID uuid = pal.getUniqueId();
                        int now = Calendar.getInstance().get(Calendar.YEAR);
                        int your = PluginData.getDataMap().get(uuid).calendar.get(Calendar.YEAR);
                        int year = now - your;
                        int index = todaybirthday.size() - 1;
                        String val = todaybirthday.get(index);

                        if (value.equalsIgnoreCase(val) == false) {
                            builder.append(value + " [" + year + "], ");
                        } else {
                            builder.append(value + " [" + year + "] !");
                        }

                    } else {

                        int index = todaybirthday.size() - 1;
                        String val = todaybirthday.get(index);

                        if (value.equalsIgnoreCase(val) == false) {
                            builder.append(value + " " + ", ");
                        } else {
                            builder.append(value + " " + "!");
                        }

                    }
                }
                String text = builder.toString();
                if (liston == true) {
                    for (Player pl : Bukkit.getOnlinePlayers()) {
                        String si = todaybirthday.toString();
                        if (!todaybirthday.isEmpty()) {
                            pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Today is the Birthday of " + ChatColor.YELLOW + text);
                        }

                    }
                }

            }

        }.runTaskTimer(this, broadcastlistevery * 60 * 20, broadcastlistevery * 60 * 20);

    }

    public void OtherPeopleBirthday(String nameplayer, PlayerJoinEvent e) {

        StringBuilder builder = new StringBuilder();

        for (String value : todaybirthday) {

            if (playeragebool == true && !value.equalsIgnoreCase(nameplayer)) {
                OfflinePlayer pal = Bukkit.getOfflinePlayer(value);
                UUID uuid = pal.getUniqueId();
                int now = Calendar.getInstance().get(Calendar.YEAR);
                int your = PluginData.getDataMap().get(uuid).calendar.get(Calendar.YEAR);
                int year = now - your;
                int index = todaybirthday.size() - 1;
                String val = todaybirthday.get(index);

                if (value.equalsIgnoreCase(val) == false) {
                    builder.append(value + " [" + year + "], ");
                } else {
                    builder.append(value + " [" + year + "] !");
                }

            } else if (playeragebool == false && !value.equalsIgnoreCase(nameplayer)) {

                int index = todaybirthday.size() - 1;
                String val = todaybirthday.get(index);

                if (value.equalsIgnoreCase(val) == false) {
                    builder.append(value + " " + ", ");
                } else {
                    builder.append(value + " " + "!");
                }

            } else {

            }
        }
        String text = builder.toString();

        e.setJoinMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Today is also the Birthday of " + ChatColor.YELLOW + text);

    }

    public void SetTodayBirthdays() {

        todaybirthday.clear();
        for (Map.Entry<UUID, BirthDayData> entry : PluginData.getDataMap().entrySet()) {
            UUID uuid = entry.getKey();
            Calendar cale = entry.getValue().calendar;
            String name = Bukkit.getOfflinePlayer(uuid).getName();
            int dc = cale.get(Calendar.DAY_OF_MONTH);
            int mc = cale.get(Calendar.MONTH);
            int dayn = call.get(Calendar.DAY_OF_MONTH);
            int monthn = call.get(Calendar.MONTH);
            int moonthn1 = monthn + 1;

            if (dc == dayn && mc == moonthn1) {

                if (todaybirthday.contains(name) == true) {

                } else {

                    todaybirthday.add(name);
                }

            }
        }

    }

    public void ShowList(Player pl, PlayerJoinEvent e) {

        StringBuilder builder = new StringBuilder();

        for (String value : todaybirthday) {
            if (playeragebool == true) {
                OfflinePlayer pal = Bukkit.getOfflinePlayer(value);
                UUID uuid = pal.getUniqueId();
                int now = Calendar.getInstance().get(Calendar.YEAR);
                int your = PluginData.getDataMap().get(uuid).calendar.get(Calendar.YEAR);
                int year = now - your;
                int index = todaybirthday.size() - 1;
                String val = todaybirthday.get(index);

                if (value.equalsIgnoreCase(val) == false) {
                    builder.append(value + " [" + year + "], ");
                } else {
                    builder.append(value + " [" + year + "] !");
                }

            } else {

                int index = todaybirthday.size() - 1;
                String val = todaybirthday.get(index);

                if (value.equalsIgnoreCase(val) == false) {
                    builder.append(value + " " + ", ");
                } else {
                    builder.append(value + " " + "!");
                }

            }
        }
        String text = builder.toString();
        if (todaybirthday.isEmpty() == false) {

            e.setJoinMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Today is the Birthday of " + ChatColor.YELLOW + text);

        }
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) throws FileNotFoundException {
        final Player pl = e.getPlayer();
        final UUID uuid = pl.getUniqueId();
        String nameplayer = Bukkit.getOfflinePlayer(uuid).getName();
        boolean listonjoin = getConfig().getBoolean("listonjoin");
        int now = Calendar.getInstance().get(Calendar.YEAR);

        if (!PluginData.getDataMap().containsKey(uuid)) {

            pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + " You haven't set your birthday yet! Use /birthday set");

        }
        if (todaybirthday.contains(nameplayer)) {
            int your = PluginData.getDataMap().get(uuid).calendar.get(Calendar.YEAR);
            int year = now - your;
            e.setJoinMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW.BOLD + " Happy Birthday " + ChatColor.YELLOW.BOLD + nameplayer + ChatColor.YELLOW + " from all the Minecraft Middle Earth Community "
                    + ChatColor.YELLOW + year + ChatColor.YELLOW + " years is a great achievement");
            if (todaybirthday.size() > 1) {
                OtherPeopleBirthday(nameplayer, e);

            }

        } else if (listonjoin == true && !todaybirthday.contains(nameplayer)) {
            ShowList(pl, e);
        }
        if (particles == true && todaybirthday.contains(nameplayer)) {

            new BukkitRunnable() {
                @Override
                public void run() {

                    if (PluginData.getDataMap().get(uuid).particles == true) {
                        Location location = pl.getLocation().add(0, 2, 0);
                        pl.getWorld().spawnParticle(Particle.REDSTONE, location, 10, 1.0, 1.0, 1.0, new Particle.DustOptions(Color.RED, 1));
                        pl.getWorld().spawnParticle(Particle.REDSTONE, location, 10, 1.0, 1.0, 1.0, new Particle.DustOptions(Color.YELLOW, 1));
                        pl.getWorld().spawnParticle(Particle.REDSTONE, location, 10, 1.0, 1.0, 1.0, new Particle.DustOptions(Color.GREEN, 1));
                        pl.getWorld().spawnParticle(Particle.REDSTONE, location, 10, 1.0, 1.0, 1.0, new Particle.DustOptions(Color.FUCHSIA, 1));
                    } else if (PluginData.getDataMap().get(uuid).particles == false) {

                    }

                }

            }.runTaskTimer(this, 0, 10L);
        }
    }

    class Commands implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

            if (sender instanceof Player) {

                Player pl = (Player) sender;
                UUID uuid = pl.getUniqueId();
                Calendar call = Calendar.getInstance();

                if (args.length < 1) {

                    pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Invalid Usage ! Type /birthday help for information");

                }

                if (args.length > 1) {
                    if (args[0].equalsIgnoreCase("set")) {

                        if (args.length == 4) {

                            if (PluginData.getDataMap().get(uuid).cooldown > System.currentTimeMillis()) {

                                long timerem = PluginData.getDataMap().get(uuid).cooldown - System.currentTimeMillis();
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
                                if (PluginData.getDataMap().containsKey(uuid)) {
                                    BirthDayData ob = PluginData.getDataMap().get(uuid);
                                    ob.calendar = cal;

                                    if (mactive == true) {
                                        pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + message));
                                    }
                                    pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Birthday set correctly! Now you must wait " + ChatColor.YELLOW + coold + ChatColor.YELLOW + " hours to update it"));
                                    ob.cooldown = (System.currentTimeMillis() + (cooldown1 * 3600) * 1000);

                                } else {
                                    BirthDayData datas = new BirthDayData(serialize(uuid, cal, System.currentTimeMillis() + (cooldown1 * 3600) * 1000));
                                    PluginData.getDataMap().put(datas.getUuid(), datas);

                                    if (mactive == true) {
                                        pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] : " + ChatColor.YELLOW + message));
                                    }
                                    pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Birthday set correctly! Now you must wait " + ChatColor.YELLOW + coold + ChatColor.YELLOW + " hours to update it"));

                                }

                            }

                        } else {

                            pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Not enough argouments! Type /birthday help");
                        }

                    }
                } else {

                }
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("help")) {

                        pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " To set your birthday write /birthday set dd mm yyyy");

                    }
                }

                if (args.length > 1) {
                    if (args[0].equalsIgnoreCase("particles") == true) {
                        if (args[1].equalsIgnoreCase("on")) {
                            pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Particles on");
                            BirthDayData dss = PluginData.getDataMap().get(uuid);
                            dss.particles = true;

                        } else if (args[1].equalsIgnoreCase("off") == true) {
                            pl.sendMessage(ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Particles off");
                            BirthDayData dss = PluginData.getDataMap().get(uuid);
                            dss.particles = false;

                        }
                    }

                }

                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("removedatab") && args.length > 0) {

                        if (coolsure.containsKey(uuid) && coolsure.get(uuid) > System.currentTimeMillis()) {
                            pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " All your data has been removed from our databases"));

                            PluginData.getDataMap().remove(uuid);

                            return false;

                        }

                        long time = System.currentTimeMillis() + (30 * 1000);
                        coolsure.put(uuid, time);
                        pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Are you sure? Retype /birthday removedatab to confirm"));

                    }
                }
            } else {

                System.out.println("You can't use this command in console");

            }

            return false;
        }
    }

}
