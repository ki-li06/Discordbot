package JDA.SimpleCommands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Test {
    public Test(GuildMessageReceivedEvent event) {
        event.getJDA().upsertCommand("ping", "ein richtig krasser Slash Command").queue();
        System.out.println("Test Check - Slash Command erstellt");
        event.getMessage().reply("Started").queue();

    }
}
