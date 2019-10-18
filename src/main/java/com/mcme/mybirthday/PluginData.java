/*
 * Copyright (C) 2019 MCME (Fraspace5)
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Fraspace5
 */
public class PluginData {

    @Getter
    @Setter
    private static Map<UUID, BirthDayData> dataMap = new HashMap<>();

    public static void saveData(File file) throws FileNotFoundException {
        for (BirthDayData data : dataMap.values()) {

            String storageData = data.serialize();
            try (PrintWriter out = new PrintWriter(new FileOutputStream(file))) {
                out.println(storageData);
            }
        }
    }

    public static void loadData(File file) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                BirthDayData data = new BirthDayData(line);
                dataMap.put(data.getUuid(), data);
            }
        }
    }

}
