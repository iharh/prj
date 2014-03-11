# !!! install.packages(c("testthat")) - need to install the packages
# installed.packages() - list installed packages
# available.packages() - list available (in repository) packages

require(stats)
require(datasets)
require(grDevices); require(graphics)
require(testthat, quietly=TRUE) # depends on plyr

# is.na(x) # returns TRUE of x is missing
# newdata <- na.omit(mydata) 
# Filter(...)

processCsv <- function(csv_file_name)
{
	f <- read.csv(csv_file_name)
	expect_equal(names(f), c(
		"X.PDH.CSV.4.0...Eastern.Daylight.Time..240."         , # [[01]]
		"X..WKCC012CNC.Process.KRetriever..Virtual.Bytes"     , # [[02]]
		"X..WKCC012CNC.Process.KRetriever..Working.Set"       , # [[03]]
		"X..WKSS011CNC.Process.KRetriever..Virtual.Bytes"     , # [[04]]
		"X..WKSS011CNC.Process.KRetriever..Working.Set"       , # [[05]]
		"X..WKSS012CNC.Process.KRetriever..Virtual.Bytes"     , # [[06]]
		"X..WKSS012CNC.Process.KRetriever..Working.Set"       , # [[07]]
		"X..WKSS013CNC.Process.KRetriever..Virtual.Bytes"     , # [[08]]
		"X..WKSS013CNC.Process.KRetriever..Working.Set"       , # [[09]]
		"X..WKCC011CNC.Process.KRetriever..Virtual.Bytes"     , # [[10]]
		"X..WKCC011CNC.Process.KRetriever..Working.Set"       , # [[11]]
		"X..WKCC013CNC.Process.KRetriever..Virtual.Bytes"     , # [[12]]
		"X..WKCC013CNC.Process.KRetriever..Working.Set"       , # [[13]]
		"X..WKSS014CNC.Process.KRetriever..Virtual.Bytes"     , # [[14]]
		"X..WKSS014CNC.Process.KRetriever..Working.Set"       , # [[15]]
		"X..WKCC012CNC.Process.KRetriever..Virtual.Bytes.Peak", # [[16]]
		"X..WKCC012CNC.Process.KRetriever..Working.Set.Peak"  , # [[17]]
		"X..WKCC013CNC.Process.KRetriever..Virtual.Bytes.Peak", # [[18]]
		"X..WKCC013CNC.Process.KRetriever..Working.Set.Peak"  , # [[19]]
		"X..WKSS011CNC.Process.KRetriever..Virtual.Bytes.Peak", # [[20]]
		"X..WKSS011CNC.Process.KRetriever..Working.Set.Peak"  , # [[21]]
		"X..WKSS014CNC.Process.KRetriever..Virtual.Bytes.Peak", # [[22]]
		"X..WKSS014CNC.Process.KRetriever..Working.Set.Peak"  , # [[23]]
		"X..WKCC011CNC.Process.KRetriever..Virtual.Bytes.Peak", # [[24]]
		"X..WKCC011CNC.Process.KRetriever..Working.Set.Peak"  , # [[25]]
		"X..WKSS012CNC.Process.KRetriever..Virtual.Bytes.Peak", # [[26]]
		"X..WKSS012CNC.Process.KRetriever..Working.Set.Peak"  , # [[27]]
		"X..WKSS013CNC.Process.KRetriever..Virtual.Bytes.Peak", # [[28]]
		"X..WKSS013CNC.Process.KRetriever..Working.Set.Peak"    # [[29]]
	), label = "CSV Column names")
#	print(names(f))
	names(f) <- c("DateTimeVal",
		"VB-KCC012", "WS-KCC012",
		"VB-KSS011", "WS-KSS011",
		"VB-KSS012", "WS-KSS012",
		"VB-KSS013", "WS-KSS013",
		"VB-KCC011", "WS-KCC011",
		"VB-KCC013", "WS-KCC013",
		"VB-KSS014", "WS-KSS014",
		"VBP-KCC012", "WSP-KCC012",
		"VBP-KSS011", "WSP-KSS011",
		"VBP-KSS012", "WSP-KSS012",
		"VBP-KSS013", "WSP-KSS013",
		"VBP-KCC011", "WSP-KCC011",
		"VBP-KCC013", "WSP-KCC013",
		"VBP-KSS014", "WSP-KSS014"
	)
	#print(names(f))

	dt <- f[[1]]
	dt_begin <- dt[1]
	dt_end <- dt[length(dt)]

	f[[1]] <- substring(f[[1]], 1, 10) # trim the time
	f[is.na(f)] <- 0 # replace NA by 0 since we are interested in max-values

	# f[, -1] -- all but the first column
	# f[2:15] -- from 2 to 15 columns
	f_agg = aggregate(f[, -1], by = list(day = f$DateTimeVal), FUN = max)
	#print(f_agg[1:2])
	#f_agg_vb = data.frame()
	f_agg_vb <- f_agg[-grep("WS",colnames(f_agg))]
	f_agg_ws <- f_agg[-grep("VB",colnames(f_agg))]
	#print(f_agg_vb)
	#print(f_agg_ws)
	write.csv(f_agg_vb, paste0("vb-", csv_file_name), row.names = FALSE)
	write.csv(f_agg_ws, paste0("ws-", csv_file_name), row.names = FALSE)
}

processCsv(commandArgs(trailingOnly = TRUE)[1])

