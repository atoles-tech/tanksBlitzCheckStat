package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class Inspector {
    private static final String URL = "https://api.blitzstats.ru/user/v2/?id=%d&accessToken=b7dc5889af9b3609e0806ea75e73a116d6d7c393";

    public static PlayerStats getInfo(String nickname, Integer id){
        Map<Integer, Tank> tanks = new HashMap();
        PlayerStats stats = new PlayerStats(id,nickname);

        ObjectMapper mapper = new ObjectMapper();

        try{

            JsonNode root = mapper.readTree(getJSON(id));
            JsonNode statistic = root.path("userData").path("statistics");
            Integer battles = statistic.path("battles").asInt();
            stats.setTotalBattles(battles);
            Integer wins = statistic.path("wins").asInt();
            stats.setTotalWins(wins);
            Integer wn8 = statistic.path("wn8").asInt();
            stats.setWn8(wn8);
            JsonNode tanksNode = root.path("userTanks");
            Iterator<JsonNode> it = tanksNode.iterator();

            while(it.hasNext()){
                JsonNode node = it.next();
                String name = node.path("tankData").path("name").asText();
                Integer tier = node.path("tankData").path("tier").asInt();
                Integer battles_t = node.path("statistics").path("battles").asInt();
                Integer wins_t = node.path("statistics").path("wins").asInt();
                Integer damage = node.path("statistics").path("avgDamage").asInt()*battles_t;
                String url = node.path("tankData").path("image_preview").asText();
                String nation = node.path("tankData").path("nation").asText();
                Integer wn8_t = node.path("statistics").path("wn8").asInt();
                stats.addDamage(damage);
                tanks.put(node.path("tank_id").asInt(),new Tank(name,tier,battles_t,damage,wins_t,url,nation,wn8_t));
            }

        }catch (Exception ignored){}

        stats.setTanks(tanks);

        return stats;
    }

    public static PlayerStats getInfo(String str){
        Map<Integer, Tank> tanks = new HashMap();
        PlayerStats stats = new PlayerStats(null,null);

        ObjectMapper mapper = new ObjectMapper();

        try{

            JsonNode root = mapper.readTree(str);
            stats = new PlayerStats(root.path("userData").path("personal").path("lestaData").path("account_id").asInt(),root.path("userData").path("personal").path("lestaData").path("nickname").asText());
            JsonNode statistic = root.path("userData").path("statistics");
            Integer battles = statistic.path("battles").asInt();
            stats.setTotalBattles(battles);
            Integer wins = statistic.path("wins").asInt();
            stats.setTotalWins(wins);
            Integer wn8 = statistic.path("wn8").asInt();
            stats.setWn8(wn8);
            JsonNode tanksNode = root.path("userTanks");
            Iterator<JsonNode> it = tanksNode.iterator();

            while(it.hasNext()){
                JsonNode node = it.next();
                String name = node.path("tankData").path("name").asText();
                Integer tier = node.path("tankData").path("tier").asInt();
                Integer battles_t = node.path("statistics").path("battles").asInt();
                Integer wins_t = node.path("statistics").path("wins").asInt();
                Integer damage = node.path("statistics").path("avgDamage").asInt()*battles_t;
                String url = node.path("tankData").path("image_preview").asText();
                String nation = node.path("tankData").path("nation").asText();
                double wn8_t = node.path("statistics").path("wn8").asDouble();
                stats.addDamage(damage);
                tanks.put(node.path("tank_id").asInt(),new Tank(name,tier,battles_t,damage,wins_t,url,nation,wn8_t));
            }

        }catch (Exception ignored){
        }

        stats.setTanks(tanks);

        return stats;
    }

    public static String getJSON(Integer id) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Safari/537.36")
                .uri(new URI(String.format(URL,id)))
                .build();

        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        String contentEncoding = response.headers().firstValue("Content-Encoding").orElse("");
        byte[] responseBody = response.body();

        String decodedResponse;
        if ("gzip".equals(contentEncoding)) {
            decodedResponse = decompressGzip(responseBody);
        } else {
            decodedResponse = new String(responseBody, StandardCharsets.UTF_8);
        }

        return decodedResponse;
    }

    public static BufferedImage getImage(String url) throws Exception {
        return ImageIO.read(new URL(url));
    }

    public static void saveInFile(String filename,BufferedImage image) throws IOException{
        FileOutputStream file = new FileOutputStream("data/" + filename + ".png");
        ImageIO.write(image,"png",file);
    }

    private static String decompressGzip(byte[] compressedData) throws IOException {
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(compressedData));
             InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }
    }
}
