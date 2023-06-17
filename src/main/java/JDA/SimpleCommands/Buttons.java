package JDA.SimpleCommands;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.ArrayList;
import java.util.List;

public class Buttons {
    public Buttons(GuildMessageReceivedEvent event){
        event.getChannel().sendMessage("Wähle schlauer als Timon").setActionRow(sendButtons()).queue();

    }
    public static List<Button> sendButtons(){
        List<Button> buttons = new ArrayList<>();
        buttons.add(Button.primary("schere", Emoji.fromMarkdown("✂")));
        buttons.add(Button.success("pog", "POG in den Chat"));
        buttons.add(Button.danger("danger", "gefährlich"));
        buttons.add(Button.secondary("cat", "Catn't"));
        buttons.add(Button.link("https://www.youtube.com/watch?v=xvFZjo5PgG0", "link vom tutorial"));

        return buttons;
    }
}
