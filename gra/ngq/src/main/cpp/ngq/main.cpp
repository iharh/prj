#include <iostream>

#include "util/string_piece.hh"

namespace lm {
namespace ngram {

class ProbingModel {
public:
    ProbingModel(const char *file, void *config) {
    }
};

float Query(const ProbingModel &model, StringPiece word) {
    std::cout << "word: " << word << std::endl;
/*
    typename ProbingModel::State state, out;
    // iterate lines
    state = model.BeginSentenceState() : // model.NullContextState(); if !sentence_context
    //while (in.ReadWordSameLine(word)) {
    lm::WordIndex vocab = model.GetVocabulary().Index(word);
    lm::FullScoreReturn ret = model.FullScore(state, vocab, out);
    float total = ret.prob;
    //printer.Word(word, vocab, ret);
    state = out;
    //}
    ret = model.FullScore(state, model.GetVocabulary().EndSentence(), out);
    total += ret.prob;
    //printer.Word("</s>", model.GetVocabulary().EndSentence(), ret);
    //printer.Line(oov, total);
    return total;
*/
    return 0.0;
}

} // namespace ngram
} // namespace lm


int
main(void) {
    const char *file = "";
    void *config = NULL;
    lm::ngram::ProbingModel model(file, config);

    float total = Query(model, StringPiece("V"));

    std::cout << "total: " << total << std::endl;
    return 0;
}
