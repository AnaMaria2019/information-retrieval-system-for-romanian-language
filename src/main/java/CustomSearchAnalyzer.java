import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;


public class CustomSearchAnalyzer extends Analyzer {
    private String queryString;

    public CustomSearchAnalyzer(String userQuery){
        queryString = userQuery;
    }

    private static CharArraySet getRoStopWords(File stopWordsFile) throws IOException {
        String content = FileUtils.readFileToString(stopWordsFile, "UTF-8");
        // Remove diacritics of the stop words
        content = content.replaceAll("[ăâ]", "a");
        content = content.replaceAll("[ț]", "t");
        content = content.replaceAll("[ș]", "s");
        content = content.replaceAll("[î]", "i");
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

    @Override
    protected TokenStreamComponents createComponents(String fieldName){
        File roStopWordsFile = new File("ro-stop-words.txt");
        CharArraySet roStopWords = null;

        try {
            roStopWords = getRoStopWords(roStopWordsFile);
        } catch (IOException e) {
            System.out.println("Stop words file might be corrupted!");
        }

        Tokenizer source = new StandardTokenizer();
        source.setReader(new StringReader(queryString));
        TokenStream tokens = null;

        tokens = new LowerCaseFilter(source);
        tokens = new StopFilter(tokens, roStopWords);
        tokens = new SnowballFilter(tokens, "Romanian");

        return new TokenStreamComponents(source, tokens);
    }
}

