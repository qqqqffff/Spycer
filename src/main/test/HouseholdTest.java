import com.apollor.spycer.database.Database;
import com.apollor.spycer.database.Household;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

public class HouseholdTest {
    @Test
    public void restAPITest() throws IOException {
        Household original = Database.getHouseholdByUID("6ea9715e-5034-4153-b6e6-975dd26d081f");
        System.out.println(original.householdName);
        Household[] households = Database.getHouseholdsByID(original.householdId);
        System.out.println(Arrays.toString(households));
    }
}
