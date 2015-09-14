import numpy as np
from gensim.models.word2vec import Word2Vec
import datetime

model = Word2Vec.load_word2vec_format('D:/clb/src/spikes/cb-cps/templates/word2vecModels_bck/GoogleNews', binary=True)

words = open('en-words-100.txt').read().splitlines()

for w in words:
    print(datetime.datetime.now(), " ", w)
    mostSimilar=model.most_similar(positive=[w])
