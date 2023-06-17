package JDA.SimpleCommands;

import JDA.core.Config;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Purge {
    public Purge (GuildMessageReceivedEvent event){
        if(event.getMessage().getContentDisplay().split(" ").length == 2 && event.getAuthor().getIdLong() == Config.AdminID){
            try {
                event.getChannel().deleteMessages(event.getChannel().getHistory().retrievePast(Integer.parseInt(event.getMessage().getContentDisplay().split(" ")[1])).complete());
            }
            catch (IllegalArgumentException iaex){
                event.getMessage().reply("fehler").queue();
            }
        }

    }
}
