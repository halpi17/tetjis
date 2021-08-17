import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;

public class VanishData{
    public Point pos;
    public int len;
    public int dir;
    private static HashMap<Integer, String> map = new HashMap<>();
    private static ArrayList<String> dictionary = new ArrayList<String>();
    public VanishData(Point pos, int len, int dir){
        this.pos = pos;
        this.len = len;
        this.dir = dir;
    }

    public static boolean canVanish(ArrayList<Integer> list1) {
        String word = "";
        for ( int i = 0; i < list1.size(); i++ ) {
            word += map.get(list1.get(i));
        }
        return dictionary.contains(word);
    }

    public static void makeCharList() {
        String filename = "data/character.txt";
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"))){
            String line;
            while((line = in.readLine()) != null) {
                String[] data =line.split("\\s+");
                try {
                    int num = Integer.parseInt(data[1]);
                    map.put(num, data[0]);
                } catch ( NumberFormatException e ) {
                    System.exit(0);
                }
            }
        } catch (FileNotFoundException e){ 
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e){ 
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void makeDic() {
        String filename = "data/dic.txt";
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"))){
            String line;
            while((line = in.readLine()) != null) {
                dictionary.add(line);
            }
        } catch (FileNotFoundException e){ 
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e){ 
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
