import com.apollor.spycer.Application;
import com.apollor.spycer.utils.RecipeHandler;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class RecipeHandlerTest {
    @Test
    public void compilationTest() throws IOException, URISyntaxException {
        Map<String, Map<Integer, String[]>> data = new HashMap<>();
        Map<Integer, String[]> options = new HashMap<>();
        options.put(0, new String[]{"test recipe"});
        options.put(1, new String[]{"2.7"});
        options.put(2, new String[]{"Apollo Rowe"});
        data.put("options", options);
        Map<Integer, String[]> ingredients = new HashMap<>();
        ingredients.put(0, new String[]{"butter", "1 cup"});
        data.put("ingredients", ingredients);
        Map<Integer, String[]> procedures = new HashMap<>();
        procedures.put(0, new String[]{"Mix thoroughly", "1_h"});
        data.put("procedures", procedures);
        Map<Integer, String[]> notes = new HashMap<>();
        notes.put(0, new String[]{"a very insightful note"});
        data.put("notes", notes);
        Map<Integer, String[]> tags = new HashMap<>();
        tags.put(0, new String[]{"delicious"});
        data.put("tags", tags);
        RecipeHandler.compileRecipe(data);
    }
}
