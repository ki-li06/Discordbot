package JDA.SimpleCommands;

import JDA.core.Main;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class EditMessage {
    private static Message nachricht;
    public long ID;

    public EditMessage(GuildMessageReceivedEvent event, String neu) {
        MessageHistory h = event.getChannel().getHistory();
        List<Message> ml = h.retrievePast(30).complete();
        Message nachricht = null;
        for (int i = 1; i < ml.size() && nachricht == null; i++) {
            Message ausgang = ml.get(i);
            if (ausgang.getAuthor().getIdLong() == event.getGuild().getMemberById(Main.IDlong).getIdLong()) {
                nachricht = ausgang;
                System.out.println();
                ID = ausgang.getIdLong();
            }
        }
        nachricht.editMessage(neu).override(true).queue();
    }
}
