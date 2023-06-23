package JDA.Games.SchereSteinPapier;

import JDA.SimpleCommands.Embed;
import JDA.core.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static JDA.core.Main.IDlong;
import JDA.Listeners.ListenerGuildCommands;

public class SchereSteinPapier {
    private static final String link = "https://i.ibb.co/pQgBT8D/schere-stein-papier-sticker-removebg-preview.png";
    private static final String haken = "U+2705";
    private static final String kreuz = "U+274e";
    public static final String schere = "U+2702U+fe0f";
    public static final String stein = "U+1faa8";
    public static final String papier = "U+1f4f0";
    private static final String R = "U+1f1f7";
    private static final String S = "U+1f1f8";
    private static final String eins = "U+31U+fe0fU+20e3";
    private static final String zwei = "U+32U+fe0fU+20e3";
    private static final String drei = "U+33U+fe0fU+20e3";
    private static boolean allowedopen = true;
    private String prefix = ListenerGuildCommands.prefix;

    public SchereSteinPapier(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if(args.length == 2 && args[1].equals("access")){
            if(event.getAuthor().getIdLong() == Config.AdminID) {
                allowedopen = !allowedopen;
                event.getMessage().reply("allowedopen zu " + allowedopen).queue();
            }
            else{
                event.getMessage().reply("Du bist für diese Aktion nicht befugt").queue();
            }
        }
        else if(args.length == 2 && args[1].equals("resign")){
            if(data.herausforderl.contains(event.getAuthor()) || data.gegnerl.contains(event.getAuthor())) {
                int stelle = data.indexof(event.getAuthor());
                System.out.println("author        : " + event.getAuthor());
                System.out.println("gegner        : " + data.gegnerl  + data.gegnerl.contains(event.getAuthor()));
                System.out.println("herausforderer: " + data.herausforderl + data.herausforderl.contains(event.getAuthor()));
                System.out.println("==> : " +stelle);
                Message Ausgangsmessage = data.ausgangsmessage.get(stelle);
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("Schere, Stein, Papier", data.ausgangsmessage.get(stelle).getJumpUrl());
                embed.setColor(Color.cyan);
                OffsetDateTime messagedate = Ausgangsmessage.getTimeEdited();
                if(messagedate == null){
                    messagedate = Ausgangsmessage.getTimeCreated();
                }
                OffsetDateTime now = event.getMessage().getTimeCreated();
                if(data.sentemoji(event.getAuthor()) != null) {
                    if(now.isAfter(messagedate.plusHours(3))){
                        embed.setFooter("Gegner: " + data.anderer(event.getAuthor()).getName(), data.anderer(event.getAuthor()).getAvatarUrl());
                        embed.setDescription("Du hast aufgegeben");
                        data.sentdm(data.anderer(event.getAuthor())).editMessageEmbeds(embed.build()).queue();
                        embed.setFooter("Gegner: " + event.getAuthor().getName(), event.getAuthor().getAvatarUrl());
                        embed.setDescription("Dein Gegner hat aufgegeben");
                        data.sentdm(event.getAuthor()).removeReaction(schere).queue();
                        data.sentdm(event.getAuthor()).removeReaction(stein).queue();
                        data.sentdm(event.getAuthor()).removeReaction(papier).queue();
                        data.sentdm(event.getAuthor()).editMessageEmbeds(embed.build()).queue();
                        embed.setColor(Color.green);
                        embed.setDescription("Aufgegeben: ");
                        embed.addField(event.getMember().getEffectiveName(),
                                       "Die Partie wurde nicht gewertet", false);
                        embed.setThumbnail(link);
                        data.ausgangsmessage.get(stelle).editMessageEmbeds(embed.build()).queue();
                        data.delete(stelle);
                        event.getMessage().delete().queue();
                    }
                    else{
                    event.getMessage().reply("Sorry, du kannst erst nach 3 Stunden aufgeben").queue();
                    }
                }
                else{
                    event.getMessage().reply("Du kannst erst aufgeben, wenn du selbst ein Zeichen abgegeben hast!").queue();
                }
                embed.clear();
            }
            else{
                event.getMessage().reply("Du kannst nur aufgeben, wenn du in einem Spiel bist!").queue();
            }
        }
        else if (args.length > 1 && args[1].equals("stats")){
            if(allowedopen) {
                if (args.length == 2 || (args.length == 3 && args[2].equals("1"))) {
                    Message davor = event.getChannel().getHistory().retrievePast(1).complete().get(0);
                    event.getChannel().sendTyping().queue();
                    EmbedBuilder embed = allgemeinestatsersteseite(event.getJDA(), event.getAuthor());
                    event.getChannel().sendMessageEmbeds(embed.build()).queue();
                    Message own = null;
                    while ((own = event.getChannel().getHistory().retrievePast(1).complete().get(0)).equals(davor)) {
                    }
                    own.addReaction(eins).queue();
                    own.addReaction(zwei).queue();
                    embed.clear();
                }
                else if (args.length == 3 && args[2].equals("2")){
                    Message davor = event.getChannel().getHistory().retrievePast(1).complete().get(0);
                    event.getChannel().sendTyping().queue();
                    EmbedBuilder embed = allgemeinestatszweiteseite(event.getJDA(), event.getAuthor());
                    event.getChannel().sendMessageEmbeds(embed.build()).queue();
                    Message own = null;
                    while ((own = event.getChannel().getHistory().retrievePast(1).complete().get(0)).equals(davor)) {
                    }
                    own.addReaction(eins).queue();
                    own.addReaction(zwei).queue();
                    embed.clear();
                }
                else if (args.length == 3) {
                    User user = nutzer(event.getJDA(), args[2]);
                    if (user != null) {
                        if(Speichern.wardabei(user.getIdLong())) {
                            event.getChannel().sendTyping().queue();
                            Message davor = event.getChannel().getHistory().retrievePast(1).complete().get(0);
                            event.getChannel().sendTyping().queue();
                            EmbedBuilder embed = spezifischersteseite(event.getJDA(), user.getIdLong(), event.getAuthor());
                            event.getChannel().sendMessageEmbeds(embed.build()).queue();
                            embed.clear();
                            Message own =
                                    null;
                            while ((own = event.getChannel().getHistory().retrievePast(1).complete().get(0)).equals(davor)) {
                            }
                            own.addReaction(eins).queue();
                            own.addReaction(zwei).queue();
                            own.addReaction(drei).queue();
                        }
                        else{
                            EmbedBuilder embed = new EmbedBuilder();
                            embed.setTitle("Schere, Stein, Papier");
                            embed.setThumbnail(link);
                            embed.addField("Fehler", "Der angegebene User hat noch kein Spiel gespielt\n" +
                                            "Fordere ihn mit **" + prefix + "ssp** heraus", false);
                            embed.setFooter("", event.getAuthor().getAvatarUrl());
                            event.getChannel().sendMessageEmbeds(embed.build()).queue();
                            embed.clear();
                        }
                    } else {
                        event.getMessage().reply("Du hast einen ungültigen User eingegeben").queue();
                    }
                }
            }
            else{
                event.getMessage().reply("Sorry, aktuell sind die Stats vom Admin blockiert").queue();
            }
        }
        else if (args.length == 2 && args[1].equals("info")){
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.CYAN);
            embed.setTitle("Schere, Stein, Papier");
            embed.setDescription("Es folgt die Info zu den Commands über Schere-Stein-Papier\n" +
                    "Schreibe immer " + prefix + "ssp davor");
            String text = "";
            text += "Fordere einen User mit **" + prefix + "ssp *User*** heraus \n";
            text += """
                    Parameter:
                    User: Ein User als Erwähnung oder seine ID
                    """;
            text += "Beispiel: **" + prefix + "ssp <@910176776033038367>**";
            embed.addField("*Spiel starten*", text, false);
            text = "Gib deine Partie mit **" + prefix + "ssp resign** auf\n";
            text += "*keine Parameter*\n";
            text += "Beispiel: **" + prefix + "ssp resign**";
            embed.addField("*Aufgeben*", text, false);
            text = "Lass dir bestimmte Statistiken mit **" + prefix + "ssp stats *(User)*** anzeigen\n";
            text += "Parameter: \n";
            text += "User: Die Statistiken des bestimmten Users\n";
            text += "*kann weggelassen werden, wenn man die allgemeinen Statistiken sehen will*\n";
            text += "Beispiel: \n" ;
            text += "**" + prefix + "ssp stats <@910176776033038367>**\n";
            text += "-> die Statistiken von dem User *Hackerman* werden angezeigt\n";
            text += "**" + prefix + "ssp stats **\n";
            text += "-> die allgemeinen Statistiken werden angezeigt";
            embed.addField("*Statistiken*", text, false);
            text = "Diese Seite hier wird mit **" + prefix + "ssp info** angezeigt";
            embed.addField("*Info*", text, false);
            User admin = event.getJDA().retrieveUserById(912382584565153883L).complete();
            embed.setFooter("by " + admin.getAsTag(), admin.getAvatarUrl());
            event.getMessage().replyEmbeds(embed.build()).queue();
            embed.clear();
        }
        else if (args.length == 2) {
            User gegner = nutzer(event.getJDA(), args[1]);
            if (gegner != null) {
                if (gegner != event.getAuthor()) {
                    if (event.getGuild().isMember(gegner)) {
                        if (!data.schondabei(event.getAuthor())) {
                            if (!data.schondabei(gegner)) {
                                Message davor;
                                davor = event.getChannel().getHistory().retrievePast(1).complete().get(0);
                                EmbedBuilder embed = new EmbedBuilder();
                                embed.setTitle("Schere, Stein, Papier");
                                embed.setColor(Color.green);
                                embed.setThumbnail(link);
                                String text = gegner.getAsMention() + ", "
                                        + event.getAuthor().getAsMention() +
                                        " hat dich zu einer Runde Schere-Stein-Papier herausgefordert \n\n" +
                                        ":white_check_mark: Annehmen \n"+
                                        ":negative_squared_cross_mark: Ablehnen";
                                embed.addField("Herausforderung", text, false);
                                event.getChannel().sendMessageEmbeds(embed.build()).queue();
                                embed.clear();
                                Message own = null;
                                while ((own = event.getChannel().getHistory().retrievePast(1).complete().get(0)).equals(davor)) {
                                }
                                own.addReaction(haken).queue();
                                own.addReaction(kreuz).queue();
                                new data(event.getAuthor(), gegner, null, null, null, null, own);
                            } else {
                                event.getMessage().reply(gegner.getName() + " befindet sich schon in einem Spiel mit " + data.anderer(gegner).getName() + "\n" +
                                        data.ausgangsmessage.get(data.indexof(gegner)).getJumpUrl()).queue();
                            }
                        } else {
                            event.getMessage().reply("Du befindest dich schon in einem Spiel mit " + data.anderer(event.getAuthor()).getName() + " " +  data.ausgangsmessage.get(data.indexof(event.getAuthor())).getJumpUrl()).queue();
                        }
                    } else {
                        event.getMessage().reply("Der angegebene User befindet sich nicht auf dem Server, du kannst deshalb leider nicht mit ihm spielen").queue();
                    }
                } else {
                    event.getMessage().reply("Du kannst nicht dich selbst herausfordern").queue();
                }
            }
            else{
                event.getMessage().reply( event.getAuthor().getAsMention() + " du hast den Command missbraucht \nBitte überprüfe bei **" + prefix + "ssp info**").queue();
            }
        }
        else {
            event.getMessage().reply( event.getAuthor().getAsMention() + " du hast den Command missbraucht \nBitte überprüfe bei **" + prefix + "ssp info**").queue();
        }
    }
    public SchereSteinPapier(GuildMessageReactionAddEvent event, Message message, MessageReaction.ReactionEmote emote) {
        if (emote.isEmoji()) {
            if (emote.getAsCodepoints().equals(kreuz) || emote.getAsCodepoints().equals(haken)) {
                if (data.ausgangsmessage.contains(message) && data.passtdazu(message, event.getUser())) {
                    User herausforderer = data.herausforderl.get(data.ausgangsmessage.indexOf(message));
                    User gegner = data.gegnerl.get(data.ausgangsmessage.indexOf(message));
                    if (event.getUser() == herausforderer && emote.getAsCodepoints().equals(kreuz)) {
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setTitle("Schere, Stein, Papier");
                        embed.setColor(Color.green);
                        embed.setThumbnail(link);
                        String text = event.getGuild().getMember(herausforderer).getAsMention() + " hat seine Herausforderung gegen "
                                + event.getGuild().getMember(gegner).getAsMention() +
                                " zurückgenommen";
                        embed.addField("Herausforderung", text, false);
                        message.editMessageEmbeds(embed.build()).override(true).queue();
                        message.clearReactions().queue();
                        embed.clear();
                        data.delete(data.ausgangsmessage.indexOf(message));
                    } else if (event.getUser() == gegner && emote.getAsCodepoints().equals(kreuz)) {
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setTitle("Schere, Stein, Papier");
                        embed.setColor(Color.green);
                        embed.setThumbnail(link);
                        String text = event.getGuild().getMember(herausforderer).getAsMention() + ", "
                                + event.getGuild().getMember(gegner).getAsMention() +
                                " hat deine Herausforderung abgelehnt!";
                        embed.addField("Herausforderung", text, false);
                        message.editMessageEmbeds(embed.build()).override(true).queue();
                        message.clearReactions().queue();
                        data.delete(data.ausgangsmessage.indexOf(message));

                        embed.clear();
                    } else if (event.getUser() == gegner && emote.getAsCodepoints().equals(haken)) {
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setTitle("Schere, Stein, Papier");
                        embed.setThumbnail(link);
                        embed.setColor(Color.green);
                        try {
                            dms(message, herausforderer, gegner);
                            String text = "Es läuft eine Partie zwischen " + herausforderer.getAsMention() + " und "
                                    + gegner.getAsMention() +
                                    "!";
                            embed.addField("Partie", text, false);
                            text = herausforderer.getAsMention() + ": \n" + data.herausforderm.get(data.indexof(herausforderer)).getJumpUrl() + "\n";
                            text += gegner.getAsMention() + ": \n" + data.gegnerm.get(data.indexof(gegner)).getJumpUrl();
                            embed.addField("Links: ", text, false);
                            message.editMessageEmbeds(embed.build()).override(true).queue();
                            message.clearReactions().queue();
                            embed.clear();
                        } catch (net.dv8tion.jda.api.exceptions.ErrorResponseException erex) {
                            System.out.println("herausforderer: " + herausforderer.getAsTag());
                            System.out.println("gegner: " + gegner.getAsTag());
                            int stelle = data.indexof(event.getUser());
                            embed.addField("Fehler", """
                                    Ein User hat eingestellt, dass er keine DM empfangen lässt
                                    Bitte überprüft, ob ihr beide diese Einstellung aktiviert habt
                                    """, false);
                            embed.addField("Überprüfe",
                                    """
                                            - Einstellungen -> Benutzereinstellungen -> Privatsphäre und Sicherheit -> Standard-Privatsphäreinstellungen -> Direktnachrichten von Servermitgliedern erlauben -> true)
                                            - Server -> Privatsphäreeinstellungen -> Direktnachrichten von Servermitgliedern erlauben - > true
                                            """, false);
                            message.editMessageEmbeds(embed.build()).override(true).queue();
                            message.clearReactions().queue();
                            embed.clear();
                            if (data.herausforderm.get(stelle) != null) {
                                data.herausforderm.get(stelle).delete().queue();
                            }
                            if (data.gegnerm.get(stelle) != null) {
                                data.gegnerm.get(stelle).delete().queue();
                            }
                            data.delete(stelle);
                        }

                    }
                }
            }
            else if (emote.getAsCodepoints().equals(R)) {
                if (Embed.isanEmbed(message)) {
                    String überschrift = message.getEmbeds().get(0).getTitle();
                    User user1 = nutzer(event.getJDA(), message.getEmbeds().get(0).getFields().get(0).getValue().split(" ")[0]);
                    User user2 = nutzer(event.getJDA(), message.getEmbeds().get(0).getFields().get(0).getValue().split(" ")[8]);
                    if (event.getUser().equals(user1) || event.getUser().equals(user2)) {
                        User herausforderer = event.getUser();
                        User gegner;
                        if (herausforderer.equals(user1)) {
                            gegner = user2;
                        } else {
                            gegner = user1;
                        }
                        if (event.getReaction().retrieveUsers().complete().contains(user1) && event.getUser().equals(user2)) {
                            if (event.getGuild().isMember(user1)) {
                                new data(herausforderer, gegner, null, null, null, null, message);
                                dms(message, herausforderer, gegner);
                                event.getChannel().retrieveMessageById(event.getMessageId()).complete().clearReactions().queue();
                                EmbedBuilder embed = new EmbedBuilder();
                                embed.setTitle("Schere, Stein, Papier");
                                embed.setThumbnail(link);
                                embed.setColor(Color.green);
                                String text = "Es läuft eine Partie zwischen " + herausforderer.getAsMention() + " und "
                                        + gegner.getAsMention() +
                                        "!";
                                embed.addField("Partie", text, false);
                                text = herausforderer.getAsMention() + ": \n" + data.herausforderm.get(data.indexof(herausforderer)).getJumpUrl() + "\n";
                                text += gegner.getAsMention() + ": \n" + data.gegnerm.get(data.indexof(gegner)).getJumpUrl();
                                embed.addField("Links: ", text, false);
                                message.editMessageEmbeds(embed.build()).override(true).queue();
                                message.clearReactions().queue();
                                embed.clear();
                            } else {
                                message.reply("Sorry" + user1.getAsMention() + ", " + user1.getName() + " befindet sich nichtmehr auf dem Server").queue();
                            }
                        } else if (event.getReaction().retrieveUsers().complete().contains(user2) && event.getUser().equals(user1)) {
                            if (event.getGuild().isMember(user2)) {
                                new data(herausforderer, gegner, null, null, null, null, message);
                                dms(message, herausforderer, gegner);
                                event.getChannel().retrieveMessageById(event.getMessageId()).complete().clearReactions().queue();
                                EmbedBuilder embed = new EmbedBuilder();
                                embed.setTitle("Schere, Stein, Papier");
                                embed.setThumbnail(link);
                                embed.setColor(Color.green);
                                String text = "Es läuft eine Partie zwischen " + herausforderer.getAsMention() + " und "
                                        + gegner.getAsMention() +
                                        "!";
                                embed.addField("Partie", text, false);
                                text = herausforderer.getAsMention() + ": \n" + data.herausforderm.get(data.indexof(herausforderer)).getJumpUrl() + "\n";
                                text += gegner.getAsMention() + ": \n" + data.gegnerm.get(data.indexof(gegner)).getJumpUrl();
                                embed.addField("Links: ", text, false);
                                message.editMessageEmbeds(embed.build()).override(true).queue();
                                message.clearReactions().queue();
                                embed.clear();
                            } else {
                                message.reply("Sorry" + user2.getAsMention() + ", " + user1.getName() + " befindet sich nicht mehr auf dem Server").queue();
                            }
                        }
                        event.getChannel().retrieveMessageById(event.getMessageId()).complete().removeReaction(R).queue();
                    }
                }
            }
            else if (emote.getAsCodepoints().equals(S)) {
                if (message.getEmbeds().size() == 1) {
                    MessageEmbed embed = message.getEmbeds().get(0);
                    if (embed.getTitle().equals("**Schere, Stein, Papier**")) {
                        System.out.println("Description: " + embed.getDescription());
                        System.out.println("Field1: " + embed.getFields().get(0).getName());
                        if ((embed.getDescription() != null && embed.getDescription().equals("Der Gewinner ist")) ||
                                embed.getFields().get(0).getName().equals("Unentschieden")) {
                            event.getChannel().sendTyping().queue();
                            message.clearReactions(S).queue();
                            long user1 = nutzer(event.getJDA(), message.getEmbeds().get(0).getFields().get(0).getValue().split(" ")[0]).getIdLong();
                            long user2 = nutzer(event.getJDA(), message.getEmbeds().get(0).getFields().get(0).getValue().split(" ")[8]).getIdLong();
                            EmbedBuilder schicken = begegnungersteseite(event.getJDA(), user1, user2, event.getUser());
                            message.replyEmbeds(schicken.build()).queue();
                        }
                    }
                }
            }
            else if (emote.getAsCodepoints().equals(eins)) {
                if (message.getEmbeds().size() == 1) {
                    MessageEmbed embed = message.getEmbeds().get(0);
                    if (embed.getTitle().equals("Schere, Stein, Papier") && embed.getFooter().getText().startsWith("Seite")
                            && !embed.getFooter().getText().endsWith("1")) {
                        if (embed.getDescription().equals("Die fett krassen Stats von diesem Bot")) {
                            message.clearReactions().queue();
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setTitle("Schere, Stein, Papier");
                            embedBuilder.setDescription("Die fett krassen Stats von diesem Bot");
                            embedBuilder.addField("*in Arbeit ...*", "", false);
                            embedBuilder.setThumbnail(link);
                            embedBuilder.setColor(Color.green);
                            message.editMessageEmbeds(embedBuilder.build()).queue();
                            EmbedBuilder embedschicken = allgemeinestatsersteseite(event.getJDA(), event.getUser());
                            message.editMessageEmbeds(embedschicken.build()).queue();
                            message.addReaction(eins).queue();
                            message.addReaction(zwei).queue();
                        } else if (embed.getDescription().startsWith("Die fett krassen Stats von dem User")) {
                            message.removeReaction(emote.getAsCodepoints(), event.getUser()).queue();
                            User user = nutzer(event.getJDA(), embed.getDescription().split(" ")[7]);
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setTitle("Schere, Stein, Papier");
                            embedBuilder.setDescription("Die fett krassen Stats von dem User " + user.getAsMention());
                            embedBuilder.addField("*in Arbeit ...*", "", false);
                            embedBuilder.setThumbnail(link);
                            embedBuilder.setColor(Color.green);
                            message.editMessageEmbeds(embedBuilder.build()).queue();
                            EmbedBuilder schicken = spezifischersteseite(event.getJDA(), user.getIdLong(), event.getUser());
                            message.editMessageEmbeds(schicken.build()).queue();

                        }
                    }
                }
            }
            else if (emote.getAsCodepoints().equals(zwei)) {
                if (message.getEmbeds().size() == 1) {
                    MessageEmbed embed = message.getEmbeds().get(0);
                    System.out.println("Embed footer" + embed.getFooter().getText());
                    if (embed.getTitle().equals("Schere, Stein, Papier") && embed.getFooter().getText().startsWith("Seite")
                            && (!embed.getFooter().getText().endsWith("2"))) {
                        if (embed.getDescription().equals("Die fett krassen Stats von diesem Bot")) {
                            message.clearReactions().queue();
                            EmbedBuilder embedschicken = allgemeinestatszweiteseite(event.getJDA(), event.getUser());
                            message.editMessageEmbeds(embedschicken.build()).queue();
                            message.addReaction(eins).queue();
                            message.addReaction(zwei).queue();
                        }
                        else if (embed.getDescription().startsWith("Die fett krassen Stats von dem User")) {
                            message.removeReaction(zwei, event.getUser()).queue();
                            User user = nutzer(event.getJDA(), embed.getDescription().split(" ")[7]);
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setTitle("Schere, Stein, Papier");
                            embedBuilder.setDescription("Die fett krassen Stats von dem User " + user.getAsMention());
                            embedBuilder.addField("*in Arbeit ...*", "", false);
                            embedBuilder.setThumbnail(link);
                            embedBuilder.setColor(Color.green);
                            message.editMessageEmbeds(embedBuilder.build()).queue();
                            EmbedBuilder embedschicken = spezifischzweiteseite(event.getJDA(), user.getIdLong(), event.getUser());
                            message.editMessageEmbeds(embedschicken.build()).queue();
                        }
                    }
                }
            }
            else if (emote.getAsCodepoints().equals(drei)) {
                if (message.getEmbeds().size() == 1) {
                    MessageEmbed embed = message.getEmbeds().get(0);
                    if (embed.getTitle().equals("Schere, Stein, Papier")) {
                        if (embed.getDescription().startsWith("Die fett krassen Stats von dem User ")) {
                            if (embed.getFooter().getText().startsWith("Seite") && !embed.getFooter().getText().endsWith("3")) {
                                message.removeReaction(emote.getAsCodepoints(), event.getUser()).queue();
                                User user = nutzer(event.getJDA(), embed.getDescription().split(" ")[7]);
                                EmbedBuilder schicken = spezifischdritteseite(event.getJDA(), user.getIdLong(), event.getUser());
                                message.editMessageEmbeds(schicken.build()).queue();
                            }
                        }
                    }
                }
            }
        }
    }
    public SchereSteinPapier(PrivateMessageReactionAddEvent event, Message message){
        System.out.println("code:   " + event.getReactionEmote().getAsCodepoints());
        if(event.getReactionEmote().getAsCodepoints().equals(schere) || event.getReactionEmote().getAsCodepoints().equals(stein) || event.getReactionEmote().getAsCodepoints().equals(papier)) {
            if (data.gegnerm.contains(message)) {
                int stelle = data.gegnerm.indexOf(message);
                if (data.gegnere.get(stelle) == null) {
                    data.gegnere.set(stelle, event.getReactionEmote().getAsCodepoints());
                    System.out.println("gegner hat eingegeben");
                    data.gegnerm.get(stelle).removeReaction(schere).queue();
                    data.gegnerm.get(stelle).removeReaction(stein).queue();
                    data.gegnerm.get(stelle).removeReaction(papier).queue();
                    if (data.herausforderere.get(stelle) != null) {
                        System.out.println("Beide haben eingegeben");
                        ende(event, message, stelle);
                    }
                }
            }
            else if (data.herausforderm.contains(message)) {
                int stelle = data.herausforderm.indexOf(message);
                if (data.herausforderere.get(stelle) == null) {
                    data.herausforderere.set(stelle, event.getReactionEmote().getAsCodepoints());
                    System.out.println("herausforderer hat eingegeben");
                    data.herausforderm.get(stelle).removeReaction(schere).queue();
                    data.herausforderm.get(stelle).removeReaction(stein).queue();
                    data.herausforderm.get(stelle).removeReaction(papier).queue();
                    if (data.gegnere.get(stelle) != null) {
                        System.out.println("Beide haben eingegeben");
                        ende(event, message, stelle);
                    }
                }
            }
        }
    }

    public void dms(Message ausgangsmessage, User herausforderer, User gegner) throws net.dv8tion.jda.api.exceptions.ErrorResponseException{
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("**Schere, Stein, Papier**");
        String footer = "Gegner: " + herausforderer.getName();
        embed.addField("Entscheide zwischen: ",
                ":scissors: Schere\n" +
                        ":rock: Stein\n" +
                        ":newspaper: Papier \n  ", false);
        embed.setFooter(footer, herausforderer.getAvatarUrl());
        embed.setColor(Color.blue);
        gegner.openPrivateChannel().complete().sendMessageEmbeds(embed.build()).complete();
        Message geschrieben = null;
        while ((geschrieben = gegner.openPrivateChannel().complete().getHistory().retrievePast(1).complete().get(0)).getAuthor().getIdLong() != IDlong) {
        }
        String[] zeichen = randomzeichen();
        geschrieben.addReaction(zeichen[0]).queue();
        geschrieben.addReaction(zeichen[1]).queue();
        geschrieben.addReaction(zeichen[2]).queue();
        gegner.openPrivateChannel().complete().close().queue();
        data.gegnerm.set(data.ausgangsmessage.indexOf(ausgangsmessage), geschrieben);
        footer = footer.replace(herausforderer.getName(), gegner.getName());
        embed.setFooter(footer, gegner.getAvatarUrl());
        herausforderer.openPrivateChannel().complete().sendMessageEmbeds(embed.build()).complete();
        while ((geschrieben = herausforderer.openPrivateChannel().complete().getHistory().retrievePast(1).complete().get(0)).getAuthor().getIdLong() != IDlong) {
        }
        zeichen = randomzeichen();
        geschrieben.addReaction(zeichen[0]).queue();
        geschrieben.addReaction(zeichen[1]).queue();
        geschrieben.addReaction(zeichen[2]).queue();
        data.herausforderm.set(data.ausgangsmessage.indexOf(ausgangsmessage), geschrieben);
        herausforderer.openPrivateChannel().complete().close().queue();
    }
    public void ende (PrivateMessageReactionAddEvent event, Message message, int stelle){
        String egegner = data.gegnere.get(stelle);
        String eherausforderer = data.herausforderere.get(stelle);
        User gewinner = null;
        String egewinner = "";
        String everlierer = "";
        String text;
        switch(eherausforderer){
            case schere:
                if(egegner.equals(papier)){
                    gewinner = data.herausforderl.get(stelle);
                    egewinner = eherausforderer;
                    everlierer = egegner;
                }
                else if(egegner.equals(stein)){
                    egewinner = egegner;
                    everlierer = eherausforderer;
                    gewinner = data.gegnerl.get(stelle);
                }
                break;
            case stein:
                if(egegner.equals(schere)){
                    everlierer = egegner;
                    egewinner = eherausforderer;
                    gewinner = data.herausforderl.get(stelle);
                }
                else if(egegner.equals(papier)){
                    everlierer = eherausforderer;
                    egewinner = egegner;
                    gewinner = data.gegnerl.get(stelle);
                }
                break;
            case papier:
                if(egegner.equals(stein)){
                    everlierer = egegner;
                    egewinner = eherausforderer;
                    gewinner = data.herausforderl.get(stelle);
                }
                else if(egegner.equals(schere)){
                    everlierer = eherausforderer;
                    egewinner = egegner;
                    gewinner = data.gegnerl.get(stelle);
                }
                break;
            default: break;
        }
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("**Schere, Stein, Papier**");
        embed.setColor(Color.green);
        if(!eherausforderer.equals(egegner)) {
            embed.setDescription("Der Gewinner ist");
            text = gewinner.getAsMention() + " " +  egewinner + "   -   " + everlierer + " " + data.anderer(gewinner).getAsMention();
            text = replaceemojis(text);
            embed.addField(gewinner.getName(), text, false);
            embed.setThumbnail(gewinner.getAvatarUrl());
            System.out.println("gewinner: " + gewinner.getName());
            try {
                if(allowedopen) {
                    new Speichern(gewinner.getIdLong(), egewinner, everlierer, data.anderer(gewinner).getIdLong(), 1);
                }
            }
            catch(Exception ex){
                System.out.println("Fehler beim Eintragen");
                ex.printStackTrace();
            }
        }
        else{
            text = data.herausforderl.get(stelle).getAsMention() + " " +  eherausforderer + "   -   " + egegner + " " + data.gegnerl.get(stelle).getAsMention();
            text = replaceemojis(text);
            embed.setThumbnail(link);
            embed.addField("Unentschieden", text, false);
            System.out.println("gewinner:  unentschieden");
            try {
                if(allowedopen) {
                    new Speichern(data.herausforderl.get(stelle).getIdLong(), data.herausforderere.get(stelle), data.gegnere.get(stelle), data.gegnerl.get(stelle).getIdLong(), 0);
                }
            }
            catch(Exception ex){
                System.out.println("Fehler beim Eintragen");
                ex.printStackTrace();
            }
        }
        embed.addField("", ":regional_indicator_r: Revanche?\n"+
        ":regional_indicator_s: Stats?", false);
        data.ausgangsmessage.get(stelle).editMessageEmbeds(embed.build()).queue();
        data.ausgangsmessage.get(stelle).addReaction(R).queue();
        data.ausgangsmessage.get(stelle).addReaction(S).queue();
        embed.clear();
        embed.setColor(Color.cyan);
        embed.setTitle("Ergebnis", data.ausgangsmessage.get(stelle).getJumpUrl());
        String footer = "Gegner: " + data.gegnerl.get(stelle).getName();
        embed.setFooter(footer, data.gegnerl.get(stelle).getAvatarUrl());
        data.herausforderm.get(stelle).editMessageEmbeds(embed.build()).queue();
        footer = "Gegner: " + data.herausforderl.get(stelle).getName();
        embed.setFooter(footer, data.herausforderl.get(stelle).getAvatarUrl());
        data.gegnerm.get(stelle).editMessage(embed.build()).queue();
        data.delete(stelle);
        embed.clear();
    }

    private EmbedBuilder allgemeinestatsersteseite(JDA jda, User author){
        EmbedBuilder embed = new EmbedBuilder();
        int anzahlspieler = Speichern.spieleranzahl();
        int anzahlspiele = Speichern.spieleanzahl();
        embed.setTitle("Schere, Stein, Papier");
        embed.setDescription("Die fett krassen Stats von diesem Bot");
        embed.setColor(Color.green);
        embed.setThumbnail(link);
            embed.addField("Generell",
                    "Wie viele Spiele?  **" + anzahlspiele +
                            "**\nWie viele Spieler? **" + anzahlspieler + "**",
                    false);
            //Häufigste User:
            long[] user = Speichern.häufigsteuser();
            String[] userString = new String[user.length];
            for (int i = 0; i < user.length; i++) {
                if (user[i] == author.getIdLong()) {
                    userString[i] = jda.retrieveUserById(user[i]).complete().getAsMention();
                } else {
                    userString[i] = jda.retrieveUserById(user[i]).complete().getAsTag();
                }
            }
            String text = "";
            for (int i = 0; i < userString.length; i++) {
                text += (i + 1) + ": " + userString[i] + "\n";
            }
            embed.addField("Häufigste User: ", text, false);
            //spalte 1
                //beste User
            String spalte1 = "";
            user = Speichern.besteuser();
            userString = new String[user.length];
            for (int i = 0; i < user.length; i++) {
                if (user[i] == author.getIdLong()) {
                    userString[i] = jda.retrieveUserById(user[i]).complete().getAsMention();
                } else {
                    userString[i] = jda.retrieveUserById(user[i]).complete().getAsTag();
                }
            }
            for (int i = 0; i < userString.length; i++) {
                spalte1 += (i + 1) + ": " + userString[i] + "\n";
            }
              //schlechteste user1
            spalte1 += "\n**Schlechteste User:**\n";
            user = Speichern.schlechtesteuser();
            userString = new String[user.length];
            for (int i = (user.length-1); i >= 0; i--) {
                if (user[i] == author.getIdLong()) {
                    userString[i] = jda.getUserById(user[i]).getAsMention();
                } else {
                    userString[i] = jda.getUserById(user[i]).getAsTag();
                }
            }
            for (int i = 0; i < userString.length; i++) {
                spalte1 += (userString.length -i) + ": " + userString[i] + "\n";
            }
            embed.addField("Beste User: ", spalte1, true);
            //spalte 2 ==> Siegesquote
              //Sieger
            String spalte2 = "";
            user = Speichern.besteuser();
            userString = new String[user.length];
            for (int i = 0; i < user.length; i++) {
                userString[i] = Speichern.siegesquote(user[i]) + "%";
            }
            for (int i = 0; i < userString.length; i++) {
                spalte2 += userString[i] + "\n";
            }
            //Verlierer
            spalte2 += "\n**Ihre Niederlagenquote:**\n";
            user = Speichern.schlechtesteuser();
            userString = new String[user.length];
            for(int i = (userString.length-1); i >= 0; i--){
                userString[i] = Speichern.niederlagenquote(user[i]) + "%";
            }
            for(int i = 0; i < userString.length; i++){
                spalte2 += userString[i] + "\n";
            }
            embed.addField("Ihre Siegesquote: ", spalte2, true);
            embed.setFooter("Seite 1", author.getAvatarUrl());
        return embed;
    }
    private EmbedBuilder allgemeinestatszweiteseite (JDA jda, User author){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Schere, Stein, Papier");
        embed.setThumbnail(link);
        embed.setDescription("Die fett krassen Stats von diesem Bot");
        embed.setColor(Color.green);
        String[] häufigstezeichen = Speichern.häufigstezeichen();
        embed.addField("Häufigste Zeichen:",
                replaceemojis(häufigstezeichen[0]) + ", " + replaceemojis(häufigstezeichen[1]) + ", " + replaceemojis(häufigstezeichen[2]),
        false);
        int[][] absolut = Speichern.bestezeichenabsolut();
        embed.addField("Absolute Siegesquote:", "*Sieg* / *Unentschieden* / *Niederlage*", false);
        String spalte1 = absolut[0][0] + "\n" + absolut[0][1] + "\n" +absolut[0][2];
        String spalte2 = absolut[1][0] + "\n" + absolut[1][1] + "\n" +absolut[1][2];
        String spalte3 = absolut[2][0] + "\n" + absolut[2][1] + "\n" +absolut[2][2];
        embed.addField("Schere", spalte1, true);
        embed.addField("Stein", spalte2, true);
        embed.addField("Papier", spalte3, true);
        String[] reihenfolgeabsolut = Speichern.besteszeichenabsolut();
        for(int i = 0; i < reihenfolgeabsolut.length; i++){
            reihenfolgeabsolut[i] = replaceemojis(reihenfolgeabsolut[i]);
        }
        embed.addField("also: ",
                reihenfolgeabsolut[0] + " , " + reihenfolgeabsolut[1] + " , " + reihenfolgeabsolut[2],
                false
                );
        double[][] relativ = Speichern.bestezeichenrelativ();
        embed.addField("Relative Siegesquote:", "*Sieg* / *Unentschieden* / *Niederlage*", false);
        spalte1 = relativ[0][0] + "%\n" + relativ[0][1] + "%\n" + relativ[0][2] + "%";
        spalte2 = relativ[1][0] + "%\n" + relativ[1][1] + "%\n" + relativ[1][2] + "%";
        spalte3 = relativ[2][0] + "%\n" + relativ[2][1] + "%\n" + relativ[2][2] + "%";
        embed.addField("Schere", spalte1, true);
        embed.addField("Stein", spalte2, true);
        embed.addField("Papier", spalte3, true);
        String[] reihenfolgerelativ = Speichern.besteszeichenrelativ();
        for(int i = 0; i < reihenfolgerelativ.length; i++){
            reihenfolgerelativ[i] = replaceemojis(reihenfolgerelativ[i]);
        }
        embed.addField("also: ",
                reihenfolgerelativ[0] + " , " + reihenfolgerelativ[1] + " , " + reihenfolgerelativ[2],
                false
        );
        embed.setFooter("Seite 2", author.getAvatarUrl());
        return embed;
    }
    private EmbedBuilder spezifischersteseite (JDA jda, long user, User author){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Schere, Stein, Papier");
        embed.setThumbnail(link);
        embed.setColor(Color.green);
        embed.setDescription("Die fett krassen Stats von dem User " + jda.retrieveUserById(user).complete().getAsMention());
        String text = "Wie viele Spiele? ";
        text += Speichern.anzahlspiele(user);
        text += "\nWie viele Gegner? " + Speichern.anzahlgegner(user);
        text += "\nPosition im Ranking: " + Speichern.position(user);
        embed.addField("Generell", text, false);
        text = "Siegesquote: \nUnentschiedenquote: \nNiederlagenquote: ";
        embed.addField("Quoten", text, true);
        String spalte2;
        spalte2 = Speichern.siegesquote(user) + "%";
        spalte2 += "\n" + Speichern.unentschiedenquote(user) + "%";
        spalte2 += "\n" + Speichern.niederlagenquote(user) + "%";
        embed.addField(" ", spalte2, true);
        embed.setFooter("Seite 1", author.getAvatarUrl());
        return embed;
    }
    private EmbedBuilder spezifischzweiteseite(JDA jda, long user, User author){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Schere, Stein, Papier");
        embed.setThumbnail(link);
        embed.setColor(Color.green);
        embed.setDescription("Die fett krassen Stats von dem User " + jda.retrieveUserById(user).complete().getAsMention());
        embed.setFooter("Seite 2", author.getAvatarUrl());
        long[] häufigstegegner = Speichern.häufigstegegner(user);
        String text = "";
        for(int i = 0; i < häufigstegegner.length; i++){
            if(häufigstegegner[i] != 0) {
                text += (i + 1) + ". " + jda.retrieveUserById(häufigstegegner[i]).complete().getAsTag() + "\n";
            }
        }
        text = text.substring(0, text.length()-1);
        embed.addField("Häufigste Gegner", text, false);
        long[] bestegegner = Speichern.bestegegnerrelativ(user);
        String spalte1  = "";
        for(int i = 0; i < bestegegner.length; i++){
            if(bestegegner[i] != 0) {
                spalte1 += (i + 1) + ". " + jda.retrieveUserById(bestegegner[i]).complete().getAsTag() + "\n";
            }
        }
        spalte1 = spalte1.substring(0, spalte1.length()-1);
        String spalte2 = "";
        for(int i = 0; i < bestegegner.length; i++){
            if(bestegegner[i] != 0) {
                spalte2 += " mit einer Siegesquote von \n";
            }
        }
        spalte2 = spalte2.substring(0, spalte2.length()-1);
        String spalte3 = "";
        for (long l : bestegegner) {
            if (l != 0) {
                spalte3 += Math.round(Speichern.quotegegenrelativ(user, l)[1]*1000d)/10d + "%\n";
            }
        }
        spalte3 = spalte3.substring(0, spalte3.length()-1);
        embed.addField("Beste Gegner", spalte1, true);
        embed.addField("", spalte2, true);
        embed.addField("", spalte3, true);
        long[] schlechtestegegner = Speichern.schlegegnerrelativ(user);
        spalte1  = "";
        for(int i = 0; i < schlechtestegegner.length; i++){
            if(schlechtestegegner[i] != 0) {
                spalte1 += (schlechtestegegner.length - i) + ". " + jda.retrieveUserById(schlechtestegegner[i]).complete().getAsTag() + "\n";
            }
        }
        spalte1 = spalte1.substring(0, spalte1.length()-1);
        spalte2 = "";
        for(int i = 0; i < schlechtestegegner.length; i++){
            if(schlechtestegegner[i] != 0) {
                spalte2 += " mit einer Niederlagenquote von \n";
            }
        }
        spalte2 = spalte2.substring(0, spalte2.length()-1);
        spalte3 = "";
        for (long l : schlechtestegegner) {
            if (l != 0) {
                spalte3 += Math.round(Speichern.quotegegenrelativ(user, l)[2]*1000d)/10d + "%\n";
            }
        }
        spalte3 = spalte3.substring(0, spalte3.length()-1);
        embed.addField("Schlechteste Gegner", spalte1, true);
        embed.addField("", spalte2, true);
        embed.addField("", spalte3, true);

        return embed;

    }
    private EmbedBuilder spezifischdritteseite(JDA jda, long user, User author){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Schere, Stein, Papier");
        embed.setThumbnail(link);
        embed.setColor(Color.green);
        embed.setDescription("Die fett krassen Stats von dem User " + jda.retrieveUserById(user).complete().getAsMention());
        embed.setFooter("Seite 3", author.getAvatarUrl());
        int[][] absolut = Speichern.bestezeichenabsolut(user);
        embed.addField("Absolute Siegesquote:", "*Sieg* / *Unentschieden* / *Niederlage*", false);
        String spalte1 = absolut[0][0] + "\n" + absolut[0][1] + "\n" +absolut[0][2];
        String spalte2 = absolut[1][0] + "\n" + absolut[1][1] + "\n" +absolut[1][2];
        String spalte3 = absolut[2][0] + "\n" + absolut[2][1] + "\n" +absolut[2][2];
        embed.addField("Schere", spalte1, true);
        embed.addField("Stein", spalte2, true);
        embed.addField("Papier", spalte3, true);
        String[] reihenfolgeabsolut = Speichern.besteszeichenabsolut(user);
        for(int i = 0; i < reihenfolgeabsolut.length; i++){
            reihenfolgeabsolut[i] = replaceemojis(reihenfolgeabsolut[i]);
        }
        embed.addField("also: ",
                reihenfolgeabsolut[0] + " , " + reihenfolgeabsolut[1] + " , " + reihenfolgeabsolut[2],
                false
        );
        double[][] relativ = Speichern.bestezeichenrelativ(user);
        embed.addField("Relative Siegesquote:", "*Sieg* / *Unentschieden* / *Niederlage*", false);
        spalte1 = relativ[0][0] + "%\n" + relativ[0][1] + "%\n" + relativ[0][2] + "%";
        spalte2 = relativ[1][0] + "%\n" + relativ[1][1] + "%\n" + relativ[1][2] + "%";
        spalte3 = relativ[2][0] + "%\n" + relativ[2][1] + "%\n" + relativ[2][2] + "%";
        embed.addField("Schere", spalte1, true);
        embed.addField("Stein", spalte2, true);
        embed.addField("Papier", spalte3, true);
        String[] reihenfolgerelativ = Speichern.besteszeichenrelativ(user);
        for(int i = 0; i < reihenfolgerelativ.length; i++){
            reihenfolgerelativ[i] = replaceemojis(reihenfolgerelativ[i]);
        }
        embed.addField("also: ",
                reihenfolgerelativ[0] + " , " + reihenfolgerelativ[1] + " , " + reihenfolgerelativ[2],
                false
        );
        return embed;
    }
    private EmbedBuilder begegnungersteseite(JDA jda, long user1, long user2, User author){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Schere, Stein, Papier");
        embed.setThumbnail(link);
        embed.setColor(Color.green);
        embed.setFooter("Eine Seite", author.getAvatarUrl());
        embed.setDescription("Die fett krassen Stats zwischen " + jda.retrieveUserById(user1).complete().getAsMention() +
                " und " + jda.retrieveUserById(user2).complete().getAsMention());
        String text = "";
        int anzahlBegegnungen = Speichern.anzahlbegegnungen(user1, user2);
        if(anzahlBegegnungen > 0) {
            text = "";
            text += "Wie viele Begegnungen? " + anzahlBegegnungen;
            text += "\nWer war besser? ";
            long bester = Speichern.bestervonbeiden(user1, user2);
            if      (bester == user1 && jda.retrieveUserById(user1).complete().equals(author)) {
                text += author.getAsMention();
            }
            else if (bester == user1) {
                text += jda.retrieveUserById(user1).complete().getAsTag();
            }
            else if (bester == user2 && jda.retrieveUserById(user2).complete().equals(author)) {
                text += jda.retrieveUserById(user2).complete().getAsMention();
            }
            else if (bester == user2){
                text += jda.retrieveUserById(user2).complete().getAsTag();
            }
            else if (bester == 0){
                text += "beide gleich gut";
            }
            embed.addField("Generell", text, false);
            int[] siegesverteilung = Speichern.siegesverteilungabsolut(user1, user2);
            text = "Siege ";
            text += jda.retrieveUserById(user1).complete().getAsTag();
            embed.addField(text, String.valueOf(siegesverteilung[1]), true);
            embed.addField("Unentschieden", String.valueOf(siegesverteilung[0]), true);
            text = "Siege ";
            text += jda.retrieveUserById(user2).complete().getAsTag();
            embed.addField(text, String.valueOf(siegesverteilung[2]), true);
            String[] haeufigstesZeichen = Speichern.häufigstezeichen(user1, user2);
            text = "";
            for (int i = 0; i < haeufigstesZeichen.length; i++){
                text += replaceemojis(haeufigstesZeichen[i]) + ", ";
            }
            text = text.substring(0, text.length()-2);
            embed.addField("Häufigstes Zeichen: ", text, false);
            embed.addField("Beste Zeichen",
                    "*Sieg* / *Unentschieden* / *Niederlage*", false);
            text = "";
            int[][] bestezeichen = Speichern.bestezeichenabsolut(user1, user2);
            String[] spalten = new String[3];
            spalten[0] = bestezeichen[0][0] + "\n" + bestezeichen[0][1] + "\n" + bestezeichen[0][2];
            spalten[1] = bestezeichen[1][0] + "\n" + bestezeichen[1][1] + "\n" + bestezeichen[1][2];
            spalten[2] = bestezeichen[2][0] + "\n" + bestezeichen[2][1] + "\n" + bestezeichen[2][2];
            embed.addField("Schere", spalten[0], true);
            embed.addField("Stein", spalten[1], true);
            embed.addField("Papier", spalten[2], true);
            String[] besteszeichen = Speichern.besteszeichenabsolut(user1, user2);
            for(int i = 0; i < besteszeichen.length; i++){
                text += replaceemojis(besteszeichen[i]) + ", ";
            }
            text = text.substring(0, text.length()-2);
            embed.addField("also", text, false);

        }
        else{
            text = jda.getUserById(user1).getAsTag() + " und " + jda.getUserById(user2).getAsTag() +
                    " haben noch nie gegeneinander gespielt";
            embed.addField("Fehler", text, false);
        }

        return embed;
    }

    public String[] randomzeichen(){
        Random random = new Random();
        String eins = intzuzeichen(random.nextInt(3) +1);
        String zwei = eins;
        while(eins.equals(zwei)){
            random = new Random();
            zwei = intzuzeichen(random.nextInt(3)+1);
        }
        String drei = zwei;
        while(drei.equals(zwei) || drei.equals(eins)){
            random = new Random();
            drei = intzuzeichen(random.nextInt(3)+1);
        }
        String[] ausgabe = new String[3];
        ausgabe[0] = eins;
        ausgabe[1] = zwei;
        ausgabe[2] = drei;
        return ausgabe;
    }
    private String intzuzeichen (int zeichen){
        switch (zeichen){
            case 1, 4 -> {
                return schere;
            }
            case 2, 5 -> {
                return stein;
            }
            default -> {
                return papier;
            }
        }
    }
    public String replaceemojis (String eingabe){
        String ausgabe = eingabe;
        ausgabe = ausgabe.replace(schere, ":scissors:");
        ausgabe = ausgabe.replace(stein, ":rock:");
        ausgabe = ausgabe.replace(papier, ":newspaper:");
        return ausgabe;
    }
    public static User nutzer (JDA jda, String eingabe){
        User ausgabe;
        if(eingabe.contains("@")){
            String nurID = eingabe.replace("<", "");
            nurID = nurID.replace(">", "");
            nurID = nurID.replace("@", "");
            nurID = nurID.replace("!", "");
            try {
                ausgabe = jda.retrieveUserById(nurID).complete();
            }
            catch (NumberFormatException nfex){
                ausgabe = null;
            }
        }
        else{
            try {
                ausgabe = jda.retrieveUserById(eingabe).complete();
            }
            catch (ErrorResponseException | NumberFormatException ex){
                ausgabe = null;
            }
        }
        if(ausgabe != null){
            System.out.println("Der eingegebene User ist: " + ausgabe.getName() + "#" + ausgabe.getDiscriminator());
        }
        else{
            System.out.println("Der eingebene User existiert nicht");
        }
        return ausgabe;
    }
}
class data {
    public static List<User> herausforderl = new ArrayList<>();
    public static List<User> gegnerl = new ArrayList<>();
    public static List<Message> herausforderm = new ArrayList<>();
    public static List<Message> gegnerm = new ArrayList<>();
    public static List<String> herausforderere = new ArrayList<>();
    public static List<String> gegnere = new ArrayList<>();
    public static List<Message> ausgangsmessage = new ArrayList<>();

    public data(User herausforder, User gegner, Message heraussfordermessage, Message gegnermessage, String herausfordereremoji, String gegneremoji, Message ausgangsmessag){
        herausforderl.add(herausforder);
        gegnerl.add(gegner);
        herausforderm.add(heraussfordermessage);
        gegnerm.add(gegnermessage);
        herausforderere.add(herausfordereremoji);
        gegnere.add(gegneremoji);
        ausgangsmessage.add(ausgangsmessag);
    }
    public static int indexof(User eingabeuser){
        int herausforderer = herausforderl.indexOf(eingabeuser);
        int ausgabe = herausforderer;
        if(herausforderer == -1){
            ausgabe = gegnerl.indexOf(eingabeuser);
        }
        return ausgabe;
    }
    public static void delete (int index){
        herausforderl.remove(index);
        gegnerl.remove(index);
        gegnerm.remove(index);
        herausforderm.remove(index);
        gegnere.remove(index);
        herausforderere.remove(index);
        ausgangsmessage.remove(index);
    }
    public static boolean schondabei(User eingabe){
        return herausforderl.contains(eingabe) || gegnerl.contains(eingabe);
    }
    public static User anderer(User eingabe){
        User ausgabe = null;
        if(herausforderl.contains(eingabe)){
            ausgabe = gegnerl.get(herausforderl.indexOf(eingabe));
        }
        else if(gegnerl.contains(eingabe)){
            ausgabe = herausforderl.get(gegnerl.indexOf(eingabe));
        }
        return ausgabe;
    }
    public static boolean passtdazu (Message message, User user){
        return herausforderl.get(ausgangsmessage.indexOf(message)).equals(user) || gegnerl.get(ausgangsmessage.indexOf(message)).equals(user);
    }
    public static void ausgeben(){
        System.out.println("herausforderl: " + herausforderl);
        System.out.println("gegnerl      : " + gegnerl);
        System.out.println("herausforderm: " + herausforderm);
        System.out.println("gegnerm      : " + gegnerm);
        System.out.println("herausfordere: " + herausforderere);
        System.out.println("gegnere      : " + gegnere);
        System.out.println("ausgangsmessa: " + ausgangsmessage);
    }
    public static String sentemoji(User eingabe){
        String ausgabe = null;
        if(herausforderl.contains(eingabe)){
            ausgabe = herausforderere.get(herausforderl.indexOf(eingabe));
        }
        else if(gegnerl.contains(eingabe)){
            ausgabe = gegnere.get(gegnerl.indexOf(eingabe));
        }
        return ausgabe;
    }
    public static Message sentdm(User eingabe){
        Message ausgabe = null;
        if(herausforderl.contains(eingabe)){
            ausgabe = gegnerm.get(herausforderl.indexOf(eingabe));
        }
        else if(gegnerl.contains(eingabe)){
            ausgabe = herausforderm.get(gegnerl.indexOf(eingabe));
        }
        return ausgabe;
    }
}
