# !!! install.packages(c("testthat")) - need to install the packages
# installed.packages() - list installed packages
# available.packages() - list available (in repository) packages

require(stats)
require(datasets)
require(grDevices); require(graphics)
require(testthat, quietly=TRUE) # depends on plyr

plotStat <- function(host, dt, ws)
{
# is.na(x) # returns TRUE of x is missing
# newdata <- na.omit(mydata) 
# Filter(...)
	max_ws <- max(ws, na.rm = TRUE)
	str_title <- paste("MAX WorkingSet at", host, "is", max_ws)
	str_sub <- paste("from", dt[1], "to", dt[length(dt)])

	plot(ws, type = "n", ann = FALSE)
	title(main = str_title, sub = str_sub, xlab = "Time intervals (1 minute)", ylab = "Working Set")
	lines(ws, col = "green4", lty = "dotted")
	points(ws, bg = "limegreen", pch = 21)
}

processCsvs <- function(csv_file_name)
{
	f <- read.csv(csv_file_name)
	expect_equal(names(f), c(
		"X.PDH.CSV.4.0...Eastern.Daylight.Time..240."    , # [[1]]
		"X..WKCC012CNC.Process.KRetriever..Virtual.Bytes", # [[2]]
		"X..WKCC012CNC.Process.KRetriever..Working.Set"  , # [[3]]
		"X..WKSS011CNC.Process.KRetriever..Virtual.Bytes", # [[4]]
		"X..WKSS011CNC.Process.KRetriever..Working.Set"  , # [[5]]
		"X..WKSS012CNC.Process.KRetriever..Virtual.Bytes", # [[6]]
		"X..WKSS012CNC.Process.KRetriever..Working.Set"  , # [[7]]
		"X..WKSS013CNC.Process.KRetriever..Virtual.Bytes", # [[8]]
		"X..WKSS013CNC.Process.KRetriever..Working.Set"  , # [[9]]
		"X..WKCC011CNC.Process.KRetriever..Virtual.Bytes", # [[10]]
		"X..WKCC011CNC.Process.KRetriever..Working.Set"  , # [[11]]
		"X..WKCC013CNC.Process.KRetriever..Virtual.Bytes", # [[12]]
		"X..WKCC013CNC.Process.KRetriever..Working.Set"  , # [[13]]
		"X..WKSS014CNC.Process.KRetriever..Virtual.Bytes", # [[14]]
		"X..WKSS014CNC.Process.KRetriever..Working.Set"    # [[15]]
	), label = "CSV Column names")
#	print(names(f))

	dt <- f[[1]]

	pdf(gsub("\\.csv$", ".pdf", csv_file_name), bg = "white")

	plotStat("WKCC012CNC", dt, f[[3]])
	plotStat("WKSS011CNC", dt, f[[5]])
	plotStat("WKSS012CNC", dt, f[[7]])
	plotStat("WKSS013CNC", dt, f[[9]])
	plotStat("WKCC011CNC", dt, f[[11]])
	plotStat("WKCC013CNC", dt, f[[13]])
	plotStat("WKSS014CNC", dt, f[[15]])
}

processCsvs(commandArgs(trailingOnly = TRUE)[1])

