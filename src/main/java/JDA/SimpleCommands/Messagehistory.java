package JDA.SimpleCommands;

import JDA.Games.SchereSteinPapier.SchereSteinPapier;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.OffsetDateTime;
import java.util.List;

public class Messagehistory {
    public Messagehistory(GuildMessageReceivedEvent event) {
        if (event.getAuthor().getIdLong() == Config.AdminID) {
            User asdf = SchereSteinPapier.nutzer(event.getJDA(), event.getMessage().getContentRaw().split(" ")[1]);
            List<Message> history = asdf.openPrivateChannel().complete().getHistory().retrievePast(100).complete();
            System.out.println("Nachrichtenverlauf zwichen Hackerman und " + asdf.getAsTag());
            OffsetDateTime time;
            for (int i = (history.size() - 1); i >= 0; i--) {
                if (!history.get(i).getContentDisplay().equals("")) {
                    System.out.println();
                    time = history.get(i).getTimeCreated();
                    System.out.println(" um " + time.getHour() + ":" + time.getMinute() + " am " + time.getDayOfMonth() + "." + time.getMonthValue() + "." + time.getYear()
                            + "    von " + history.get(i).getAuthor().getAsTag());
                    System.out.println(history.get(i).getContentDisplay());
                }
            }
        }
    }
}
