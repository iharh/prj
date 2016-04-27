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
        sentences = []
        count = 0
        total = len(content)
        for sentence in content:
            count = count + 1
            exclude = set(string.punctuation)
            s = ''.join(ch for ch in sentence if ch not in exclude)
            sentences.append(s)
            if (count % 100000 == 0):
                print str(count) + "/" + str(total) + " lines processed"
        if return_option == "boolean":
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
    valid = True
    if not combined:
        for input_filename, output_filename in itertools.izip(input_files, output_files):
            if not single_file_preprocessing(input_filename, output_filename, return_option="boolean"):
                print "File", input_filename, "failed to be transcribed into preprocessed format."
                valid = False
            else:
                print "File", input_filename, "was transcribed into preprocessed format."
    else:
        f_out = open(output_files[0], 'w')
        #result = []
        for input_filename in input_files:
            try:
                #result +=
                result = single_file_preprocessing(input_filename, "None", return_option="sentences")
                for item in result:
                    f_out.write("%s\n" % item)
                print "File", input_filename, "was transcribed into preprocessed format."
            except:
                print "File", input_filename, "failed to be transcribed into preprocessed format."
                valid = False
    if valid:
        print "All files were successfully processed."
    else:
        print "Not all files were successfully processed."
    return valid

def test():
    print "start preparation"
    de_in_data = [
        'in/de/news.2015.de.shuffled'
        , 'in/de/news.2014.de.shuffled'
        , 'in/de/news.2013.de.shuffled'
        , 'in/de/news.2012.de.shuffled'
        , 'in/de/news.2011.de.shuffled'
        , 'in/de/news.2010.de.shuffled'
        , 'in/de/news.2009.de.shuffled'
        , 'in/de/news.2008.de.shuffled'
        , 'in/de/news.2007.de.shuffled'
    ]
    fr_in_data = [
        'in/fr/news.2014.fr.shuffled.v2'
        , 'in/fr/news.2013.fr.shuffled'
        , 'in/fr/news.2012.fr.shuffled'
        , 'in/fr/news.2011.fr.shuffled'
        , 'in/fr/news.2010.fr.shuffled'
        , 'in/fr/news.2009.fr.shuffled'
        , 'in/fr/news.2008.fr.shuffled'
        , 'in/fr/news.2007.fr.shuffled'
    ]
    es_in_data = [
        'in/es/news.2011.es.shuffled'
        , 'in/es/news.2010.es.shuffled'
        , 'in/es/news.2009.es.shuffled'
        , 'in/es/news.2008.es.shuffled'
        , 'in/es/news.2007.es.shuffled'
    ]
    de_out_data = ['out/data/de/de_data']
    fr_out_data = ['out/data/fr/fr_data']
    es_out_data = ['out/data/es/es_data']
    files_preprocessing(es_in_data, es_out_data)
    print "complete preparation"

test()
