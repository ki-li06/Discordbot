package JDA.util;

import java.time.OffsetDateTime;

public class Zeit {
    public static String format(OffsetDateTime time){
        String ausgabe = "";
        ausgabe += "0".repeat(2-String.valueOf(time.getHour()).length());
        ausgabe += time.getHour();
        ausgabe += ":";
        ausgabe += "0".repeat(2-String.valueOf(time.getMinute()).length());
        ausgabe += time.getMinute();
        ausgabe += ":";
        ausgabe += "0".repeat(2-String.valueOf(time.getSecond()).length());
        ausgabe += time.getSecond();
        return ausgabe;
    }
}
