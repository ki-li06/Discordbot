package JDA.Features.UserOnlineTracking;

import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;

import static JDA.util.LokalSpeichern.read;

public class Handling {
    public static final String filename = "UOS.txt";
    public static List<OnlineTime> getOnlineTimes(User u){
        List<OnlineTime> all = new ArrayList<>(read(filename, OnlineTime.class));
        all.removeIf(i->i.userid != u.getIdLong());
        return all;
    }
}
