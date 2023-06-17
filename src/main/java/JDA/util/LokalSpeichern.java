package JDA.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LokalSpeichern {

    public static void write (String filename, List<? extends Serializable> liste){
        if(liste.size() == 0){
            File f = new File(filename);
            boolean b = f.delete();
            File f2 = new File(filename);
            try {
                b = f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileOutputStream fos = new FileOutputStream(filename)){
            ObjectOutputStream os = new ObjectOutputStream(fos);
            for (Serializable s : liste) {
                os.writeObject(s);
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
    public static <R> List<R> read (String filename, Class R){
        List<R> ausgabe = new ArrayList<>();
        try{
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            while(true){
                try {
                    Object o = ois.readObject();
                    R add = (R) o;
                    ausgabe.add(add);

                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            //e.printStackTrace();
        }
        return ausgabe;

    }
}
