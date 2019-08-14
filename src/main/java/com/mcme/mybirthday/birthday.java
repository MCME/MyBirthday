/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.mybirthday;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


/**
 *
 * @author Fraspace5
 */
public class birthday extends JavaPlugin implements Listener {
 
@Override
public void onEnable(){
this.saveDefaultConfig();
this.getConfig().options().copyDefaults();
System.out.println("Plugin MyBirthday Enabled");
getCommand("birthday").setExecutor(new Commands());
Bukkit.getPluginManager().registerEvents(this, this);
SetListRunnable();
ShowListRunnable();
}

@Override
public void onDisable(){

System.out.println("Plugin MyBirthday Disabled");


}
 HashMap<UUID, Calendar > date = new HashMap<>(); 
 
 List<UUID> player = new ArrayList<>();
 
 HashMap<UUID,Boolean> particlesbool = new HashMap<>();
 
 HashMap<UUID,Double> playerage = new HashMap<>();
 
 HashMap<UUID,Long> cooldown = new HashMap<>();  

 List<String> todaybirthday = new ArrayList<>();
 
 HashMap<UUID, Long> coolsure = new HashMap<>();
 
 Calendar call = Calendar.getInstance();
 
 boolean liston = this.getConfig().getBoolean("broadcastlist");
 
 int broadcastlistevery = this.getConfig().getInt("broadcastlistevery");
 
 int hour = broadcastlistevery * 72000;
 
 boolean particles = this.getConfig().getBoolean("particles");
 
 String message = this.getConfig().getString("privacymessage");

boolean mactive = this.getConfig().getBoolean("messageactive");

String coold = this.getConfig().getString("wait-time");

long cooldown1 = this.getConfig().getLong("wait-time");
 
public void SetListRunnable(){

new BukkitRunnable() {

@Override 
public void run(){


SetTodayBirthdays();
System.out.println("setto");
}

}.runTaskTimer(this, 0L, 600L);



}

public void ShowListRunnable(){

    
new BukkitRunnable() {

@Override 
public void run(){
if (liston == true){
for (Player pl : Bukkit.getOnlinePlayers()){
    String si = todaybirthday.toString();
    if (!todaybirthday.isEmpty()){
pl.sendMessage(ChatColor.YELLOW+"[MyBirthday] :"+" Today is the Birthday of "+ChatColor.YELLOW+si);
}
  


}
}


}

}.runTaskTimer(this, 0L, hour);




}


public void SetTodayBirthdays(){

todaybirthday.clear();
for (Map.Entry<UUID, Calendar> entry : date.entrySet())
{
    UUID uuid = entry.getKey();
    Calendar cale = entry.getValue();
    String name = Bukkit.getOfflinePlayer(uuid).getName();
    int dc = cale.get(Calendar.DAY_OF_MONTH);
    int mc = cale.get(Calendar.MONTH);
    int dayn = call.get(Calendar.DAY_OF_MONTH);
    int monthn = call.get(Calendar.MONTH);
    int moonthn1 = monthn+1;
    System.out.println(dc+""+mc+""+dayn+""+monthn);
    if (dc == dayn && mc == moonthn1){
      
        if(todaybirthday.contains(name) == true){
      
      }else{
       
            todaybirthday.add(name);}
        
    }
 }

}

public void ShowList(Player pl, PlayerJoinEvent e){
String si = todaybirthday.toString();

if (todaybirthday.isEmpty()== false){
e.setJoinMessage(ChatColor.GOLD.BOLD +"[MyBirthday] :"+" Today is the Birthday of "+ChatColor.YELLOW+si);
}


}

@EventHandler
public void onJoin(final PlayerJoinEvent e){
    final Player pl = e.getPlayer();
    final UUID uuid = pl.getUniqueId();
    String nameplayer = Bukkit.getOfflinePlayer(uuid).getName();
    boolean listonjoin = getConfig().getBoolean("listonjoin");
    int now = Calendar.getInstance().get(Calendar.YEAR);
    
    
    if (listonjoin == true){
    
        ShowList(pl,e);
        System.out.println("Dovrei mostrare");
    }
    if (!player.contains(uuid)){
    
   pl.sendMessage(ChatColor.GOLD.BOLD +"[MyBirthday] :"+" You haven't set your birthday yet! Use /birthday set");
    
    
    }
    if (todaybirthday.contains(nameplayer)){
    int your = date.get(uuid).get(Calendar.YEAR);
    int year = now - your; 
     e.setJoinMessage(ChatColor.GOLD.BOLD +"[MyBirthday] :"+ChatColor.YELLOW.BOLD+" Happy Birthday "+ChatColor.YELLOW.BOLD+ nameplayer + ChatColor.YELLOW+" from all the Minecraft Middle Earth Community"
     +ChatColor.YELLOW+year+ChatColor.YELLOW+" years is a great achievement");
    }
    if (particles == true  && todaybirthday.contains(nameplayer)){
    
 new BukkitRunnable(){ 
@Override 
public void run(){

if (particlesbool.get(uuid).equals(true)){
Location location = pl.getLocation().add(0, 2, 0);

pl.getWorld().spawnParticle(Particle.REDSTONE,location,10,1.0,1.0,0.0,null);

}
    
}

}.runTaskTimer(this, 0, 10L);


    
    
    
    }





}
 class Commands implements CommandExecutor {
 
 @Override
 public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
   
   
   
   if (sender instanceof Player){
       
   Player pl = (Player) sender;
   UUID uuid = pl.getUniqueId();
   Calendar call = Calendar.getInstance();
   
   if (args.length < 1){
   
       pl.sendMessage(ChatColor.GOLD.BOLD +"[MyBirthday] :"+ChatColor.YELLOW+" Invalid Usage ! Type /birthday help for information");
   
   }
   
   
   
   
   if (args.length > 1){
   if (args[0].equalsIgnoreCase("set") ){
   
       if (args.length == 4){
       
       if (cooldown.containsKey(uuid) && cooldown.get(uuid)> System.currentTimeMillis()){
       
       long timerem = cooldown.get(uuid) - System.currentTimeMillis();
       int remaining = (int) (timerem/1000);
       pl.sendMessage(ChatColor.GOLD.BOLD +"[MyBirthday] :"+ChatColor.YELLOW+" Sorry you can't set your birthday. You must wait "+ChatColor.YELLOW+remaining+ChatColor.YELLOW+" seconds");
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
       if (date.containsKey(uuid) && player.contains(uuid) && playerage.containsKey(uuid)){
       date.replace(uuid, cal);
       if (mactive == true){
       pl.sendMessage((ChatColor.GOLD.BOLD+"[MyBirthday] :"+ChatColor.YELLOW+ message));
       }
       pl.sendMessage((ChatColor.GOLD.BOLD+"[MyBirthday] :"+ChatColor.YELLOW+" Birthday set correctly! Now you must wait "+ChatColor.YELLOW+coold+ChatColor.YELLOW+" hours to update it"));
       cooldown.put(uuid,System.currentTimeMillis()+ (cooldown1*3600)*1000);
       particlesbool.put(uuid, true);
       }else {
       date.put(uuid, cal);
       player.add(uuid);
        if (mactive == true){
       pl.sendMessage((ChatColor.GOLD.BOLD+"[MyBirthday] : "+ChatColor.YELLOW+ message));
       }
       pl.sendMessage((ChatColor.GOLD.BOLD+"[MyBirthday] :"+ChatColor.YELLOW+" Birthday set correctly! Now you must wait "+ChatColor.YELLOW+coold+ChatColor.YELLOW+" hours to update it"));
       cooldown.put(uuid,System.currentTimeMillis()+ (cooldown1*3600)*1000);
       
       }
       
       }
   
       }else {
       
       pl.sendMessage(ChatColor.GOLD.BOLD+"[MyBirthday] :"+ChatColor.YELLOW+" Not enough argouments! Type /birthday help");}
   
   
   
   
   
   
   }
   }
   else {
     
   }
    if (args[0].equalsIgnoreCase("help") && args.length > 0){
   
   pl.sendMessage(ChatColor.GOLD.BOLD+"[MyBirthday] :"+ChatColor.YELLOW+" To set your birthday write /birthday set dd mm yyyy yourage");
   
   
   
   }
    
     if (args.length>1){
     if (args[0].equalsIgnoreCase("particles") == true){
     if (args[1].equalsIgnoreCase("on")){
     pl.sendMessage(ChatColor.GOLD.BOLD+"[MyBirthday] :"+ChatColor.YELLOW+" Particles on");
     particlesbool.remove(uuid);
     particlesbool.put(uuid, true);
     
     }
     else if (args[1].equalsIgnoreCase("off")== true){
     pl.sendMessage(ChatColor.GOLD.BOLD+"[MyBirthday] :"+ChatColor.YELLOW+" Particles off");
     particlesbool.remove(uuid);
     particlesbool.put(uuid, false);
     
     
     }}
   
   
   }
     
     
     if (args[0].equalsIgnoreCase("removedatab") && args.length > 0){
   
   if (coolsure.containsKey(uuid) && coolsure.get(uuid)> System.currentTimeMillis()){
   pl.sendMessage((ChatColor.GOLD.BOLD+"[MyBirthday] :"+ChatColor.YELLOW+" All your data has been removed from our databases"));
   date.remove(uuid);
   particlesbool.remove(uuid);
   player.remove(uuid);
   
   return false;
   
   }
   
   long time = System.currentTimeMillis()+ (30*1000) ;
   coolsure.put(uuid, time);
   pl.sendMessage((ChatColor.GOLD.BOLD+"[MyBirthday] :"+ChatColor.YELLOW+" Are you sure? Retype /birthday removedatab to confirm"));
   
   }
   
   
   
 
   }else {
   
   System.out.println("You can't use this command in console");}

   return false;
   }
   
   
}
 
}
