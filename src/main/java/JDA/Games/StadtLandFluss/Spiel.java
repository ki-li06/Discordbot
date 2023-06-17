package JDA.Games.StadtLandFluss;

import JDA.Games.StadtLandFluss.TeilvonSpiel.Kategorie;
import JDA.Games.StadtLandFluss.TeilvonSpiel.Runde;
import JDA.Games.StadtLandFluss.RundenEnden.RundenendeI;
import JDA.Games.StadtLandFluss.TeilvonSpiel.Lobby;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.List;

import static JDA.core.Main.*;
import static JDA.util.Lists.wieoft;
import static JDA.util.Random.randomString;
import static JDA.Games.StadtLandFluss.Aktionen.Commands.CommandMain;

public class Spiel {
    public static final String bild = "https://www.wort-suchen.de/2/m/2019/01/stadtlandfluss_400.png";
    public static final int maximaleAnzahlKategorien = 7;

    private static List<Spiel> aktuelle = new ArrayList<>();

    private int index;
    private User host;
    private List<User> spieler = new ArrayList<>();

    private List<Kategorie> kategorien = new ArrayList<>();
    private int runde;
    private Runde[] runden;
    private RundenendeI rundenende;
    private Thread waitingonroundend;
    private boolean finishedsettings;

    private Message lobbymessage;
    private Message settingmessage;
    private Message finishedsettingsmessage;
    private Message ende;
    public Lobby lobby = new Lobby();

    public Spiel(User host, Message lobbymessage){
        System.out.println("StadtLandFluss Spiel erstellt by " + host.getName());
        this.host = host;
        spieler.add(host);
        this.lobbymessage = lobbymessage;
        System.out.println("Message - Author: " + lobbymessage.getAuthor().getName() + "; URL: " + lobbymessage.getJumpUrl());
        finishedsettings = false;
        index = aktuelle.size();
        kategorien.addAll(Kategorie.kategorienStadtLand());
        aktuelle.add(this);
    }
    public Spiel(){
        System.out.println("StadtLandFluss Spiel erstellt");
        index = aktuelle.size();
        aktuelle.add(this);
    }
    public String toString (){
        return "{Spieler: " + aktuelle.get(index).spieler + "}";
    }

    public Spiel returnthis (){
        return aktuelle.get(index);
    }
    public User getHost () { return host;}
    public List<User> getSpieler () {
        return spieler;}
    public Message getLobbymessage() {
        aktuelle.get(index).lobbymessage = updated(aktuelle.get(index).lobbymessage);
        return aktuelle.get(index).lobbymessage;
    }
    public Message getSettingmessage() {
        return aktuelle.get(index).settingmessage;
    }
    public Message getFinishedsettingsmessage(){
        return aktuelle.get(index).finishedsettingsmessage;
    }
    public boolean beigetreten (User user){
        return aktuelle.get(index).lobby.getBeigetreten().contains(user);
    }
    public boolean gebannt (User user){
        return aktuelle.get(index).lobby.getGebannt().contains(user);
    }
    public List<Kategorie> getKategorien() {
        return aktuelle.get(index).kategorien;
    }
    public boolean finishedsettings(){
        return aktuelle.get(index).finishedsettings;
    }
    public int getRunde() {
        return aktuelle.get(index).runde;
    }
    public int getRundenanzahl() { return aktuelle.get(index).runden.length;}
    public RundenendeI getRundenende(){
        return aktuelle.get(index).rundenende;
    }
    public boolean getInRunde(){
        return aktuelle.get(index).runden[runde].isInRunde();
    }
    public boolean fertig(User u){
        List<String> eingaben = new ArrayList<>(Arrays.stream(getEingaben(u, runde)).toList());
        for(String s : eingaben){
            if(s == null){
                return false;
            }
        }
        return true;

    }
    public Runde getaktuelleRunde(){
        return aktuelle.get(index).runden[runde];
    }
    public String[] getEingaben (User u, int runde){
        int indexu = aktuelle.get(index).spieler.indexOf(u);
        return aktuelle.get(index).runden[runde].geteingaben(indexu);
    }
    public int anzahleingaben (User u){
        int indexu = aktuelle.get(index).spieler.indexOf(u);
        List<String> eingaben = new ArrayList<>(Arrays.stream(aktuelle.get(index).runden[runde].geteingaben(indexu)).toList());
        return aktuelle.get(index).kategorien.size() - wieoft(eingaben, null);
    }
    public String getEingabe (User u, Kategorie k){
        return aktuelle.get(index).runden[runde].geteingabe(
                spieler.indexOf(u),
                kategorien.indexOf(k)
        );
    }
    public String[] TabelleÜberschriften (){
        String[] ausgabe = new String[getKategorien().size() + 2];
        ausgabe[0] = "Spielernamen";
        System.arraycopy(
                getKategorien().stream().map(Kategorie::getName).toArray(String[]::new),
                0,
                ausgabe,
                1,
                getKategorien().size()
        );
        ausgabe[ausgabe.length-1] = "Gesamtpunkte";
        System.out.println(List.of(ausgabe) + " (" + ausgabe.length + ")");
        return ausgabe;
    }
    public String[][] TabelleWerte(){
        String[][] eingaben = getaktuelleRunde().getEingaben();
        int[][] punkte = getaktuelleRunde().getPunkte();
        String[][] ausgabe = new String[eingaben.length][eingaben[0].length+2];
        for (int i = 0; i < ausgabe.length; i++) {
            ausgabe[i][0] = spieler.get(i).getAsTag();
        }
        for (int i = 0; i < eingaben.length; i++) {
            for (int j = 0; j < eingaben[0].length; j++) {
                if(eingaben[i][j] != null){
                    ausgabe[i][j+1] = eingaben[i][j] + "(" + punkte[i][j] + ")";
                }
                else{
                    ausgabe[i][j+1] = "–";
                }
            }
        }
        for (int i = 0; i < ausgabe.length; i++) {
            ausgabe[i][ausgabe[0].length-1] = String.valueOf(getaktuelleRunde().getgesamtPunkte(i));
        }
        getaktuelleRunde().ausgeben(ausgabe);
        return ausgabe;
    }
    public String[] EndeTabelleÜberschriften(){
        String[] ausgabe = new String[runden.length + 2];
        ausgabe[0] = "Spielernamen";
        ausgabe[ausgabe.length - 1] = "Gesamt";
        for (int i = 1; i < ausgabe.length - 1; i++) {
            ausgabe[i] = "Runde " + i;
        }
        System.out.println("tabelle überschriften: " + Arrays.stream(ausgabe).toList());
        return ausgabe;
    }
    public String[][] EndeTabelleWerte(){
        System.out.println("runden.length: " + runden.length);
        String[][] ausgabe = new String[spieler.size()][runden.length + 2];
        System.out.println("lengths: " + ausgabe.length + "  |  " + ausgabe[0].length);
        for (int i = 0; i < ausgabe.length; i++) {
            ausgabe[i][0] = spieler.get(i).getAsTag();
        }
        for (int runde = 1; runde < ausgabe[0].length - 1; runde++) {
            for (int spieler = 0; spieler < ausgabe.length; spieler++) {
                ausgabe[spieler][runde] = String.valueOf(aktuelle.get(index).runden[runde-1]
                        .getgesamtPunkte(spieler));
            }
        }
        for (int i = 0; i < ausgabe.length; i++) {
            ausgabe[i][ausgabe[0].length-1] = String.valueOf(getgesamtpunkte(spieler.get(i)));
        }
        System.out.println("werte: ");
        for (int i = 0; i < ausgabe.length; i++) {
            for (int j = 0; j < ausgabe[0].length; j++) {
                System.out.printf("%-10.10s", ausgabe[i][j]);
                System.out.print("|");
            }
            System.out.println();
        }
        return ausgabe;
    }
    public int getgesamtpunkte(User u){
        int indexu = spieler.indexOf(u);
        int gesamt = 0;
        for (int rundei = 0; rundei < runden.length; rundei++) {
            gesamt += aktuelle.get(index).runden[rundei].getgesamtPunkte(indexu);
        }
        return gesamt;
    }
    public List<User> SpielernachPunktensortiert(){
        class Player{
            final User u;
            final int punkte;
            public Player(User u, int punkte) {
                this.u = u;
                this.punkte = punkte;
            }
        }
        List<Player> players = new ArrayList<>(
                spieler.stream().map(i->new Player(i, getgesamtpunkte(i))).toList());
        players.sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                int punktevergleich = Integer.compare(o1.punkte, o2.punkte);
                if(punktevergleich != 0) {
                    return Integer.compare(o1.punkte, o2.punkte);
                }
                int gemachteeingaben1 = getgesamtgemachteEingaben(o1.u);
                int gemachteeingaben2 = getgesamtgemachteEingaben(o2.u);
                return Integer.compare(gemachteeingaben1, gemachteeingaben2);
            }
        });
        Collections.reverse(players);
        return players.stream().map(i->i.u).toList();
    }
    public int getgesamtgemachteEingaben (User u){
        int indexu = aktuelle.get(index).spieler.indexOf(u);
        int ausgabe = 0;
        for(int i = 0; i < getRundenanzahl(); i++){
            ausgabe += (
                    kategorien.size() -
                            wieoft(Arrays.stream(runden[runde].geteingaben(indexu)).toList(), null)
                    );
        }
        return ausgabe;
    }
    public Message getEnde() {
        return ende;
    }

    public void setMitspielerbylobby (){
        aktuelle.get(index).spieler.addAll(lobby.getBeigetreten());
        aktuelle.get(index).lobby.setOpen(false);
        System.out.println("mitspieler wurden gesetzt");
    }
    public void setRundenanzahl (int runden){
        aktuelle.get(index).runde = 0;
        aktuelle.get(index).runden = new Runde[runden];
    }
    public void setSettingmessage (Message m){
        aktuelle.get(index).settingmessage = m;
    }
    public void setFinishedsettingsmessage (Message m){
        aktuelle.get(index).finishedsettingsmessage = m;
    }
    public void setRundenende(RundenendeI rundenende) {
        aktuelle.get(index).rundenende = rundenende;
    }
    public void setFinishedSettings(boolean setsettings){
        aktuelle.get(index).finishedsettings = setsettings;
    }
    public void setKategorien (List<Kategorie> kategorien){
        if(aktuelle.get(index).runden != null) {
            aktuelle.get(index).kategorien.addAll(kategorien);
            aktuelle.get(index).runden[0] = new Runde(
                    aktuelle.get(index).spieler.size(),
                    aktuelle.get(index).kategorien.size(),
                    new ArrayList<>()
            );
            for (int i = 1; i < runden.length; i++) {
                aktuelle.get(index).runden[i] = new Runde(
                        aktuelle.get(index).spieler.size(),
                        aktuelle.get(index).kategorien.size(),
                        Arrays.stream(aktuelle.get(index).runden).limit(i).
                                map(Runde::getBuchstabe).toList());
            }
        }
    }
    public void neueRunde (){
        System.out.println("Go in new round");
        aktuelle.get(index).runden[runde].setInRunde(false);
        if(aktuelle.get(index).runde != aktuelle.get(index).getRundenanzahl() - 1) {
            aktuelle.get(index).runde++;
        }
    }
    public void setInrunde(boolean neu){
        aktuelle.get(index).runden[runde].setInRunde(neu);
    }
    public void setRundeStartMessage (Message m){
        aktuelle.get(index).runden[runde].start = m;
    }
    public void setRundenStartZeit (OffsetDateTime time){
        aktuelle.get(index).runden[runde].startzeit = time;
    }
    public void updatefortschritt (){
        EmbedBuilder embed = new EmbedBuilder(aktuelle.get(index).runden[runde].start.getEmbeds().get(0));
        List<MessageEmbed.Field> fields = new ArrayList<>(embed.getFields());
        MessageEmbed.Field field2 = new MessageEmbed.Field(
                fields.get(1).getName(), aktuelle.get(index).rundenende.fortschritt(this), false);
        fields.set(1, field2);
        embed = embed.clearFields();
        for (MessageEmbed.Field field : fields) {
            embed = embed.addField(field);
        }
        aktuelle.get(index).runden[runde].start.editMessageEmbeds(embed.build()).queue();
        aktuelle.get(index).runden[runde].start = updated(aktuelle.get(index).runden[runde].start);
    }
    public void warteAufRundenEnde(){
        waitingonroundend = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("run waiting on round end");
                        String aktuellertext = getaktuelleRunde().start.getEmbeds().get(0).getFields().get(1).getValue();
                        while(!aktuelle.get(index).rundenende.fertig(aktuelle.get(index))){
                            //System.out.println(aktuelle.get(index).rundenende.fortschritt(Spiel.this));
                            delay(20L);
                            if(!aktuelle.get(index).rundenende.fortschritt(Spiel.this).equals(aktuellertext)){
                                aktuellertext = aktuelle.get(index).rundenende.fortschritt(Spiel.this);
                                updatefortschritt();
                            }
                        }
                        System.out.println("finished run loop");

                        List<MessageEmbed.Field> fields = new ArrayList<>(getaktuelleRunde().start.getEmbeds().get(0).getFields());
                        fields.set(1, new MessageEmbed.Field("Fortschritt", "Diese Runde ist vorbei.", false));
                        EmbedBuilder embed = new EmbedBuilder(getaktuelleRunde().start.getEmbeds().get(0));
                        embed = embed.clearFields();
                        for (MessageEmbed.Field f : fields) {
                            embed = embed.addField(f);
                        }
                        getaktuelleRunde().start.editMessageEmbeds(embed.build()).queue();
                        aktuelle.get(index).runden[runde].start = updated(getaktuelleRunde().start);
                        aktuelle.get(index).runden[runde].setEingabevorbei(true);
                        aktuelle.get(index).runden[runde].kontrolliereAnfangsbuchstaben();
                        aktuelle.get(index).runden[runde].eingabenausgeben();
                        embed = new EmbedBuilder();
                        String ownavatarurl = Objects.requireNonNull(getaktuelleRunde().start.getJDA().getUserById(ID)).getAvatarUrl();
                        String myid = randomString();
                        embed = embed
                                .setColor(Color.CYAN)
                                .setThumbnail(bild)
                                .setDescription(myid)
                                .setFooter("StadtLandFluss by Hackerman", ownavatarurl)
                                .setTitle("Stadt Land Fluss - Korrektur")
                                .addField(getaktuelleRunde().start.getEmbeds().get(0).getFields().get(0))
                                .addField(getaktuelleRunde().start.getEmbeds().get(0).getFields().get(2));
                        if(aktuelle.get(index).runde == 0) {
                            embed = embed.addField("Was muss ich machen?",
                                    "Jetzt müssen bei allen Eingaben von  allen Spielern überprüft werden, " +
                                            "ob die Eingaben auch zur Kategorie passen. Deshalb bekommt jeder Spieler zu jeder Kategorie eine Eingabe eines " +
                                            "Mitspielers. Dafür benutzt man den */" + CommandMain +
                                            " korrigieren* Command. Wenn man den Command ohne Parameter nutzt, " +
                                            "werden die zu korrigierenden Eingaben nummeriert angezeigt. Dann muss man mit dem gleichen Command - mit Parametern - angeben, " +
                                            "ob die Eingabe zur Kategorie passt", false);
                        }
                        else{
                            embed = embed.addField("Was muss ich machen?", "siehe hier: " + aktuelle.get(index).runden[0].endeeingabe.getJumpUrl(), false);
                        }
                        embed = embed.addField(getaktuelleRunde().start.getEmbeds().get(0).getFields().get(3));
                        MessageChannel mc = aktuelle.get(index).runden[0].start.getChannel();
                        mc.sendMessageEmbeds(embed.build()).queue();
                        Message m = mc.getHistory().retrievePast(1).complete().get(0);
                        while(!Objects.requireNonNull(m.getEmbeds().get(0).getDescription()).equals(myid)){
                            m = mc.getHistory().retrievePast(1).complete().get(0);
                        }
                        embed.setDescription(getaktuelleRunde().start.getEmbeds().get(0).getDescription());
                        m.editMessageEmbeds(embed.build()).queue();
                        m = updated(m);
                        aktuelle.get(index).runden[runde].endeeingabe = m;
                    }
                }, "waiting on round end (" + index + ")");
        waitingonroundend.start();
        System.out.println("started warte auf runden ende");
    }
    public void eingabe (User u, Kategorie k, String value){
        int indexu = aktuelle.get(index).spieler.indexOf(u);
        int indexk = aktuelle.get(index).kategorien.indexOf(k);
        aktuelle.get(index).runden[runde].eingabe(indexu, indexk, value);
        //System.out.println("Der User " + u + " (" + indexu + ") hat in der Kategorie " + k + " (" + indexk + ") den Wert " + value + " eingetragen");
    }
    public void correct (User byuser, int kategorie, boolean value){
        int user = aktuelle.get(index).spieler.indexOf(byuser);
        aktuelle.get(index).runden[runde].setKontrolliert(user, kategorie, value);
    }
    public void addMitspieler(User user){
        aktuelle.get(index).lobby.addbeigetreten(user);
    }
    public void removeMitspieler (User user){
        try {
            aktuelle.get(index).lobby.removebeigetreten(user);
        }
        catch (Exception ex){
            System.out.println("FEHLER ---- Remove user: " + user.getAsTag());
            ex.printStackTrace();
        }
    }
    public void addgebannt(User user){
        aktuelle.get(index).lobby.addGebannt(user);

    }
    public void removegebannt (User user){
        try {
            aktuelle.get(index).lobby.removeGebannt(user);
        }
        catch (Exception ex){
            System.out.println("FEHLER ---- Remove user: " + user.getAsTag());
            ex.printStackTrace();
        }
    }
    public void setEnde(Message ende) {
        aktuelle.get(index).ende = ende;
    }
    public void delete(){
        for (Runde r :
                aktuelle.get(index).runden) {
            r.start.delete().queue();
            r.endeeingabe.delete().queue();
        }
        aktuelle.get(index).settingmessage.delete().queue();
        aktuelle.get(index).finishedsettingsmessage.delete().queue();
        aktuelle.get(index).waitingonroundend.interrupt();
        for(int i = index + 1; i < aktuelle.size(); i++){
            aktuelle.get(i).index--;
        }
        aktuelle.remove(index);
        index = -1;
    }



    public static Spiel getSpielbyLobbyMessage (Message lobbym){
        int stelle = -1;
        for(Spiel s : aktuelle){
            if(s.lobbymessage.getId().equals(lobbym.getId()) && s.lobbymessage.getChannel().getId().equals(lobbym.getChannel().getId())){
                stelle = s.index;
                break;
            }
        }
        return aktuelle.get(stelle);
    }
    public static Spiel getSpielbyUser (User user){
        int stelle = -1;
        for (int i = 0; i < aktuelle.size(); i++) {
            if(aktuelle.get(i).lobby.getBeigetreten().contains(user)){
                stelle = i;
                break;
            }
            else if(aktuelle.get(i).spieler.contains(user)) {
                stelle = i;
                break;
            }
        }
        return aktuelle.get(stelle);
    }
    public static boolean imSpiel (User user){
        for (Spiel spiel : aktuelle) {
            for (User u : spiel.lobby.getBeigetreten()) {
                if (u.equals(user)) {
                    return true;
                }
            }
            for (User u : spiel.spieler) {
                if (u.equals(user)) {
                    return true;
                }
            }

        }
        return false;
    }

    public static OffsetDateTime lastupdate (Message m){
        if(m.isEdited()){
            return m.getTimeEdited();
        }
        return m.getTimeCreated();
    }
    public static Message updated (Message m){
        TextChannel tc = m.getTextChannel();
        String id = m.getId();
        Message ausgabe;
        ausgabe = tc.retrieveMessageById(id).complete();
        return ausgabe;
    }


}
