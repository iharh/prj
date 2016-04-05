import sys
import csv

ifilename = sys.argv[1]
ofilename = sys.argv[2]

with open(ofilename, 'w', newline='') as ofile:
    wrtr = csv.writer(ofile, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
    with open(ifilename, newline='', encoding='utf-8') as ifile:
        rdr = csv.reader(ifile, delimiter=',', quotechar='"')
        try:
            for row in rdr:
                if ''.join(row).strip():
                    wrtr.writerow(map(lambda x : x.replace('\n', ' '), row))
        except csv.Error as e:
            sys.exit('error at file {}, line {}: {}'.format(ifilename, rdr.line_num, e))
