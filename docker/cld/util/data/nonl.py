import csv
ifilename = 'other.csv'
with open(ifilename, newline='', encoding='utf-8') as ifile:
    rdr = csv.reader(ifile, delimiter=',', quotechar='"')
    try:
        for row in rdr:
            print(', '.join(row).replace('\n', ' ').replace('\r', ' '))
    except csv.Error as e:
        sys.exit('error at file {}, line {}: {}'.format(ifilename, rdr.line_num, e))
