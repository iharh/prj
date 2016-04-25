__author__ = 'Mingsheng Wu'

# to run word2vec fast version properly, one has to ensure that the machine has proper cmake, cython, and blas installed
# in addition, one has to ensure the proper version of scipy is installed since some versions of scipy has a deprecated
# version of blas.

import gensim
from gensim.models.word2vec import Word2Vec, LineSentence

import re
import string
import itertools, logging

logging.basicConfig(format='%(asctime)s : %(levelname)s : %(message)s', level=logging.INFO)

# preprocessing the word2vec training documents, removing all non-character symbols.
# preprocessed documents can be combined as well as saved in separate files.
# before running from this script, the input files are preprocessed so that the files are formatted one sentence per
# line.

# the option parameter allows the method to either return a list of preprocessed sentences as result or a simple boolean
# value with all the outputs written to a list of files. When the boolean option is used, the method expects output
# location as output_filename. If sentences option is selected, no destination location needs to be specified.

def single_file_preprocessing(input_filename, output_filename, return_option='boolean'):
    try:
        with open(input_filename) as f:
            content = f.readlines()
        sentences=[]
        count=0
        total=len(content)
        for sentence in content:
            count=count+1
            exclude = set(string.punctuation)
            s = ''.join(ch for ch in sentence if ch not in exclude)
            sentences.append(s)
            if (count%1000==0):
                print str(count)+"/"+str(total)+" lines processed"
        if return_option=="boolean":
            f_out = open(output_filename, 'w')
            for item in sentences:
                f_out.write("%s\n" % item)
            return True
        else:
            return sentences
    except:
        return False

# the combined option allows one to specify if one wants the result not to be combined into one training file. If
# combined, one has to give a list of file names as output files with the same number of input file names.

def files_preprocessing(input_files, output_files, combined=True):
    if not combined:
        valid=True
        for input_filename, output_filename in itertools.izip(input_files, output_files):
            if not single_file_preprocessing(input_filename, output_filename, return_option="boolean"):
                print "File", input_filename, "failed to be transcribed into preprocessed format."
                valid = False
            else:
                print "File", input_filename, "was transcribed into preprocessed format."
    else:
        valid=True
        result=[]
        for input_filename in input_files:
            try:
                result+=single_file_preprocessing(input_filename,"None",return_option="sentences")
                print "File", input_filename, "was transcribed into preprocessed format."
            except:
                print "File", input_filename, "failed to be transcribed into preprocessed format."
                valid=False
        f_out = open(output_files[0], 'w')
        for item in result:
            f_out.write("%s\n" % item)
    if valid:
        print "All files were successfully processed."
    else:
        print "Not all files were successfully processed."
    return valid


# a method to build a word2vec model from a list of file names, but the vocabulary of the model is only taken from the
# first file
# the resulting model is saved to parameter model_name in the same directory as the working directory
def build_model_en(files, model_name):
    print "Model training started."
    if len(files)==1:
        sentences=LineSentence(files[0])
        model = Word2Vec(sentences, size=300, window=10, min_count=50, workers=4)
    else:
        first=True
        for file in files:
            sentences=LineSentence(file)
            if first:
                model = Word2Vec(sentences, size=300, window=10, min_count=50, workers=4)
            else:
                model.train(sentences)
    print "Model successfully built."
    model.save_word2vec_format(model_name,binary=True)
    return model


def test():
    out_data = 'DE_data'
    files_preprocessing(['news-commentary-v10.de'],
                         [out_data])

    sentences=LineSentence(out_data);
    #model = Word2Vec(sentences, size=300, window=10, min_count=10, workers=4)
    bigram_transformer = gensim.models.Phrases(sentences)
    model = Word2Vec(bigram_transformer[sentences], size=300, window=10, min_count=10, workers=4)
    print "saving the model..."
    model.save_word2vec_format("News_" + out_data + ".bin", binary=True)
    print "All complete"

test()
