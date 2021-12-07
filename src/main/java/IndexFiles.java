import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriter;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.metadata.Metadata;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.commons.io.FileUtils;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;


// TikaException is raised if a stream can be read, but not parsed
// (for example when the document is corrupted).
public class IndexFiles {
    private static CharArraySet getRoStopWords(File stopWordsFile) throws IOException {
        String content = FileUtils.readFileToString(stopWordsFile, "UTF-8");
        // Remove diacritics of the stop words
        content = Utils.removeRoDiacritics(content);

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

    private static TokenStream getDocumentTokens(String content) throws IOException {
        Tokenizer tokenizer = new StandardTokenizer();
        tokenizer.setReader(new StringReader(content));
        StringBuilder sb = new StringBuilder();

        File roStopWordsFile = new File("ro-stop-words.txt");
        CharArraySet roStopWords = getRoStopWords(roStopWordsFile);

        TokenStream finalTokens;

        CharTermAttribute token = tokenizer.getAttribute(CharTermAttribute.class);

        tokenizer.reset();
        while(tokenizer.incrementToken()){
            if(sb.length() > 0){
                sb.append(" ");
            }
            sb.append(token.toString());
        }

        tokenizer.end();
        tokenizer.close();

        String tempTokens = sb.toString().toLowerCase();
        tempTokens = Utils.removeRoDiacritics(tempTokens);

        Tokenizer tokenizer2 = new StandardTokenizer();
        tokenizer2.setReader(new StringReader(tempTokens));

        finalTokens = new LowerCaseFilter(tokenizer2);
        finalTokens = new StopFilter(finalTokens, roStopWords);
        finalTokens = new SnowballFilter(finalTokens, "Romanian");

        return finalTokens;
    }

    private static Document createDocument(File document) throws IOException, TikaException {
        Document doc = new Document();
        Tika tika = new Tika();
        TokenStream documentTokens = null;
        String documentContent;

        String documentType = tika.detect(document);

        switch (documentType) {
            case "application/pdf":
                // System.out.println("This is a pdf file.");
                BodyContentHandler handler = new BodyContentHandler();
                Metadata metadata = new Metadata();
                FileInputStream inputStream = new FileInputStream(document);
                ParseContext pContext = new ParseContext();
                PDFParser pdfparser = new PDFParser();

                try {
                    pdfparser.parse(inputStream, handler, metadata, pContext);
                    documentContent = handler.toString();
                    documentTokens = getDocumentTokens(documentContent);
                } catch (Exception TikaException) {
                    System.out.println("The document is corrupted!");
                } finally {
                    inputStream.close();
                }
                break;
            case "text/plain":
                documentContent = FileUtils.readFileToString(document, "UTF-8");
                documentTokens = getDocumentTokens(documentContent);
                break;
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                documentContent = tika.parseToString(document);
                documentTokens = getDocumentTokens(documentContent);
                break;
            default:
                System.out.println("We don't recognize the document type, sorry.");
        }

        if (documentTokens != null) {
            TextField contentField = new TextField("contents", documentTokens);
            StringField filePathField = new StringField("path", document.getCanonicalPath(), Field.Store.YES);
            doc.add(contentField);
            doc.add(filePathField);
        }

        StringField fileNameField = new StringField("file-name", document.getName(), Field.Store.YES);
        doc.add(fileNameField);

        return doc;
    }

    public static void main(String[] args) throws IOException, TikaException {
        IndexWriterConfig writerConfig = new IndexWriterConfig();

        if (args.length > 1) {
            System.out.println("You can pass only one argument, the open mode of the index component!");
            System.out.println("You have 2 options of valid argument:" +
                    " --open-mode=CREATE or --open-mode=APPEND");
            System.exit(1);
        }

        String indexPath = "index";
        String docsPath = "files";
        String openMode = args[0];

        if (openMode.equals("--open-mode=CREATE")) {
            writerConfig.setOpenMode(OpenMode.CREATE);
        } else if (openMode.equals("--open-mode=APPEND")) {
            writerConfig.setOpenMode(OpenMode.APPEND);
        } else {
            System.out.println("Check your argument! It must be one of the following options:" +
                    " --open-mode=CREATE or --open-mode=APPEND");
            System.out.println("The argument passed is: " + openMode);
        }

        Path indexDirectoryPath = Paths.get(indexPath);
        Directory indexDirectory = FSDirectory.open(indexDirectoryPath);
        IndexWriter writer = new IndexWriter(indexDirectory, writerConfig);

        File[] docs = new File(docsPath).listFiles();
        CustomFileFilter typeFilter = new CustomFileFilter();

        if (docs != null) {
            for (File document : docs) {
                if (typeFilter.accept(document) && document.canRead()) {
                    Document doc = createDocument(document);
                    writer.addDocument(doc);
                    System.out.println("We index the file: " + document.getName());
                }
            }
        }

        // NOTE: In order to be able to call the SearchFiles class the index writer must be closed,
        // otherwise, when trying to call SearchFiles you will get "IndexNotFoundException no segments*" exception.
        writer.close();
    }
}
