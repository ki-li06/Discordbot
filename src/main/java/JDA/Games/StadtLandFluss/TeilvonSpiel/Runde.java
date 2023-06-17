package JDA.Games.StadtLandFluss.TeilvonSpiel;

import net.dv8tion.jda.api.entities.Message;

import java.sql.SQLOutput;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.IntStream;

import static JDA.util.Random.random;
import static JDA.util.Lists.wieoft;

public class Runde{
    public Message start;
    public OffsetDateTime startzeit;
    public Message endeeingabe;
    public Message enderunde;

    private boolean inRunde;
    private boolean eingabevorbei;
    private String buchstabe;
    private String[][] eingaben; //spieler - kategorie
    private Tausch[][] tausche;         //die werte die angezeigt werden nach dem tausch
    private Kontrolle[][] kontrolliert;
    private int[][] punkte;

    public Runde(int spieleranzahl, int kategorieanzahl, List<String> verbotenebuchstaben){
        inRunde = false;
        eingabevorbei = false;
        List<String> nichterlaubt = new ArrayList<>(verbotenebuchstaben);
        nichterlaubt.addAll(List.of(new String[]{"X", "Y", "Q"}));
        do {
            char c = (char) random(65, 90);
            buchstabe = String.valueOf(c);
        }
        while (nichterlaubt.contains(buchstabe));
        System.out.println("Neue Runde mit dem Buchstaben: " + buchstabe);
        eingaben = new String[spieleranzahl][kategorieanzahl];
        /*
        for (int i = 0; i < spieleranzahl; i++) {
            for (int j = 0; j < kategorieanzahl; j++) {
                eingaben[i][j] = "(spie. " + i + "; kat. " + j + ")";
            }
        }
        */
        tausche = new Tausch[spieleranzahl][kategorieanzahl];
        kontrolliert = new Kontrolle[spieleranzahl][kategorieanzahl];
        punkte = new int[spieleranzahl][kategorieanzahl];
        for (int i = 0; i < punkte.length; i++) {
            for (int j = 0; j < punkte[i][j]; j++) {
                punkte[i][j] = -1;
            }
        }
        setTausche();
    }

    public String getBuchstabe(){
        return buchstabe;
    }
    public int spieleranzahl(){
        return eingaben.length;
    }
    public int kategorieanzahl(){
        return eingaben[0].length;
    }
    public String[] geteingaben (int user){
        return eingaben[user];
    }
    public Kontrolle[] getGemachteKontrollen (int user){
        if(schongetauscht()) {
            Tausch[] tauschs = tausche[user];
            int[] angezeigtespieler = Arrays.stream(tauschs).mapToInt(i->i.neu).toArray();
            //System.out.println(Arrays.stream(angezeigtespieler).boxed().toList());
            Kontrolle[] ausgabe = new Kontrolle[kategorieanzahl()];
            for (int i = 0; i < ausgabe.length; i++) {
                ausgabe[i] = kontrolliert[angezeigtespieler[i]][i];
            }
            return ausgabe;
        }
        return null;
    }
    public String geteingabe (int spieler, int kategorie){
        return eingaben[spieler][kategorie];
    }
    public String[] anzeigendeWerte (int user){
        String[] ausgabe = new String[kategorieanzahl()];
        Tausch[] tausches = tausche[user];
        for (int i = 0; i < kategorieanzahl(); i++) {
            ausgabe[i] = eingaben[tausches[i].neu][i];
        }
        return ausgabe;
    }
    private int punkteprivate (int spieler, int kategorie){
        if(kontrolliert[spieler][kategorie] != null) {
            if(kontrolliert[spieler][kategorie].kontrolliert) {
                List<String> werte = new ArrayList<>(Arrays.stream(eingaben).map(s -> s[kategorie]).toList());
                werte.remove(spieler);
                if (eingaben[spieler][kategorie] != null && eingaben[spieler][kategorie].toUpperCase().startsWith(buchstabe)) {
                    if (wieoft(werte.stream().map(this::formatted).toList(), null) == werte.size()) {
                        return 20;
                    }
                    else if (wieoft(werte.stream().map(this::formatted).toList(), formatted(eingaben[spieler][kategorie])) == 0) {
                        return 10;
                    }
                    else {
                        return 5;
                    }
                }
            }
        }
        else{
            System.out.println("Eingabe null" + eingaben[spieler][kategorie]);
        }
        return 0;
    }
    public int punkte(int spieler, int kategorie){
        if(punkte[0][0] == -1){
            setPunkte();
        }
        return punkte[spieler][kategorie];
    }
    private String formatted (String eingabe){
        if(eingabe == null){
            return null;
        }
        return eingabe
                .toLowerCase()
                .replaceAll("[^A-Za-z üäö,]", "")
                .replaceAll("\\s+", " ");
    }
    public boolean isInRunde (){
        return inRunde;
    }
    public boolean isEingabevorbei (){
        return eingabevorbei;
    }
    public boolean schongetauscht(){
        if(tausche[0][0] == null){
            System.out.println("FEHLER - NOCH NICHT GETAUSCHT");
        }
        return tausche[0][0] != null;
    }
    public boolean fertig (int user){
        for (int i = 0; i < eingaben[user].length; i++) {
            if(eingaben[user][i] == null){
                return false;
            }
        }
        return true;
    }
    public boolean fertigkontrolliert (){
        for (int i = 0; i < kontrolliert.length; i++) {
            for (int j = 0; j < kontrolliert[i].length; j++) {
                if(kontrolliert[i][j] == null && eingaben[i][j] != null){
                    return false;
                }
            }
        }
        return true;
    }
    public boolean fertigkontrolliert (int user){
        int[] kontrollen = Arrays.stream(tausche[user]).mapToInt(i->i.neu).toArray();
        Kontrolle[] gemachtekontrollen = new Kontrolle[kategorieanzahl()];
        for (int i = 0; i < kategorieanzahl(); i++) {
            if(eingaben[kontrollen[i]][i] != null) {
                gemachtekontrollen[i] = kontrolliert[kontrollen[i]][i];
            }
            else{
                gemachtekontrollen[i] = new Kontrolle(true);
            }
        }
        return !Arrays.stream(gemachtekontrollen).toList().contains(null);
    }
    public int[][] getPunkte(){
        if(punkte[0][0] == -1){
            setPunkte();
        }
        return punkte;
    }
    public String[][] getEingaben(){
        return Arrays.stream(eingaben).map(String[]::clone).toArray(String[][]::new);
    }
    public int getgesamtPunkte(int spieler){
        return IntStream.range(0, kategorieanzahl()).boxed().mapToInt(i->punkte(spieler, i)).sum();
    }
    private Tausch alt (int neu, int kategorie){
        if(schongetauscht()) {
            Tausch[] tauscheinderkategorie = new Tausch[eingaben.length];
            for (int i = 0; i < tausche.length; i++) {
                tauscheinderkategorie[i] = tausche[i][kategorie];
            }
            for (int i = 0; i < tauscheinderkategorie.length; i++) {
                if (tauscheinderkategorie[i].neu == neu) {
                    return tauscheinderkategorie[i];
                }
            }
        }
        Tausch ausgabe = new Tausch();
        ausgabe.alt = -1;
        ausgabe.neu = -1;
        return ausgabe;
    }


    public void eingabenausgeben(){
        ausgeben(this.eingaben);
    }
    public void getauschtausgeben(){
        String[][] tauschen = new String[eingaben.length][eingaben[0].length];
        for (int i = 0; i < tauschen.length; i++) {
            for (int j = 0; j < tauschen[i].length; j++) {
                int neuerspieler = tausche[i][j].neu;
                tauschen[i][j] = eingaben[neuerspieler][j];
            }
        }
        ausgeben(tauschen);
    }
    public void tauscheausgeben(){
        String[][] ausgeben = new String[tausche.length][tausche[0].length];
        for (int i = 0; i < ausgeben.length; i++) {
            for (int j = 0; j < ausgeben[i].length; j++) {
                ausgeben[i][j] = String.valueOf(tausche[i][j]);
            }
        }
        ausgeben(ausgeben);
    }
    public void kontrolliertausgeben(){
        String[][] kontrolliert = new String[eingaben.length][eingaben[0].length];
        for (int i = 0; i < kontrolliert.length; i++) {
            for (int j = 0; j < kontrolliert[i].length; j++) {
                kontrolliert[i][j] = String.valueOf(this.kontrolliert[i][j]);
            }
        }
        ausgeben(kontrolliert);
    }
    public void ausgeben(String[][] eingabe){
        String formatusernummer = "%-" + (String.valueOf(eingabe.length - 1).length()+6) + "." + (String.valueOf(eingabe.length - 1).length()+6) + "s";
        int längsteeingabe = 6;
        for (String[] strings : eingabe) {
            for (String string : strings) {
                if (string != null && string.length() > längsteeingabe) {
                    längsteeingabe = string.length();
                }
            }
        }
        String formateingaben = "%-" + längsteeingabe + "." + längsteeingabe + "s";
        System.out.print(" ".repeat(String.valueOf(eingabe.length - 1).length() + 7));
        for (int i = 0; i < eingabe[0].length; i++) {
            System.out.print("| " + String.format(formateingaben, "kat. " + i) + " ");
        }
        System.out.println();
        for(int i = 0; i < eingabe.length; i++){
            System.out.print(String.format(formatusernummer, "spie. " + i) + " ");
            for (int j = 0; j < eingabe[0].length; j++) {
                System.out.print("| " + String.format(formateingaben, eingabe[i][j]) + " ");
            }
            System.out.println();
        }
    }
    public void kontrolliereAnfangsbuchstaben(){
        for (int i = 0; i < eingaben.length; i++) {
            for (int j = 0; j < eingaben[i].length; j++) {
                if(eingaben[i][j] != null  && !eingaben[i][j].toUpperCase().startsWith(buchstabe)){
                    eingaben[i][j] = null;
                }
            }
        }
        //eingabenausgeben();
    }
    public void kontrollenumsetzen(){
        for (int i = 0; i < eingaben.length; i++) {
            for (int j = 0; j < eingaben[i].length; j++) {
                if(kontrolliert[i][j] == null || !kontrolliert[i][j].kontrolliert){
                    eingaben[i][j] = null;
                }
            }
        }
    }
    public void eingabe(int spieler, int kategorie, String eingabe){
        eingaben[spieler][kategorie] = eingabe;
    }
    public void setInRunde (boolean inrunde){
        this.inRunde = inrunde;
    }
    public void setEingabevorbei (boolean eingabevorbei){
        this.eingabevorbei = eingabevorbei;
    }
    private void setTausche(){
        if(tausche[0][0] == null) {
            for (int kategorienummer = 0; kategorienummer < eingaben[0].length; kategorienummer++) {
                boolean fertig;
                int[] neu = new int[eingaben.length];
                for (int i = 0; i < eingaben.length; i++) {
                    tausche[i][kategorienummer] = new Tausch();
                    tausche[i][kategorienummer].alt = i;
                    neu[i] = i;
                }
                do {
                    fertig = true;
                    List<Integer> geshuffelt = new ArrayList<>(Arrays.stream(neu).boxed().toList());
                    Collections.shuffle(geshuffelt);
                    neu = geshuffelt.stream().mapToInt(i -> i).toArray();
                    for (int i = 0; i < eingaben.length; i++) {
                        tausche[i][kategorienummer].neu = neu[i];
                    }
                    for (int i = 0; i < eingaben.length; i++) {
                        if (tausche[i][kategorienummer].alt == tausche[i][kategorienummer].neu) {
                            fertig = false;
                            break;
                        }
                    }
                }
                while (!fertig);
            }
        }
    }
    public void setKontrolliert (int byuser, int kategorie, boolean wert){
        if(schongetauscht()){
            int angezeigterspieler = tausche[byuser][kategorie].neu;
            if(eingaben[angezeigterspieler][kategorie] != null) {
                kontrolliert[angezeigterspieler][kategorie] = new Kontrolle(wert);
            }
            else{
                kontrolliert[angezeigterspieler][kategorie] = new Kontrolle(false);
            }
        }
    }
    public void setPunkte(){
        if(fertigkontrolliert()) {
            for (int i = 0; i < spieleranzahl(); i++) {
                for (int j = 0; j < kategorieanzahl(); j++) {
                    punkte[i][j] = punkteprivate(i, j);
                }
            }
        }
    }

    private static class Tausch{
        public int alt;     //der wert des spielers
        public int neu;     //der wert der neu angezeigten eingabe
        public String toString(){
            return "[" + alt + " -> " + neu + "]";
        }
    }
    public static class Kontrolle{
        public boolean kontrolliert;
        public Kontrolle(boolean kontrolliert){
            this.kontrolliert = kontrolliert;
        }
        public String toString(){
            return String.valueOf(kontrolliert);
        }
    }
}
