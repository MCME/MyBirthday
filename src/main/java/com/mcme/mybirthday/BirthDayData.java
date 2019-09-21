/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.mybirthday;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import lombok.Getter;

/**
 *
 * @author Fraspace5
 */
public class BirthDayData {
    
 HashMap<UUID, Calendar > date = new HashMap<>(); 
 
 List<UUID> player = new ArrayList<>();
 
 @Getter
 Map<UUID,BirthDayData> data = new HashMap<>();
 
 HashMap<UUID,Boolean> particlesbool = new HashMap<>();
    
 public void loadData(){
 try(Scanner scanner = new Scanner(new File("test"))) {
  while (scanner.hasNext()) {
    String[] data = scanner.nextLine().split(";");
    /*convert the data in the String array to construct your Birthday data*/ 
    }
}
 
 }
 
 public void storeData(){
 for ( UUID uuid : player){
 BirthDayData data = dataMap.get(uuid);
 Calendar cal = data.getCalendar();
 boolean bool = data.getShowParticle();
     
 try(PrintWriter out = new PrintWriter(new FileOutputStream(new File("test")))) {
    out.println(uuid+";"+cal+";"+bool+";");
}
 
 }
 
 
 }
    
    
    
    
}
