package JDA.Games.SchereSteinPapier;

import JDA.util.Ordnen;
import jxl.Workbook;
import jxl.write.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Speichern{
    public Speichern(long user1ID, String user1Emote, String user2Emote, long user2ID, int gewinner) {
        Speicherndata.add(user1ID, user1Emote, user2Emote, user2ID, gewinner);
        int gewinnerinverse = (int) ((gewinner * 3.5) - (gewinner*gewinner*1.5));
        Speicherndata.add(user2ID, user2Emote, user1Emote, user1ID, gewinnerinverse);
    }
    public static int        spieleanzahl()           {
        Speicherndata.connect(false);
        int insgesamt = Speicherndata.getGewinner().size();
        Speicherndata.disconnect();
        return insgesamt/2;
    }
    public static int        spieleranzahl()          {
        return usernachwinrate().size();
    }
    public static long[]     häufigsteuser()          {
        long[] ausgabe;
        if(spieleranzahl() >= 3){
            ausgabe = new long[3];
        }
        else{
            ausgabe = new long[spieleranzahl()];
        }
        try {
            Speicherndata.connect(false);
            List<Long> users = new ArrayList<>(Speicherndata.getUser1());
            Speicherndata.disconnect();
            List<Long> geordnetuser = Ordnen.nachHäufigkeitlong(users);
            ausgabe[0] = geordnetuser.get(0);
            ausgabe[1] = geordnetuser.get(1);
            ausgabe[2] = geordnetuser.get(2);
            return ausgabe;
        }
        catch(IndexOutOfBoundsException ioobex){
            return ausgabe;
        }
    }
    public static long[]     besteuser()              {
        long[] ausgabe = new long[3];
        try {
            List<Long> mylist = usernachwinrate();
            ausgabe[0] = mylist.get(0);
            ausgabe[1] = mylist.get(1);
            ausgabe[2] = mylist.get(2);
            return ausgabe;
        }
        catch (IndexOutOfBoundsException ioobex){
            return ausgabe;
        }
    }
    public static long[]     schlechtesteuser()       {
        long[] ausgabe = new long[3];
        try {
            List<Long> mylist = usernachwinrate();
            ausgabe[0] = mylist.get(mylist.size() - 3);
            ausgabe[1] = mylist.get(mylist.size() - 2);
            ausgabe[2] = mylist.get(mylist.size() - 1);
            return ausgabe;
        }
        catch(IndexOutOfBoundsException ioobex){
            return ausgabe;
        }
    }
    public static String[]   häufigstezeichen()       {
        String[] häufigste = new String[3];
        try {
            Speicherndata.connect(false);
            List<String> zeichen = new ArrayList<>(Speicherndata.getUser1emote());
            Speicherndata.disconnect();
            List<String> geordnet = Ordnen.nachHäufigkeit(zeichen);
            häufigste[0] = geordnet.get(0);
            häufigste[1] = geordnet.get(1);
            häufigste[2] = geordnet.get(2);
            return häufigste;
        }
        catch(IndexOutOfBoundsException ioobex){
            return häufigste;
        }
    }
    public static int[][]    bestezeichenabsolut()    {
        int[][] ausgabe = new int[3][3];
        Speicherndata.connect(false);
        List<String> zeichen = new ArrayList<>(Speicherndata.getUser1emote());
        List<Integer> ergebnis = new ArrayList<>(Speicherndata.getGewinner());
        Speicherndata.disconnect();
        int[][] ergebnisse = new int[3][];
        ergebnisse[0] = indexesofint(ergebnis, 1);
        ergebnisse[1] = indexesofint(ergebnis, 0);
        ergebnisse[2] = indexesofint(ergebnis, 2);
        int[][] mittel = new int[3][];
        mittel[0] = indexesofString(zeichen, SchereSteinPapier.schere);
        mittel[1] = indexesofString(zeichen, SchereSteinPapier.stein);
        mittel[2] = indexesofString(zeichen, SchereSteinPapier.papier);
        for(int i = 0; i < 3; i++){
            for(int i2 = 0; i2 < 3; i2++){
                ausgabe[i][i2] = indexeszusammen(mittel[i], ergebnisse[i2]).length;
            }
        }
        return ausgabe;
    }
    public static String[]   besteszeichenabsolut()   {
        int[][] bestezeichen = bestezeichenabsolut();
        String[] ausgabe = new String[3];
        List<String> wort = new ArrayList<>();
        wort.add(SchereSteinPapier.schere);
        wort.add(SchereSteinPapier.stein);
        wort.add(SchereSteinPapier.papier);
        List<Integer> bestezeichengewinn = new ArrayList<>();
        for (int[] ints : bestezeichen) {
            bestezeichengewinn.add(ints[0]);
        }
        List<Integer> bestezeichenunentschieden = new ArrayList<>();
        for (int[] ints : bestezeichen) {
            bestezeichenunentschieden.add(ints[1]);
        }
        List<Integer> bestezeichenniederlage = new ArrayList<>();
        for (int[] ints : bestezeichen) {
            bestezeichenniederlage.add(ints[2]);
        }
        int i = 0;
        List<Integer> subbestezeichengewinn;
        List<Integer> subbestezeichenunentschieden;
        List<Integer> subbestezeichenniederlage;
        boolean kleinerals;
        while(i < bestezeichen.length){
            subbestezeichengewinn = bestezeichengewinn.subList((i+1), bestezeichen.length);
            subbestezeichenunentschieden = bestezeichenunentschieden.subList((i+1), bestezeichen.length);
            subbestezeichenniederlage = bestezeichenniederlage.subList((i+1), bestezeichen.length);
            kleinerals = false;
            for (int i2 = 0; i2 < subbestezeichengewinn.size(); i2++) {
                if (subbestezeichengewinn.get(i2) > bestezeichengewinn.get(i)) {
                    kleinerals = true;
                }
                else if((Objects.equals(subbestezeichengewinn.get(i2), bestezeichengewinn.get(i))) && (subbestezeichenunentschieden.get(i2) > bestezeichenunentschieden.get(i))){
                    kleinerals = true;
                }
                else if((Objects.equals(subbestezeichengewinn.get(i2), bestezeichengewinn.get(i))) &&
                        (Objects.equals(subbestezeichenunentschieden.get(i2), bestezeichenunentschieden.get(i))) &&
                        (subbestezeichenniederlage.get(i2) < bestezeichenniederlage.get(i))) {
                    kleinerals = true;
                }
            }
            if(kleinerals){
                bestezeichengewinn.add(bestezeichengewinn.get(i));
                bestezeichengewinn.remove(i);
                bestezeichenniederlage.add(bestezeichengewinn.get(i));
                bestezeichenniederlage.remove(i);
                bestezeichenunentschieden.add(bestezeichenunentschieden.get(i));
                bestezeichenunentschieden.remove(i);
                wort.add(wort.get(i));
                wort.remove(i);
                i--;
            }
            i++;
        }
        ausgabe[0] = wort.get(0);
        ausgabe[1] = wort.get(1);
        ausgabe[2] = wort.get(2);
        return ausgabe;
    }
    public static double[][] bestezeichenrelativ()    {
        int[][] absolut = bestezeichenabsolut();
        Speicherndata.connect(false);
        List<String> zeichen = new ArrayList<>(Speicherndata.getUser1emote());
        Speicherndata.disconnect();
        int[] nutzung = new int[3];
        nutzung[0] = wieoftString(zeichen, SchereSteinPapier.schere);
        nutzung[1] = wieoftString(zeichen, SchereSteinPapier.stein);
        nutzung[2] = wieoftString(zeichen, SchereSteinPapier.papier);
        double[][] ausgabe = new double[3][3];
        double zahl;
        for(int i = 0; i < 3; i++){
            for(int i2 = 0; i2 < 3; i2++){
                if(nutzung[i] > 0) {
                    zahl = 100d * ((double) absolut[i][i2] / nutzung[i]);
                    ausgabe[i][i2] = Math.round(zahl * 10d)/10d;
                }
                else{
                    ausgabe[i][i2] = 0d;
                }
            }
        }
        return ausgabe;
    }
    public static String[]   besteszeichenrelativ()   {
        double[][] bestezeichen = bestezeichenrelativ();
        String[] ausgabe = new String[3];
        List<String> wort = new ArrayList<>();
        wort.add(SchereSteinPapier.schere);
        wort.add(SchereSteinPapier.stein);
        wort.add(SchereSteinPapier.papier);
        List<Double> bestezeichengewinn = new ArrayList<>();
        for (double[] doubles : bestezeichen) {
            bestezeichengewinn.add(doubles[0]);
        }
        List<Double> bestezeichenunentschieden = new ArrayList<>();
        for (double[] doubles : bestezeichen) {
            bestezeichenunentschieden.add(doubles[1]);
        }
        List<Double> bestezeichenniederlage = new ArrayList<>();
        for (double[] doubles : bestezeichen) {
            bestezeichenniederlage.add(doubles[2]);
        }
        int i = 0;
        List<Double> subbestezeichengewinn;
        List<Double> subbestezeichenunentschieden;
        List<Double> subbestezeichenniederlage;
        boolean kleinerals;
        while(i < bestezeichen.length){
            subbestezeichengewinn = bestezeichengewinn.subList((i+1), bestezeichen.length);
            subbestezeichenunentschieden = bestezeichenunentschieden.subList((i+1), bestezeichen.length);
            subbestezeichenniederlage = bestezeichenniederlage.subList((i+1), bestezeichen.length);
            kleinerals = false;
            for (int i2 = 0; i2 < subbestezeichengewinn.size(); i2++) {
                if (subbestezeichengewinn.get(i2) > bestezeichengewinn.get(i)) {
                    kleinerals = true;
                }
                else if((Objects.equals(subbestezeichengewinn.get(i2), bestezeichengewinn.get(i))) && (subbestezeichenunentschieden.get(i2) > bestezeichenunentschieden.get(i))){
                    kleinerals = true;
                }
                else if((Objects.equals(subbestezeichengewinn.get(i2), bestezeichengewinn.get(i))) &&
                        (Objects.equals(subbestezeichenunentschieden.get(i2), bestezeichenunentschieden.get(i))) &&
                        (subbestezeichenniederlage.get(i2) > bestezeichenniederlage.get(i))) {
                    kleinerals = true;
                }

            }
            if(kleinerals){
                bestezeichengewinn.add(bestezeichengewinn.get(i));
                bestezeichengewinn.remove(i);
                bestezeichenniederlage.add(bestezeichengewinn.get(i));
                bestezeichenniederlage.remove(i);
                bestezeichenunentschieden.add(bestezeichenunentschieden.get(i));
                bestezeichenunentschieden.remove(i);
                wort.add(wort.get(i));
                wort.remove(i);
                i--;
            }
            i++;
        }
        ausgabe[0] = wort.get(0);
        ausgabe[1] = wort.get(1);
        ausgabe[2] = wort.get(2);
        return ausgabe;
    }

    public static boolean     wardabei            (long user)             {

        return usernachwinrate().contains(user);
    }
    public static double      siegesquote         (long user)             {
        double ausgabe;
        Speicherndata.connect(false);
        List<Long> users = new ArrayList<>(Speicherndata.getUser1());
        List<Integer> ergebnis = new ArrayList<>(Speicherndata.getGewinner());
        Speicherndata.disconnect();
        int[] stellen = indexesoflong(users, user);
        List<Integer> eigenepartien = unterlisteint(ergebnis, stellen);
        ausgabe = (double) wieoftint(eigenepartien, 1) / eigenepartien.size();
        ausgabe *= 100;
        ausgabe = Math.round(ausgabe * 100d)/100d;
        return ausgabe;
    }
    public static double      unentschiedenquote  (long user)             {
        double ausgabe = 0d;
        Speicherndata.connect(false);
        List<Long> users = new ArrayList<>(Speicherndata.getUser1());
        List<Integer> ergebnis = new ArrayList<>(Speicherndata.getGewinner());
        Speicherndata.disconnect();
        int[] stellen = indexesoflong(users, user);
        List<Integer> eigenepartien = unterlisteint(ergebnis, stellen);
        if(wieoftint(eigenepartien, 0) > 0) {
            ausgabe = (double) wieoftint(eigenepartien, 0) / eigenepartien.size();
            ausgabe *= 100;
        }

        ausgabe = Math.round(ausgabe * 100d)/100d;
        return ausgabe;
    }
    public static double      niederlagenquote    (long user)             {
        double ausgabe;
        Speicherndata.connect(false);
        List<Long> users = new ArrayList<>(Speicherndata.getUser1());
        List<Integer> ergebnis = new ArrayList<>(Speicherndata.getGewinner());
        Speicherndata.disconnect();
        int[] stellen = indexesoflong(users, user);
        List<Integer> eigenepartien = unterlisteint(ergebnis, stellen);
            ausgabe = (double) wieoftint(eigenepartien, 2) / eigenepartien.size();
            ausgabe *= 100;

        ausgabe = Math.round(ausgabe * 100d)/100d;
        return ausgabe;
    }
    public static int         position            (long user)             {
        return (usernachwinrate().indexOf(user)+1);
    }
    public static int         anzahlspiele        (long user)             {
        Speicherndata.connect(false);
        List<Long>    users  = new ArrayList<>(Speicherndata.getUser1());
        Speicherndata.disconnect();
        int ausgabe = 0;
        if(users.contains(user)) {
            int[] stellen = indexesoflong(users, user);
            ausgabe = stellen.length;
        }
        return ausgabe;
    }
    public static int         anzahlgegner        (long user)             {

        return gegnernachwinabsolut(user).size();
    }
    public static long[]      häufigstegegner     (long user)             {
        long[] ausgabe = new long[3];
        try {
            Speicherndata.connect(false);
            List<Long> gegner = new ArrayList<>(Speicherndata.getUser2());
            List<Long> users  = new ArrayList<>(Speicherndata.getUser1());
            Speicherndata.disconnect();
            int[] stellen = indexesoflong(users, user);
            gegner = unterlistlong(gegner, stellen);
            List<Long> geordnetuser = Ordnen.nachHäufigkeitlong(gegner);
            ausgabe[0] = geordnetuser.get(0);
            ausgabe[1] = geordnetuser.get(1);
            ausgabe[2] = geordnetuser.get(2);
            return ausgabe;
        }
        catch(IndexOutOfBoundsException ioobex){
            return ausgabe;
        }
    }
    private static List<Long> gegnernachwinrate   (long user)             {
        Speicherndata.connect(false);
        List<Long> Users = new ArrayList<>(Speicherndata.getUser2());
        List<Long> selbst = new ArrayList<>(Speicherndata.getUser1());
        Speicherndata.disconnect();
        int[] stellen = indexesoflong(selbst, user);
        Users = unterlistlong(Users, stellen);
        Users = Ordnen.clean(Users);
        List<Double> winrate = new ArrayList<>();
        for (Long j : Users) {
            winrate.add(quotegegenrelativ(user, j)[1]);
        }
        List<Double> unentschiedenrate = new ArrayList<>();
        for (Long j : Users) {
            unentschiedenrate.add(quotegegenrelativ(user, j)[0]);
        }
        List<Long> zwischen = new ArrayList<>(Users);
        int i = 0;
        List<Double> sublistwin;
        List<Double> sublistunentschieden;
        long zwischenspeicher;
        double zwischenwinrate;
        double zwischenunentschiedenrate;
        boolean kleinerals;
        while(i < Users.size()){
            sublistwin = winrate.subList((i+1), (Users.size()));
            sublistunentschieden = unentschiedenrate.subList((i+1), (Users.size()));
            kleinerals = false;
            for (int i2 = 0; i2 < sublistwin.size(); i2++) {
                if (sublistwin.get(i2) > winrate.get(i)) {
                    kleinerals = true;
                }
                else if((Objects.equals(sublistwin.get(i2), winrate.get(i))) && (sublistunentschieden.get(i2) > unentschiedenrate.get(i))){
                    kleinerals = true;
                }
            }
            if(kleinerals){
                zwischenspeicher = zwischen.get(i);
                zwischen.remove(i);
                zwischen.add(zwischenspeicher);
                zwischenwinrate = winrate.get(i);
                winrate.remove(i);
                winrate.add(zwischenwinrate);
                zwischenunentschiedenrate = unentschiedenrate.get(i);
                unentschiedenrate.remove(i);
                unentschiedenrate.add(zwischenunentschiedenrate);
                i--;
            }
            i++;
        }
        Users = zwischen;
        return Users;
    }
    public static long[]      bestegegnerrelativ  (long user)             {
        List<Long> gegnernachwinrate = gegnernachwinrate(user);
        long[] ausgabe = new long[3];
        try{
            ausgabe[0] = gegnernachwinrate.get(0);
            ausgabe[1] = gegnernachwinrate.get(1);
            ausgabe[2] = gegnernachwinrate.get(2);
            return ausgabe;
        }
        catch (IndexOutOfBoundsException ioofbex){
            return ausgabe;
        }
    }
    public static long[]      schlegegnerrelativ  (long user)             {
        List<Long> gegnernachwinrate = gegnernachwinrate(user);
        long[] ausgabe = new long[1];
        try{
            if(gegnernachwinrate.size() >= 3) {
                ausgabe = new long[3];
                ausgabe[0] = gegnernachwinrate.get(gegnernachwinrate.size() - 3);
                ausgabe[1] = gegnernachwinrate.get(gegnernachwinrate.size() - 2);
                ausgabe[2] = gegnernachwinrate.get(gegnernachwinrate.size() - 1);
            }
            else if(gegnernachwinrate.size() == 2){
                ausgabe = new long[2];
                ausgabe[0] = gegnernachwinrate.get(0);
                ausgabe[1] = gegnernachwinrate.get(1);
            }
            else{
                ausgabe[0] = gegnernachwinrate.get(0);
            }
            return ausgabe;
        }
        catch (IndexOutOfBoundsException ioofbex){
            return ausgabe;
        }
    }
    private static List<Long> gegnernachwinabsolut(long user)             {
        Speicherndata.connect(false);
        List<Long> selbst = new ArrayList<>(Speicherndata.getUser1());
        List<Long> gegner = new ArrayList<>(Speicherndata.getUser2());
        Speicherndata.disconnect();
        gegner = unterlistlong(gegner, indexesoflong(selbst, user));
        gegner = Ordnen.clean(gegner);
        List<Integer> siegrate = new ArrayList<>();
        List<Integer> unentschiedenrate = new ArrayList<>();
        List<Integer> niederlagenrate = new ArrayList<>();
        for (Long aLong : gegner) {
            int[] dazu = quotegegenxabsolut(user, aLong);
            siegrate.add(dazu[1]);
            unentschiedenrate.add(dazu[0]);
            niederlagenrate.add(dazu[2]);
        }
        int i = 0;
        List<Long> zwischen = new ArrayList<>(gegner);
        List<Integer> siegsublist;
        List<Integer> unentschiedensublist;
        List<Integer> niederlagensublist;
        boolean move;
        while(i < gegner.size()){
            siegsublist = siegrate.subList((i+1), siegrate.size());
            unentschiedensublist = unentschiedenrate.subList((i+1), unentschiedenrate.size());
            niederlagensublist = niederlagenrate.subList((i+1), niederlagenrate.size());
            move = false;
            for(int i2 = 0; i2 < siegsublist.size(); i2++){
                if(siegsublist.get(i2) > siegrate.get(i)){
                    move = true;
                }
                else if(Objects.equals(siegsublist.get(i2), siegrate.get(i))
                        && unentschiedensublist.get(i2) > unentschiedenrate.get(i)){
                    move = true;
                }
                else if(Objects.equals(siegsublist.get(i2), siegrate.get(i))
                        && Objects.equals(unentschiedensublist.get(i2), unentschiedenrate.get(i))
                        && niederlagensublist.get(i2) < niederlagenrate.get(i)){
                    move = true;
                }
            }
            if(move){
                siegrate.add(siegrate.get(i));
                siegrate.remove(i);
                niederlagenrate.add(niederlagenrate.get(i));
                niederlagenrate.remove(i);
                unentschiedenrate.add(unentschiedenrate.get(i));
                unentschiedenrate.remove(i);
                zwischen.add(zwischen.get(i));
                zwischen.remove(i);
                i--;
            }
            i++;
        }
        gegner = zwischen;
        return gegner;
    }
    public static int[][]     bestezeichenabsolut (long user)             {
        int[][] ausgabe = new int[3][3];
        Speicherndata.connect(false);
        List<Long> selbst = new ArrayList<>(Speicherndata.getUser1());
        List<String> zeichen = new ArrayList<>(Speicherndata.getUser1emote());
        List<Integer> ergebnis = new ArrayList<>(Speicherndata.getGewinner());
        Speicherndata.disconnect();
        zeichen = unterlistString(zeichen, indexesoflong(selbst, user));
        ergebnis = unterlisteint(ergebnis, indexesoflong(selbst, user));
        int[][] ergebnisse = new int[3][];
        ergebnisse[0] = indexesofint(ergebnis, 1);
        ergebnisse[1] = indexesofint(ergebnis, 0);
        ergebnisse[2] = indexesofint(ergebnis, 2);
        int[][] mittel = new int[3][];
        mittel[0] = indexesofString(zeichen, SchereSteinPapier.schere);
        mittel[1] = indexesofString(zeichen, SchereSteinPapier.stein);
        mittel[2] = indexesofString(zeichen, SchereSteinPapier.papier);
        for(int i = 0; i < 3; i++){
            for(int i2 = 0; i2 < 3; i2++){
                ausgabe[i][i2] = indexeszusammen(mittel[i], ergebnisse[i2]).length;
            }
        }
        return ausgabe;
    }
    public static String[]    besteszeichenabsolut(long user)             {
        int[][] bestezeichen = bestezeichenabsolut(user);
        String[] ausgabe = new String[3];
        List<String> wort = new ArrayList<>();
        wort.add(SchereSteinPapier.schere);
        wort.add(SchereSteinPapier.stein);
        wort.add(SchereSteinPapier.papier);
        List<Integer> bestezeichengewinn = new ArrayList<>();
        for (int[] ints : bestezeichen) {
            bestezeichengewinn.add(ints[0]);
        }
        List<Integer> bestezeichenunentschieden = new ArrayList<>();
        for (int[] ints : bestezeichen) {
            bestezeichenunentschieden.add(ints[1]);
        }
        List<Integer> bestezeichenniederlage = new ArrayList<>();
        for (int[] ints : bestezeichen) {
            bestezeichenniederlage.add(ints[2]);
        }
        int i = 0;
        List<Integer> subbestezeichengewinn;
        List<Integer> subbestezeichenunentschieden;
        List<Integer> subbestezeichenniederlage;
        boolean kleinerals;
        while(i < bestezeichen.length){
            subbestezeichengewinn = bestezeichengewinn.subList((i+1), bestezeichen.length);
            subbestezeichenunentschieden = bestezeichenunentschieden.subList((i+1), bestezeichen.length);
            subbestezeichenniederlage = bestezeichenniederlage.subList((i+1), bestezeichen.length);
            kleinerals = false;
            for (int i2 = 0; i2 < subbestezeichengewinn.size(); i2++) {
                if (subbestezeichengewinn.get(i2) > bestezeichengewinn.get(i)) {
                    kleinerals = true;
                }
                else if((Objects.equals(subbestezeichengewinn.get(i2), bestezeichengewinn.get(i))) && (subbestezeichenunentschieden.get(i2) > bestezeichenunentschieden.get(i))){
                    kleinerals = true;
                }
                else if((Objects.equals(subbestezeichengewinn.get(i2), bestezeichengewinn.get(i))) &&
                        (Objects.equals(subbestezeichenunentschieden.get(i2), bestezeichenunentschieden.get(i))) &&
                        (subbestezeichenniederlage.get(i2) > bestezeichenniederlage.get(i))) {
                    kleinerals = true;
                }

            }
            if(kleinerals){
                bestezeichengewinn.add(bestezeichengewinn.get(i));
                bestezeichengewinn.remove(i);
                bestezeichenniederlage.add(bestezeichengewinn.get(i));
                bestezeichenniederlage.remove(i);
                bestezeichenunentschieden.add(bestezeichenunentschieden.get(i));
                bestezeichenunentschieden.remove(i);
                wort.add(wort.get(i));
                wort.remove(i);
                i--;
            }
            i++;
        }
        ausgabe[0] = wort.get(0);
        ausgabe[1] = wort.get(1);
        ausgabe[2] = wort.get(2);
        return ausgabe;
    }
    public static double[][]  bestezeichenrelativ (long user)             {
        int[][] absolut = bestezeichenabsolut(user);
        Speicherndata.connect(false);
        List<Long> user1 = new ArrayList<>(Speicherndata.getUser1());
        List<String> zeichen = new ArrayList<>(Speicherndata.getUser1emote());
        Speicherndata.disconnect();
        int[] indexes = indexesoflong(user1, user);
        zeichen = unterlistString(zeichen, indexes);
        int[] nutzung = new int[3];
        nutzung[0] = wieoftString(zeichen, SchereSteinPapier.schere);
        nutzung[1] = wieoftString(zeichen, SchereSteinPapier.stein);
        nutzung[2] = wieoftString(zeichen, SchereSteinPapier.papier);
        double[][] ausgabe = new double[3][3];
        double zahl;
        for(int i = 0; i < 3; i++){
            for(int i2 = 0; i2 < 3; i2++){
                if(nutzung[i] > 0) {
                    zahl = 100 * ((double) absolut[i][i2] / nutzung[i]);
                    ausgabe[i][i2] = Math.round(zahl * 10d)/10d;
                }
                else{
                    ausgabe[i][i2] = 0d;
                }
            }
        }
        return ausgabe;
    }
    public static String[]    besteszeichenrelativ(long user)             {
        double[][] bestezeichen = bestezeichenrelativ(user);
        String[] ausgabe = new String[3];
        List<String> wort = new ArrayList<>();
        wort.add(SchereSteinPapier.schere);
        wort.add(SchereSteinPapier.stein);
        wort.add(SchereSteinPapier.papier);
        List<Double> bestezeichengewinn = new ArrayList<>();
        for (double[] doubles : bestezeichen) {
            bestezeichengewinn.add(doubles[0]);
        }
        List<Double> bestezeichenunentschieden = new ArrayList<>();
        for (double[] doubles : bestezeichen) {
            bestezeichenunentschieden.add(doubles[1]);
        }
        List<Double> bestezeichenniederlage = new ArrayList<>();
        for (double[] doubles : bestezeichen) {
            bestezeichenniederlage.add(doubles[2]);
        }
        int i = 0;
        List<Double> subbestezeichengewinn;
        List<Double> subbestezeichenunentschieden;
        List<Double> subbestezeichenniederlage;
        boolean kleinerals;
        while(i < bestezeichen.length){
            subbestezeichengewinn = bestezeichengewinn.subList((i+1), bestezeichen.length);
            subbestezeichenunentschieden = bestezeichenunentschieden.subList((i+1), bestezeichen.length);
            subbestezeichenniederlage = bestezeichenniederlage.subList((i+1), bestezeichen.length);
            kleinerals = false;
            for (int i2 = 0; i2 < subbestezeichengewinn.size(); i2++) {
                if (subbestezeichengewinn.get(i2) > bestezeichengewinn.get(i)) {
                    kleinerals = true;
                }
                else if((Objects.equals(subbestezeichengewinn.get(i2), bestezeichengewinn.get(i))) && (subbestezeichenunentschieden.get(i2) > bestezeichenunentschieden.get(i))){
                    kleinerals = true;
                }
                else if((Objects.equals(subbestezeichengewinn.get(i2), bestezeichengewinn.get(i))) &&
                        (Objects.equals(subbestezeichenunentschieden.get(i2), bestezeichenunentschieden.get(i))) &&
                        (subbestezeichenniederlage.get(i2) > bestezeichenniederlage.get(i))) {
                    kleinerals = true;
                }

            }
            if(kleinerals){
                bestezeichengewinn.add(bestezeichengewinn.get(i));
                bestezeichengewinn.remove(i);
                bestezeichenniederlage.add(bestezeichengewinn.get(i));
                bestezeichenniederlage.remove(i);
                bestezeichenunentschieden.add(bestezeichenunentschieden.get(i));
                bestezeichenunentschieden.remove(i);
                wort.add(wort.get(i));
                wort.remove(i);
                i--;
            }
            i++;
        }
        ausgabe[0] = wort.get(0);
        ausgabe[1] = wort.get(1);
        ausgabe[2] = wort.get(2);
        return ausgabe;
    }

    public static int         anzahlbegegnungen      (long user1, long user2) {
        int[] ausgabe = user1uuser2(user1, user2);
        return ausgabe.length;
    }
    public static int[]       siegesverteilungabsolut(long user1, long user2) {
        Speicherndata.connect(false);
        List<Integer> ergebnis = new ArrayList<>(Speicherndata.getGewinner());
        Speicherndata.disconnect();
        int[] stellen = user1uuser2(user1, user2);
        ergebnis = unterlisteint(ergebnis, stellen);
        int[] ausgabe = new int[3];
        ausgabe[0] = wieoftint(ergebnis, 0);
        ausgabe[1] = wieoftint(ergebnis, 1);
        ausgabe[2] = wieoftint(ergebnis, 2);
        return ausgabe;
    }
    public static long        bestervonbeiden        (long user1, long user2) {
        int[] verteilung = siegesverteilungabsolut(user1, user2);
        long ausgabe;
        if(verteilung[1] == verteilung[2]) {
            ausgabe = 0;
        }
        else if(verteilung[1] > verteilung[2]){
            ausgabe = user1;
        }
        else{
            ausgabe = user2;
        }
        return ausgabe;
    }
    public static String[]    häufigstezeichen       (long user1, long user2) {
        Speicherndata.connect(false);
        List<String> zeichen1 = new ArrayList<>(Speicherndata.getUser1emote());
        List<String> zeichen2 = new ArrayList<>(Speicherndata.getUser2emote());
        Speicherndata.disconnect();
        int[] stellen = user1uuser2(user1,user2);
        zeichen1 = unterlistString(zeichen1, stellen);
        zeichen2 = unterlistString(zeichen2, stellen);
        List<String> gesamt = new ArrayList<>(zeichen1);
        gesamt.addAll(zeichen2);
        gesamt = Ordnen.nachHäufigkeit(gesamt);
        return gesamt.toArray(new String[3]);
    }
    public static int[][]     bestezeichenabsolut    (long user1, long user2) {
        int[][] ausgabe = new int[3][3];
        Speicherndata.connect(false);
        List<String> zeichen = new ArrayList<>(Speicherndata.getUser1emote());
        List<Integer> ergebnis = new ArrayList<>(Speicherndata.getGewinner());
        Speicherndata.disconnect();
        int[] stellen = user1uuser2(user1, user2);
        zeichen = unterlistString(zeichen, stellen);
        ergebnis = unterlisteint(ergebnis, stellen);
        int[][] ergebnisse = new int[3][];
        ergebnisse[0] = indexesofint(ergebnis, 1);
        ergebnisse[1] = indexesofint(ergebnis, 0);
        ergebnisse[2] = indexesofint(ergebnis, 2);
        int[][] mittel = new int[3][];
        mittel[0] = indexesofString(zeichen, SchereSteinPapier.schere);
        mittel[1] = indexesofString(zeichen, SchereSteinPapier.stein);
        mittel[2] = indexesofString(zeichen, SchereSteinPapier.papier);
        for(int i = 0; i < 3; i++){
            for(int i2 = 0; i2 < 3; i2++){
                ausgabe[i][i2] = indexeszusammen(mittel[i], ergebnisse[i2]).length;
            }
        }
        Speicherndata.connect(false);
        zeichen = new ArrayList<>(Speicherndata.getUser1emote());
        ergebnis = new ArrayList<>(Speicherndata.getGewinner());
        Speicherndata.disconnect();
        stellen = user1uuser2(user2, user1);
        zeichen = unterlistString(zeichen, stellen);
        ergebnis = unterlisteint(ergebnis, stellen);
        ergebnisse = new int[3][];
        ergebnisse[0] = indexesofint(ergebnis, 1);
        ergebnisse[1] = indexesofint(ergebnis, 0);
        ergebnisse[2] = indexesofint(ergebnis, 2);
        mittel = new int[3][];
        mittel[0] = indexesofString(zeichen, SchereSteinPapier.schere);
        mittel[1] = indexesofString(zeichen, SchereSteinPapier.stein);
        mittel[2] = indexesofString(zeichen, SchereSteinPapier.papier);
        for(int i = 0; i < 3; i++){
            for(int i2 = 0; i2 < 3; i2++){
                ausgabe[i][i2] += indexeszusammen(mittel[i], ergebnisse[i2]).length;
            }
        }
        return ausgabe;
    }
    public static String[]    besteszeichenabsolut   (long user1, long user2) {
        int[][] bestezeichen = bestezeichenabsolut(user1, user2);
        String[] ausgabe = new String[3];
        List<String> wort = new ArrayList<>();
        wort.add(SchereSteinPapier.schere);
        wort.add(SchereSteinPapier.stein);
        wort.add(SchereSteinPapier.papier);
        List<Integer> bestezeichengewinn = new ArrayList<>();
        for (int[] ints : bestezeichen) {
            bestezeichengewinn.add(ints[0]);
        }
        List<Integer> bestezeichenunentschieden = new ArrayList<>();
        for (int[] ints : bestezeichen) {
            bestezeichenunentschieden.add(ints[1]);
        }
        List<Integer> bestezeichenniederlage = new ArrayList<>();
        for (int[] ints : bestezeichen) {
            bestezeichenniederlage.add(ints[2]);
        }
        int i = 0;
        List<Integer> subbestezeichengewinn;
        List<Integer> subbestezeichenunentschieden;
        List<Integer> subbestezeichenniederlage;
        boolean kleinerals;
        while(i < bestezeichen.length){
            subbestezeichengewinn = bestezeichengewinn.subList((i+1), bestezeichen.length);
            subbestezeichenunentschieden = bestezeichenunentschieden.subList((i+1), bestezeichen.length);
            subbestezeichenniederlage = bestezeichenniederlage.subList((i+1), bestezeichen.length);
            kleinerals = false;
            for (int i2 = 0; i2 < subbestezeichengewinn.size(); i2++) {
                if (subbestezeichengewinn.get(i2) > bestezeichengewinn.get(i)) {
                    kleinerals = true;
                }
                else if((Objects.equals(subbestezeichengewinn.get(i2), bestezeichengewinn.get(i))) && (subbestezeichenunentschieden.get(i2) > bestezeichenunentschieden.get(i))){
                    kleinerals = true;
                }
                else if((Objects.equals(subbestezeichengewinn.get(i2), bestezeichengewinn.get(i))) &&
                        (Objects.equals(subbestezeichenunentschieden.get(i2), bestezeichenunentschieden.get(i))) &&
                        (subbestezeichenniederlage.get(i2) < bestezeichenniederlage.get(i))) {
                    kleinerals = true;
                }
            }
            if(kleinerals){
                bestezeichengewinn.add(bestezeichengewinn.get(i));
                bestezeichengewinn.remove(i);
                bestezeichenniederlage.add(bestezeichengewinn.get(i));
                bestezeichenniederlage.remove(i);
                bestezeichenunentschieden.add(bestezeichenunentschieden.get(i));
                bestezeichenunentschieden.remove(i);
                wort.add(wort.get(i));
                wort.remove(i);
                i--;
            }
            i++;
        }
        ausgabe[0] = wort.get(0);
        ausgabe[1] = wort.get(1);
        ausgabe[2] = wort.get(2);
        return ausgabe;
    }

    public static List<Long> usernachwinrate()            {
        Speicherndata.connect(false);
        List<Long> Users = new ArrayList<>(Speicherndata.getUser1());
        Speicherndata.disconnect();
        Users = Ordnen.clean(Users);
        List<Double> winrate = new ArrayList<>();
        for (Long aLong : Users) {
            winrate.add(siegesquote(aLong));
        }
        List<Double> unentschiedenrate = new ArrayList<>();
        for (Long user : Users) {
            unentschiedenrate.add(unentschiedenquote(user));
        }
        List<Long> zwischen = new ArrayList<>(Users);
        List<Double> winrate2 = new ArrayList<>(winrate);
        List<Double> unentschiedenrate2 = new ArrayList<>(unentschiedenrate);
        int i = 0;
        List<Double> sublistwin;
        List<Double> sublistunentschieden;
        long zwischenspeicher;
        double zwischenwinrate;
        double zwischenunentschiedenrate;
        boolean kleinerals;
        while(i < Users.size()){
            sublistwin = winrate2.subList((i+1), (Users.size()));
            sublistunentschieden = unentschiedenrate2.subList((i+1), (Users.size()));
            kleinerals = false;
            for (int i2 = 0; i2 < sublistwin.size(); i2++) {
                if (sublistwin.get(i2) > winrate2.get(i)) {
                    kleinerals = true;
                }
                else if((Objects.equals(sublistwin.get(i2), winrate2.get(i))) && (sublistunentschieden.get(i2) > unentschiedenrate2.get(i))){
                    kleinerals = true;
                }
            }
            if(kleinerals){
                zwischenspeicher = zwischen.get(i);
                zwischen.remove(i);
                zwischen.add(zwischenspeicher);
                zwischenwinrate = winrate2.get(i);
                winrate2.remove(i);
                winrate2.add(zwischenwinrate);
                zwischenunentschiedenrate = unentschiedenrate2.get(i);
                unentschiedenrate2.remove(i);
                unentschiedenrate2.add(zwischenunentschiedenrate);
                i--;
            }
            i++;
        }
        Users = zwischen;
        return Users;
    }

    private static int[] user1uuser2 (long user1, long user2) {
        Speicherndata.connect(false);
        List<Long> users1 = new ArrayList<>(Speicherndata.getUser1());
        List<Long> users2 = new ArrayList<>(Speicherndata.getUser2());
        Speicherndata.disconnect();
        List<Integer> ausgabe = new ArrayList<>();
        for(int i = 0; i < users1.size(); i++){
            if((Objects.equals(users1.get(i), user1)) && (Objects.equals(users2.get(i), user2))){
                ausgabe.add(i);
            }
        }
        return ausgabe.stream().mapToInt(i->i).toArray();
    }
    private static int[] quotegegenxabsolut (long user1, long user2) {
        Speicherndata.connect(false);
        List<Integer> gewinner = new ArrayList<>(Speicherndata.getGewinner());
        Speicherndata.disconnect();
        int[] stellen = user1uuser2(user1, user2);
        List<Integer> bestimmteergebnisse = unterlisteint(gewinner, stellen);
        int[] ausgabe = new int[3];
        ausgabe[0] = wieoftint(bestimmteergebnisse, 0);
        ausgabe[1] = wieoftint(bestimmteergebnisse, 1);
        ausgabe[2] = wieoftint(bestimmteergebnisse, 2);
        return ausgabe;
    }
    public static double[] quotegegenrelativ (long user1, long user2) {
        int[] absolut = quotegegenxabsolut(user1, user2);
        double[] ausgabe = new double[3];
        int wieoft = absolut[0] + absolut[1] + absolut[2];
        ausgabe[0] = (double)absolut[0]/wieoft;
        ausgabe[1] = (double)absolut[1]/wieoft;
        ausgabe[2] = (double)absolut[2]/wieoft;
        return ausgabe;
    }
    
    private static int wieoftint    (List<Integer> eingabe, int referenz){
        int ausgabe = 0;
        for(int j : eingabe){
            if(Objects.equals(j, referenz)){
                ausgabe++;
            }
        }
        return ausgabe;
    }
    private static int wieoftString (List<String> eingabe, String referenz){
        int ausgabe = 0;
        for(String j : eingabe){
            if(j.equals(referenz)){
                ausgabe++;
            }
        }
        return ausgabe;
    }

    private static List<Integer> unterlisteint   (List<Integer> eingabe, int[] stellen){
        List<Integer> ausgabe = new ArrayList<>();
        for (int j : stellen) {
            ausgabe.add(eingabe.get(j));
        }
        return ausgabe;
    }
    private static List<String>  unterlistString (List<String> eingabe, int[] stellen){
        List<String> ausgabe = new ArrayList<>();
        for (int j : stellen) {
            ausgabe.add(eingabe.get(j));
        }
        return ausgabe;
    }
    private static List<Long>    unterlistlong   (List<Long> eingabe, int[] stellen){
        List<Long> ausgabe = new ArrayList<>();
        for (int j : stellen) {
            ausgabe.add(eingabe.get(j));
        }
        return ausgabe;
    }

    private static int[] indexesofString (List<String> mylist, String eingabe){
        List<Integer> listeausgabe = new ArrayList<>();
        for(int i = 0; i < mylist.size(); i++){
            if(mylist.get(i).equals(eingabe)){
                listeausgabe.add(i);
            }
        }
        return listeausgabe.stream().mapToInt(i->i).toArray();
    }
    private static int[] indexesoflong   (List<Long> mylist, Long eingabe){
        List<Integer> listeausgabe = new ArrayList<>();
        for(int i = 0; i < mylist.size(); i++){
            if(Objects.equals(mylist.get(i), eingabe)){
                listeausgabe.add(i);
            }
        }
        return listeausgabe.stream().mapToInt(i->i).toArray();
    }
    private static int[] indexesofint    (List<Integer> mylist, int eingabe){
        List<Integer> listeausgabe = new ArrayList<>();
        for(int i = 0; i < mylist.size(); i++){
            if(mylist.get(i).equals(eingabe)){
                listeausgabe.add(i);
            }
        }
        return listeausgabe.stream().mapToInt(i->i).toArray();
    }

    private static int[] indexeszusammen (int[] indexes1, int[] indexes2){
        List<Integer> ausgabe = new ArrayList<>();
        for (int j : indexes1) {
            boolean dabei = false;
            for (int i : indexes2) {
                if (j == i) {
                    dabei = true;
                    break;
                }
            }
            if (dabei) {
                ausgabe.add(j);
            }
        }
        return ausgabe.stream().mapToInt(i->i).toArray();
    }
}
class Speicherndata {
    private static final String filename = "SSPstats.xls";
    public static WritableWorkbook file;

    private static final List<Long> user1 = new ArrayList<>();
    private static final List<String> user1emote = new ArrayList<>();
    private static final List<String> user2emote = new ArrayList<>();
    private static final List<Long> user2 = new ArrayList<>();
    private static final List<Integer> gewinner = new ArrayList<>();

    public static void connect(boolean println){
        try {
            File myFile = new File(filename);
            if (!myFile.exists()) {
                System.out.println("exists: " + myFile.exists());
            }
            Workbook workbook = Workbook.getWorkbook(myFile);
            file = Workbook.createWorkbook(myFile, workbook);
            WritableSheet sheet = file.getSheet(0);
            if (println) {
                System.out.println("Numberofsheets: " + file.getNumberOfSheets());
            }
            user1.addAll(Speichernexcel.spaltenwertelong(sheet, 1, 1));
            user1emote.addAll(Speichernexcel.spaltenwerteString(sheet, 2, 1));
            user2emote.addAll(Speichernexcel.spaltenwerteString(sheet, 3, 1));
            user2.addAll(Speichernexcel.spaltenwertelong(sheet, 4, 1));
            gewinner.addAll(Speichernexcel.spaltenwerteint(sheet, 5, 1));
            sheet.addCell(Speichernexcel.feld(0, 0, ""));
        }
        catch(Exception ex){
            System.out.println("Fehler in connect");
            ex.printStackTrace();
        }
    }
    public static List<Long> getUser1(){
        return user1;
    }
    public static List<Long> getUser2(){

        return user2;
    }
    public static List<String> getUser1emote(){

        return user1emote;
    }
    public static List<String> getUser2emote(){

        return user2emote;
    }
    public static List<Integer> getGewinner(){

        return gewinner;
    }
    public static void disconnect(){
        try {
            file.write();
            file.close();
            user1.clear();
            user1emote.clear();
            user2.clear();
            user2emote.clear();
            gewinner.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void add(long user1ID, String user1Emote, String user2Emote, long user2ID, int gewinner){
        connect(false);
        addgame(user1ID, user1Emote, user2Emote, user2ID, gewinner);
        disconnect();
    }
    public static void addgame(long user1ID, String user1Emote, String user2Emote, long user2ID, int gewinner) {
        WritableSheet sheet = file.getSheet(0);
        int stelle = Speichernexcel.nächsterfreiiny(sheet, 1);
        try {
            sheet.addCell(Speichernexcel.feld(1, stelle, String.valueOf(user1ID)));
            sheet.addCell(Speichernexcel.feld(2, stelle, user1Emote));
            sheet.addCell(Speichernexcel.feld(3, stelle, user2Emote));
            sheet.addCell(Speichernexcel.feld(4, stelle, String.valueOf(user2ID)));
            sheet.addCell(Speichernexcel.feld(5, stelle, String.valueOf(gewinner)));
            file.write();
            user1.add(user1ID);
            user2.add(user2ID);
            user1emote.add(user1Emote);
            user2emote.add(user2Emote);
            Speicherndata.gewinner.add(gewinner);
        }
        catch(WriteException wex){
            System.out.println("Sorry, kann hier nicht schreiben");
        }
        catch (IOException ioex){
            System.out.println("Sorry, IOException " + ioex.getLocalizedMessage());
        }
    }
    public static void ausgeben(){
        System.out.println("Länge:      " + user1.size() + " | " + user1emote.size() + " | " + user2emote.size() + " | " + user2.size() + " | " + gewinner.size());
        System.out.println(" User1     : " + user1);
        System.out.println(" User1emote: " + user1emote);
        System.out.println(" User2emote: " + user2emote);
        System.out.println(" User2     : " + user2);
        System.out.println(" Gewinner  : " + gewinner);
    }
}
class Speichernexcel {
    public static Label feld(int x, int y, String text){
        WritableFont times16font = new WritableFont(WritableFont.ARIAL, 10);
        WritableCellFormat times16format = new WritableCellFormat(times16font);
        return new Label(x, y, text, times16format);
    }
    public static List<Long> spaltenwertelong (WritableSheet sheet, int spalte, int ab){
        List<Long> ausgabe = new ArrayList<>();
        List<String> eingabeString = spaltenwerteString(sheet, spalte, ab);
        String inhalt = "leerer wert";
        for(int i = 0; i < eingabeString.size(); i++) {
            try {
                    inhalt = eingabeString.get(i);
                    inhalt = Speichernexcel.replacelong(inhalt);
                    ausgabe.add(Long.parseLong(inhalt));
            } catch (NumberFormatException nfex) {
                    System.out.println("Fehler: Die Zelle " + spalte + "|" + (ab + i) + " enthält einen ungültigen Wert: " + inhalt + " (" + inhalt.length() + ")");
            }
        }
        return ausgabe;
    }
    public static List<Integer> spaltenwerteint (WritableSheet sheet, int spalte, int ab){
        List<Integer> ausgabe = new ArrayList<>();
        List<String> eingabeString = spaltenwerteString(sheet, spalte, ab);
        String inhalt = "leerer wert";
        for(int i = 0; i < eingabeString.size(); i++) {
            try {
                inhalt = eingabeString.get(i);
                inhalt = Speichernexcel.replacelong(inhalt);
                ausgabe.add(Integer.parseInt(inhalt));
            } catch (NumberFormatException nfex) {
                System.out.println("Fehler: Die Zelle " + spalte + "|" + (ab + i) + " enthält einen ungültigen Wert: " + inhalt + " (" + inhalt.length() + ")");

            }
        }
        return ausgabe;
    }
    public static List<String> spaltenwerteString (WritableSheet sheet, int spalte, int ab){
        List<String> ausgabe = new ArrayList<>();
        int i = 0;
        String inhalt = "null";
        while(!inhalt.equals("")){
            inhalt = sheet.getCell(spalte, (ab + i)).getContents();
            i++;
            ausgabe.add(inhalt);
        }
        ausgabe.remove(ausgabe.size()-1);
        return ausgabe;
    }
    public static int nächsterfreiiny(WritableSheet sheet, int spalte){
        int i = 0;
        while(!(sheet.getCell(spalte, i).getContents().equals(""))){
            i++;
        }
        return i;
    }
    public static String replacelong (String eingabe){
        String inhalt =  eingabe;
        inhalt = inhalt.replace(".0", "");
        inhalt = inhalt.replace(".","");
        if(inhalt.contains("E")) {
            String hinterE = inhalt.split("E")[1];
            inhalt = inhalt.replace("E" + hinterE, "");
        }
        return inhalt;
    }
}