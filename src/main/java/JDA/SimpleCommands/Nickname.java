package JDA.SimpleCommands;

import JDA.core.Main;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Nickname {
    GuildMessageReceivedEvent event;
    public Nickname (GuildMessageReceivedEvent event){
        this.event = event;
        String[] args = event.getMessage().getContentRaw().split(" ");

        int exception = 0;
        String ausgabe = "*ausgabe*";
        User user = event.getAuthor();
        String name = "";
        if(event.getMessage().getMentionedMembers().size() > 0){
            if(event.getMessage().getMentionedMembers().size() > 1)
                if(gibts(args[1].substring(2, args[1].length() - 1))){
                    exception = 1;
                }
                else {
                    exception = 2;
                }
            else{
                user = event.getMessage().getMentionedMembers().get(0).getUser();
                if(args[1].equals(event.getGuild().getMember(user).getAsMention())) {
                    System.out.println(" User anhand von Mention festgelegt");
                    if (args.length > 2) {
                        name = zueinem(args, 2);
                    } else {
                        name = user.getName();
                    }
                }
                else{
                    exception = 6;
                }
            }
        }
        else if(args.length > 1) {
            int keinanderer = 0;
            for (int i = 1; i < args.length; i++) {
                if (gibts(args[i])) {
                    keinanderer++;
                }
            }
            if (keinanderer == 1) {
                if(isteineID(args[1])){
                try {
                    user = User.fromId(Long.parseLong(args[1]));
                    user = event.getJDA().getUserById(user.getId());
                    System.out.println(" User anhand ID festgelegt");
                    if(args.length > 2){
                        name = zueinem(args, 2);
                    }
                    else{
                        name = user.getName();
                    }
                } catch (UnsupportedOperationException uoex) {
                    System.out.println("Fehler: " + uoex.getLocalizedMessage());
                } catch (NumberFormatException nfex) {
                    System.out.println("Fehler: " + nfex.getLocalizedMessage());
                }
                }
                else{
                    exception = 5;
                }
            }
            else if(keinanderer > 1){
                exception = 3;
            }
        }
        else {
           user = event.getAuthor();
           if(args.length > 1){
               name = zueinem(args, 2);
           }
           else{
               name = user.getName();
           }
        }
        if(exception == 0 && !event.getGuild().getMemberById(Main.IDlong).canInteract(event.getGuild().getMember(user))){
            exception = 4;
        }
        name = formatierungentfernen(name);
        System.out.println("  exeption: " + exception);
        switch (exception){
            case 1: ausgabe = "Du darfst nur einen User erwähnen"; break;
            case 6: ausgabe = "Erwähne den User bitte als erstes"; break;
            case 2: ausgabe = "Gib deinen User bitte an erster Stelle an"; break;
            case 3: ausgabe = "Bitte gib nur eine User-Id an"; break;
            case 4: ausgabe = "Ich bin leider nicht befugt, den von Namen von " + user.getName() + " zu ändern"; break;
            case 5: ausgabe = "Bitte gib die User-Id als erstes an"; break;
            default:
                ausgabe = event.getMember().getAsMention();
                ausgabe += " du hast den Namen von ";
                ausgabe += user.getName();
                ausgabe += " auf '";
                ausgabe += name;
                ausgabe += "' geändert";
                event.getGuild().getMember(user).modifyNickname(name).queue();
                break;
        }
        System.out.println("  User: " + user.getName() + " -->  Name: " + name);
        event.getMessage().reply(ausgabe).queue();
    }
    public static String zueinem (String[] eingabearray, int start) {
        String ausgabe = "";
        if (start < eingabearray.length) {
            for (int i = start; i < eingabearray.length; i++){
                ausgabe += eingabearray[i];
                ausgabe += " ";
            }
            ausgabe = ausgabe.substring(0, ausgabe.length()-1);
        }
        else{
            System.out.println("Fehler: start höher als eingabearray.length");
        }
        return ausgabe;
    }
    public User userbyid(Long ID){
        User ausgabe = null;
        try{
            ausgabe = event.getJDA().getUserById(ID);
        }
        catch (UnsupportedOperationException uoex){
            System.out.println("Fehler: " + uoex.getLocalizedMessage());
        }
        return ausgabe;
    }
    public boolean gibts (String eingabe) {
        boolean ausgabe = false;
        long ID;
        try {
            ID = Long.parseLong(eingabe);
            if (userbyid(ID) != null) {
                ausgabe = true;
            }
        } catch (NumberFormatException nfex) {
            ausgabe = false;
        }
        return ausgabe;
    }
    private String formatierungentfernen(String eingabe){
        String ausgabe = eingabe;
        ausgabe = ausgabe.replace("*", "");
        ausgabe = ausgabe.replace("*", "");
        ausgabe = ausgabe.replace("_", "");
        ausgabe = ausgabe.replace("`", "");
        ausgabe = ausgabe.replace("~~", "");
        ausgabe = ausgabe.replace("`", "");
        ausgabe = ausgabe.replace("||", "");
        return ausgabe;

    }
    private boolean isteineID (String eingabestring){
        boolean ausgabe = true;
        try{
            Long.parseLong(eingabestring);
        }
        catch (NumberFormatException nfex){
            ausgabe = false;
        }
        if(ausgabe && eingabestring.length() != 18){
            ausgabe = false;
        }
        return ausgabe;
    }
}

