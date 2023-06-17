package JDA.Games.StadtLandFluss.RundenEnden;

import JDA.Games.StadtLandFluss.Spiel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

import static JDA.util.Lists.wieoft;

public class OhneZeitlimit implements RundenendeI {
    @Override
    public String getName() {
        return "Kein Zeitlimit";
    }

    @Override
    public String getErklärung() {
        return "Es wird solange gespielt bis alle bei jedem Feld etwas eingegeben haben.";
    }

    @Override
    public boolean fertig(Spiel spiel) {
        for(User u : spiel.getSpieler()){
            if(!spiel.fertig(u)){
                return false;
            }
        }
        return true;
    }

    @Override
    public String fortschritt(Spiel s) {
        Spiel spiel = s.returnthis();
        int allefertig = wieoft(spiel.getSpieler().stream().map(
                i->spiel.getaktuelleRunde().fertig(spiel.getSpieler().indexOf(i))).toList(), true);
        if(allefertig == 0){
            return "Bisher ist kein Spieler fertig.";
        }
        if(allefertig == 1){
            return "Bisher ist einer von " + spiel.getSpieler().size() + " Spielern fertig.";
        }
        return "Bisher sind " + allefertig + " von " + spiel.getSpieler().size() + " Spielern fertig.";
    }
}
