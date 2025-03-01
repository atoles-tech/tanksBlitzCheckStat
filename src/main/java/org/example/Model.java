package org.example;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {
    private PlayerStats stats;
    private PlayerStats lastStats;
    private Integer id_player;
    private String nickname_player;
    private Map<String, BufferedImage> nations = new HashMap<>();
    private List<Tank> tanks = new ArrayList<>();

    public Model(){
        File data = new File("data");
        if(!data.exists()){
            data.mkdir();
        }
        File save = new File("save");
        if(!save.exists()){
            save.mkdir();
        }
        Thread temp = new Thread(){
            @Override
            public void run() {
                try {
                    nations.put("china", Inspector.getImage("https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Flag_of_the_People%27s_Republic_of_China.svg/1200px-Flag_of_the_People%27s_Republic_of_China.svg.png"));
                    nations.put("germany",Inspector.getImage("https://www.blitztankstats.com/assets/icons/germany_r_color.png"));
                    nations.put("ussr",Inspector.getImage("https://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/Flag_of_the_Soviet_Union.svg/1200px-Flag_of_the_Soviet_Union.svg.png"));
                    nations.put("uk",Inspector.getImage("https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Flag_of_the_United_Kingdom_%283-5%29.svg/640px-Flag_of_the_United_Kingdom_%283-5%29.svg.png"));
                    nations.put("usa",Inspector.getImage("https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Flag_of_the_United_States_%2851_stars%29.svg/300px-Flag_of_the_United_States_%2851_stars%29.svg.png"));
                    nations.put("france",Inspector.getImage("https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Ensign_of_France.svg/200px-Ensign_of_France.svg.png"));
                    nations.put("other",Inspector.getImage("https://www.blitztankstats.com/assets/icons/other_r_color.png"));
                    nations.put("japan",Inspector.getImage("https://img.freepik.com/premium-photo/japanese-flag-japan-texturized-background_469558-39668.jpg"));
                    nations.put("european",Inspector.getImage("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b7/Flag_of_Europe.svg/1200px-Flag_of_Europe.svg.png"));
                }catch (Exception ignored){}
            }
        };
       temp.start();
    }

    public void start(){
        try {
            this.stats = Inspector.getInfo(Inspector.getJSON(id_player));
            this.lastStats = (PlayerStats) stats.clone();
        }catch (Exception ignored){}

    }

    public void setStats(PlayerStats stats) {
        this.stats = stats;
        getTanksForStats();
    }

    public PlayerStats getCurrentStats(){
        return Inspector.getInfo(this.nickname_player,this.id_player);
    }

    public void setNameAndId(String str){
        String[] strings = str.split("/");
        this.nickname_player = strings[0];
        this.id_player = Integer.parseInt(strings[1]);
    }

    public Integer getId_player() {
        return id_player;
    }

    public String getNickname_player() {
        return nickname_player;
    }

    public PlayerStats getStats() {
        return stats;
    }

    public PlayerStats getLastStats() {
        return lastStats;
    }

    public void getTanksForStats(){
        List<Tank> tanks = new ArrayList<>();

        for(Integer tank_id: lastStats.getTanks().keySet()){
            if(!stats.getTanks().containsKey(tank_id)){
                tanks.add(lastStats.getTanks().get(tank_id));
                if(!(new File("data/" + lastStats.getTanks().get(tank_id).getName() + ".png").exists())){
                    try {
                        Inspector.saveInFile(lastStats.getTanks().get(tank_id).getName(), Inspector.getImage(lastStats.getTanks().get(tank_id).getUrl()));
                    }catch (Exception ignored){}
                }
            }
            else if(stats.getTanks().get(tank_id).getTotalBattles() != lastStats.getTanks().get(tank_id).getTotalBattles()){
                Tank s1 = stats.getTanks().get(tank_id);
                Tank s2 = lastStats.getTanks().get(tank_id);
                Tank tank = new Tank(s1.getName(),s1.getTier(), s2.getTotalBattles()- s1.getTotalBattles(),s2.getTotalDamage()-s1.getTotalDamage(),s2.getTotalWins()-s1.getTotalWins(),s2.getUrl(),s1.getNation(),s2.getWn8());
                tanks.add(tank);
                if(!(new File("data/" + lastStats.getTanks().get(tank_id).getName() + ".png").exists())){
                    try {
                        Inspector.saveInFile(lastStats.getTanks().get(tank_id).getName(), Inspector.getImage(lastStats.getTanks().get(tank_id).getUrl()));
                    }catch (Exception ignored){}
                }
            }
        }
        this.tanks = tanks;
    }

    public void updateLastStats(){
        PlayerStats s = getCurrentStats();
        if(s.getNickname() == null){return;}
        this.lastStats = s;
        getTanksForStats();
    }

    public void updateLastStats(String json){
        this.lastStats = Inspector.getInfo(json);
        getTanksForStats();
    }


    public Map<String, BufferedImage> getNations() {
        return nations;
    }

    public List<Tank> getTanks() {
        return tanks;
    }
}
