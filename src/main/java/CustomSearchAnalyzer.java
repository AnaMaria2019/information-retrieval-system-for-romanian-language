import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;


public class CustomSearchAnalyzer extends Analyzer {
    private final String queryString;

    public CustomSearchAnalyzer(String userQuery){
        queryString = userQuery;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName){
        File roStopWordsFile = new File("ro-stop-words.txt");
        CharArraySet roStopWords = null;

        try {
            roStopWords = Utils.getRoStopWords(roStopWordsFile);
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

