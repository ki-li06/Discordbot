package JDA.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Random {
    /**
     * returns a random int
     * @param min the lower limit (inclusive)
     * @param max the upper limit (inclusive)
     */
    public static int random (int min, int max){
        java.util.Random r = new java.util.Random();
        return r.nextInt(max - min + 1) + min;
    }
    public static String randomString (){
        int length = random(10, 15);
        StringBuilder ausgabe = new StringBuilder();
        List<Character> chars = new ArrayList<>();
        chars.addAll(IntStream.rangeClosed(48, 57).boxed().map(i-> (char) ((int) i)).toList());
        chars.addAll(IntStream.rangeClosed(65, 90).boxed().map(i-> (char) ((int) i)).toList());
        chars.addAll(IntStream.rangeClosed(97, 122).boxed().map(i-> (char) ((int) i)).toList());
        for (int i = 0; i < length; i++) {
            char add = chars.get(random(0, chars.size()-1));
            ausgabe.append(add);
        }
        return ausgabe.toString();

    }
}
