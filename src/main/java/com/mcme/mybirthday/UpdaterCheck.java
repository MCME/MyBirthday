/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.mybirthday;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Stream;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author fraspace5
 */
public class UpdaterCheck {

    private HttpURLConnection connection;
    private String WRITE_STRING;

    private String oldVersion = "0.0";
    private String newVersion = "0.0";

    public UpdaterCheck(JavaPlugin plugin) {

        oldVersion = plugin.getDescription().getVersion();

        try {
            connection = (HttpURLConnection) new URL("https://raw.githubusercontent.com/fraspace5/MyBirthday/master/src/main/resources/plugin.yml").openConnection();
            connection.connect();
            newVersion = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine().substring(9);

            if (newVersion.equalsIgnoreCase("oldVersion")) {

                birthday.getPluginInstance().clogger.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "MyBirthday" + ChatColor.DARK_GRAY + "] - " + "New version " + newVersion + " available for this Plugin");

            }

        } catch (IOException e) {
            return;
        }

    }
}
