import unicodedata
import csv

#def myfun(s):
#    return unicodedata.normalize("NFC", s);

with open("out.csv", "w", newline="", encoding="UTF-8") as outF:
    writer = csv.writer(outF, quoting=csv.QUOTE_NONNUMERIC)
    with open("in.csv", newline="", encoding="UTF-8") as inF:
        reader = csv.reader(inF)
        for row in reader:
            cvt_row = map(lambda s: unicodedata.normalize("NFC", s), row)
            writer.writerow(list(cvt_row)) #", ".join(cvt_row) + "\n"

#with open("output.txt", "w", encoding="UTF-8") as outF:
  #outF.write("abc\n") # care about newlines
  #outF.write("def\n")
