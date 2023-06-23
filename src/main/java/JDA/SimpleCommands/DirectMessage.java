package JDA.SimpleCommands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

public class DirectMessage {
    public
    DirectMessage(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        long ID;
        try {
            if (args.length == 1) {
                ID = event.getAuthor().getIdLong();
            } else if (args[1].startsWith("<@")) {
                System.out.println(innerstring(args[0], "<@!", ">"));
                ID = Long.parseLong(innerstring(args[1], "<@!", ">"));
            } else {
                ID = Long.parseLong(args[1]);
            }
            User nutzer = event.getJDA().retrieveUserById(String.valueOf(ID)).complete();
            if (!nutzer.equals(event.getJDA().retrieveUserById(Config.AdminID).complete())) {
                String inhalt = new String();
                inhalt = "\uD83D\uDC7E **                    HAHAHA                   ** \uD83D\uDC7E \n\uD83D\uDC7E **DU WIRST JETZT GEHACKT** \uD83D\uDC7E ";
                nutzer.openPrivateChannel().complete().sendMessage(inhalt).complete();
                System.out.println("DM an " + nutzer.getName() + " geschickt");
                nutzer.openPrivateChannel().complete().close();
            } else {
                event.getChannel().sendMessage("Ne, dankemerkel bekommt keine DMs").queue();
                System.out.println("DM an dankemerkel abgewehrt");
            }
        } catch (NumberFormatException nfex) {
            event.getChannel().sendMessage("Gib bitte die User ID als zweiten Teil deiner Nachricht an").queue();
        } catch (ErrorResponseException erex) {
            event.getChannel().sendMessage("Ich kann diesem User leider keine Nachricht schreiben").queue();
        }
    }

    private String innerstring(String eingabe, String erster, String zweiter) {
        String ausgabe = "";
        int stelle1 = eingabe.indexOf(erster) + erster.length();
        String allesaußererster = eingabe.substring(stelle1);
        int stelle2 = allesaußererster.indexOf(zweiter) + stelle1;
        if (stelle1 != 0 && stelle2 != 0 && (stelle2 > stelle1)) {
            ausgabe = eingabe.substring(stelle1, stelle2);
        }
        return ausgabe;
    }
}
