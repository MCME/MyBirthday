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
import java.util.logging.Level;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Fraspace5
 */
public class birthday extends JavaPlugin implements Listener {

    Logger Logger = Bukkit.getLogger();
    ConsoleCommandSender clogger = this.getServer().getConsoleSender();
    private File data;
    @Getter
    public Connection con;

    @Getter
    private static birthday pluginInstance;

    private void checkUpdate() {
        final UpdaterCheck updater = new UpdaterCheck(this);
    }

    @Override
    public void onEnable() {
        pluginInstance = this;
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults();
        clogger.sendMessage(ChatColor.GREEN + "---------------------------------------");
        clogger.sendMessage(ChatColor.YELLOW + "MyBirthday Plugin 2.25 Enabled");
        clogger.sendMessage(ChatColor.GREEN + "---------------------------------------");

        getCommand("birthday").setExecutor(new command());
        Bukkit.getPluginManager().registerEvents(this, this);
        try {
            InitiateFile();
        } catch (IOException ex) {
            Logger.getLogger(birthday.class.getName()).log(Level.SEVERE, null, ex);
        }
        SetListRunnable();
        ShowListRunnable();
        if (this.getConfig().getBoolean("findupdates")) {

            checkUpdate();
        }

        try {
            openConnection();
            clogger.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "MyBirthday" + ChatColor.DARK_GRAY + "] - " + ChatColor.GREEN + "Database Found! ");
        } catch (SQLException ex) {
            ex.printStackTrace();
            clogger.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "MyBirthday" + ChatColor.DARK_GRAY + "] - " + ChatColor.RED + "Database error! (MyBirthday)");
            Logger.getLogger(birthday.class.getName()).log(Level.SEVERE, null, ex);
            Bukkit.getPluginManager().disablePlugin(this);
        }
        ConnectionRunnable();
    }

    public void openConnection() throws SQLException {
        if (con != null && !con.isClosed()) {
            return;
        }
        if (birthday.getPluginInstance().password.equalsIgnoreCase("default")) {
            clogger.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "MyBirthday" + ChatColor.DARK_GRAY + "] - " + ChatColor.YELLOW + "Plugin INITIALIZED, change database information!");
            Bukkit.getPluginManager().disablePlugin(this);
        } else {

            con = DriverManager.getConnection("jdbc:mysql://" + birthday.getPluginInstance().host + ":"
                    + birthday.getPluginInstance().port + "/"
                    + birthday.getPluginInstance().database,
                    birthday.getPluginInstance().username,
                    birthday.getPluginInstance().password);

        }

    }

    public void ConnectionRunnable() {

        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    if (!con.isValid(5)) {

                        openConnection();

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(birthday.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }.runTaskTimer(birthday.getPluginInstance(), 60L, 150L);

    }

    public void InitiateFile() throws IOException {

        data = new File(Bukkit.getServer().getPluginManager().getPlugin("MyBirthday").getDataFolder(), "data.yml");

        if (!data.exists()) {

            data.createNewFile();
        }

    }

    @Override
    public void onDisable() {

        clogger.sendMessage(ChatColor.RED + "---------------------------------------");
        clogger.sendMessage(ChatColor.YELLOW + "MyBirthday Plugin 2.25 Disabled");
        clogger.sendMessage(ChatColor.RED + "---------------------------------------");

    }
    @Getter
    @Setter
    public boolean connect;
    @Getter
    List<String> todaybirthday = new ArrayList<>();
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
                    Logger.getLogger(birthday.class.getName()).log(Level.SEVERE, null, ex);
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

        }.runTaskTimer(this, broadcastlistevery * 60 * 20, broadcastlistevery * 60 * 20);

    }

    public void OtherPeopleBirthday(String nameplayer, PlayerJoinEvent e) throws SQLException {

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
    }
}
