package JDA.Listeners;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import JDA.Games.StadtLandFluss.Aktionen.Buttons;

public class ListenerButtons extends ListenerAdapter {
    @Override
    public void onButtonClick(ButtonClickEvent event){
        System.out.println("Clicked on button - Member: \"" + event.getUser().getName() + "\", Button: \"" + Objects.requireNonNull(event.getButton()).getId() + "\"");
        switch (Objects.requireNonNull(Objects.requireNonNull(event.getButton()).getId())){
            case "schere":
                event.getChannel().sendMessage("der bus fehlt").queue();
                event.getMessage().editMessage(event.getMessage()).queue();
                event.getInteraction().editButton(event.getButton().asDisabled()).queue();
                break;
            case "pog":
                event.getChannel().sendMessage("pog").queue();
                break;
            case "danger":
                danger(event.getMember(), event.getChannel());
                break;
            case "cat":
                event.getChannel().sendMessage(":smile_cat:").queue();
                break;
            case Buttons.jointext:
                Buttons.handlejoin(event);
                break;
            case Buttons.leavetext:
                Buttons.handleleave(event);
                break;
            case Buttons.starttext:
                Buttons.handlestart(event);
                break;
            case Buttons.finishsettings:
                Buttons.handlefinishsettings(event);
                break;
            default:
                System.out.println("Unnkown Button: " + event.getChannel().getId());
                break;
        }
        if(!event.isAcknowledged()) {
            event.deferEdit().queue();
        }
    }
    public void danger(Member member, MessageChannel channel){
        String name = "radioaktiv dumm wie Timon";
        Role roletoadd = null;
        boolean istschongefährlich = false;
        for(Role r : member.getRoles()){
            System.out.print("rollenname: " + r.getName());
            if(r.getName().equals(name)){
                istschongefährlich = true;
                roletoadd = r;
            }
            System.out.println();
        }
        if(!istschongefährlich) {
            roletoadd = null;
            List<Role> rolesfromserver = new ArrayList<>(member.getGuild().getRoles());
            for (Role role : rolesfromserver) {
                if (role.getName().equals(name)) {
                    roletoadd = role;
                    break;
                }
            }
            if (roletoadd == null) {
                rolesfromserver = new ArrayList<>(member.getGuild().getRoles());
                member.getGuild().createRole().setName(name).setColor(Color.red).queue();
                for (Role role : rolesfromserver) {
                    if (role.getName().equals(name)) {
                        roletoadd = role;
                        break;
                    }
                }
            }
            System.out.println("roletoadd: " + roletoadd.getName() + "  - member: " + member.getEffectiveName());
            try {
                member.getGuild().addRoleToMember(member, roletoadd).queue();
            }
            catch (Exception ignored){

            }
            channel.sendMessage("du bist nun gefährlich").queue();
        }
        else{
            member.getGuild().removeRoleFromMember(member, roletoadd).queue();
            channel.sendMessage("du bist nun nicht mehr gefährlich").queue();
        }
    }
}
