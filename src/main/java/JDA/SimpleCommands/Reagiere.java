package JDA.SimpleCommands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static JDA.core.Main.delay;

public class Reagiere {
    public Reagiere(GuildMessageReceivedEvent event) {
        Message davor = event.getChannel().getHistory().retrievePast(1).complete().get(0);
        event.getChannel().sendMessage("Reagier mal auf mein stuff").queue();
        delay(100);
        MessageHistory h = event.getChannel().getHistory();
        Message own = null;
        while ((own = event.getChannel().getHistory().retrievePast(1).complete().get(0)).equals(davor)) {
        }
        own.addReaction("U+1f604").queue();
        own.addReaction("U+1FAA8").queue();
    }
}
