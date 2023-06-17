package JDA.SimpleCommands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.VanityInvite;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.InviteAction;
import net.dv8tion.jda.internal.requests.Route;

import java.util.ArrayList;
import java.util.List;

public class Einladungen {
    public Einladungen(GuildMessageReceivedEvent event){
        JDA jda = event.getJDA();
        List<Guild> server = new ArrayList<>(jda.getGuilds());
        List<Invite> einladungen = new ArrayList<>();
        for(int i = 0; i < server.size(); i++){
            System.out.println("Server: " + server.get(i).getName());
            einladungen.add(server.get(i).getChannels().get(0).createInvite().complete());
        }
        for(int i = 0; i < einladungen.size(); i++){
            System.out.println(einladungen.get(i).toString());
        }
    }
}
