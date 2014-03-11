package kkma;

import org.snu.ids.ha.ma.MExpression;
import org.snu.ids.ha.ma.MorphemeAnalyzer;
import org.snu.ids.ha.ma.Sentence;

import java.util.List;
import java.util.Scanner;

// import java.io.PrintStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.nio.charset.StandardCharsets.*;

public class KKMAProcessor {
    private static final Logger log = LoggerFactory.getLogger(KKMAProcessor.class);
    
    public static void main(String [] args) {
        //System.setProperty("DO_DEBUG", "DO_DEBUG");
        MorphemeAnalyzer ma = new MorphemeAnalyzer();

        // create logger, null then System.out is set as a default logger
        ma.createLogger(null);

        Scanner sc = new Scanner(System.in, UTF_8.name());
        // PrintStream out = new PrintStream(System.out, true, UTF_8.name())
        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out, UTF_8))) {
            while (sc.hasNextLine()) {
                String s = sc.nextLine();

                // analyze morpheme without any post processing 
                List<MExpression> ret = ma.analyze(s);

                // refine spacing
                ret = ma.postProcess(ret);

                // leave the best analyzed result
                ret = ma.leaveJustBest(ret);

                // divide result to setences
                List<Sentence> stl = ma.divideToSentences(ret);

                // print the result
                for (int i = 0; i < stl.size(); ++i) {
                    Sentence st = stl.get(i);
                    out.println("===>  " + st.getSentence());
                    for (int j = 0; j < st.size(); ++j) {
                        out.println(st.get(j));
                    }
                }
            }

            out.println("Done with KKMA!");
            out.flush();
        }
        catch (Exception e) {
            // out.flush();
            log.error("Encountered ERROR: " + e.getMessage(), e);
            System.err.println("Encountered ERROR: " + e.getMessage());
        }

        ma.closeLogger();
    }
};

