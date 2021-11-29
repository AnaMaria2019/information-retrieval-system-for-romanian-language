import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.document.Document;


public class SearchFiles {
    public static void main(String[] args) throws IOException {
        String pathToQuery;

        if (args.length != 2) {
            System.out.println("There are too many arguments, you are only allowed" +
                    " to pass 2, the first argument must be '--query-path' and the" +
                    " second one, the path to the query file.");
            System.exit(1);
        }

        if (!args[0].equals("--query-path")) {
            System.out.println("The first argument must be '--query-path'.");
            System.exit(1);
        }

        pathToQuery = args[1];
        File queryFile = new File(pathToQuery);
        String queryFileContent = FileUtils.readFileToString(queryFile, "UTF-8");
        queryFileContent = queryFileContent.replaceAll("[ăâ]", "a");
        queryFileContent = queryFileContent.replaceAll("[ș]", "s");
        queryFileContent = queryFileContent.replaceAll("[ț]", "t");
        queryFileContent = queryFileContent.replaceAll("[î]", "i");

        Path indexDirectoryPath = Paths.get("index");
        Directory indexDirectory = FSDirectory.open(indexDirectoryPath);
        IndexReader reader = DirectoryReader.open(indexDirectory);

        IndexSearcher searcher = new IndexSearcher(reader);
        CustomSearchAnalyzer analyzer = new CustomSearchAnalyzer(queryFileContent);
        QueryParser parser = new QueryParser("contents", analyzer);
        Query query = null;

        try {
            query = parser.parse(queryFileContent);
        } catch (ParseException e) {
            System.out.println("Exception occurred while trying to parse the query!");
        }

        TopDocs results = searcher.search(query, 10);
        ScoreDoc[] hits = results.scoreDocs;
        for(ScoreDoc scoreDoc : hits){
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println("Document path: " + doc.get("path"));
            System.out.println("Score: " + scoreDoc.score);
            System.out.println("\n");
        }
    }
}

