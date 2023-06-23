package JDA.Games.StadtLandFluss.Aktionen;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static JDA.core.Main.*;
import static JDA.util.Lists.wertedoppelt;
import static JDA.util.Random.randomString;

import static JDA.Games.StadtLandFluss.Spiel.*;
import JDA.Games.StadtLandFluss.Spiel;
import JDA.Games.StadtLandFluss.RundenEnden.RundenendeI;
import JDA.util.TabelleFile;
import JDA.Games.StadtLandFluss.TeilvonSpiel.Kategorie;
import JDA.Games.StadtLandFluss.TeilvonSpiel.Lobby;
import JDA.Games.StadtLandFluss.TeilvonSpiel.Runde;




/*
        Diese Klasse ist sowohl für die Vorlage als auch Verarbeitung der Commands
     */
public class Commands {
    public static final String CommandMain = "slf";
    /**
     * erstellt eine Command Vorlage (CommandData) für die Commands
     * @return die Vorlage
     */
    public static CommandData vorlage (){
        CommandData commandData = new CommandData(CommandMain, "Spiele Stadt-Land-Fluss mit deinen Freunden");
        List<SubcommandData> subcommandDataList = new ArrayList<>();
        //der new Command
        subcommandDataList.add(new SubcommandData("new", "erstelle ein neues Spiel Stadt-Land-Fluss"));
        //der bann Command
        SubcommandData bann = new SubcommandData("bann", "blocke eine Person aus der Lobby");
        bann.addOption(OptionType.USER, "user", "der User, der geblockt werden soll", true);
        subcommandDataList.add(bann);
        //der entbann Command
        SubcommandData entbann = new SubcommandData("entbann", "entblocke eine Person aus der Lobby");
        entbann.addOption(OptionType.USER, "user", "der User, der entblockt werden soll", true);
        subcommandDataList.add(entbann);
        //der setsettingscommand
        subcommandDataList.add(setsettings());
        //der eingeben command
        SubcommandData eingeben = new SubcommandData("eingeben", "gebe deine Wörter ein");
        eingeben.addOption(OptionType.STRING, "kategorie1", "Gebe hier den Wert für die Kategorie \"Stadt\" ein", false);
        eingeben.addOption(OptionType.STRING, "kategorie2", "Gebe hier den Wert für die Kategorie \"Land\" ein", false);
        for(int i = 3; i <= maximaleAnzahlKategorien; i++){
            eingeben.addOption(OptionType.STRING, "kategorie" + i, "Gebe hier deinen Wert für Kategorie " + i + " ein. (-> Tabelle)", false);
        }
        subcommandDataList.add(eingeben);
        //der showerte command
        SubcommandData showwerte = new SubcommandData("showwerte", "zeige dir deinen eingegeben werte an");
        commandData.addSubcommands(showwerte);
        //der startround command
        SubcommandData startgame = new SubcommandData("startround", "starte eine Runde");
        commandData.addSubcommands(startgame);
        commandData.addSubcommands(subcommandDataList);
        //der korrigieren command
        SubcommandData korrigieren = new SubcommandData("korrigieren", "korrigiere die Eingaben deiner Mitspieler");
        for (int i = 1; i <= maximaleAnzahlKategorien; i++) {
            korrigieren.addOption(OptionType.BOOLEAN, "eingabe" + i, "bestätige ob die eingabe" + i + " zur kategorie passt", false);
        }
        korrigieren.addOption(OptionType.BOOLEAN, "alle_eingaben", "bestätige alle eingaben", false);
        commandData.addSubcommands(korrigieren);
        SubcommandData help = new SubcommandData("help", "zeige dir Hilfe für diese Command-Reihe an");
        OptionData kategorie = new OptionData(OptionType.STRING, "kategorie", "zu welcher Kategorie willst du genauere Infos erhalten?", false);
        Stream.concat(Kategorie.kategorienSonderAuswahl().stream(), Kategorie.kategorienStadtLand().stream()).forEach(
                i-> kategorie.addChoice(i.getName(), i.getName())
        );
        help.addOptions(kategorie);
        OptionData rundenende = new OptionData(OptionType.STRING, "runden_ende", "zu welchem Modus für das Ende einer Runde willst Infos erhalten?", false);
        RundenendeI.Rundenenden().forEach(i->rundenende.addChoice(i.getName(), i.getName()));
        help.addOptions(rundenende);
        commandData.addSubcommands(help);

        return commandData;
    }
    public static SubcommandData setsettings(){
        SubcommandData setsettings = new SubcommandData("setsettings", "setze die Spiel-Einstellungen");
        OptionData rundenanzahl = new OptionData(OptionType.INTEGER,"runden-anzahl", "Wie viele Runden möchtet ihr spielen?", true);
        int[] rundenmöglichkeiten = new int[]{1, 2, 3, 4 , 5};
        for(int i : rundenmöglichkeiten){
            rundenanzahl.addChoice(String.valueOf(i), i);
        }
        setsettings.addOptions(rundenanzahl);
        OptionData rundenende = new OptionData(OptionType.STRING, "runden-ende", "Wie oder nach welcher Zeit soll eine Runde beendet werden?", true);
        for(RundenendeI s : RundenendeI.Rundenenden()){
            rundenende.addChoice(s.getName(), s.getName());
        }
        setsettings.addOptions(rundenende);
        OptionData kategorie = new OptionData(OptionType.STRING, "kategorie", "Welche Kategorie (außer Stadt & Land) soll dein Spiel beinhalten?", false);
        for(Kategorie k : Kategorie.kategorienSonderAuswahl()){
            kategorie.addChoice(k.getName(), k.getName());
        }
        for(int i = 3; i <= 7; i++){
            OptionData add = new OptionData(kategorie.getType(), kategorie.getName(), kategorie.getDescription(), kategorie.isRequired());
            add.addChoices(kategorie.getChoices());
            add.setName(add.getName() + i);
            setsettings.addOptions(add);
        }
        return setsettings;
    }


    /**
     * verarbeitet die eingegeben Commands
     */
    public static void handle(SlashCommandEvent event){
        System.out.println("StadtLandFluss handle - " + event.getSubcommandName());
        switch (Objects.requireNonNull(event.getSubcommandName())){
            case "new":
                if(!imSpiel(event.getUser())) {
                    event.reply("Eine Lobby für eine Runde Stadt-Land-Fluss wird erstellt ...").setEphemeral(true).queue();
                    EmbedBuilder embed = new EmbedBuilder();
                    String myid = randomString();
                    embed = embed.setTitle(Lobby.titel)
                            .setThumbnail(bild)
                            .setDescription(myid)
                            .setColor(Color.CYAN)
                            .addField("Beigetreten", "• " + event.getMember().getUser().getAsTag(), false)
                            .setFooter("StadtLandFluss by Hackerman", Objects.requireNonNull(event.getJDA().getUserById(ID)).
                                    getAvatarUrl());
                    event.getChannel().sendMessageEmbeds(embed.build()).setActionRow(Buttons.lobbybuttons()).queue();
                    Message m = event.getChannel().getHistory().retrievePast(1).complete().get(0);
                    while(  m.getEmbeds().size() != 1 ||
                            !Objects.requireNonNull(m.getEmbeds().get(0).getDescription()).equals(myid)){
                        m = event.getChannel().getHistory().retrievePast(1).complete().get(0);
                    }
                    embed.setDescription("Dies ist eine Lobby für ein Spiel Stadt-Land-Fluss. \nSie wurde von " + Objects.requireNonNull(event.getMember()).
                            getAsMention() + " erstellt.");
                    m.editMessageEmbeds(embed.build()).queue();
                    new Spiel(event.getMember().getUser(), m);
                }
                break;
            case "bann":
                if(isadminofagame(event.getUser())){
                    Spiel s = getSpielbyUser(event.getUser());
                    User tobann = Objects.requireNonNull(event.getOption("user")).getAsUser();
                    System.out.println(event.getUser().getAsTag() + " will "  + tobann.getAsTag() +
                            " bannnen");
                    if(!tobann.equals(event.getUser())) {
                        if (s.lobby.getBeigetreten().contains(tobann) && (!s.lobby.getGebannt().contains(tobann))) {
                            MessageEmbed m = s.getLobbymessage().getEmbeds().get(0);
                            EmbedBuilder embed = new EmbedBuilder(m);
                            List<MessageEmbed.Field> fields = new ArrayList<>(embed.getFields());
                            String text1 = fields.get(0).getValue();
                            text1 = text1.replace(
                                    text1.substring(text1.indexOf(tobann.getAsTag()) - 3, text1.indexOf(tobann.getAsTag()) + tobann.getAsTag().length())
                                    , "");
                            embed = embed.clearFields().addField("Beigetreten", text1, false);
                            if(s.lobby.getGebannt().size() == 0){
                                embed = embed.addField("Gebannt", Lobby.auflistungszeichen + tobann.getAsTag(), false);
                            }
                            else{
                                String text2 = fields.get(1).getValue();
                                text2 += "\n" + Lobby.auflistungszeichen + tobann.getAsTag();
                                embed = embed.addField("Gebannt", text2, false);
                            }
                            s.getLobbymessage().editMessageEmbeds(embed.build()).queue();
                            s.removeMitspieler(tobann);
                            s.addgebannt(tobann);
                            System.out.println("Mitspieler: " + s.lobby);
                            System.out.println("Gebannt:    " + s.lobby.getGebannt());
                            event.reply("Du hast " + tobann.getAsMention() + " gebannt").setEphemeral(true).queue();

                        }
                        else if(!s.lobby.getGebannt().contains(tobann)){
                            MessageEmbed m = s.getLobbymessage().getEmbeds().get(0);
                            EmbedBuilder embed = new EmbedBuilder(m);
                            if(s.lobby.getGebannt().size() == 0){
                                embed =
                                        embed.addField("Gebannt", Lobby.auflistungszeichen + tobann.getAsTag(), false);
                            }
                            else{
                                String neuertext = embed.getFields().get(1).getValue();
                                MessageEmbed.Field field1 = embed.getFields().get(0);
                                embed = embed.clearFields().addField(field1);
                                neuertext += "\n" + Lobby.auflistungszeichen + tobann.getAsTag();
                                embed =
                                        embed.addField("Gebannt", neuertext, false);
                            }
                            s.getLobbymessage().editMessageEmbeds(embed.build()).queue();
                            s.addgebannt(tobann);
                            event.reply("Du hast " + tobann.getAsMention() + " gebannt").setEphemeral(true).queue();
                            System.out.println(tobann.getAsTag() + " wurde gebannt");
                        }
                    }
                    else{
                        event.reply("Du kannst nicht dich selbst bannen. " +
                                "Wenn du die Lobby verlassen möchtest, klicke auf **" +
                                        Buttons.showedleavetext + "**"
                                ).setEphemeral(true).queue();
                    }
                }
                break;
            case "entbann":
                if(isadminofagame(event.getUser())){
                    Spiel s = getSpielbyUser(event.getUser());
                    User entbann = Objects.requireNonNull(event.getOption("user")).getAsUser();
                    System.out.println(event.getUser().getAsTag() + " will " + entbann.getAsTag() +
                            " entbannnen");
                    if (!entbann.equals(event.getUser())) {
                        if(s.lobby.getGebannt().contains(entbann)){
                            MessageEmbed m = s.getLobbymessage().getEmbeds().get(0);
                            EmbedBuilder embed = new EmbedBuilder(m);
                            List<MessageEmbed.Field> fields = new ArrayList<>(embed.getFields());
                            String text2 = fields.get(1).getValue();
                            embed = embed.clearFields().addField(fields.get(0));
                            if(s.lobby.getGebannt().size() > 1){
                                try {
                                    text2 = text2.replace(
                                            text2.substring(text2.indexOf(entbann.getAsTag()) - 3, text2.indexOf(entbann.getAsTag()) + entbann.getAsTag().length())
                                            , "");
                                }
                                catch (IndexOutOfBoundsException ioobe){
                                    text2 = text2.substring(text2.indexOf("\n"));
                                }
                                embed = embed.addField("Gebannt", text2, false);
                            }
                            s.getLobbymessage().editMessageEmbeds(embed.build()).queue();
                            s.removegebannt(entbann);
                            event.reply("Du hast " + entbann.getAsMention() + " entbannt").setEphemeral(true).queue();
                        }
                    }
                    else{
                        event.reply("du kannst nicht dich selbst bannen").setEphemeral(true).queue();
                    }
                }
                break;
            case "setsettings":
                if(isadminofagame(event.getUser())){
                    Spiel s = getSpielbyUser(event.getUser());
                    System.out.println("handle settsettings 1");
                    System.out.println("open: " + (!s.lobby.getopen()));
                    System.out.println("finishedsettings: " + (!s.finishedsettings()));
                    System.out.println("same channel: " + s.getLobbymessage().getChannel().getId().equals(event.getChannel().getId()));
                    if(!s.lobby.getopen() && !s.finishedsettings() && s.getLobbymessage().getChannel().getId().equals(event.getChannel().getId())){
                        System.out.println("handle settsettings 2");
                        OptionMapping ra = event.getOption("runden-anzahl");
                        OptionMapping re = event.getOption("runden-ende");
                        assert ra != null && re != null;
                        RundenendeI rundenendeI = RundenendeI.getRundenEndebyname(re.getAsString());
                        assert rundenendeI != null;
                        System.out.println("Rundenanzahl: " + ra.getAsLong());
                        System.out.println("Rundenende: " + rundenendeI.getName() + " (" + rundenendeI.getErklärung() + ")");
                        List<Kategorie> kategories =
                                new ArrayList<>(event.getOptions().subList(2, event.getOptions().size()).stream().map(i -> Kategorie.kategoriebyName(i.getAsString())).toList());
                        if(!wertedoppelt(kategories)) {
                            event.reply("Du hast die Einstellungen festgelegt").setEphemeral(true).queue();
                            System.out.println("neue kategorien: " + kategories);
                            s.setRundenanzahl((int) ra.getAsLong());
                            s.setRundenende(RundenendeI.getRundenEndebyname(re.getAsString()));
                            s.setKategorien(kategories);
                            String myid = randomString();
                            EmbedBuilder embed = new EmbedBuilder();
                            embed = embed
                                    .setTitle("Stadt Land Fluss - Spieleinstellungen")
                                    .setThumbnail(bild)
                                    .setColor(Color.CYAN)
                                    .setDescription(myid)
                                    .setFooter("StadtLandFluss by Hackerman", Objects.requireNonNull(event.getJDA().getUserById(ID)).getAvatarUrl());
                            embed = embed.addField("Wie viele Runden gibt es?", "Es gibt **" + s.getRundenanzahl() + "** Runden.", false);
                            embed = embed.addField("Wie wird eine Runden beendet?",
                                    "Dies erfolgt nach dem Prinzip **" + s.getRundenende().getName() + "**: " + s.getRundenende().getErklärung(),
                                    false);
                            String textkategorien = "";
                            for (Kategorie k : s.getKategorien()) {
                                textkategorien += "\n" + Lobby.auflistungszeichen + k.getName();
                            }
                            textkategorien += "\n*bei genaueren Erklärungen nutze /" + CommandMain + " help kategorien*";
                            embed = embed.addField("Welche Kategorien werden gespielt?", textkategorien, false);
                            event.getChannel().sendMessageEmbeds(embed.build()).
                                    setActionRow(Buttons.finishsettings()).queue();
                            Message m = event.getTextChannel().getHistory().retrievePast(1).complete().get(0);
                            while (!Objects.requireNonNull(m.getEmbeds().get(0).getDescription()).equals(myid)) {
                                m = event.getTextChannel().getHistory().retrievePast(1).complete().get(0);
                            }
                            embed.setDescription("Dies sind die Einstellungen für dieses Spiel. Sie wurden von " + s.getHost().getAsMention() + " festgelegt.\n" +
                                    "Du kannst sie mit dem gleichen Comamnd nochmal ändern. Wenn du fertig bist, drücke auf *Finish Settings*");
                            //System.out.println("description1: " + embed.getDescriptionBuilder());
                            m.editMessageEmbeds(embed.build()).queue();

                            delay(100L);
                            m = updated(m);
                            s.setSettingmessage(m);
                            //System.out.println("description2: " + m.getEmbeds().get(0).getDescription());
                        }
                        else{
                            event.reply("Du kannst keine Kategorien doppelt auswählen.").setEphemeral(true).queue();
                        }
                    }
                }
                break;
            case "startround":
                if(isadminofagame(event.getUser())){
                    System.out.println("handle - start round");
                    Spiel s = getSpielbyUser(event.getUser());
                    if(s.finishedsettings() && !s.getaktuelleRunde().isInRunde()){
                        event.reply("Du hast eine neue Runde gestartet").setEphemeral(true).queue();
                        s.setInrunde(true);
                        String myid = randomString();
                        EmbedBuilder embed = new EmbedBuilder();
                        embed = embed.setThumbnail(bild)
                                .setColor(Color.CYAN)
                                .setTitle("Stadt Land Fluss - Runde")
                                .setDescription(myid)
                                .setFooter("StadtLandFluss by Hackerman",
                                        Objects.requireNonNull(event.getJDA().getUserById(ID)).getAvatarUrl())
                                .addField(
                                        "Gesuchter Anfangsbuchstabe",
                                        "Der gesuchte Buchstabe ist:    :regional_indicator_"  + s.getaktuelleRunde().getBuchstabe().toLowerCase() + ":",
                                        false)
                                .addField(
                                        "Fortschritt der Runde",
                                        s.getRundenende().fortschritt(s),
                                        false)
                                .addField(s.getFinishedsettingsmessage().getEmbeds().get(0).getFields().get(3))
                                .addField(
                                        "Spieleinstellungen",
                                        s.getFinishedsettingsmessage().getJumpUrl(),
                                        false);
                        String description = "Dies ist die " + (s.getRunde() + 1) + ". Runde des von " +
                                s.getHost().getAsMention() + " gehostetem Spiels. Es gibt insgesamt " +
                                s.getRundenanzahl() + " Runden.";
                        event.getChannel().sendMessageEmbeds(embed.build()).queue();
                        Message m = event.getTextChannel().getHistory().retrievePast(1).complete().get(0);
                        while(m.getEmbeds().size() != 1 ||
                                !Objects.requireNonNull(m.getEmbeds().get(0).getDescription()).equals(myid)){
                            m = event.getTextChannel().getHistory().retrievePast(1).complete().get(0);
                        }
                        embed.setDescription(description);
                        m.editMessageEmbeds(embed.build()).queue();
                        while(Objects.requireNonNull((m = updated(m)).getEmbeds().get(0).getDescription()).equals(myid)){
                            delay(5);
                        }
                        s.setRundeStartMessage(m);
                        s.setRundenStartZeit(lastupdate(m));
                        s.warteAufRundenEnde();
                    }
                }
                break;
            case "eingeben":
                if(imSpiel(event.getUser())){
                    Spiel s = getSpielbyUser(event.getUser());
                    System.out.println("handle - eingeben");
                    if(s.getInRunde() && !s.getaktuelleRunde().isEingabevorbei() && event.getOptions().size()>0){
                        List<String> kategorien = new ArrayList<>(event.getOptions().stream().map(OptionMapping::getName).toList());
                        List<String> eingaben = new ArrayList<>(event.getOptions().stream().map(OptionMapping::getAsString).toList());
                        for (int i = 0; i < event.getOptions().size(); i++) {
                            if(kategorien.get(i).charAt(9)-49<s.getKategorien().size()) {
                                Kategorie k = s.getKategorien().get(kategorien.get(i).charAt(9) - 49);
                                s.eingabe(event.getUser(), k, eingaben.get(i));
                            }
                        }
                        System.out.println("neuer fortschritt: " + s.getRundenende().fortschritt(s));
                        String[] eingabenuser = s.getEingaben(event.getUser(), s.getRunde());
                        String[] kategorienspiel = s.getKategorien().stream().map(Kategorie::getName).toArray(String[]::new);
                        StringBuilder rückgabe = new StringBuilder("Deine bisherigen Eingaben: \n");
                        for (int i = 0; i < eingabenuser.length; i++) {
                            if(i > 0){
                                rückgabe.append(" | ");
                            }
                            String dazu = eingabenuser[i];
                            if(dazu == null){
                                dazu = "-";
                            }
                            rückgabe
                                    .append("__")
                                    .append(kategorienspiel[i])
                                    .append("__")
                                    .append(": ")
                                    .append(dazu);
                        }
                        event.reply(rückgabe.toString()).setEphemeral(true).queue();
                        //s.getaktuelleRunde().eingabenausgeben();
                    }
                }
                break;
            case "showwerte":
                if(imSpiel(event.getUser())){
                    Spiel s = getSpielbyUser(event.getUser());
                    System.out.println("handle - showwerte");
                    if(s.getInRunde()){
                        String[] eingabenuser = s.getEingaben(event.getUser(), s.getRunde());
                        String[] kategorienspiel = s.getKategorien().stream().map(Kategorie::getName).toArray(String[]::new);
                        StringBuilder rückgabe = new StringBuilder("Deine bisherigen Eingaben: \n");
                        for (int i = 0; i < eingabenuser.length; i++) {
                            if(i > 0){
                                rückgabe.append(" | ");
                            }
                            String dazu = eingabenuser[i];
                            if(dazu == null){
                                dazu = "-";
                            }
                            rückgabe
                                    .append("__")
                                    .append(kategorienspiel[i])
                                    .append("__")
                                    .append(": ")
                                    .append(dazu);
                        }
                        event.reply(rückgabe.toString()).setEphemeral(true).queue();
                    }
                }
                break;
            case "korrigieren":
                if(imSpiel(event.getUser())){
                    Spiel s = getSpielbyUser(event.getUser());
                    if(s.getInRunde() && s.getaktuelleRunde().isEingabevorbei()){
                        if(event.getOptions().size() == 0){
                            String[] zukontrollierendewerte = s.getaktuelleRunde().anzeigendeWerte(s.getSpieler().indexOf(event.getUser()));
                            String[] kategorien = s.getKategorien().stream().map(Kategorie::getName).toArray(String[]::new);
                            StringBuilder text = new StringBuilder();
                            for (int i = 0; i < zukontrollierendewerte.length; i++) {
                                text.append("**eingabe").append(i + 1).append(":**  ");
                                text.append("__").append(kategorien[i]).append("__ ->  ");
                                if(zukontrollierendewerte[i] != null) {
                                    text.append(zukontrollierendewerte[i]);
                                }
                                else{
                                    text.append("*-keine Eingabe-*");
                                }
                                text.append("\n");
                            }
                            event.reply(text.toString()).setEphemeral(true).queue();
                        }
                        else{
                            System.out.println("wants correct something");
                            List<Integer> nummerstocorrect;
                            List<Boolean> values;
                            if(event.getOptions().stream().map(OptionMapping::getName).toList().contains("alle_eingaben")){
                                nummerstocorrect = new ArrayList<>(IntStream.range(0, s.getKategorien().size()).boxed().toList());
                                boolean valuealle_eingaben = Objects.requireNonNull(event.getOption("alle_eingaben")).getAsBoolean();
                                values = new ArrayList<>(Arrays.stream(new Boolean[s.getKategorien().size()]).map(i -> valuealle_eingaben).toList());
                            }
                            else {
                                nummerstocorrect = new ArrayList<>(event.getOptions().stream().map(i -> i.getName().charAt(7) - 49).toList());
                                values = new ArrayList<>(event.getOptions().stream().map(OptionMapping::getAsBoolean).toList());
                            }
                            //System.out.println("nummern: " + nummerstocorrect);
                            //System.out.println("values: " + values);
                            for (int i = 0; i < nummerstocorrect.size(); i++) {
                                if(nummerstocorrect.get(i) < s.getKategorien().size()){
                                    s.correct(event.getUser(), nummerstocorrect.get(i), values.get(i));
                                }
                            }
                            Runde.Kontrolle[] kontrollen = s.getaktuelleRunde().getGemachteKontrollen(s.getSpieler().indexOf(event.getUser()));
                            String[] zukontrollierendewerte = s.getaktuelleRunde().anzeigendeWerte(s.getSpieler().indexOf(event.getUser()));
                            String[] kategorien = s.getKategorien().stream().map(Kategorie::getName).toArray(String[]::new);
                            StringBuilder text = new StringBuilder();
                            for (int i = 0; i < zukontrollierendewerte.length; i++) {
                                text.append("**eingabe").append(i + 1).append(":**  ");
                                text.append("__").append(kategorien[i]).append("__ ->  ");
                                if(zukontrollierendewerte[i] != null) {
                                    text.append(zukontrollierendewerte[i]);
                                }
                                else{
                                    text.append("*-keine Eingabe-*");
                                }
                                text.append("  --> ");
                                if(kontrollen[i] != null){
                                    if(kontrollen[i].kontrolliert){
                                        text.append("gültig");
                                    }
                                    else{
                                        text.append("ungültig");
                                    }
                                }
                                else{
                                    text.append("*-noch keine Kontrolle-*");
                                }
                                text.append("\n");
                            }   //zeige die bisherigen kontrollen an
                            if(s.getaktuelleRunde().fertigkontrolliert(s.getSpieler().indexOf(event.getUser()))){
                                text.append("Du hast alles fertig kontrolliert, nur deine Mitspieler noch nicht. Warte auf diese!");
                            }
                            event.reply(text.toString()).setEphemeral(true).queue();
                            if(s.getaktuelleRunde().fertigkontrolliert()){
                                s.getaktuelleRunde().kontrollenumsetzen();
                                s.getaktuelleRunde().setPunkte();
                                System.out.println("aktuellerunde: " + s.getRunde());
                                System.out.println("rundenanzahl: " + s.getRundenanzahl());
                                System.out.println("ende runde");
                                User own = event.getJDA().retrieveUserById(ID).complete();
                                String myid = randomString();
                                EmbedBuilder embed = new EmbedBuilder()
                                        .setDescription(myid)
                                        .setTitle("Stadt Land Fluss - Runde")
                                        .setColor(Color.CYAN)
                                        .setThumbnail(bild)
                                        .setFooter("StadtLandFluss by Hackerman", own.getAvatarUrl())
                                        .addField("Was ist passiert?", "Nachdem alle Eingaben aller Spieler kontrolliert wurden," +
                                                " ist diese Runde vorbei. Deshalb werden hier jetzt die Ergebnisse der ersten Runde angezeigt. " +
                                                "Um die nächste Runde zu starten, muss der Host den Command */" + CommandMain + " startround* ausführen.", false)
                                        .addField("Eingaben", "*Eingabe (Punkte)*\n*waiting...*", false);
                                event.getChannel().sendMessageEmbeds(embed.build()).queue();
                                Message sent = event.getChannel().getHistory().retrievePast(1).complete().get(0);
                                while (sent.getEmbeds().size() != 1 ||
                                        !Objects.requireNonNull((sent = event.getChannel().getHistory().retrievePast(1).complete().get(0))
                                                .getEmbeds().get(0).getDescription()).equals(myid)){
                                    delay(10);
                                }
                                String description = "Das war die " + (s.getRunde()+1) + ". Runde des von " + s.getHost().getAsMention() + " gehosteten Spiels. " +
                                        "Insgesamt gibt es " + s.getRundenanzahl() + " Runden.";
                                embed.setDescription(description);
                                sent.editMessageEmbeds(embed.build()).queue();
                                sent = updated(sent);
                                TabelleFile t = new TabelleFile(s.TabelleÜberschriften(), s.TabelleWerte());
                                String filename = "SLFTabelle.png";
                                t.writeFile(filename);
                                Message bild = replyFile(sent, new File(filename));
                                embed = embed
                                        .clearFields()
                                        .addField(sent.getEmbeds().get(0).getFields().get(0))
                                        .addField("Eingaben", "*Eingabe (Punkte)*\n" + bild.getJumpUrl(), false);
                                sent.editMessageEmbeds(embed.build()).queue();
                                sent = updated(sent);
                                s.getaktuelleRunde().enderunde = sent;
                                //System.out.println(sent.getJumpUrl());
                                if(s.getRunde() + 1 == s.getRundenanzahl()){
                                    System.out.println("ende eingeleitet");
                                    myid = randomString();
                                    embed = new EmbedBuilder()
                                            .setTitle("Stadt Land Fluss - Ende")
                                            .setDescription(myid)
                                            .setColor(Color.CYAN)
                                            .setThumbnail(Spiel.bild)
                                            .setFooter("StadtLandFluss by Hackerman", own.getAvatarUrl());
                                    event.getChannel().sendMessageEmbeds(embed.build()).queue();
                                    Message ende = event.getChannel().getHistory().retrievePast(1).complete().get(0);
                                    while(ende.getEmbeds().size() != 1 ||
                                            !Objects.requireNonNull(ende.getEmbeds().get(0).getDescription()).equals(myid)){
                                        ende = event.getChannel().getHistory().retrievePast(1).complete().get(0);
                                        delay(5);
                                    }
                                    System.out.println("end loop");
                                    description = "Dies war ein Spiel Stadt-Land-Fluss. Es wurde von " + s.getHost().getAsMention() + " gehostet.";
                                    embed.setDescription(description);
                                    ende.editMessageEmbeds(embed.build()).queue();
                                    ende = updated(ende);

                                    t = new TabelleFile(s.EndeTabelleÜberschriften(), s.EndeTabelleWerte());
                                    t.writeFile(filename);
                                    Message endresults  = replyFile(ende, new File(filename));

                                    List<User> playersorted = s.SpielernachPunktensortiert();
                                    embed.addField("Der Gewinner ist ", playersorted.get(0).getAsMention() + "\n", false);
                                    StringBuilder platzierungen = new StringBuilder();
                                    for (int i = 1; i < playersorted.size(); i++) {
                                        platzierungen.append(i + 1).append(": ").append(playersorted.get(i).getAsTag()).append("\n");
                                    }
                                    embed.addField("Restliche Platzierungen", platzierungen.toString(), false);
                                    embed.addField("Genauere Ergebnisse", endresults.getJumpUrl(), false);
                                    ende.editMessageEmbeds(embed.build()).queue();
                                    System.out.println("gewinner: " + playersorted.stream().map(User::getName).toList());

                                    s.setEnde(ende);
                                    s.delete();
                                }
                                else{
                                    s.neueRunde();
                                }
                                }
                            }
                        }
                    }
                break;
            case "help":
                if(event.getOptions().size() == 0){
                    User bot = event.getJDA().retrieveUserById(ID).complete();
                    User author = event.getJDA().retrieveUserById(Config.AdminID).complete();
                    event.reply("Dies ist ein Version Stadt-Land-Fluss für den Discordbot " + bot.getAsMention() + ". " +
                            "Diese wurde programmiert von " + author.getAsMention() + ". " +
                            "Um ein Spiel zu starten, nutze den Command /*" + CommandMain + " new*").setEphemeral(true).queue();
                }
                else if(event.getOptions().size() == 1){
                    OptionMapping om = event.getOptions().get(0);
                    if(om.getName().equals("kategorie")){
                        Kategorie k = Kategorie.kategoriebyName(om.getAsString());
                        assert k != null;
                        event.reply("Genauere Beschreibung der Kategorie: **" + k.getName() + "**:\n" + k.getBeschreibung()).setEphemeral(true).queue();
                    }
                    else if(om.getName().equals("runden_ende")){
                        RundenendeI re = RundenendeI.getRundenEndebyName(om.getAsString());
                        assert re != null;
                        event.reply("Genauere Beschreibung des Runden-Endes: **" + re.getName() + "**:\n" + re.getErklärung()).setEphemeral(true).queue();
                    }
                }
                break;
            default:
                break;
        }
    }
    public static boolean isadminofagame(User u){
        return Spiel.imSpiel(u) && Spiel.getSpielbyUser(u).getHost().equals(u);
    }
    private static Message replyFile (Message replyto, File file){
        replyto.reply(file).queue();
        Message m;
        while(true){
            File sentfile = null;
            m = replyto.getChannel().getHistory().retrievePast(1).complete().get(0);
            Message.Attachment attachment = null;
            if(m.getAttachments().size() == 1) {
                attachment = m.getAttachments().get(0);
                try {
                    sentfile = attachment.downloadToFile().get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                if (sentfile != null && sentfile.equals(file)) {
                    break;
                }
            }
        }
        return m;
    }
}
