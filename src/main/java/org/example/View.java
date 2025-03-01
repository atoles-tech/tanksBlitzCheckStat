package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class View{

    private Controller controller;

    private JFrame frame = new JFrame("TanksInfo");
    private JButton updateButton = new JButton("Обновить");
    private JButton restartButton = new JButton("Обнулить статистику");
    private JButton fileButton = new JButton("Установить первичную стату");
    private JButton saveFileButton = new JButton("Скачать текущую стату");
    private JPanel sessionInfo = new JPanel(){
        @Override
        public void paint(Graphics g) {
            super.paint(g);

            saveFileButton.setBounds(7,400,208,25);
            this.add(saveFileButton);

            if(controller.getModel().getStats() == null){return;}
            g.setFont(new Font("Arial",Font.BOLD,12));
            g.setColor(Color.BLACK);
            g.drawString("Первичная статистика:",10,20);
            g.drawString("Кол-во боев:",10,40);
            g.drawString(controller.getModel().getStats().getTotalBattles().toString(),90,40); //Поменять
            g.drawString("Процент побед:",10,60);
            g.drawString(String.format("%.2f",controller.getModel().getStats().getTotalWins()/(double)controller.getModel().getStats().getTotalBattles()*100) + "%",105,60);
            g.drawString("Нанесенный урон:",10,80);
            g.drawString(controller.getModel().getStats().getTotalDamage().toString(),125,80);
            g.drawString("WN8:"+ controller.getModel().getStats().getWn8(),10,100);

            int offset_stat = 120;

            g.drawString("Последняя статистика:",10,20+offset_stat);
            g.drawString("Кол-во боев:",10,40+offset_stat);
            g.drawString(controller.getModel().getLastStats().getTotalBattles().toString(),90,40+offset_stat);
            g.drawString("Процент побед:",10,60+offset_stat);
            g.drawString(String.format("%.2f",controller.getModel().getLastStats().getTotalWins()/(double)controller.getModel().getLastStats().getTotalBattles()*100) + "%",105,60+offset_stat);
            g.drawString("Нанесенный урон:",10,80+offset_stat);
            g.drawString(controller.getModel().getLastStats().getTotalDamage().toString(),125,80+offset_stat);
            g.drawString("WN8:"+ controller.getModel().getLastStats().getWn8(),10,100+offset_stat);

            int offset = 120;

            int battles = controller.getModel().getLastStats().getTotalBattles() -controller.getModel().getStats().getTotalBattles();

            g.setFont(new Font("Arial",Font.BOLD,14));
            g.drawString("Статистика за сессию:",50,140 + offset);
            g.drawString("Средний урон:",10,165+ offset);
            g.drawString(battles!=0?String.format("%.2f",(controller.getModel().getLastStats().getTotalDamage()-controller.getModel().getStats().getTotalDamage())/(double)battles):"0 боев(((",115,165+offset);
            g.drawString("Процент побед:",10,190+ offset);
            g.drawString(battles!=0?String.format("%.2f",(controller.getModel().getLastStats().getTotalWins()-controller.getModel().getStats().getTotalWins())/(double)battles*100) + "%":"0 боев(((",125,190+offset);
            g.drawString("Кол-во боев:",10,215+ offset);
            g.drawString(battles+"",100,215+offset);
        }
    };

    private JPanel tanksInfo = new JPanel(){
        Map<String, BufferedImage> images = new HashMap<>();
        List<Tank> tanks = new ArrayList<>();

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            this.tanks = controller.getModel().getTanks();
            g.setFont(new Font("Arial",Font.BOLD,12));
            g.setColor(Color.BLACK);
            g.drawString("Бои сыграны на:",10,20);
            if(controller.getModel().getStats()== null){return;}
            if(tanks.size() > 5){
                tanksInfo.setPreferredSize(new Dimension(200, 40 + 85*tanks.size()));
            }
            for(int i = 0;i < tanks.size();i++) {
                int offset = i*85;
                g.drawLine(0, 30+offset, 200, 30+offset);
                g.drawLine(110,30+offset,110,115+offset);
                g.drawImage(controller.getModel().getNations().get(tanks.get(i).getNation()),110,30 + offset,95,85,this);
                try {
                    if (!images.containsKey(tanks.get(i).getName())) {
                        images.put(tanks.get(i).getName(), ImageIO.read(new File("data/" + tanks.get(i).getName() + ".png")));
                    }
                }catch (Exception ignored){}
                g.drawString(tanks.get(i).getName(), 10, 47+offset);
                g.drawImage(images.get(tanks.get(i).getName()),110,30+offset,95,85,this);
                g.drawString("Боев:" + tanks.get(i).getTotalBattles(), 10, 63+offset);

                double pr = (tanks.get(i).getTotalWins()/((double)tanks.get(i).getTotalBattles()))*100;
                if(pr > 64){
                    g.setColor(new Color(0xAA31D5));
                }
                else if(pr > 58){
                    g.setColor(new Color(0x4EA0BD));
                }
                else if(pr > 50){
                    g.setColor(new Color(0x5DFF00));
                }
                else if(pr > 45){
                    g.setColor(new Color(0xFFFF00));
                }
                else{
                    g.setColor(new Color(0xC90D1C));
                }
                g.drawLine(0,30+offset,0,115+offset);
                g.drawLine(1,30+offset,1,115+offset);
                g.drawLine(2,30+offset,2,115+offset);

                g.drawString("Процент:" + String.format("%.2f",pr) + "%", 10, 79+offset);
                g.setColor(Color.BLACK);
                g.drawString("Ср.урон:" + String.format("%.2f",tanks.get(i).getTotalDamage()/(double)tanks.get(i).getTotalBattles()), 10, 95+offset);
                g.drawString("WN8:" + String.format("%.2f",tanks.get(i).getWn8()),10,110+offset);
                g.drawLine(0, 115+offset, 200, 115+offset);
            }

        }

    };

    private JPanel buttonsMenu = new JPanel(){
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            this.add(fileButton);
            this.add(updateButton);
            this.add(restartButton);
        }
    };

    public View(Controller controller){this.controller = controller;}

    public void repaintSessionInfo(){
        tanksInfo.repaint();
        sessionInfo.repaint();
    }

    public void initView(){
        frame.setSize(500,500);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tanksInfo.setDoubleBuffered(true);

        updateButton.setBackground(new Color(0xFFCCCB));
        restartButton.setBackground(new Color(0xFFCCCB));
        fileButton.setBackground(new Color(0xFFCCCB));
        saveFileButton.setBackground(new Color(0xFFCCCB));

        updateButton.setForeground(new Color(0x000000));
        restartButton.setForeground(new Color(0x000000));
        fileButton.setForeground(new Color(0x000000));
        saveFileButton.setForeground(new Color(0x000000));

        updateButton.setBorderPainted(false);
        restartButton.setBorderPainted(false);
        fileButton.setBorderPainted(false);
        saveFileButton.setBorderPainted(false);

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.update();
            }
        });

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.restart();
            }
        });

        fileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    if(!selectedFile.getPath().endsWith(".json")){
                        JOptionPane.showMessageDialog(frame,"Выбранный файл не соответствует формату!","Ошибка",JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        try {
                            BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                            StringBuilder builder = new StringBuilder();
                            while (reader.ready()) {
                                builder.append(reader.readLine());
                            }
                            controller.setModelFirstStats(builder.toString());
                            repaintSessionInfo();
                        } catch (IOException ignored) {
                        }
                    }
                }
            }
        });

        saveFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.saveData();
                showInfo();
            }
        });

        sessionInfo.setPreferredSize(new Dimension(270, 400));
        tanksInfo.setPreferredSize(new Dimension(200, 500));

        tanksInfo.setLayout(new BoxLayout(tanksInfo, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(tanksInfo);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xFDE6D2), 1));

        sessionInfo.setBackground(new Color(0xFFF2E6));
        tanksInfo.setBackground(new Color(0xFDE6D2));
        buttonsMenu.setBackground(new Color(0xFFF2E6));

        frame.getContentPane().add(sessionInfo, BorderLayout.WEST);
        frame.getContentPane().add(scrollPane,BorderLayout.EAST);
        frame.getContentPane().add(buttonsMenu,BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public String getAccount() {
        return JOptionPane.showInputDialog(
                frame,
                "Введите ник/id:",
                "Ввод данных",
                JOptionPane.QUESTION_MESSAGE);
    }

    public void showInfo(){
        JOptionPane.showMessageDialog(frame,"Файл сохранен в save/current.json","Файл сохранен",JOptionPane.INFORMATION_MESSAGE);
    }
    public void showErrorAboutId(){
        JOptionPane.showMessageDialog(frame,"Вставленный файл не соответсвует id","Ошибка",JOptionPane.ERROR_MESSAGE);

    }


}
