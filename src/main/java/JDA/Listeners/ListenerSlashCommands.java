package JDA.Listeners;

import JDA.SimpleCommands.Anyone;
import JDA.Games.StadtLandFluss.Aktionen.Commands;
import JDA.Games.StadtLandFluss.TeilvonSpiel.Kategorie;
import JDA.core.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static JDA.core.Main.delay;

public class ListenerSlashCommands extends ListenerAdapter {
    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event){
        System.out.println("Slash Command used - Name: " + event.getName());
        switch (event.getName()){
            case "ping":
                StringBuilder text = new StringBuilder();
                for(int i = 97; i < 123; i++){
                    char c = (char) i;
                    text.append(":regional_indicator_").append(c).append(":");
                }
                event.reply("used the ping command (by merkel)").queue();
                event.getChannel().sendMessage(text.toString()).queue();
                break;
            case "anyone":
                Member member = Anyone.anyone(event.getMember(), event.getTextChannel().getMembers());
                assert member != null;
                event.reply(member.getAsMention()).queue();
                break;
            case "update":
                if(event.getUser().getIdLong() == Config.AdminID){
                    System.out.println("updated slash commands");
                    event.getJDA().updateCommands().queue();
                    event.reply("updated slash commands").queue();
                }

                break;
            case "test":
                OptionMapping om = event.getOption("auswählen");
                OptionMapping om2 = event.getOption("auswählen2");
                assert om != null;
                Kategorie kategorie = Kategorie.kategoriebyName(om.getAsString());
                System.out.println("auswählen: " + om.getAsString());
                System.out.println("auswählen2: " + om2.getAsString());
                event.reply("ausgewählt: " + kategorie.getName() + " - > " + kategorie.getBeschreibung()).queue();
                break;
            case "slf":
                Commands.handle(event);
                break;
            case "purge":
                if(Objects.requireNonNull(event.getMember()).hasPermission(Permission.MESSAGE_MANAGE)) {
                    int anzahl = (int) event.getOption("anzahl").getAsLong();
                    System.out.println("wants to delete " + anzahl + " messages");
                    if(anzahl > 100){
                        anzahl = 100;
                    }
                    event.reply("delete " + anzahl + " messages").setEphemeral(true).queue();
                    System.out.print("Delete ");
                    List<Message> messages = event.getChannel().getHistory().retrievePast(anzahl).complete();
                    for (Message message : messages) {
                        message.delete().queue();
                        delay(20L);
                    }
                    System.out.println(" Finished");
                }
                break;
            default:
                break;
        }


    }

    public static void erstellen(JDA jda){

        jda.upsertCommand("ping", "ein richtig krasser Slash Command").queue();
        jda.upsertCommand("anyone", "Ping anyone from the server").queue();
        jda.upsertCommand("update", "update all Commands").queue();
        jda.upsertCommand("upsert", "upsert all Commands").queue();

        CommandData purge = new CommandData("purge", "lösche die letzten Nachrichten");
        purge.addOption(OptionType.INTEGER, "anzahl", "so viele Nachrichten werden gelöscht", true);
        jda.upsertCommand(purge).queue();

        CommandData test = new CommandData("test", "ein Test zum testen");
        SubcommandData sub1 = new SubcommandData("comamndeins", "der übergeordnete command");
        test.addSubcommands(sub1);
        SubcommandData sub2 = new SubcommandData("commandzwei", "der command mit unterordnern");
        SubcommandData sub2_1 = new SubcommandData("commandzweieins", "untergeordnet eins");

        jda.upsertCommand(test).queue();

        jda.upsertCommand(Commands.vorlage()).queue();

        System.out.println("Erstellen Slash-Commands");
    }


}
