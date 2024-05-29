import com.apollor.spycer.utils.RecipeHandler;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class RecipeHandlerTest {
    @Test
    public void compilationTest() throws IOException, URISyntaxException {
        Map<String, Map<String, String>> data = new HashMap<>();
        Map<String, String> options = new HashMap<>();
        options.put("title", "test recipe");
        data.put("options", options);
        RecipeHandler.compileRecipe(data);
    }
}
