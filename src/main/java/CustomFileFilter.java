import java.io.File;
import java.io.FileFilter;

import org.apache.tika.Tika;

import java.io.IOException;


public class CustomFileFilter implements FileFilter {
    @Override
    public boolean accept(File file) {
        Tika tika = new Tika();
        boolean isFileAccepted = false;

        try {
            String fileType = tika.detect(file);

            if (fileType.equals("application/pdf") ||
                    fileType.equals("text/plain") ||
                    fileType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
            ) {
                isFileAccepted = true;
            }

        } catch (IOException error) {
            return isFileAccepted;
        }

        return isFileAccepted;
    }
}

