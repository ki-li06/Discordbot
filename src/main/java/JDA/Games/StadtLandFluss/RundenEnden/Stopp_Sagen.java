package JDA.Games.StadtLandFluss.RundenEnden;

import JDA.Games.StadtLandFluss.Spiel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stopp_Sagen implements RundenendeI{
    @Override
    public String getName() {
        return "Stopp-Sagen";
    }

    @Override
    public String getErklärung() {
        return "Es wird solange gespielt bis der erste Spieler fertig ist.";
    }

    @Override
    public boolean fertig(Spiel spiel) {
        return spiel.getSpieler().stream().map(spiel::fertig).toList().contains(true);
    }

    @Override
    public String fortschritt(Spiel spiel) {
        List<Integer> anzahleingaben = new ArrayList<>(spiel.getSpieler().stream().map(spiel::anzahleingaben).toList());
        Collections.sort(anzahleingaben);
        Collections.reverse(anzahleingaben);
        if(anzahleingaben.get(0) == 0){
            return "Bisher hat noch niemand etwas eingegeben.";
        }
        if(anzahleingaben.get(0) == 1){
            return "Die Person mit den meisten Eingaben hat bisher einen Wert eingegeben.";
        }
        return "Die Person mit den meisten Eingaben hat bisher " + anzahleingaben.get(0) + " Werte eingegeben.";
    }
}
