import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvToBeanBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.regex.Pattern;

public class LoadRecipeTest {
    public static class Food {
        public Food(){}
        @CsvBindByPosition(position = 0)
        public String food;
    }
    public static class Measurements{
        public Measurements(){}
        @CsvBindByPosition(position = 0)
        public String measurement;
    }

    @Test
    public void foodGrabber(){
        String url = "https://foodforbreastcancer.com/food-list.php";
        List<String> foods = new ArrayList<>();
        try{
            Document doc = Jsoup.connect(url).get();
            foods.addAll(doc.select("a").stream().filter(x -> {
                Attribute href = x.attribute("href");
                return href != null && href.toString().contains("foods");
            }).map(Element::ownText).toList());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        foods.forEach(System.out::println);
    }

    @Test
    public void recipeReader(){
        String url = "https://www.thecookierookie.com/homemade-tartar-sauce-recipe/";
        url = "https://www.inspiredtaste.net/37062/juicy-skillet-pork-chops/";
        try {
            String base_path = Paths.get("").toAbsolutePath() + "/src/main/resources/com/apollor/spycer/tables/";
            File f = new File(base_path + "food_items.csv");
            List<Food> foods = new CsvToBeanBuilder<Food>(new FileReader(f))
                    .withType(Food.class)
                    .build()
                    .parse();
            f = new File(base_path + "measurements.csv");
            List<Measurements> measurements = new CsvToBeanBuilder<Measurements>(new FileReader(f))
                    .withType(Measurements.class)
                    .build()
                    .parse();
//
//
            Document doc = Jsoup.connect(url).get();
            //span, div, li, h
            List<String> ingredients = new ArrayList<>();
            //tags
            List<String> metas = new ArrayList<>();

            Thread spans = new Thread(() -> {
                ingredients.addAll(doc.select("span").eachText().stream().map(String::toLowerCase).toList());
            });
            Thread h = new Thread(() -> {
                ingredients.addAll(doc.select("h").eachText().stream().map(String::toLowerCase).toList());
            });
            Thread div = new Thread(() -> {
                ingredients.addAll(doc.select("div").eachText().stream().map(String::toLowerCase).toList());
            });
            Thread li = new Thread(() -> {
                ingredients.addAll(doc.select("li").eachText().stream().map(String::toLowerCase).toList());
            });
            Thread p = new Thread(() -> {
                ingredients.addAll(doc.select("p").eachText().stream().map(String::toLowerCase).toList());
            });
            Thread meta = new Thread(() -> {
                metas.addAll(doc.select("meta").eachText().stream().map(String::toLowerCase).toList());
            });

            spans.start();
            h.start();
            div.start();
            li.start();
            p.start();
            meta.start();
            if(!spans.join(Duration.ofSeconds(5)) ||
                    !h.join(Duration.ofSeconds(5)) ||
                    !div.join(Duration.ofSeconds(5)) ||
                    !li.join(Duration.ofSeconds(5)) ||
                    !meta.join(Duration.ofSeconds(5)) ||
                    !p.join(Duration.ofSeconds(5))
            ) throw new RuntimeException("Could not collect elements");

            //collecting around an indexes of 'ingredients'
            HashSet<String> noDuplicates = new LinkedHashSet<>(ingredients);
            ingredients.clear();
            ingredients.addAll(noDuplicates);
            int alpha = (int) Math.sqrt(ingredients.size()) * 2;

            System.out.println(alpha);
            Map<String, Double> similarityMap = new LinkedHashMap<>();
            for(int i = 0; i < ingredients.size(); i++){
                HashMap<Character, Integer> occurrences = new HashMap<>();
                String temp = ingredients.get(i).replaceAll("\\W", "");
                for(char c : temp.toCharArray()){
                    occurrences.put(c, occurrences.getOrDefault(c, 0) + 1);
                }
                double maxSimilarity = Integer.MIN_VALUE;
                double maxSimilarMeasure = Integer.MIN_VALUE;
                String mostSimilar = "";
                String mostSimilarMeasure = "";
                for(String name : foods.stream().map(y -> y.food.toLowerCase()).toList()){
                    //arbitrary values
                    if(temp.contains(name)) {
                        double lengthDifference = Math.abs(name.length() - temp.length()) * -(1/3.0);
                        int sameChars = 0;
                        Map<Character, Integer> tempOccurrence = new HashMap<>(occurrences);
                        for(char c : name.toCharArray()){
                            if(tempOccurrence.getOrDefault(c, 0) != 0){
                                occurrences.put(c, tempOccurrence.get(c) - 1);
                                sameChars++;
                            }
                        }
                        double similarity = lengthDifference + sameChars;
                        if(similarity > maxSimilarity){
                            mostSimilar = name;
                            maxSimilarity = similarity;
                        }
                    }
                }
                for(String measure : measurements.stream().map(y -> y.measurement.toLowerCase()).toList()){
                    if(temp.contains(measure)){
                        int sameChars = 0;
                        Map<Character, Integer> tempOccurrence = new HashMap<>(occurrences);
                        for(char c : measure.toCharArray()){
                            if(tempOccurrence.getOrDefault(c, 0) != 0){
                                tempOccurrence.put(c, occurrences.get(c) - 1);
                                sameChars++;
                            }
                        }
                        double similarity = sameChars;
                        if(similarity > maxSimilarMeasure){
                            mostSimilarMeasure = measure;
                            maxSimilarity = similarity;
                        }
                    }
                }
                maxSimilarity += maxSimilarMeasure * 3;
                if(maxSimilarity > -100) {
                    System.out.println(temp + ":" + mostSimilar + "," + maxSimilarity);
                    similarityMap.put(ingredients.get(i), maxSimilarity);
                }
            }
//            avgOccurences /= totalOccurences;
//            int bottom = Math.max(avgOccurences - alpha, 0);
//            int top = Math.min(avgOccurences + alpha + 1, ingredients.size() - 1);
//            noDuplicates = new HashSet<>(ingredients.subList(bottom, top));
//            ingredients.clear();
//            ingredients.addAll(noDuplicates);
            System.out.println("INGREDIENTS (" + similarityMap.size() + ") :");
            similarityMap.forEach((x, y) -> System.out.println(x + "," + y));
//            ingredients.forEach(System.out::println);
//            for(int i : ingredientIndexes){
//                //initial guess

//                List<String> sublist = ingredients.subList(bottom, top);
//                int totalOccr = 0;
//                int totalSimilarity = 0;
//                int averageIndex = 0;
//                int index = 0;
//
                //computing similarity
//                for(String line : sublist){

//                    index++;
//                }
//                if(compVector[0] < totalOccr ||
//                    compVector[0] == totalOccr && totalSimilarity > compVector[1]) {
//
//                    alpha /= 2;
//                    averageIndex /= totalOccr;
//                    compVector[1] = totalSimilarity;
//                    compVector[0] = totalOccr;
//
//                    //re-centering
//                    top = Math.min(averageIndex + alpha + 1, ingredients.size() - 1);
//                    bottom = Math.max(averageIndex - alpha, 0);
//
//                    System.out.println(top + ", " + bottom + ", " + averageIndex + "," + alpha);
//
//                    maxOccurrences.clear();
//                    maxOccurrences.addAll(ingredients.subList(bottom, top));
//                }
//            }
//            System.out.println(new HashSet<>(ingredients.stream()
//                    .filter(x -> !x.isEmpty()).toList()));

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
