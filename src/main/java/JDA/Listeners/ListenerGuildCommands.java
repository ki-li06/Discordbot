package JDA.Listeners;

import JDA.Games.SchereSteinPapier.SchereSteinPapier;
import JDA.SimpleCommands.*;
import JDA.core.Config;
import JDA.core.Main;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.File;


public class ListenerGuildCommands extends ListenerAdapter {
    public static final String prefix = "+";
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if(event.getAuthor().isBot() && !event.getAuthor().getId().equals(Main.ID) && event.getChannel().getId().equals("910210766215475220")){
            delete(event);
            System.out.println("Ein Bot hat etwas in den verbotenen channel geschrieben");
        }
        else if(!event.getAuthor().getId().equals(Main.ID)
                && event.getMessage().getEmbeds().size() == 0
                && event.getMessage().getStickers().size() == 0 &&
                event.getMessage().getContentRaw().length() > 0) {
            //richtiger Befehl
            if (prefix.equals(String.valueOf(args[0].charAt(0)))) {
                showtext(event);
                String switchwert = args[0].replace(prefix, "");
                switch (switchwert) {
                    default:
                        break;
                    case "command":
                        event.getChannel().sendMessage("This bot is working! \nzweite Zeile").queue();
                        delete(event);
                        break;
                    case "embed":
                        new Embed(event);
                        break;
                    case "addrole":
                        new Addrole(event);
                        break;
                    case "removerole":
                        new Removerole(event);
                        break;
                    case "nickname":
                        new Nickname(event);   //hierarchie beachten
                        break;
                    case "reaction":
                        new Reagiere(event);
                        break;
                    case "edit":
                        new EditMessage(event, "diese Nachricht wurde geupdatet");
                        break;
                    case "dm":
                        new DirectMessage(event);
                        break;
                    case "ssp":
                        new SchereSteinPapier(event);
                        break;
                    case "messagehistory":
                        new Messagehistory(event);
                        break;
                    case "buttons":
                        new Buttons(event);
                        break;
                    case "timonistdoof":
                        TimonsPingBlockade.revanche(event.getChannel());
                        break;
                    case "einladungen":
                        new Einladungen(event);
                        break;
                    case "purge":
                        new Purge(event);
                        break;
                    case "test":
                        new Test(event);
                        break;
                    case "updateslash":
                        ListenerSlashCommands.erstellen(event.getJDA());
                        event.getMessage().reply("executed updateslash").queue();
                        System.out.println("executed updateslash");
                        break;
                    case "timon":
                        String location = Main.class.getProtectionDomain().getCodeSource().getLocation().getFile();
                        System.out.println(location);
                        location = location.substring(0, location.indexOf("target")) + "/src/main/java/JDA/Main.java";
                        event.getJDA().getUserById(Config.AdminID).openPrivateChannel().complete().sendFile(new File(location)).queue();
                        break;
                    case "tabelle":
                        new TabelleBild(event);
                        break;
                }
            }
            else if(event.getMessage().getContentRaw().contains("@anyone")){
                Member anyone = Anyone.anyone(event.getMember(), event.getChannel().getMembers());
                if(anyone != null){
                    event.getChannel().sendMessage(anyone.getAsMention()).queue();
                }
            }
            else if(event.getMessage().getContentRaw().startsWith("<ping <@" + Config.AdminID + ">")){
                new TimonsPingBlockade(event);
            }
        }
    }
    private void delete(@NotNull GuildMessageReceivedEvent event){
        event.getMessage().delete().queue();
    }
    private void showtext(@NotNull GuildMessageReceivedEvent event){
        String Message = event.getMessage().getContentRaw();
        System.out.println("Der eingegebene Text lautet: " + Message);

    }
}

