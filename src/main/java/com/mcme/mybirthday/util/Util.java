/*
 * Copyright (C) 2021 MCME (Fraspace5)
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
package com.mcme.mybirthday.util;

/**
 *
 * @author Fraspace5
 */
public class Util {

    public static String sendCheck(String[] date) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n Do you want to set your birthday as");
        if (date[0].equalsIgnoreCase("1") || date[0].equalsIgnoreCase("21") || date[0].equalsIgnoreCase("31")) {
            builder.append(" ").append(date[0]).append("st");
        } else if (date[0].equalsIgnoreCase("2") || date[0].equalsIgnoreCase("22")) {
            builder.append(" ").append(date[0]).append("nd");
        } else if (date[0].equalsIgnoreCase("3") || date[0].equalsIgnoreCase("23")) {
            builder.append(" ").append(date[0]).append("rd");
        } else {
            builder.append(" ").append(date[0]).append("th");
        }

        switch (date[1]) {
            case "01":
                builder.append(" January");
                break;
            case "02":
                builder.append(" February");
                break;
            case "03":
                builder.append(" March");
                break;
            case "04":
                builder.append(" April");
                break;
            case "05":
                builder.append(" May");
                break;
            case "06":
                builder.append(" June");
                break;
            case "07":
                builder.append(" July");
                break;
            case "08":
                builder.append(" August");
                break;
            case "09":
                builder.append(" September");
                break;
            case "10":
                builder.append(" October");
                break;
            case "11":
                builder.append(" November");
                break;
            case "12":
                builder.append(" December");
                break;
            default:
                break;
        }

        builder.append(" ").append(date[2]).append(" ?");
        builder.append("\n Retype the same command to confirm");
        return builder.toString();
    }
}
