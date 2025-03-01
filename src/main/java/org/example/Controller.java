package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class Controller {
    private Model model;
    private View view;

    public Controller(Model model){
        this.model = model;
        this.view = new View(this);
    }

    public void run(){
        view.initView();
        String str = view.getAccount();
        model.setNameAndId(str);
        model.start();
        view.repaintSessionInfo();
    }

    public void update(){
        model.updateLastStats();
        view.repaintSessionInfo();
    }

    public void restart(){
        model.start();
        view.repaintSessionInfo();
    }

    public Model getModel() {
        return model;
    }

    public void saveData(){
        LocalDateTime currentTime = LocalDateTime.now();

        try(FileOutputStream file = new FileOutputStream("save/current.json")){
            String json =Inspector.getJSON(model.getId_player());
            file.write(json.getBytes(StandardCharsets.UTF_8));
            model.updateLastStats(json);
        }catch (Exception ignored){}
    }

    public void setModelFirstStats(String str){
        PlayerStats stats = Inspector.getInfo(str);

        if(stats.getId().intValue() != model.getId_player().intValue()){
            view.showErrorAboutId();
        }
        else{
            model.setStats(stats);
        }
    }
}
