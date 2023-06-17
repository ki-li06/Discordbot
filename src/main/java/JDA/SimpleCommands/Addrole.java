package JDA.SimpleCommands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;
import java.util.Objects;

public class Addrole {
    public Addrole(GuildMessageReceivedEvent event){
        if(event.getMessage().getMentionedRoles().toArray().length == 1){
            if(event.getMessage().getMentionedMembers().toArray().length == 1){
                Member andererMember = event.getMessage().getMentionedMembers().get(0);
                if(Objects.requireNonNull(event.getMember()).canInteract(andererMember)) {
                    Member nutzer = event.getMember();
                    Role rolle = event.getMessage().getMentionedRoles().get(0);
                    event.getGuild().addRoleToMember(andererMember, rolle).queue();
                    event.getMessage().reply(andererMember.getEffectiveName() + " hat nun die Rolle " + rolle.getName()).queue();
                }
                else{
                    event.getChannel().sendMessage("Du kannst nur auf Rollen von Personen zugreifen, die unter dir in der Server-Hierarchie sind").queue();
                }
            }
            else{
                event.getMessage().reply("Bitte nur einen Benutzer eingeben").queue();
            }
        }
        else{
            event.getMessage().reply("Bitte nur eine Rolle eingeben").queue();
        }
    }
    private int hoechsteRolle (GuildMessageReceivedEvent event, Member member){
        Guild guild = event.getGuild();
        Member nutzer = event.getMember();
        List<Role> roles = member.getRoles();
        int ausgabe;
        if(roles.size() == 0){
            ausgabe = 0;
        }
        else {
            ausgabe = roles.get(0).getPosition();
        }
        return ausgabe;
    }
}