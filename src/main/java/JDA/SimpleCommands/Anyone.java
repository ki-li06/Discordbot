package JDA.SimpleCommands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Anyone {
    public static Member anyone(Member author, List<Member> members){
        List<Member> memberList = new ArrayList<>(members);
        memberList.remove(author);
        for(int i = 0; i < memberList.size(); i++){
            if(memberList.get(i).getUser().isBot()){
                memberList.remove(i);
            }
        }
        if(memberList.size() > 0){
            return memberList.get(random(memberList.size()));
        }
        return null;
    }
    private static int random(int limit){
        Random rand = new Random();
        return rand.nextInt(limit);
    }
}
