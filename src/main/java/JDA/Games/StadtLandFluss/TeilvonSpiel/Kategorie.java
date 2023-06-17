package JDA.Games.StadtLandFluss.TeilvonSpiel;

import java.util.ArrayList;
import java.util.List;

public class Kategorie{
    private String name;
    private String beschreibung;
    public Kategorie(String name, String beschreibung){
        this.name = name;
        this.beschreibung = beschreibung;
    }
    public String getBeschreibung() {
        return beschreibung;
    }
    public String getName() {
        return name;
    }
    public String toString(){
        return "K:" + getName() + "";
    }

    public static Kategorie kategoriebyName (String name){
        List<Kategorie> kategories = new ArrayList<>(kategorienSonderAuswahl());
        kategories.addAll(kategorienStadtLand());
        for (Kategorie k :
                kategories) {
            if(k.getName().equals(name)){
                return k;
            }
        }
        return null;
    }
    public static List<Kategorie> kategorienStadtLand(){
        List<Kategorie> ausgabe = new ArrayList<>();
        ausgabe.add(new Kategorie("Stadt", "richtige Stadt, also kein einfacher Ort"));
        ausgabe.add(new Kategorie("Land", "offiziell anerkannter Staat"));
        return ausgabe;
    }
    public static List<Kategorie> kategorienSonderAuswahl(){
        List<Kategorie> ausgabe = new ArrayList<>();
        ausgabe.add(new Kategorie("Gewässer",
                "deutsche Schreibweise; alle Ozeane in Kurzfassung (Pazifischer Ozean -> Pazifik)"));
        ausgabe.add(new Kategorie("Name",
                "Vorname; kein Doppelname (Anna-Lena -> Anna, Lena ODER Annalena)"));
        ausgabe.add(new Kategorie("Lebensmittel",
                "voller Name (Cashew -> Cashewnuss); keine Produkte von Tieren wie Schweinsbraten"));
        ausgabe.add(new Kategorie("Beruf",
                "keine Züchter (Giraffenzüchter -> Tierzüchter); keine Sportler (Fußballer -> Profisportler)"));
        ausgabe.add(new Kategorie("Sportart",
                "kein Kampfkunst-Unterart wie Aikido; kein lokalen Arten wie American Football -> Football"));
        ausgabe.add(new Kategorie("Sportler",
                "Nachname (Format \"Ronaldo, Cristiano\"); keine Schachspieler und E-Sportler"));
        ausgabe.add(new Kategorie("Promi",
                "Nachname (Format \"Kardashian, Kim\"); bei gebräuchlicher Verwendung Künstlername"));
        ausgabe.add(new Kategorie("Musiker",
                "Nachname (Format \"Sheeran, Ed\"); bei gebräuchlicher Verwendung Künstlername"));
        ausgabe.add(new Kategorie("Poltiker",
                "Nachname (Format \"Merkel, Angela\"); deutsche Schreibweise (Zelenskyy -> Selenskyj"));
        ausgabe.add(new Kategorie("Lied",
                "ausgeschriebener offizieller Name"));
        ausgabe.add(new Kategorie("Marke",
                "noch existierende Firmen (z.B. nicht Air-Berlin)"));
        ausgabe.add(new Kategorie("Videospiel",
                "ausgeschriebener Name (COD -> Call of Duty); keine Versionen (Total War: Warhammer III -> Total War"));
        return ausgabe;
    }
    public static int Kategorienummer (String kategorie){
        return switch (kategorie) {
            case "stadt" -> 1;
            case "land" -> 2;
            default -> kategorie.charAt(9) - 48;
        };
    }
    public static boolean isKateogrie(String s){
        return kategorienStadtLand().stream().map(Kategorie::getName).toList().contains(s) ||
                kategorienSonderAuswahl().stream().map(Kategorie::getName).toList().contains(s);
    }
}