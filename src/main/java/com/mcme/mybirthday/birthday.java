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
 
public void OnEnable(){

this.getConfig().options().copyDefaults();
saveDefaultConfig();
System.out.println("Plugin MyBirthday Enabled");
getCommand("birthday").setExecutor(new BirthdayCommand());
SetListRunnable();
ShowListRunnable();
}


public void OnDisable(){

System.out.println("Plugin MyBirthday Disabled");


}
public void SetListRunnable(){

new BukkitRunnable() {

@Override 
public void run(){


SetTodayBirthdays();

}

}.runTaskTimer(this, 0L, 100L);



}

public void ShowListRunnable(){

    
new BukkitRunnable() {

@Override 
public void run(){
if (liston == true){
for (Player pl : Bukkit.getOnlinePlayers()){
ShowList(pl);
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
    
    if (cale.equals(call) == true){
      
        if(todaybirthday.contains(name) == true){
      
      }else{
       
            todaybirthday.add(name);}
        
    }
 }

}

public void ShowList(Player pl){
String si = todaybirthday.toString();
pl.sendMessage(ChatColor.YELLOW+"[MyBirthday] :"+"Today is the Birthday of "+ChatColor.YELLOW+si);

}

@EventHandler
public void onJoin(final PlayerJoinEvent e){
    final Player pl = e.getPlayer();
    final UUID uuid = pl.getUniqueId();
    String nameplayer = Bukkit.getOfflinePlayer(uuid).getName();
    boolean listonjoin = this.getConfig().getBoolean("listonjoin");
    int now = Calendar.getInstance().get(Calendar.YEAR);
    int your = this.date.get(uuid).get(Calendar.YEAR);
    int year = now - your;
    
    if (listonjoin == true){
    ShowList(pl);
    }
    if (!player.contains(uuid)){
    
   pl.sendMessage(ChatColor.YELLOW+"[MyBirthday] :"+"You haven't set your birthday yet! Use /birthday set");
    
    
    }
    if (todaybirthday.contains(nameplayer)){
    
     pl.sendMessage(ChatColor.YELLOW+"[MyBirthday] :"+ChatColor.YELLOW.BOLD+"Happy Birthday "+ChatColor.YELLOW.BOLD+ nameplayer + ChatColor.YELLOW+" from all the Minecraft Middle Earth Community"
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

    
    
}
