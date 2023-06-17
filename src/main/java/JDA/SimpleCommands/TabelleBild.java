package JDA.SimpleCommands;

import JDA.util.TabelleFile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static JDA.core.Main.delay;
import static JDA.util.Random.randomString;
import static JDA.Games.StadtLandFluss.Spiel.updated;

public class TabelleBild {
    public TabelleBild(GuildMessageReceivedEvent event) {
        // Die Daten für das Table
        String[][] data = new String[][]{
                {"dankemerkel", "Frankfurt am Main (20)", "gegse", "4a", "5a", "6a", "7a"},
                {"Tarin", "Amsterdam(20)", "b(5)", "4b", "5b", "6b", "7b"},
                {"XerionLLD", "Weißrussland(20)", "jeeet", "4c", "5c", "6c", "7c"}
        };

        // Die Column-Titles
        String[] title = new String[]{
                "Spielername", "Stadt", "Gewässer", "4_0", "5_0", "6_0", "7_0"
        };
        TabelleFile tabelle = new TabelleFile(title, data);
        String name = "SLFTabelle.png";

        tabelle.writeFile(name);


        File f = new File(name);
        EmbedBuilder embed = new EmbedBuilder(){ };
        String myid = randomString();
        embed = embed.
                setTitle("Tabelle")
                .setColor(Color.red)
                .setDescription("description")
                .setFooter(myid)
                .addField("Bild", "waiting", false);
        event.getChannel().sendMessageEmbeds(embed.build()).queue();
        Message sent = event.getChannel().getHistory().retrievePast(1).complete().get(0);
        System.out.println("myid: " + myid);
        while(sent.getEmbeds().size() != 1 ||
                !Objects.requireNonNull(
                        (Objects.requireNonNull(
                                (sent = event.getChannel().getHistory().retrievePast(1).complete().get(0))
                                        .getEmbeds().get(0).getFooter())).getText()).equals(myid)){
            //System.out.println(sent.getEmbeds().get(0).getFooter().getText());
            delay(10);
        }
        embed = embed.setFooter("received");
        sent.editMessageEmbeds(embed.build()).queue();
        sent = updated(sent);
        System.out.println("sent: " + sent.getJumpUrl());
        sent.reply(f).queue();
        Message bild;
        while(true){
            File sentfile = null;
            bild = event.getChannel().getHistory().retrievePast(1).complete().get(0);
            Message.Attachment attachment = null;
            if(bild.getAttachments().size() == 1) {
                attachment = bild.getAttachments().get(0);
                try {
                    sentfile = attachment.downloadToFile().get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                if (sentfile != null && sentfile.equals(f)) {
                    break;
                }
            }
        }
        embed = embed.clearFields().addField("Bild", bild.getJumpUrl(), false);
        sent.editMessageEmbeds(embed.build()).queue();
        System.out.println("bild: " + bild.getJumpUrl());

        while (!f.delete()){
            Thread t = new Thread(f::deleteOnExit);
            t.start();
            t = null;
        }
        System.out.println("f exists?: " + f.delete());

    }

}
