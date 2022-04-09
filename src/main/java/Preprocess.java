import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import cz.zcu.kiv.nlp.ir.AdvancedTokenizer;
import cz.zcu.kiv.nlp.ir.BasicPreprocessing;
import cz.zcu.kiv.nlp.ir.PorterStemmerWrapper;
import cz.zcu.kiv.nlp.ir.Preprocessing;
import cz.zcu.kiv.nlp.ir.metacritic.MetacriticGame;
import cz.zcu.kiv.nlp.ir.metacritic.MetacriticJsonDeserializer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import org.apache.log4j.Logger;

public class Preprocess {

    // "Java Script"

    private static final Logger LOGGER = Logger.getLogger(Preprocess.class.getName());

    private static final LanguageDetector detector = LanguageDetectorBuilder.fromAllLanguages()
            .build();

    private static final Set<String> defaultStopWords = Set.of("http", "https", ".", "?", "!", "/",
            "this", "these", "my", "yourself", "ourselves", "themselves", "them",
            "yourselves", "his", "hers", "its", "theirs", "being", "am", "mine", "yours", "despite", "while", "their");

    private static Set<String> loadStopWords() {
        try {
            var lines = Files.readAllLines(Paths.get("src/main/resources/stopwords.txt"));
            return Set.copyOf(lines);
        } catch (IOException exception) {
            LOGGER.error("Error while loading stopwords: " + exception.getMessage());
            LOGGER.error("Using default stopwords instead.");
            return defaultStopWords;
        }
    }

    private static void processMetacriticGame(MetacriticGame game, Preprocessing preprocessor) {
        game.getUserReviews().forEach(userReview -> {
            var text = userReview.getText();
            var language = detector.detectLanguageOf(text);
            if (language != Language.ENGLISH) {
                return;
            }

            userReview.setText(preprocessor.getProcessedForm(text));
        });
    }

    public static void main(String[] args) throws IOException {
        var preprocessor = new BasicPreprocessing(
                new PorterStemmerWrapper(), new AdvancedTokenizer(), loadStopWords(),
                false, true, true);

        var metacriticGames = new MetacriticJsonDeserializer().deserialize("rawdata.json");

        // Filter non-english reviews as they are completely useless for us
        metacriticGames.forEach(game -> processMetacriticGame(game, preprocessor));

        var objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("preprocessed.json"), metacriticGames);
        LOGGER.info("Prepocessed data written to preprocessed.json");
    }
}
