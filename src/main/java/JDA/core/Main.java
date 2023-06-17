package JDA.core;


import JDA.Listeners.*;
import JDA.Games.StadtLandFluss.TeilvonSpiel.Runde;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.*;
import java.util.stream.IntStream;


/**
 * SchereSteinPapier:
 *                      -Private Message Embed nach Reaktion verbessern
 *                      -Revanche Möglichkeit
 *                      -Info
 * YouTube:
 *                      - wird ausgelöst, wenn eine Nachricht https://www.youtube.com/watch?v= enthält
 *                      - Reaction button Youtube
 *                      - Embed Antwort mit allen möglichen Werten (Channelname, Abos, ...)
 */

public class Main {
    public static final long IDlong = 910176776033038367L;
    public static final String ID = String.valueOf(IDlong);

    public static void mai (String[] args){
        Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        delay(2000L);
                        long start = System.currentTimeMillis();
                        while((System.currentTimeMillis() - start) < 3000){
                            System.out.println("thread1");
                        }
                    }
                }, "erster thread");
        thread1.start();
        long start = System.currentTimeMillis();
        while((System.currentTimeMillis() - start) < 3000){
            System.out.println("thread0");
        }
    }


    public static void main(String[] args) throws LoginException {
        JDABuilder jda = JDABuilder.createDefault("");
        jda.setActivity(Activity.playing("mit deiner Mum"));
        jda.setAutoReconnect(true);
        jda.setStatus(OnlineStatus.ONLINE);
        jda.enableIntents(
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_PRESENCES
                );
        jda.setChunkingFilter(ChunkingFilter.ALL);
        jda.setMemberCachePolicy(MemberCachePolicy.ALL);
        jda.enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.ACTIVITY);
        jda.enableIntents(GatewayIntent.GUILD_MEMBERS);
        jda.addEventListeners(new ListenerGuildCommands());
        jda.addEventListeners(new ListenerGuildReactions());
        jda.addEventListeners(new ListenerPrivateReactions());
        jda.addEventListeners(new ListenerPrivateCommands());
        jda.addEventListeners(new ListenerButtons());
        jda.addEventListeners(new ListenerUserUpdate());
        jda.addEventListeners(new ListenerSlashCommands());
        jda.build();

    }


    public static void delay(long milisekunden) {
        try {
            Thread.sleep(milisekunden);
        } catch (InterruptedException iex) {
            System.out.println("InterruptedExeption");
        }
    }


}
