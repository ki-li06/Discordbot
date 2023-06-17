package JDA.SimpleCommands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Objects;

public class TimonsPingBlockade {
    private static final long timonsbotid = 913137579677872140L;
    public TimonsPingBlockade(GuildMessageReceivedEvent event){
        if(event.getMessage().getContentRaw().startsWith("<ping <@913137579677872140>")){
            boolean botaufserver = false;
            for(Member m : event.getGuild().getMembers()){
                if(m.getUser().getIdLong() == timonsbotid){
                    botaufserver = true;
                    break;
                }
            }
            if(!botaufserver){
                event.getMessage().reply("Nein das darfst du nicht").queue();
                Objects.requireNonNull(event.getGuild().getMemberById(timonsbotid)).kick().queue();
            }
        }
    }
    public static void revanche(MessageChannel channel){
        for(int i = 0; i < 1000; i++){
            channel.sendMessage("<@623496550353731585>").queue();
            try{
                Thread.sleep(10L);
            }
            catch (InterruptedException ex){
                System.out.println("f");
            }
        }
        channel.sendMessage("<ping <@623496550353731585> 1000").queue();
    }
}
