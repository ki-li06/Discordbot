package JDA.Listeners;

import JDA.Features.UserOnlineTracking.OnlineTime;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static JDA.Features.UserOnlineTracking.Handling.filename;
import static JDA.Features.UserOnlineTracking.Handling.getOnlineTimes;
import static JDA.util.LokalSpeichern.read;
import static JDA.util.LokalSpeichern.write;
import static JDA.util.Zeit.format;


public class ListenerUserUpdate extends ListenerAdapter {
    public static List<Update> updateList = new ArrayList<>();
    public static List<OnlineTime> onlineUsers = new ArrayList<>();

    @Override
    public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event){
        Update update = new Update(event.getUser(), event.getOldOnlineStatus(), event.getNewOnlineStatus(), OffsetDateTime.now());
        if(updateList.contains(update)){
            updateList.remove(update);
            return;
        }
        updateList.add(update);
        //System.out.println("update: " + update);
        if(update.alt.equals(OnlineStatus.OFFLINE) && update.neu.equals(OnlineStatus.ONLINE)){
            //System.out.println(update.user + " is now online");
            if(!onlineUsers.stream().map(i->i.userid).toList().contains(event.getUser().getIdLong())){
                onlineUsers.add(new OnlineTime(update.user.getIdLong(), update.time));
                //System.out.println(update.user.getAsTag() + " is now part of onlineUser");
                //System.out.println("onlineUser: " + onlineUsers);
            }
        }
        if(update.neu.equals(OnlineStatus.OFFLINE)){
            //System.out.println(update.user + " is now offline");
            if(onlineUsers.stream().map(i->i.userid).toList().contains(event.getUser().getIdLong())){
                int index = onlineUsers.stream().map(i->i.userid).toList().indexOf(update.user.getIdLong());
                //System.out.println(update.user.getAsTag() + " is not anymore part of onlineUser");
                OnlineTime ot = onlineUsers.get(index);
                ot.ende = OffsetDateTime.now();
                onlineUsers.remove(index);
                List<OnlineTime> ots = new ArrayList<>(read(filename, OnlineTime.class));
                ots.add(ot);
                if(event.getUser().getIdLong() == 912382584565153883L){
                    ots.removeIf(i->i.userid == 912382584565153883L);
                }
                write(filename, ots);
                List<OnlineTime> onlineTimes = new ArrayList<>(getOnlineTimes(event.getUser()));
                onlineTimes = onlineTimes.size() < 3 ? onlineTimes : onlineTimes.subList(0, 3);
                System.out.println("onlineTimes of " + event.getUser().getAsTag() + ": " + onlineTimes);
            }
        }

    }
    static class Update{
        public User user;
        public OnlineStatus alt;
        public OnlineStatus neu;
        public OffsetDateTime time;
        public Update(User u, OnlineStatus alt, OnlineStatus neu, OffsetDateTime time) {
            this.user = u;
            this.alt = alt;
            this.neu = neu;
            this.time = time;
        }
        @Override
        public String toString() {
            return "Update{ " + user.getAsTag() + " at " +
                    format(time)
                    + " : " + alt + " -> " + neu + " }";
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Update update = (Update) o;
            return Duration.between(update.time, this.time).getSeconds() <= 1
                    && Objects.equals(user, update.user) && alt == update.alt && neu == update.neu;

        }
    }
}
