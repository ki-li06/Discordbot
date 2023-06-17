package JDA.Games.StadtLandFluss.RundenEnden;

import JDA.Games.StadtLandFluss.Spiel;
import net.dv8tion.jda.api.entities.User;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sekunden implements RundenendeI {
    private int dauer;
    public Sekunden(int dauer){
        this.dauer = dauer;
    }
    @Override
    public String getName() {
        return String.valueOf(dauer) + " Sekunden";
    }

    @Override
    public String getErklärung() {
        return "Es wird " + dauer + " Sekunden lang gespielt.";
    }

    @Override
    public boolean fertig(Spiel spiel) {
        Duration duration = Duration.between(spiel.getaktuelleRunde().startzeit, OffsetDateTime.now());
        return duration.getSeconds() >= dauer;

    }

    @Override
    public String fortschritt(Spiel spiel) {
        if(spiel.getaktuelleRunde().start != null) {
            Duration duration = Duration.between(spiel.getaktuelleRunde().startzeit, OffsetDateTime.now());
            long diff = duration.getSeconds();
            return "Bisher sind  " + diff + " von " + dauer + " Sekunden vergangen. Es bleiben also noch " + (dauer - diff) + " Sekunden.";
        }
        else{
            return "Es laufen " + dauer + " Sekunden";
        }
    }
}
