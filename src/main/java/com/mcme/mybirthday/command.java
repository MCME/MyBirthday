/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.mybirthday;

import java.util.Calendar;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fraspace5
 */
public class command implements CommandExecutor {

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

                        try {
                            PluginData.setSQL(uuid, args, pl);
                        } catch (SQLException ex) {
                            Logger.getLogger(command.class.getName()).log(Level.SEVERE, null, ex);
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
                    PluginData.particlesSQL(uuid, args, pl);
                }

            }

            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("removedatab") && args.length > 0) {

                    if (birthday.getPluginInstance().getCoolsure().containsKey(uuid) && birthday.getPluginInstance().getCoolsure().get(uuid) > System.currentTimeMillis()) {
                        pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " All your data has been removed from our databases"));

                        try {
                            PluginData.deleteData(uuid);
                        } catch (SQLException ex) {
                            Logger.getLogger(command.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        return false;

                    }

                    long time = System.currentTimeMillis() + (30 * 1000);
                    birthday.getPluginInstance().getCoolsure().put(uuid, time);
                    pl.sendMessage((ChatColor.GOLD.BOLD + "[MyBirthday] :" + ChatColor.YELLOW + " Are you sure? Retype /birthday removedatab to confirm"));

                }
            }
        } else {

            System.out.println("You can't use this command in console");

        }

        return false;
    }

    

}
