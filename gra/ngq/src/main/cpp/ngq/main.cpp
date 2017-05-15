#include <iostream>

#include "lm/config.hh"
#include "lm/model.hh"
#include "util/string_piece.hh"

namespace lm {
namespace ngram {

void Query(const ProbingModel &model, StringPiece word) {
    //typename ProbingModel::State state, out;
    ProbingModel::State out;
    // iterate lines
    ProbingModel::State state = model.BeginSentenceState(); // : model.NullContextState(); if !sentence_context
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
    //return total;
    std::cout << "word: " << word << " total: " << total << std::endl;
}

} // namespace ngram
} // namespace lm


int
main(void) {
    const char *file = "";

    lm::ngram::Config config;
    lm::ngram::ProbingModel model(file, config);

    Query(model, StringPiece("V"));
    Query(model, StringPiece("P"));

    return 0;
}
