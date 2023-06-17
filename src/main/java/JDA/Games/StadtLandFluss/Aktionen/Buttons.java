package JDA.Games.StadtLandFluss.Aktionen;

import JDA.Games.StadtLandFluss.TeilvonSpiel.Lobby;
import JDA.Games.StadtLandFluss.Spiel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static JDA.core.Main.*;
import static JDA.util.Random.randomString;
import static JDA.Games.StadtLandFluss.Aktionen.Commands.CommandMain;

public class Buttons {
    public static final String starttext = "SLF.start";
    public static final String jointext = "SLF.join";
    public static final String leavetext = "SLF.leave";
    public static final String finishsettings = "SLF.finishsettings";
    public static final String showedleavetext = "Leave Game";

    public static List<Button> lobbybuttons(){
        List<Button> buttons = new ArrayList<>();
        buttons.add(Button.success(starttext, "Start Game"));
        buttons.add(Button.primary(jointext, "Join Lobby"));
        buttons.add(Button.danger(leavetext, showedleavetext));
        return buttons;
    }
    public static List<Button> finishsettings(){
        List<Button> buttons = new ArrayList<>();
        buttons.add(Button.success(finishsettings, "Finish Settings"));
        return buttons;
    }
    public static void handlejoin(ButtonClickEvent event){
        if(!Spiel.imSpiel(event.getUser()) && passendeLobbyMessage(event)){
            System.out.println("Handle Join");
            Spiel s = Spiel.getSpielbyLobbyMessage(event.getMessage());
            if(s.lobby.getopen() && (!s.getHost().equals(event.getUser())) &&
                    (!s.beigetreten(event.getUser())) && (!s.gebannt(event.getUser()))){
                MessageEmbed membed = event.getMessage().getEmbeds().get(0);
                EmbedBuilder embedBuilder = new EmbedBuilder(membed);
                embedBuilder = embedBuilder.clearFields();
                MessageEmbed.Field beigetreten = membed.getFields().get(0);
                String mitgliedertext = beigetreten.getValue();
                mitgliedertext += "\n" + Lobby.auflistungszeichen + event.getMember().getUser().getAsTag();
                if (membed.getFields().size() == 2) {                                //Mit den Geleavten oder nicht
                    embedBuilder = embedBuilder.addField(beigetreten.getName(), mitgliedertext, false)
                            .addField(membed.getFields().get(1));
                } else {
                    embedBuilder = embedBuilder.addField(beigetreten.getName(), mitgliedertext, false);
                }
                event.getMessage().editMessageEmbeds(embedBuilder.build()).queue();
                s.addMitspieler(event.getUser());
                System.out.println("Beigetreten: " + s.lobby.getBeigetreten());
            }
        }
    }

    public static void handleleave(ButtonClickEvent event){
        if(Spiel.imSpiel(event.getUser()) && passendeLobbyMessage(event)) {
            System.out.println("Handle Leave");
            Spiel s = Spiel.getSpielbyLobbyMessage(event.getMessage());
            if(s.lobby.getopen()) {
                MessageEmbed messageEmbed = event.getMessage().getEmbeds().get(0);
                EmbedBuilder embedBuilder = new EmbedBuilder(messageEmbed);
                if (event.getUser().equals(s.getHost())) {
                    embedBuilder = embedBuilder.clearFields()
                            .setDescription("Diese Lobby wurde vom Host " + event.getUser().getAsMention() + " geschlossen");
                    event.getMessage().editMessageEmbeds(embedBuilder.build()).queue();
                    System.out.println(" - Host " + event.getUser().getAsTag() + " lefted");
                    s.delete();
                }
                else if (s.beigetreten(event.getUser())){
                    embedBuilder = embedBuilder.clearFields();
                    MessageEmbed.Field beigetreten = messageEmbed.getFields().get(0);
                    String mitgliedertext = beigetreten.getValue();
                    assert mitgliedertext != null;
                    int anfang = mitgliedertext.indexOf("\n" + Lobby.auflistungszeichen + event.getMember().getUser().getAsTag());
                    int ende = anfang + ("\n" + Lobby.auflistungszeichen + event.getMember().getUser().getAsTag()).length();
                    mitgliedertext = mitgliedertext.replace(mitgliedertext.substring(anfang, ende), "");
                    if (messageEmbed.getFields().size() == 2) {                                //Mit den Geleavten oder nicht
                        embedBuilder = embedBuilder.addField(beigetreten.getName(), mitgliedertext, false)
                                .addField(messageEmbed.getFields().get(1));
                    } else {
                        embedBuilder = embedBuilder.addField(beigetreten.getName(), mitgliedertext, false);
                    }
                    event.getMessage().editMessageEmbeds(embedBuilder.build()).queue();
                    System.out.println(event.getMember().getUser() + " lefted the Game");
                    Spiel.getSpielbyLobbyMessage(event.getMessage()).removeMitspieler(event.getUser());
                }
            }
        }
        else{
            System.out.println();
        }
    }

    public static void handlestart(ButtonClickEvent event) {
        if(passendeLobbyMessage(event) && Spiel.imSpiel(event.getUser()) && Spiel.getSpielbyUser(event.getUser()).getHost().equals(event.getUser())){
            Spiel s = Spiel.getSpielbyUser(event.getUser());
            if(s.lobby.getBeigetreten().size() != 0){
                event.getInteraction().editButton(event.getButton().asDisabled()).queue();
                s.setMitspielerbylobby();
                MessageEmbed m = s.getLobbymessage().getEmbeds().get(0);
                EmbedBuilder embed = new EmbedBuilder(m);
                String mitgliedertext = Lobby.auflistungszeichen + s.getHost().getAsTag();
                for (int i = 1; i < s.getSpieler().size(); i++) {
                    mitgliedertext += "\n" + Lobby.auflistungszeichen + s.getSpieler().get(i).getAsTag();
                }
                embed = embed
                        .setTitle("Stadt Land Fluss - Lobby")
                        .setDescription(m.getDescription() + "\n" + s.getHost().getAsMention() + " wähle bitte mit dem Command" +
                                " */" + CommandMain + " setsettings* die Einstellungen aus!")
                        .clearFields()
                        .addField("Mitglieder", mitgliedertext, false);
                s.getLobbymessage().editMessageEmbeds(embed.build()).queue();


            }
            else{
                event.reply(Objects.requireNonNull(event.getMember()).getAsMention() + " du kannst nicht alleine spielen").setEphemeral(true).queue();
            }
        }
    }

    public static void handlefinishsettings(ButtonClickEvent event) {
        if(event.getMessage().getEmbeds().size() == 1 &&
                Objects.requireNonNull(event.getMessage().getEmbeds().get(0).getTitle()).equals("Stadt Land Fluss - Spieleinstellungen") &&
                event.getMessage().getAuthor().getId().equals(ID)){
            System.out.println("handle finishsettings 2");
            Spiel s = Spiel.getSpielbyUser(event.getUser());
            if(!s.lobby.getopen() && !s.finishedsettings()){
                s.setFinishedSettings(true);
                event.getInteraction().editButton(Objects.requireNonNull(event.getButton()).asDisabled()).queue();
                EmbedBuilder embed = new EmbedBuilder(s.getSettingmessage().getEmbeds().get(0));
                //System.out.println("description: " + embed.getDescriptionBuilder().toString());
                embed = embed
                        .setTitle("Stadt Land Fluss - Start")
                        .setDescription("Dies ist Teil eines Stadt Land Fluss Spiels, welches von " + s.getHost().getAsMention() + " geleitet wird.")
                        .clearFields()
                        .addField("Welche Spieleinstellungen gibt es?",
                                "Die genauen Spieleinstellungen kannst du noch mal hier nachlesen: " + s.getSettingmessage().getJumpUrl(),
                                false)
                        .addField("Wie geht das Spiel jetzt los?",
                                "Dazu muss " + s.getHost().getAsMention() + " den Command */" + CommandMain + " startround* ausführen. Dann startet die erste Runde, indem eine neue Nachricht mit dem gesuchten Anfangsbuchstaben kommt und ab dann läuft je nach Modus die Zeit.",
                                false)
                        .addField("Wie gebe ich meine Antworten ein?",
                                "Hierfür musst du den Command */" + CommandMain + " eingeben* nutzen. Dieser hat mehrere optionelle Parameter," +
                                        " die jeweils für eine Kategorie in eurem Spiel stehen (siehe unten). So steht zum Beispiel der Parameter *kategorie1* für " +
                                        "die Kategorie *Stadt* in eurem Spiel. Den Anfgangsbuchstaben musst du dafür jedes mal eingeben. Jedes mal, wenn du etwas eingegeben hast, werden dir alle deine Werte angezeigt. " +
                                        "Diese werden dir auch durch den Command */" + CommandMain + " showwerte* angezeigt.",
                                false);
                StringBuilder textkategorienparameter = new StringBuilder();
                for (int i = 0; i < s.getKategorien().size(); i++) {
                    textkategorienparameter.append("kategorie").append(i + 1).append(" - ").append(s.getKategorien().get(i).getName()).append("\n");
                }
                embed = embed.addField("Parameter - Kategorie", textkategorienparameter.toString(), false);
                String myid = randomString();
                String descriptiontext = embed.getDescriptionBuilder().toString();
                embed.setDescription(myid);
                event.getChannel().sendMessageEmbeds(embed.build()).queue();
                delay(10L);
                Message written = event.getTextChannel().getHistory().retrievePast(1).complete().get(0);
                while(!Objects.requireNonNull(written.getEmbeds().get(0).getDescription()).equals(myid)) {
                    written = event.getTextChannel().getHistory().retrievePast(1).complete().get(0);
                }
                embed.setDescription(descriptiontext);
                written.editMessageEmbeds(embed.build()).queue();
                s.setFinishedsettingsmessage(written);

            }
        }
    }

    public static boolean passendeLobbyMessage(ButtonClickEvent event){
        return event.getMessage().getEmbeds().size() == 1 && event.getMessage().getEmbeds().get(0).getTitle().equals(Lobby.titel);
    }
}
