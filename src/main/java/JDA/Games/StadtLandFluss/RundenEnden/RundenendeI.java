package JDA.Games.StadtLandFluss.RundenEnden;

import JDA.Games.StadtLandFluss.Spiel;
import JDA.Games.StadtLandFluss.TeilvonSpiel.Runde;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface RundenendeI {
    static List<RundenendeI> Rundenenden(){
        List<RundenendeI> ausgabe = new ArrayList<>();
        ausgabe.add(new OhneZeitlimit());
        ausgabe.add(new Sekunden(60));
        ausgabe.add(new Sekunden(120));
        ausgabe.add(new Sekunden(180));
        ausgabe.add(new Vorletzerfertig());
        ausgabe.add(new Stopp_Sagen());
        return ausgabe;
    }
    static RundenendeI getRundenEndebyname(String name){
        for (RundenendeI r :
                Rundenenden()) {
            if(r.getName().equals(name)){
                return r;
            }
        }
        return null;
    }
    static boolean isRundeEnde (String s){
        return Rundenenden().stream().map(RundenendeI::getName).toList().contains(s);
    }
    String getName();

    String getErklärung();
    boolean fertig(Spiel spiel);
    String fortschritt(Spiel spiel);
    static RundenendeI getRundenEndebyName (String name){
        for (RundenendeI r :
                Rundenenden()) {
            if(r.getName().equals(name)){
                return r;
            }
        }
        return null;
    }



}
