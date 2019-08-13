/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.mybirthday;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Fraspace5
 */
public class BirthdayCommand implements CommandExecutor {

    private birthday main;
    
    public BirthdayCommand(){
    this.main = main;
    
    }
   
   @Override
   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
   
   
   
   if (sender instanceof Player){
       
   Player player = (Player) sender;
   UUID uuid = player.getUniqueId();
   
   
   
   Calendar call = Calendar.getInstance();
   
   if (args[0].equalsIgnoreCase("set") == true){
   
       if (args.length == 3){
       
       if (main.cooldown.containsKey(uuid) && main.cooldown.get(uuid)> System.currentTimeMillis()){
       
       long timerem = main.cooldown.get(uuid) - System.currentTimeMillis();
       int remaining = (int) (timerem/1000);
       player.sendMessage(ChatColor.YELLOW+"[MyBirthday] :"+ChatColor.YELLOW+" Sorry you can't set your birthday. You must wait "+ChatColor.YELLOW+remaining+ChatColor.YELLOW+" seconds");
       }
       else {
       String dd = args[1];
       String mm = args[2];
       String yyyy = args[3];
       
       int d = Integer.parseInt(dd);
       int m = Integer.parseInt(mm);
       int y = Integer.parseInt(yyyy);
       Calendar cal = Calendar.getInstance();
       cal.set(y, m, d);
       if (main.date.containsKey(uuid) && main.player.contains(uuid) && main.playerage.containsKey(uuid)){
       main.date.replace(uuid, cal);
       if (main.mactive == true){
       player.sendMessage((ChatColor.YELLOW+"[MyBirthday] :"+ChatColor.YELLOW+ main.message));
       }
       player.sendMessage((ChatColor.YELLOW+"[MyBirthday] :"+ChatColor.YELLOW+" Birthday set correctly, now you must  wait "+ChatColor.YELLOW+main.coold+ChatColor.YELLOW+" hours to update it"));
       main.cooldown.put(uuid,System.currentTimeMillis()+ (main.cooldown1*3600)*1000);
       main.particlesbool.put(uuid, true);
       }else {
       main.date.put(uuid, cal);
       main.player.add(uuid);
        if (main.mactive == true){
       player.sendMessage((ChatColor.YELLOW+"[MyBirthday] :"+ChatColor.YELLOW+ main.message));
       }
       player.sendMessage((ChatColor.YELLOW+"[MyBirthday] :"+ChatColor.YELLOW+" Birthday set correctly, now you must  wait "+ChatColor.YELLOW+main.coold+ChatColor.YELLOW+" hours to update it"));
       main.cooldown.put(uuid,System.currentTimeMillis()+ (main.cooldown1*3600)*1000);
       
       }
           
           
          
       
       
       
       
       }
           
           
           
       }else {
       
       player.sendMessage(ChatColor.YELLOW+"[MyBirthday] :"+ChatColor.YELLOW+" Not enough argouments! Type /birthday help");}
   
   
   
   
   
   
   }
   else if (args[0].equalsIgnoreCase("help")== true){
   
   player.sendMessage(ChatColor.YELLOW+"[MyBirthday] :"+ChatColor.YELLOW+" To set your birthday write /birthday set dd mm yyyy yourage");
   
   
   
   }else if (args[0].equalsIgnoreCase("particles") == true){
     if (args[1].equalsIgnoreCase("on")){
     
     main.particlesbool.remove(uuid);
     main.particlesbool.put(uuid, true);
     
     }
     else if (args[1].equalsIgnoreCase("off")== true){
     
     main.particlesbool.remove(uuid);
     main.particlesbool.put(uuid, false);
     
     
     }
   
   
   }else if (args[0].equalsIgnoreCase("removedatab")== true){
   
   if (main.coolsure.containsKey(uuid) && main.coolsure.get(uuid)> System.currentTimeMillis()){
   player.sendMessage((ChatColor.YELLOW+"[MyBirthday] :"+ChatColor.YELLOW+"All your data has been removed from our databases"));
   main.date.remove(uuid);
   main.particlesbool.remove(uuid);
   main.player.remove(uuid);
   
   
   }
   
   long time = System.currentTimeMillis()+ (30*1000) ;
   main.coolsure.put(uuid, time);
   player.sendMessage((ChatColor.YELLOW+"[MyBirthday] :"+ChatColor.YELLOW+"Are you sure? Retype /birthday removedatab to confirm"));
   
   }
   
   
   else {
   
   player.sendMessage(ChatColor.YELLOW+"[MyBirthday] :"+ChatColor.YELLOW+" Invalid usage! Type /birthday help");
   
   }
   
   
   
   
   
   
   }else {
   
   System.out.println("You can't use this command in console");}
   
       
       
       
       
       
   
   return false;
   }
   
}
