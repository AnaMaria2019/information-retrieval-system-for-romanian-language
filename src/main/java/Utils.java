import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.CharArraySet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Utils {
    // NOTE: By making a private constructor we will prevent from
    // instantiating an object of this class
    private Utils() {
        throw new AssertionError();
    }

    public static String removeRoDiacritics(String text) {
        text = text.replaceAll("[ăâ]", "a");
        text = text.replaceAll("[ț]", "t");
        text = text.replaceAll("[ș]", "s");
        text = text.replaceAll("[î]", "i");

        return text;
    }

    public static CharArraySet getRoStopWords(File stopWordsFile) throws IOException {
        String content = FileUtils.readFileToString(stopWordsFile, "UTF-8");
        // Remove diacritics of the stop words
        content = removeRoDiacritics(content);
        List<String> words = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < content.length(); i++){
            if(content.charAt(i) == '\n'){
                words.add(sb.toString());
                sb = new StringBuilder();
            } else{
                sb.append(content.charAt(i));
            }
        }

        return new CharArraySet(words, true);
    }
}
