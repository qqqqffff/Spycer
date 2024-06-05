import com.apollor.spycer.Application;
import com.apollor.spycer.database.Database;
import com.apollor.spycer.database.User;
import com.apollor.spycer.utils.SessionHandler;
import javafx.scene.image.Image;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {
    @Test
    public void testUser() throws IOException {
        User user = SessionHandler.createUser("9apollo.rowe@gmail.com", "apollo", "123");
        assertEquals(user.displayName, "apollo");
        assertTrue(SessionHandler.attemptLogin("9apollo.rowe@gmail.com", "123", false));
        user.displayName = "apollinaris";
        Database.putUser(user);
        assertEquals(Database.getUser("9apollo.rowe@gmail.com").displayName, "apollinaris");
        Database.deleteUser(user.userId);
    }

    @Test
    public void userPFPTest() throws IOException {
        assertTrue(SessionHandler.attemptLogin("1apollo.rowe@gmail.com", "1Apollorow@!!", false));
        File pfp = new File(Application.datadir + "/pfp.png");
//        System.out.println(Database.postUserPFP(pfp));
        Image i = Database.getUserPFP(SessionHandler.getLoggedInUser().userId);
    }
}
