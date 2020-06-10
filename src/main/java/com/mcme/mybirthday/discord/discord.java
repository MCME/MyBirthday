/*
 * Copyright (C) 2020 MCME (Fraspace5)
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
package com.mcme.mybirthday.discord;

import com.mcme.mybirthday.MyBirthday;
import com.mcme.mybirthday.data.PluginData;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.util.DiscordUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Fraspace5
 */
public class discord {

    private static void sendDiscord(String message) {
        if ((MyBirthday.getPluginInstance().getDiscordChannel() != null) && (!MyBirthday.getPluginInstance().getDiscordChannel().equals(""))) {

            Plugin discordPlugin = Bukkit.getServer().getPluginManager().getPlugin("DiscordSRV");
            if (discordPlugin != null) {
                DiscordSRV discordPlugins = DiscordSRV.getPlugin();
                TextChannel channel = discordPlugins.getDestinationTextChannelForGameChannelName(MyBirthday.getPluginInstance().getDiscordChannel());
                if (channel != null) {
                    DiscordUtil.sendMessage(channel, message, 0, false);
                }
            }
        }

    }

    public static void createMessageandSend() {
        if (MyBirthday.isDiscordFound()) {

            final StringBuilder builder = new StringBuilder();
            builder.append(":gift:" + " ***" + "Today is the birthday of : *** ");
            try {

                final ResultSet r = MyBirthday.getSelect_data().executeQuery();

                if (r.first()) {
                    do {
                        UUID uuid = UUID.fromString(r.getString("uuid"));
                        OfflinePlayer pal = Bukkit.getOfflinePlayer(uuid);

                        if (MyBirthday.getPluginInstance().getTodaybirthday().contains(uuid)) {

                            if (MyBirthday.getPluginInstance().isPlayeragebool()) {

                                int now = Calendar.getInstance().get(Calendar.YEAR);
                                int your = r.getInt("year");
                                int year = now - your;
                                int index = MyBirthday.getPluginInstance().getTodaybirthday().size() - 1;
                                UUID val = MyBirthday.getPluginInstance().getTodaybirthday().get(index);

                                if (!uuid.equals(val)) {
                                    builder.append("\n ***").append(pal.getName()).append(" ").append(year).append(" years,  :tada: ***");
                                } else {
                                    builder.append("\n ***").append(pal.getName()).append(" ").append(year).append(" years!  :tada: ***");
                                }

                            } else if (!MyBirthday.getPluginInstance().isPlayeragebool()) {

                                int index = MyBirthday.getPluginInstance().getTodaybirthday().size() - 1;
                                UUID val = MyBirthday.getPluginInstance().getTodaybirthday().get(index);

                                if (!uuid.equals(val)) {
                                    builder.append(" *** ").append(pal.getName()).append(", :tada: ***");
                                } else {
                                    builder.append(" *** ").append(pal.getName()).append("! :tada: ***");
                                }

                            }

                        }
                    } while (r.next());

                }

            } catch (SQLException ex) {
                Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
            }

            builder.append("\n" + "Happy birthday wishes from the whole MCME community!");

            sendDiscord(builder.toString());
        }
    }

}
