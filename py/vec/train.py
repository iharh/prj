# to run word2vec fast version properly, one has to ensure that the machine has proper cmake, cython, and blas installed
# in addition, one has to ensure the proper version of scipy is installed since some versions of scipy has a deprecated
# version of blas.

import gensim
from gensim.models.word2vec import Word2Vec, LineSentence

import re
import string
import itertools, logging

logging.basicConfig(format='%(asctime)s : %(levelname)s : %(message)s', level=logging.INFO)

# a method to build a word2vec model from a list of file names, but the vocabulary of the model is only taken from the
# first file
# the resulting model is saved to parameter model_name in the same directory as the working directory
#def build_model(files, model_name):
#    if len(files) == 1:
#        sentences = LineSentence(files[0])
#        model = Word2Vec(sentences, size=300, window=10, min_count=50, workers=4)
#    else:
#        first=True
#        for file in files:
#            sentences=LineSentence(file)
#            if first:
#                model = Word2Vec(sentences, size=300, window=10, min_count=50, workers=4)
#            else:
#                model.train(sentences)
#    print "Model successfully built."
#    model.save_word2vec_format(model_name,binary=True)
#    return model

def train():
    out_data  = 'out/data/en/en_small_data'
    out_model = 'out/model/en/en_small_news'

    print "loading sentences..."
    sentences = LineSentence(out_data);
    print "phrases..."
    bigram_transformer = gensim.models.Phrases(sentences)
    print "start training..."
    model = Word2Vec(bigram_transformer[sentences], size=300, window=10, min_count=10, workers=6)
    print "saving the model..."
    model.save_word2vec_format(out_model, binary=True)
    print "All complete"

train()
