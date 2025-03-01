package org.example;

import java.util.Map;

public class PlayerStats implements Cloneable{
    private String nickname;
    private Integer id;
    private Map<Integer,Tank> tanks;
    private int totalBattles;
    private int totalWins;
    private int totalDamage;
    private int wn8;

    public PlayerStats(int totalWins, int totalDamage, int totalBattles, String nickname, Integer id,int wn8) {
        this.totalWins = totalWins;
        this.totalDamage = totalDamage;
        this.totalBattles = totalBattles;
        this.nickname = nickname;
        this.id = id;
        this.wn8 = wn8;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public PlayerStats(Integer id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public void addDamage(int damage){
        this.totalDamage+=damage;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setTotalBattles(int totalBattles) {
        this.totalBattles = totalBattles;
    }

    public void setTotalDamage(int totalDamage) {
        this.totalDamage = totalDamage;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public void setTanks(Map<Integer, Tank> tanks) {
        this.tanks = tanks;
    }

    public Integer getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public Map<Integer, Tank> getTanks() {
        return tanks;
    }

    public Integer getTotalBattles() {
        return totalBattles;
    }

    public Integer getTotalDamage() {
        return totalDamage;
    }

    public Integer getTotalWins() {
        return totalWins;
    }

    @Override
    public String toString() {
        return "PlayerStats{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", totalBattles=" + totalBattles +
                ", totalWins=" + totalWins +
                ", totalDamage=" + totalDamage +
                '}';
    }

    public void setWn8(int wn8) {
        this.wn8 = wn8;
    }

    public int getWn8() {
        return wn8;
    }
}
