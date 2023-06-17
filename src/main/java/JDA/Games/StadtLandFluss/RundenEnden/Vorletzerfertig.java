package JDA.Games.StadtLandFluss.RundenEnden;

import JDA.Games.StadtLandFluss.Spiel;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;

import static JDA.util.Lists.wieoft;

public class Vorletzerfertig implements RundenendeI {

    @Override
    public String getName() {
        return "Vorletzter fertig";
    }

    @Override
    public String getErklärung() {
        return "Es wird solange gespielt bis alle - außer ein Spieler - fertig sind.";
    }

    @Override
    public boolean fertig(Spiel spiel) {
        List<Boolean> fertig = new ArrayList<>(spiel.getSpieler().stream().map(spiel::fertig).toList());
        int wievielefertig = 0;
        for (Boolean b : fertig) {
            if(b){
                wievielefertig++;
            }
        }
        return wievielefertig >= spiel.getSpieler().size() - 1;
    }

    @Override
    public String fortschritt(Spiel spiel){
        int spielerfertig = wieoft(spiel.getSpieler().stream().map(spiel::fertig).toList(), true);
        if(spielerfertig == 0){
            return "Bisher ist kein Spieler fertig.";
        }
        String ausgabe = "";
        if(spielerfertig == 1){
            ausgabe += "Bisher ist ein Spieler fertig. ";
        }
        else{
            ausgabe += "Bisher sind " + spielerfertig + " von " + spiel.getSpieler().size() + " Spielern fertig. ";
        }
        if(spiel.getSpieler().size() - spielerfertig == 2){
            ausgabe += "Es muss also noch ein Spieler fertig werden.";
        }
        else{
            ausgabe += "Es müssen also noch " + (spiel.getSpieler().size() - spielerfertig - 1) + " Spieler fertig werden.";
        }
        return ausgabe;
    }
}
