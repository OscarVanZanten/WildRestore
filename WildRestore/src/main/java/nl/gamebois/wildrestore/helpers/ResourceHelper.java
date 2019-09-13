package nl.gamebois.wildrestore.helpers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.Bukkit;

public class ResourceHelper {
    public String GetTextFromResource(String file) {
        try {
            InputStream in = getClass().getResourceAsStream("/"+file); 
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));


            BufferedReader br = new BufferedReader(reader);
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            reader.close();
            in.close();

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getLogger().warning("File error: " + e.toString());
            return null;
        }
    }

}