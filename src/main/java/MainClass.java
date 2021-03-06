import org.apache.log4j.BasicConfigurator;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

// When the application is started, the admin must send "run" message from his main account to turn on the bot.

// Maven Run configurations :-
// heroku:deploy

/*Required Environment Variables :-
 *
 * lastBountyHunterBotTokenA    = ?????;
 * lastBountyHunterBotTokenB    = ?????;
 * lastBountyHunterMonoID       = ?????;
 * lastBountyHunterMonoPass     = ?????;
 * PrivateKey                   = ?????;
 * PublicKey                    = ?????;
 * */

/*Ropsten Addresses :-

 * RTK   :- 0x38332D8671961aE13d0BDe040d536eB336495eEA     (Amount = 1000000000000 * (10^18))
 * RTKL2 :- 0x9C72573A47b0d81Ef6048c320bF5563e1606A04C
 * RTKL3 :- 0x136A5c9B9965F3827fbB7A9e97E41232Df168B08
 * RTKL4 :- 0xfB8C59fe95eB7e0a2fA067252661687df67d87b8
 * RTKL5 :- 0x99afe8FDEd0ef57845F126eEFa945d687CdC052d
 * AMMO  :- 0x88dD15CEac31a828e06078c529F5C1ABB214b6E8
 * Swap Contract :- 0x3CCB85af88DE1A148CC942eA9065c2E8b470cf11
 * */

public class MainClass {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        disableAccessWarnings();
        System.setProperty("com.google.inject.internal.cglib.$experimental_asm7", "true");

        // Starting Telegram bot and Web3 services
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new Last_Bounty_Hunter_Bot(System.getenv("LBHPublicKey")));
            telegramBotsApi.registerBot(new Pot_Shot_Bot("High", System.getenv("PSBHPublicKey")));
            //telegramBotsApi.registerBot(new Pot_Shot_Bot("Low", System.getenv("PSBLPublicKey")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void disableAccessWarnings() {
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field field = unsafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Object unsafe = field.get(null);

            Method putObjectVolatile = unsafeClass.getDeclaredMethod("putObjectVolatile", Object.class, long.class, Object.class);
            Method staticFieldOffset = unsafeClass.getDeclaredMethod("staticFieldOffset", Field.class);

            Class loggerClass = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field loggerField = loggerClass.getDeclaredField("logger");
            Long offset = (Long) staticFieldOffset.invoke(unsafe, loggerField);
            putObjectVolatile.invoke(unsafe, loggerClass, offset, null);
        } catch (Exception ignored) {
        }
    }
}