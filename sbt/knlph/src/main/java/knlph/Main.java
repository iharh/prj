package knlph;

//import kaist.cilab.db.FileManager;
import kaist.cilab.tripleextractor.util.Configuration;
import kaist.cilab.parser.berkeleyadaptation.BerkeleyParserWrapper;
import kaist.cilab.parser.corpusconverter.sejong2treebank.sejongtree.ParseTree;
import kaist.cilab.parser.dependency.DTree;
import kaist.cilab.parser.psg2dg.Converter;

import java.util.Scanner;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import static java.nio.charset.StandardCharsets.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in, UTF_8.name());
        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out, UTF_8))) {
            while (sc.hasNextLine()) {
                String sentence = sc.nextLine();

                // CFG Parsing and dependency parsing part
                BerkeleyParserWrapper bpw = new BerkeleyParserWrapper(Configuration.parserModel);			
                //1. parse the sentence
                String parsResult = bpw.parse(sentence);
                //2. convert PSG-> DG
                Converter cv = new Converter();
                //2-1 need to function Tags so we split function tags
                String parsResult_FuncTag = Converter.functionTagReForm(parsResult);
                //2-3 delete \n information from parse result
                parsResult_FuncTag = cv.StringforDepformat(parsResult_FuncTag);
                //2-4 convert to store ParseTree OBJ
                ParseTree pt = new ParseTree(sentence, parsResult_FuncTag, 0, true);		
                //2-5 convert to store Dependency Tree OBJ
                DTree depTree = cv.convert(pt);

                // for file manage
                // FileManager fm = new FileManager();
                // fm.makeFile("ParseResult.txt", "Start#\n" + parsResult+ "End#\n" );
                // fm.makeFile("Converted.txt", depTree.toString() );
            
                out.println("sentence: " + sentence);
                out.println("");
                out.println("PSG parsing : ");
                out.println(parsResult);
                out.println("");
                out.println("DG parsing : ");
                out.println(depTree.toString());
            }
        }
    }
}
