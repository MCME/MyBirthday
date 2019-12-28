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

import org.bukkit.plugin.Plugin;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.TimeZone;
import java.util.logging.Level;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Fraspace5
 */
public class MyBirthday extends JavaPlugin implements Listener {

    Logger Logger = Bukkit.getLogger();
    ConsoleCommandSender clogger = this.getServer().getConsoleSender();

    @Getter
    public Connection con;

    @Getter
    String discordChannel = this.getConfig().getString("dchannel");
    @Setter
    @Getter
    boolean discordFound;
    @Getter
    private static MyBirthday pluginInstance;

    private void checkUpdate() {
        final UpdaterCheck updater = new UpdaterCheck(this);
    }

    @Override
    public void onEnable() {
        pluginInstance = this;
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults();
        clogger.sendMessage(ChatColor.GREEN + "---------------------------------------");
        clogger.sendMessage(ChatColor.YELLOW + "MyBirthday Plugin v" + this.getDescription().getVersion() + " Enabled");
        clogger.sendMessage(ChatColor.GREEN + "---------------------------------------");

        getCommand("birthday").setExecutor(new command());
        getCommand("birthday").setTabCompleter(new command());
        Bukkit.getPluginManager().registerEvents(this, this);

        SetListRunnable();
        ShowListRunnable();
        if (this.getConfig().getBoolean("findupdates")) {

            checkUpdate();
        }

        try {
            openConnection();

        } catch (SQLException ex) {
            ex.printStackTrace();
            clogger.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "MyBirthday" + ChatColor.DARK_GRAY + "] - " + ChatColor.RED + "Database error! (MyBirthday)");
            Logger.getLogger(MyBirthday.class.getName()).log(Level.SEVERE, null, ex);
            Bukkit.getPluginManager().disablePlugin(this);
        }
        ConnectionRunnable();
        SetListRunnable();
        ShowListRunnable();
        CheckDiscord();

    }

    public void openConnection() throws SQLException {
        if (con != null && !con.isClosed()) {
            return;
        }
        if (MyBirthday.getPluginInstance().password.equalsIgnoreCase("default")) {
            clogger.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "MyBirthday" + ChatColor.DARK_GRAY + "] - " + ChatColor.YELLOW + "Plugin INITIALIZED, change database information!");
            Bukkit.getPluginManager().disablePlugin(this);
        } else {

            con = DriverManager.getConnection("jdbc:mysql://" + MyBirthday.getPluginInstance().host + ":"
                    + MyBirthday.getPluginInstance().port + "/"
                    + MyBirthday.getPluginInstance().database + "?useSSL=false&allowPublicKeyRetrieval=true",
                    MyBirthday.getPluginInstance().username,
                    MyBirthday.getPluginInstance().password);
            clogger.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "MyBirthday" + ChatColor.DARK_GRAY + "] - " + ChatColor.GREEN + "Database Found! ");

            new BukkitRunnable() {

                @Override
                public void run() {
                    try {
                        String statement = "CREATE TABLE IF NOT EXISTS b_data (uuid VARCHAR(50), particles BOOLEAN, cooldown LONG, year INT, month INT, day INT";
                        con.createStatement().execute(statement);
                    } catch (SQLException ex) {
                        Logger.getLogger(MyBirthday.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }.runTaskAsynchronously(MyBirthday.getPluginInstance());
        }

    }

    public void CheckDiscord() {

        Plugin discordPlugin = Bukkit.getServer().getPluginManager().getPlugin("DiscordSRV");
        if (discordPlugin == null) {
            this.setDiscordFound(Boolean.FALSE);
            clogger.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "MyBirthday" + ChatColor.DARK_GRAY + "] - " + ChatColor.RED + "DiscordSRV not found!");

        } else {
            this.setDiscordFound(Boolean.TRUE);
            DiscordRunnable();
            clogger.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "MyBirthday" + ChatColor.DARK_GRAY + "] - " + ChatColor.GREEN + "DiscordSRV found!");
        }

    }

    public void ConnectionRunnable() {

        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    if (!con.isValid(2)) {

                        openConnection();

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(MyBirthday.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }.runTaskTimer(MyBirthday.getPluginInstance(), 60L, 100L);

    }

    public void DiscordRunnable() {

        new BukkitRunnable() {

            @Override
            public void run() {

                TimeZone tim = TimeZone.getTimeZone("Europe/London");
                Calendar cal = Calendar.getInstance(tim);

                if (cal.get(Calendar.HOUR_OF_DAY) == MyBirthday.getPluginInstance().getConfig().getInt("time.hours")
                        && cal.get(Calendar.MINUTE) == MyBirthday.getPluginInstance().getConfig().getInt("time.minutes")) {

                    PluginData.createMessageandSend();

                }

            }

        }.runTaskTimer(MyBirthday.getPluginInstance(), 0L, 1200L);

    }

    @Override
    public void onDisable() {

        clogger.sendMessage(ChatColor.RED + "---------------------------------------");
        clogger.sendMessage(ChatColor.YELLOW + "MyBirthday Plugin v" + this.getDescription().getVersion() + " Disabled");
        clogger.sendMessage(ChatColor.RED + "---------------------------------------");

    }
    @Getter
    @Setter
    public boolean connect;
    @Getter
    List<UUID> todaybirthday = new ArrayList<>();
    @Getter
    HashMap<UUID, Long> coolsure = new HashMap<>();
    @Getter
    Calendar call = Calendar.getInstance();
    @Getter
    boolean liston = this.getConfig().getBoolean("broadcastlist");
    @Getter
    int broadcastlistevery = this.getConfig().getInt("broadcastlistevery");
    @Getter
    boolean particles = this.getConfig().getBoolean("particles");
    @Getter
    String message = this.getConfig().getString("privacymessage");
    @Getter
    String host = this.getConfig().getString("host");
    @Getter
    String port = this.getConfig().getString("port");
    @Getter
    String database = this.getConfig().getString("database");
    @Getter
    String username = this.getConfig().getString("username");
    @Getter
    String password = this.getConfig().getString("password");
    @Getter
    boolean mactive = this.getConfig().getBoolean("messageactive");
    @Getter
    String coold = this.getConfig().getString("wait-time");
    @Getter
    long cooldown1 = this.getConfig().getLong("wait-time");
    @Getter
    boolean playeragebool = this.getConfig().getBoolean("showplayerage");

    public void SetListRunnable() {

        new BukkitRunnable() {

            @Override
            public void run() {

                try {
                    SetTodayBirthdays();
                } catch (SQLException ex) {
                    Logger.getLogger(MyBirthday.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }.runTaskTimer(this, 0L, 600L);

    }

    public void ShowListRunnable() {

        new BukkitRunnable() {

            @Override
            public void run() {
                PluginData.showlist();

            }

        }.runTaskTimer(this, broadcastlistevery * 60 * 20 * 1000000, broadcastlistevery * 60 * 20);

    }

    public void OtherPeopleBirthday(UUID nameplayer, PlayerJoinEvent e) throws SQLException {

        PluginData.otherpeopleSQL(nameplayer, e);

    }

    public void SetTodayBirthdays() throws SQLException {

        todaybirthday.clear();
        PluginData.today();

    }

    public void ShowList(Player pl, PlayerJoinEvent e) throws SQLException {

        PluginData.showListSQL(pl, e);

    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) throws FileNotFoundException, SQLException {
        PluginData.onJoinSQL(e);
        Player pl = e.getPlayer();

    }
}
