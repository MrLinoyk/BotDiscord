import events.EventRead;
import googleTable.Connection;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import javax.security.auth.login.LoginException;


public class BotMain {
    public static void main(String[] args) throws LoginException {

        JDA jda = new JDABuilder(AccountType.BOT).setToken("NTY3MDg3ODU5MTM2MjAwNzA0.XLRfhw.sc0OVej92Kvp3A7d-1ETAmMlVjM").build();
        jda.addEventListener(new Connection());
        jda.addEventListener(new EventRead());
    }
}
