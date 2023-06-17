package JDA.Listeners;

import JDA.core.Config;
import JDA.core.Main;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ListenerPrivateCommands extends ListenerAdapter {
    @Override
    public void onPrivateMessageReceived (PrivateMessageReceivedEvent event){
        if(event.getAuthor().getIdLong() != Main.IDlong) {
            System.out.println("Dm von " + event.getAuthor().getAsTag() + " bekommen");
            User spamuser = event.getJDA().getUserById(912382584565153883L);
            User admin = event.getJDA().getUserById(Config.AdminID);
            if((!event.getAuthor().equals(admin)) && (spamuser != null)) {
                spamuser.openPrivateChannel().complete().sendMessage("``Message von`` " + event.getAuthor().getAsMention()).queue();
                spamuser.openPrivateChannel().complete().sendMessage(event.getMessage()).complete();
                spamuser.openPrivateChannel().complete().close().queue();
            }
        }
    }
}
