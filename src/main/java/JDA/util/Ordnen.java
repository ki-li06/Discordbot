package JDA.util;

import java.util.ArrayList;
import java.util.List;

public class Ordnen {
    public static List<String> generell (List<String> Eingabe){
        List<String> ausgeben = new ArrayList<>(Eingabe);
        for(int i = 0; i < Eingabe.size(); i++){
            List<String> sublist = Eingabe.subList(0, i);
            if(sublist.contains(Eingabe.get(i))){
                ausgeben.set(i, null);
            }
        }
        for (int i = (ausgeben.size())-1; i >= 0; i--) {
            if(null == ausgeben.get(i)){
                ausgeben.remove(i);
            }
        }
        List<Integer> häufigkeit = new ArrayList<>();
        for (String s : ausgeben) {
            int h = 0;
            for (String value : Eingabe) {
                if (value.equals(s)) {
                    h++;
                }
            }
            häufigkeit.add(h);
        }
        List<Integer> häufigkeit2 = new ArrayList<>(häufigkeit);
        List<String> ausgeben2 = new ArrayList<>(ausgeben);
        int i = 0;
        while(i < ausgeben.size()){
            List<Integer> sublist = häufigkeit2.subList((i+1), (häufigkeit.size()));
            boolean kleinerals = false;
            for (Integer integer : sublist) {
                if (integer > häufigkeit2.get(i)) {
                    kleinerals = true;
                    break;
                }
            }
            if(kleinerals){
                int zwischenspeicher = häufigkeit2.get(i);
                häufigkeit2.remove(i);
                häufigkeit2.add(zwischenspeicher);
                String zwischenspeichers = ausgeben2.get(i);
                ausgeben2.remove(i);
                ausgeben2.add(zwischenspeichers);
                i--;
            }
            i++;
        }
        häufigkeit = häufigkeit2;
        ausgeben = ausgeben2;
        List<String> ausgabe = new ArrayList<>();
        for(int i1 = 0; i1 < ausgeben.size(); i1++){
            for(int i2 = 0; i2 < häufigkeit.get(i1); i2++){
                ausgabe.add(ausgeben.get(i1));
            }
        }
        return ausgabe;
    }
    public static List<String> nachHäufigkeit(List<String> eingabe){
        List<String> Eingabe = generell(eingabe);
        List<String> ausgeben = new ArrayList<>(Eingabe);
        for(int i = 0; i < Eingabe.size(); i++){
            List<String> sublist = Eingabe.subList(0, i);
            if(sublist.contains(Eingabe.get(i))){
                ausgeben.set(i, null);
            }
        }
        for (int i = (ausgeben.size())-1; i >= 0; i--) {
            if(null == ausgeben.get(i)){
                ausgeben.remove(i);
            }
        }
        return ausgeben;
    }
    public static List<Integer> generellint (List<Integer> Eingabe) {
        List<Integer> Ausgabe = new ArrayList<>();
        List<String> Eingabeordnen = new ArrayList<>();
        List<String> Ausgabeordnen;
        for (Integer integer : Eingabe) {
            Eingabeordnen.add(String.valueOf(integer));
        }
        Ausgabeordnen = generell(Eingabeordnen);
        for (String s : Ausgabeordnen) {
            Ausgabe.add(Integer.parseInt(s));
        }
        return Ausgabe;
    }
    public static List<Integer> nachHäufigkeitint(List<Integer> eingabe){
        List<Integer> Eingabe = generellint(eingabe);
        List<Integer> ausgeben = new ArrayList<>(Eingabe);
        for(int i = 0; i < Eingabe.size(); i++){
            List<Integer> sublist = Eingabe.subList(0, i);
            if(sublist.contains(Eingabe.get(i))){
                ausgeben.set(i, null);
            }
        }
        for (int i = (ausgeben.size())-1; i >= 0; i--) {
            if(ausgeben.get(i) == null){
                ausgeben.remove(i);
            }
        }
        return ausgeben;
    }
    public static List<Long> generelllong (List<Long> Eingabe) {
        List<Long> Ausgabe = new ArrayList<>();
        List<String> Eingabeordnen = new ArrayList<>();
        List<String> Ausgabeordnen;
        for (Long integer : Eingabe) {
            Eingabeordnen.add(String.valueOf(integer));
        }
        Ausgabeordnen = generell(Eingabeordnen);
        for (String s : Ausgabeordnen) {
            Ausgabe.add(Long.parseLong(s));
        }
        return Ausgabe;
    }
    public static List<Long> nachHäufigkeitlong(List<Long> eingabe){
        List<Long> Eingabe = generelllong(eingabe);
        List<Long> ausgeben = new ArrayList<>(Eingabe);
        for(int i = 0; i < Eingabe.size(); i++){
            List<Long> sublist = Eingabe.subList(0, i);
            if(sublist.contains(Eingabe.get(i))){
                ausgeben.set(i, null);
            }
        }
        for (int i = (ausgeben.size())-1; i >= 0; i--) {
            if(ausgeben.get(i) == null){
                ausgeben.remove(i);
            }
        }
        return ausgeben;
    }
    public static List<Long> größe (List<Long> eingabe){
        List<Long> ausgabe = new ArrayList<>(eingabe);
        List<Long> zwischen = new ArrayList<>(eingabe);
        int i = 0;
        while(i < ausgabe.size()){
            List<Long> sublist = zwischen.subList((i+1), (ausgabe.size()));
            boolean kleinerals = false;
            for (Long zahl : sublist) {
                if (zahl > zwischen.get(i)) {
                    kleinerals = true;
                    break;
                }
            }
            if(kleinerals){
                long zwischenspeicher = zwischen.get(i);
                zwischen.remove(i);
                zwischen.add(zwischenspeicher);
                i--;
            }
            i++;
        }
        ausgabe = zwischen;
        return ausgabe;
    }
    public static List<Long> clean(List<Long> mehrfach){
        List<Long> ausgabe = new ArrayList<>(mehrfach);
        for(int i = 0; i < ausgabe.size(); i++){
            List<Long> sublist = ausgabe.subList(0, i);
            if(sublist.contains(mehrfach.get(i))){
                ausgabe.set(i, null);
            }
        }
        for(int i = (ausgabe.size() - 1); i >= 0; i--){
            if(ausgabe.get(i) == null){
                ausgabe.remove(i);
            }
        }
        return ausgabe;
    }
}
