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

import com.mcme.mybirthday.listener.playerListener;
import com.mcme.mybirthday.runnables.runnable;
import org.bukkit.plugin.Plugin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Fraspace5
 */
public class MyBirthday extends JavaPlugin implements Listener {

    static final Logger Logger = Bukkit.getLogger();

    ConsoleCommandSender clogger = this.getServer().getConsoleSender();
    @Getter
    private Connection connection;
    @Getter
    private final String discordChannel = this.getConfig().getString("dchannel");
    @Setter
    @Getter
    private static boolean discordFound;
    @Getter
    private static MyBirthday pluginInstance;
    @Getter
    @Setter
    private static boolean connect;
    @Getter
    private final List<UUID> todaybirthday = new ArrayList<>();
    @Getter
    private final  HashMap<UUID, Long> coolsure = new HashMap<>();
    @Getter
    private final HashMap<UUID, String> coolsure2 = new HashMap<>();
    @Getter
    private final boolean liston = this.getConfig().getBoolean("broadcastlist");
    @Getter
    private final int broadcastlistevery = this.getConfig().getInt("broadcastlistevery");
    @Getter
    private final boolean particles = this.getConfig().getBoolean("particles");
    @Getter
    private final String message = this.getConfig().getString("privacymessage");
    @Getter
    private Boolean discorden = this.getConfig().getBoolean("dmessage");
    @Getter
    private final boolean mactive = this.getConfig().getBoolean("messageactive");
    @Getter
    private final String coold = this.getConfig().getString("wait-time");
    @Getter
    private final long cooldown1 = this.getConfig().getLong("wait-time");
    @Getter
    private final boolean playeragebool = this.getConfig().getBoolean("showplayerage");

    /**
     * Database information
     */
    private final String database = this.getConfig().getString("database");

    private final String host = this.getConfig().getString("host");

    private final String port = this.getConfig().getString("port");

    private final String username = this.getConfig().getString("username");

    private final String password = this.getConfig().getString("password");

    @Getter
    private static PreparedStatement select_bool;
    @Getter
    private static PreparedStatement insert_bool;
    @Getter
    private static PreparedStatement update_bool;
    @Getter
    private static PreparedStatement select_data;
    @Getter
    private static PreparedStatement select_pdata;
    @Getter
    private static PreparedStatement update_pdata;
    @Getter
    private static PreparedStatement insert_pdata;

    @Override
    public void onEnable() {
        pluginInstance = this;
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults();

        clogger.sendMessage(ChatColor.GREEN + "---------------------------------------");
        clogger.sendMessage(ChatColor.YELLOW + "MyBirthday Plugin v" + this.getDescription().getVersion() + " Enabled");
        clogger.sendMessage(ChatColor.YELLOW + "This plugin should be used in " + ChatColor.BLUE + this.getDescription().getAPIVersion());
        clogger.sendMessage(ChatColor.GREEN + "---------------------------------------");

        getCommand("birthday").setExecutor(new command());
        getCommand("birthday").setTabCompleter(new command());
        Bukkit.getPluginManager().registerEvents(new playerListener(), this);

        try {
            openConnection();
        } catch (SQLException ex) {
            clogger.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "MyBirthday" + ChatColor.DARK_GRAY + "] - " + ChatColor.RED + "Database error! (MyBirthday)");
            Logger.getLogger(MyBirthday.class.getName()).log(Level.SEVERE, null, ex);
            Bukkit.getPluginManager().disablePlugin(this);
        }

        if (this.isEnabled()) {
            runnable.connectionRunnable();
            runnable.setListRunnable();
            runnable.showListRunnable();
            CheckDiscord();
        }

    }

    @Override
    public void onDisable() {

        clogger.sendMessage(ChatColor.RED + "---------------------------------------");
        clogger.sendMessage(ChatColor.YELLOW + "MyBirthday Plugin v" + this.getDescription().getVersion() + " Disabled");
        clogger.sendMessage(ChatColor.RED + "---------------------------------------");
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(MyBirthday.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void openConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return;
        }
        if (MyBirthday.getPluginInstance().password.equalsIgnoreCase("default")) {
            clogger.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "MyBirthday" + ChatColor.DARK_GRAY + "] - " + ChatColor.YELLOW + "Plugin INITIALIZED, change database information!");
            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            new BukkitRunnable() {

                @Override
                public void run() {
                    try {
                        connection = DriverManager.getConnection("jdbc:mysql://" + MyBirthday.getPluginInstance().host + ":"
                                + MyBirthday.getPluginInstance().port + "/"
                                + MyBirthday.getPluginInstance().database + "?useSSL=false&allowPublicKeyRetrieval=true",
                                MyBirthday.getPluginInstance().username,
                                MyBirthday.getPluginInstance().password);
                        clogger.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "MyBirthday" + ChatColor.DARK_GRAY + "] - " + ChatColor.GREEN + "Database Found! ");

                        String statement = "CREATE TABLE IF NOT EXISTS b_data (uuid VARCHAR(50), particles BOOLEAN, cooldown LONG, year INT, month INT, day INT) ;";
                        String statement2 = "CREATE TABLE IF NOT EXISTS player_data (uuid VARCHAR(50), bool BOOLEAN) ;";

                        Statement stat1 = connection.prepareStatement(statement);
                        stat1.setQueryTimeout(10);
                        Statement stat2 = connection.prepareStatement(statement2);
                        stat2.setQueryTimeout(10);
                        stat1.execute(statement);
                        stat2.execute(statement2);

                        prepareStatements();

                    } catch (SQLException ex) {
                        Logger.getLogger(MyBirthday.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }.runTaskAsynchronously(MyBirthday.getPluginInstance());
        }

    }

    private void CheckDiscord() {

        Plugin discordPlugin = Bukkit.getServer().getPluginManager().getPlugin("DiscordSRV");
        if (discordPlugin == null) {
            setDiscordFound(Boolean.FALSE);
            clogger.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "MyBirthday" + ChatColor.DARK_GRAY + "] - " + ChatColor.RED + "DiscordSRV not found!");

        } else if (discorden) {
           setDiscordFound(Boolean.TRUE);
            runnable.discordRunnable();
            clogger.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "MyBirthday" + ChatColor.DARK_GRAY + "] - " + ChatColor.GREEN + "DiscordSRV found!");
        }

    }

    private void prepareStatements() {

        try {
            String stat_select_bool = "SELECT * FROM player_data WHERE uuid = ? ;";
            String stat_insert_bool = "INSERT INTO player_data (uuid, bool) VALUES (?, ? ); ";
            String stat_update_bool = "UPDATE player_data SET bool = ? WHERE uuid = ? ;";

            String stat_select_data = "SELECT * FROM b_data ;";

            String stat_select_pdata = "SELECT * FROM b_data WHERE uuid = ? ;";
            String stat_update_pdata = "UPDATE b_data SET day = ?, month = ?, year = ?, particles = ?, cooldown = ? WHERE uuid = ? ;";
            String stat_insert_pdata = "INSERT INTO b_data (uuid, day, particles, cooldown, month, year) VALUES (?,?, true ,?,?,?) ;";

            select_bool = connection.prepareStatement(stat_select_bool);
            insert_bool = connection.prepareStatement(stat_insert_bool);
            update_bool = connection.prepareStatement(stat_update_bool);
            select_data = connection.prepareStatement(stat_select_data);
            select_pdata = connection.prepareStatement(stat_select_pdata);
            update_pdata = connection.prepareStatement(stat_update_pdata);
            insert_pdata = connection.prepareStatement(stat_insert_pdata);

            select_bool.setQueryTimeout(10);
            insert_bool.setQueryTimeout(10);
            update_bool.setQueryTimeout(10);
            select_data.setQueryTimeout(10);
            select_pdata.setQueryTimeout(10);
            update_pdata.setQueryTimeout(10);
            insert_pdata.setQueryTimeout(10);

        } catch (SQLException ex) {
            Logger.getLogger(MyBirthday.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
