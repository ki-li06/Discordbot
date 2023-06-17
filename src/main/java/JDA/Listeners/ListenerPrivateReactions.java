package JDA.Listeners;

import JDA.core.Main;
import JDA.Games.SchereSteinPapier.SchereSteinPapier;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ListenerPrivateReactions extends ListenerAdapter {
    @Override
    public void onPrivateMessageReactionAdd (PrivateMessageReactionAddEvent event) {
        System.out.println("Private Reacted");
        Message message =
        event.getChannel().getHistory().getMessageById(event.getMessageId());
        System.out.println("Message.getAuthor().getId() " + message.getAuthor().getIdLong());
        System.out.println("event.getUser().getIdLong() " + event.getUser().getIdLong());
        if(Main.IDlong == message.getAuthor().getIdLong() && event.getUser().getIdLong() != Main.IDlong){
            ausgeben(event);
            System.out.println("new SchereSteinPapier ReactionEvent");
            new SchereSteinPapier(event, message);
        }
    }
    private void ausgeben(PrivateMessageReactionAddEvent event) {
        MessageReaction.ReactionEmote emote = event.getReactionEmote();
        System.out.println("Private Channel: Der User " + event.getUser() + " hat auf die Nachricht " + event.getMessageId() +
                "\n                 mit dem Emote " + emote.getName() + " (" + emote.getAsCodepoints() + ") reagiert");
    }
}
