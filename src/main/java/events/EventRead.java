package events;


import googleTable.Connection;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.IOException;
import java.security.GeneralSecurityException;



public class EventRead extends ListenerAdapter {

    public static String [] getMessage() {
        return message;
    }

    private static String [] message;

    public void onGuildMessageReceived(GuildMessageReceivedEvent eventRead) {

        message = eventRead.getMessage().getContentRaw().split("\n");

        if (message[0].startsWith("1.") || message[0].startsWith ("1)") ||
                message[0].startsWith("1")) {

            if (!eventRead.getMember().getUser().isBot()) {
                try {
                    Connection.writeData();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (GeneralSecurityException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

}
