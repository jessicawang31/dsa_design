import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

// import our MinPQ and OptimizedHeapMinPQ
import minpq.MinPQ;
import minpq.OptimizedHeapMinPQ;

/**
 * Display the most commonly-reported WCAG recommendations.
 */
public class ReportAnalyzer {
    public static void main(String[] args) throws IOException {
        File inputFile = new File("data/wcag.tsv");
        Map<String, String> wcagDefinitions = new LinkedHashMap<>();
        Scanner scanner = new Scanner(inputFile);
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split("\t", 2);
            String index = "wcag" + line[0].replace(".", "");
            String title = line[1];
            wcagDefinitions.put(index, title);
        }

        Pattern re = Pattern.compile("wcag\\d{3,4}");
        List<String> wcagTags = Files.walk(Paths.get("data/reports"))
                .map(path -> {
                    try {
                        return Files.readString(path);
                    } catch (IOException e) {
                        return "";
                    }
                })
                .flatMap(contents -> re.matcher(contents).results())
                .map(MatchResult::group)
                .toList();

        // TODO: Display the most commonly-reported WCAG recommendations using MinPQ
        // minpq to keep track of wcag tags
        MinPQ<String> tags = new OptimizedHeapMinPQ<>();
        for (String tag : wcagTags) {
            if (!tags.contains(tag)) {
                tags.add(tag, 0);
            } else {
                tags.changePriority(tag, tags.getPriority(tag) - 1);
            }
        }

        // display top 3 wcag recs
        for (int i = 0; i < 3; i++) {
            if (!tags.isEmpty()) {
                String tag = tags.removeMin();
                String wcagTag = wcagDefinitions.get(tag);
                System.out.println(wcagTag + ": " + tag);
            }
        }
    }
}