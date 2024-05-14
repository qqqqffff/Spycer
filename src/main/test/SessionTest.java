import com.apollor.spycer.Application;
import com.apollor.spycer.database.Database;
import com.apollor.spycer.database.Session;
import com.apollor.spycer.database.User;
import com.apollor.spycer.utils.SessionHandler;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

public class SessionTest {
    @Test
    public void testSession() throws IOException, InterruptedException {
//        User u = SessionHandler.createUser("1apollo.rowe@gmail.com", "apollo", "123");
//        SessionHandler.attemptLogin(u.emailAddress, "123");

//        String i = new Timestamp(System.currentTimeMillis()).toInstant().toString();
//        String zid = ZoneId.systemDefault().getRules().getOffset(Instant.parse(i)).toString();
//        String ts_a = i.substring(0,i.length() - 1) + "000" + zid;
//
//        i = new Timestamp(System.currentTimeMillis() + Application.defaultTokenExpireTime.toMillis()).toInstant().toString();
//        String ts_b = i.substring(0,i.length() - 1) + "000" + zid;
//
//        Session session = new Session(
//                UUID.randomUUID().toString(),
//                "ad828485-4603-43fa-a4ab-9f307eb7596b",
//                0.0,
//                0.0,
//                ts_a,
//                ts_b,
//                "2apollo.rowe@gmail.com",
//                false
//        );
//        Database.postSession(session);
    }
}
