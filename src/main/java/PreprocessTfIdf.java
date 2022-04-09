import com.fasterxml.jackson.databind.ObjectMapper;
import cz.zcu.kiv.nlp.ir.AdvancedTokenizer;
import cz.zcu.kiv.nlp.ir.BasicPreprocessing;
import cz.zcu.kiv.nlp.ir.CzechStemmerAgressive;
import cz.zcu.kiv.nlp.ir.PorterStemmerWrapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;

public class PreprocessTfIdf {

    @Data
    @AllArgsConstructor
    static
    class Document {

        private String text;
    }

    public static void main(String[] args) throws IOException {
        var czDocuments = new ArrayList<String>();
        czDocuments.add("Plzeň je krásné město a je to krásné místo");
        czDocuments.add("Ostrava je ošklivé místo");
        czDocuments.add("Praha je také krásné město Plzeň je hezčí");

        var enDocuments = new ArrayList<String>();
        enDocuments.add("tropical fish include fish found in tropical enviroments");
        enDocuments.add("fish live in a sea");
        enDocuments.add("tropical fish are popular aquarium fish");
        enDocuments.add("fish also live in Czechia");
        enDocuments.add("Czechia is a country");

        // Load czech stopwords
        var czStopWords = Files.readAllLines(Paths.get("src/main/resources/czechST.txt"));
        var enStopWords = Files.readAllLines(Paths.get("src/main/resources/stopwords.txt"));

        var preprocessor = new BasicPreprocessing(
                new CzechStemmerAgressive(),
                new AdvancedTokenizer(),
                new HashSet<>(czStopWords),
                true,
                true,
                true
        );

        var preprocessedCzDocuments = czDocuments.stream()
                .map(preprocessor::getProcessedForm)
                .map(Document::new)
                .toList();

        preprocessor = new BasicPreprocessing(
                new PorterStemmerWrapper(),
                new AdvancedTokenizer(),
                new HashSet<>(enStopWords),
                true,
                true,
                true
                );

        var preprocessedEnDocuments = enDocuments.stream()
                .map(preprocessor::getProcessedForm)
                .map(Document::new)
                .toList();

        // Serialize
        var objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("tfidf_cz.json"), preprocessedCzDocuments);
        objectMapper.writeValue(new File("tfidf_en.json"), preprocessedEnDocuments);
    }
}
