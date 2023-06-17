package JDA.Games.StadtLandFluss.TeilvonSpiel;

import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;

public class Lobby{
    public static final String titel = "Stadt Land Fluss - Lobby";
    public static final String auflistungszeichen = "• ";
    private boolean open;           //ob die Lobby offen ist
    private List<User> gebannt;
    private List<User> beigetreten;
    public Lobby(){
        open = true;
        gebannt = new ArrayList<>();
        beigetreten = new ArrayList<>();
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
    public void addGebannt(User u){
        gebannt.add(u);
    }
    public void removeGebannt (User u){
        gebannt.remove(u);
    }
    public void addbeigetreten(User u){
        beigetreten.add(u);
    }
    public void removebeigetreten (User u){
        beigetreten.remove(u);
    }
    public List<User> getBeigetreten() {
        return beigetreten;
    }
    public List<User> getGebannt() {
        return gebannt;
    }
    public boolean getopen(){
        return open;
    }
}