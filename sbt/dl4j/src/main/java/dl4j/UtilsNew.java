package dl4j;

import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;

import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;
import org.nd4j.linalg.api.ndarray.INDArray;

import com.google.common.collect.Lists;


import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class UtilsNew {

    public static Collection<String> wordsNearestNew(InMemoryLookupTable lookupTable, VocabCache vocabCache, INDArray syn0transposed, String word, int top) {
        List<String> ret = new ArrayList<>();

        if (!vocabCache.containsWord(word)) {
            return ret;
        }

        INDArray words = Nd4j.create(1, lookupTable.layerSize());
        words.putRow(0, lookupTable.vector(word));

        INDArray mean = words.isMatrix() ? words.mean(0) : words;
        INDArray weights = Transforms.unitVec(mean);

        INDArray similarity = weights.mmul(syn0transposed);
        // We assume that syn0 is normalized.
        // Hence, the following division is not needed anymore.
        // distances.diviRowVector(distances.norm2(1));
        //INDArray[] sorted = Nd4j.sortWithIndices(distances,0,false);
/*
        List<Double> highToLowSimList = getTopN(similarity, top);

        for (int i = 0; i < highToLowSimList.size(); i++) {
            String w = vocabCache.wordAtIndex(highToLowSimList.get(i).intValue());
            if (w != null && !w.equals("UNK") && !w.equals("STOP") && !w.equals(word)) {
                ret.add(w);
                if (ret.size() >= top) {
                    break;
                }
            }
        }
*/
        return ret;
    }

    private static class ArrayComparator implements Comparator<Double[]> {
        @Override
        public int compare(Double[] o1, Double[] o2) {
            return Double.compare(o1[0], o2[0]);
        }
    }

    /**
     * Get top N elements
     *
     * @param vec the vec to extract the top elements from
     * @param N the number of elements to extract
     * @return the indices and the sorted top N elements
     */
    private static List<Double> getTopN(INDArray vec, int N) {
        ArrayComparator comparator = new ArrayComparator();
        PriorityQueue<Double[]> queue = new PriorityQueue<>(vec.rows(),comparator);

        for (int j = 0; j < vec.length(); j++) {
            final Double[] pair = new Double[]{vec.getDouble(j), (double) j};
            if (queue.size() < N) {
                queue.add(pair);
            } else {
                Double[] head = queue.peek();
                if (comparator.compare(pair, head) > 0) {
                    queue.poll();
                    queue.add(pair);
                }
            }
        }

        List<Double> lowToHighSimLst = new ArrayList<>();

        while (!queue.isEmpty()) {
            double ind = queue.poll()[1];
            lowToHighSimLst.add(ind);
        }
        return Lists.reverse(lowToHighSimLst);
    }
};
