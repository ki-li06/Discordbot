package JDA.Listeners;

import JDA.core.Main;
import JDA.Games.SchereSteinPapier.SchereSteinPapier;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ListenerGuildReactions extends ListenerAdapter {
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        Message message = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
        MessageReaction.ReactionEmote emote = event.getReactionEmote();
        if (Main.ID.equals(message.getAuthor().getId()) && event.getUser().getIdLong() != Main.IDlong) {
            ausgeben(event);
            new SchereSteinPapier(event, message, emote);
        }
    }

    private void ausgeben(GuildMessageReactionAddEvent event) {
        MessageReaction.ReactionEmote emote = event.getReactionEmote();
        System.out.println("Auf dem Server \"" + event.getGuild().getName() + "\" im Channel \"" + event.getChannel().getName() +
                "\" hat der User \"" + event.getUser().getName() + "\"\n      mit dem Emote " + emote.getName() + "(" + emote.getAsCodepoints() +
                ") auf die Nachricht "+  event.getMessageId() + " reagiert");
    }

}



