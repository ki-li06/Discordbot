package JDA.SimpleCommands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class Embed {
    public Embed(GuildMessageReceivedEvent ereignis) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Embed - Titel", "https://www.youtube.com/watch?v=a3Z7zEc7AXQ");
        embed.setDescription("Julian ist schlechter als Kilian");
        embed.setThumbnail("https://i.ibb.co/pQgBT8D/schere-stein-papier-sticker-removebg-preview.png");
        embed.addField("Überschrift Feld 1", "paul ist schlechtester Programmierer aller Zeiten", false);
        embed.setColor(new Color(201, 29, 49));
        embed.addField("Überschrift Feld 2", "Inhalt Feld 2", false);
        embed.setFooter("Command ausgelöst von " + ereignis.getMember().getEffectiveName(), ereignis.getAuthor().getAvatarUrl());
        String text1 = "i1.1\n**ü2.1**\ni2.1";
        String text2 = "i1.2\n**ü2.2**\ni2.2";
        embed.addField("ü1.1", text1, true);
        embed.addField("ü1.2", text2, true);
        String text3 = "i1.3\n**u2.3**\ni2.3";
        embed.addField("ü1.3", text3, true);
        String text4 = "i1.4\n**u2.4**\ni2.4";
        embed.addField("ü1.4", text4, true);
        embed.setFooter("Seite 1", ereignis.getAuthor().getAvatarUrl());
        ereignis.getChannel().sendMessageEmbeds(embed.build()).queue();
        embed.clear();
    }

    public static boolean isanEmbed(Message message) {
        return message.getEmbeds().size() == 1;
    }
}
