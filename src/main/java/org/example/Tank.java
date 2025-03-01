package org.example;

public class Tank {
    private String name;
    private int totalBattles;
    private int totalWins;
    private int totalDamage;
    private int tier;
    private String url;
    private String nation;
    private double wn8;

    public Tank(String name, int tier, int totalBattles, int totalDamage, int totalWins,String url,String nation,double wn8) {
        this.name = name;
        this.tier = tier;
        this.totalBattles = totalBattles;
        this.totalDamage = totalDamage;
        this.totalWins = totalWins;
        this.url = url;
        this.nation = nation;
        this.wn8 = wn8;
    }

    @Override
    public String toString() {
        return "Tank{" +
                "name='" + name + '\'' +
                ", totalBattles=" + totalBattles +
                ", totalWins=" + totalWins +
                ", totalDamage=" + totalDamage +
                ", tier=" + tier +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getTier() {
        return tier;
    }

    public int getTotalBattles() {
        return totalBattles;
    }

    public int getTotalDamage() {
        return totalDamage;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public String getUrl() {
        return url;
    }

    public String getNation() {
        return nation;
    }

    public double getWn8() {
        return wn8;
    }
}
