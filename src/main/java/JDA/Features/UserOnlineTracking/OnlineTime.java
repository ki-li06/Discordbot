package JDA.Features.UserOnlineTracking;

import java.io.Serializable;
import java.time.OffsetDateTime;

import static JDA.util.Zeit.format;

public class OnlineTime implements Serializable {
    public long userid;
    public OffsetDateTime starttime;
    public OffsetDateTime ende;
    public OnlineTime (long userid, OffsetDateTime time){
        this.userid = userid;
        this.starttime = time;
    }
    public String toString(){
        String ausgabe = "{U: " + userid + " is online " +
                format(starttime) + " - ";
        if(ende != null){
            ausgabe += format(ende);
        }
        else{
            ausgabe += "?";
        }
        return ausgabe + "}";
    }



}
