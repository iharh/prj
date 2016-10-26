package antlr4

import static rx.observables.StringObservable.from
import static rx.observables.StringObservable.byLine

class Utils { 
    private static final String RES_SEP = "/";

    public static List<String> getLinesCP(String resFileName) {
        final Reader reader = getReaderCP(resFileName);
        return byLine(from(reader)).map({String s -> return s.trim(); })
            .toList().toBlocking().single();
    }

    private static Reader getReaderCP(String resFileName) {
        //new FileReader(resFileName);
        return new BufferedReader(new InputStreamReader(Utils.class.getResourceAsStream(RES_SEP + resFileName)));
    }
}
