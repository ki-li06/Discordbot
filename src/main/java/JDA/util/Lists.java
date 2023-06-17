package JDA.util;

import java.util.ArrayList;
import java.util.List;

public class Lists {
    public static int wieoft (List<?> referenz, Object vergleich){
        int ausgabe = 0;
        for (Object i :
                referenz) {
            if((i != null && i.equals(vergleich)) || (vergleich == null && i == null)){
                ausgabe++;
            }
        }
        return ausgabe;
    }
    public static boolean wertedoppelt (List<?> liste){
        List<Integer> häufigkeit = new ArrayList<>(
                liste.stream().map(i->wieoft(liste, i)).toList()
        );
        for (Integer integer : häufigkeit) {
            if (integer != 1) {
                return true;
            }
        }
        return false;
    }


}
