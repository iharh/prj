package dl4j;

public class UtilsOld {
/*
    private static Collection<String> wordsNearestOld(InMemoryLookupTable lookupTable, VocabCache vocabCache, String word, int top) {
        List<String> ret = new ArrayList<>();

        if (!vocabCache.containsWord(word)) {
            return ret;
        }

        INDArray words = Nd4j.create(1, lookupTable.layerSize());
        words.putRow(0, lookupTable.vector(word));

        INDArray mean = words.isMatrix() ? words.mean(0) : words;

        INDArray syn0 = lookupTable.getSyn0();

        INDArray weights = mean;
        INDArray distances = syn0.mmul(weights.transpose());
        distances.diviRowVector(distances.norm2(1));
        INDArray[] sorted = Nd4j.sortWithIndices(distances, 0, false);
        INDArray sort = sorted[0];

        int sortLen = sort.length();
        log.info("sort.length(): {}", sortLen);
        log.info("sort.offset(): {}", sort.offset());

        logIntArr("sort.stride", sort.stride());
        logIntArr("sort.shape", sort.shape());

        for (int i = 0; i < sortLen; ++i) {
            log.info("iter: {}", i);
            int idx = sort.getInt(i);
            log.info("idx: {}", idx);
        }

        if (top > sortLen)
            top = sortLen;
        //??? there will be a redundant word
        int end = top; // + 1;
        for (int i = 0; i < end; ++i) {
            int idx = sort.getInt(i);
            String w = vocabCache.wordAtIndex(idx);
            if (word.equals(w)) {
                ++end;
                if (end >= sortLen)
                    break;
                continue;
            }
            ret.add(w);
        }

        return ret;
    }

    private static void logIntArr(String prefix, int [] arr) {
        int len = arr.length;
        StringBuffer sb = new StringBuffer(prefix);
        sb.append(" len: ");
        sb.append(Integer.toString(len));
        sb.append(" {");
        for (int i = 0; i < len; ++i) {
            sb.append(" ");
            sb.append(Integer.toString(arr[i]));
        }
        sb.append(" }");
        log.info("{}", sb.toString());
    }
*/
};
