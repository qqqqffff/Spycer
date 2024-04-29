import com.apollor.spycer.database.Database;
import com.apollor.spycer.database.User;
import com.apollor.spycer.utils.SessionHandler;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {
    @Test
    public void testUser() throws IOException {
        User user = SessionHandler.createUser("2apollo.rowe@gmail.com", "apollo", "123");
        assertEquals(user.displayName, "apollo");
        assertTrue(SessionHandler.attemptLogin("2apollo.rowe@gmail.com", "123"));
        user.displayName = "apollinaris";
        Database.putUser(user);
        assertEquals(Database.getUser("2apollo.rowe@gmail.com").displayName, "apollinaris");
        Database.deleteUser(user.userId);
    }
}
