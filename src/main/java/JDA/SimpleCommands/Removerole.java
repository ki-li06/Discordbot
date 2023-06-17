package JDA.SimpleCommands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;
import java.util.Objects;

public class Removerole {
    public Removerole(GuildMessageReceivedEvent event){
        if(event.getMessage().getMentionedRoles().toArray().length == 1){
            if(event.getMessage().getMentionedMembers().toArray().length == 1){
                Member andererMember = event.getMessage().getMentionedMembers().get(0);
                Role rolle = event.getMessage().getMentionedRoles().get(0);
                if(andererMember.getRoles().contains(rolle)) {
                    if (Objects.requireNonNull(event.getMember()).canInteract(andererMember)) {
                        Member nutzer = event.getMember();
                        event.getGuild().removeRoleFromMember(andererMember, rolle).queue();
                        event.getMessage().reply(andererMember.getEffectiveName() + " hat nun nicht mehr die Rolle " + rolle.getName()).queue();
                    } else {
                        event.getChannel().sendMessage("Du kannst nur auf Rollen von Personen zugreifen, die unter dir in der Server-Hierarchie sind").queue();
                    }
                }
                else{
                    event.getChannel().sendMessage("Die ausgewählte Person besitzt die angegebene Rolle nicht").queue();
                }
            }
            else{
                if(event.getMessage().getMentionedMembers().toArray().length > 1){
                    event.getMessage().reply("Bitte nur einen Benutzer eingeben").queue();
                }
                else {
                    event.getMessage().reply("Bitte mindestens einen Benutzer eingeben").queue();
                }
            }
        }
        else{
            if(event.getMessage().getMentionedRoles().toArray().length > 1)
            {
            event.getMessage().reply("Bitte nur eine Rolle eingeben").queue();
            }
            else{
                event.getMessage().reply("Bitte mindestens eine Rolle eingeben").queue();
            }
        }
    }
}