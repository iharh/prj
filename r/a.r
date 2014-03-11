require(stats)
require(datasets)
require(grDevices); require(graphics)

csv_dir = "D:\\Knova\\Customers\\RIM\\621204\\2012-07-06\\csv"

plotMem <- function(csv_file_name)
{
	f <- read.csv(csv_file_name) # f is a data-frame [generalized matrix] here
	dt <- f$DateTime
	ws <- f$WorkingSet
	#vb <- # f$VirtualBytes

	str_title <- paste("KRetriever from", dt[1], "to", dt[length(dt)])
	str_sub <- paste("Working Set max:", max(ws))

#	opar <- par(bg = "white")

	plot(ws, type = "n", ann = FALSE)
	title(main = str_title, sub = str_sub, xlab = "Time intervals (1 minute)", ylab = "Working Set")
	lines(ws, col = "green4", lty = "dotted")
	points(ws, bg = "limegreen", pch = 21)

#	par(opar)
#	detach(f)
}

processCsv <- function(cur_csv)
{
	print(paste("Processing", cur_csv, "..."), quote = FALSE)
	csv_file_name <- paste(csv_dir, "\\", cur_csv, sep = "")
	#cur_pdf <- sub(".csv", ".pdf", cur_csv, fixed = TRUE)
	#pdf_file_name <- paste(csv_dir, "\\", cur_pdf, sep = "")
	#print(pdf_file_name)
	plotMem(csv_file_name)
}

processCsvs <- function()
{
	pdf("D:\\a.pdf", bg = "white")

	csvs <- list.files(path = csv_dir, pattern = "*.csv")
	for(cur_csv in csvs)
		processCsv(cur_csv)
}

#processCsv("a0.csv")
processCsvs()

