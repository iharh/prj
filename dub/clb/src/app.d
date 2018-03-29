import clbdb;
import clbrest;

import darg;

import std.format;
import std.range;
import std.stdio;

struct Options {
    @Option("help", "h") @Help("Prints this help.")
    OptionFlag help;

    @Option("prj", "p") @Help("Creates projects")
    OptionFlag prj;

    @Option("realtime", "r") @Help("Realtime request")
    OptionFlag realtime;

    @Option("upload", "u") @Help("Upload data")
    OptionFlag upload;

    @Option("db", "d") @Help("DB test")
    OptionFlag db;
}

// Generate the usage and help string at compile time.
immutable usage = usageString!Options("clb");
immutable help = helpString!Options;

int main(string[] args) {
    auto langId = "en";
    auto prjNameInt = langId ~ "0int";
    auto prjNameExt = langId ~ "0ext";
    auto prjNames = [ prjNameInt , prjNameExt ];
    auto dataFileName = langId ~ "100.txt";

    Options options;
    try
    {
        options = parseArgs!Options(args[1 .. $]);

        if (options.prj) {
            writeln("creating projects ...");
            foreach (prjName; prjNames) {
                writeln(format("creating project %s ...", prjName));
                prjCreate(langId, prjName);
            }
        }
        if (options.realtime) {
            writeln("process realtime ...");
            bool isSave = false;
            auto prjName = prjNameExt;
            auto lines = File(dataFileName).byLineCopy();
            foreach (lineNum, line; lines.enumerate(1)) {
                writeln(format("save: %s line %d: prj: %s ...", isSave, lineNum, prjName));
                pmvd(prjName, isSave, line);
            }
            // string responseBody = pmvd(prjNameExt, isSave, "I like my round dreamliner");
            // auto respFile = File("result.xml", "w");
            // respFile.write(responseBody);
        }
        if (options.upload) {
            bool isSave = true;
            auto lines = File(dataFileName).byLineCopy();
            foreach (lineNum, line; lines.enumerate(1)) {
                foreach (prjName; prjNames) {
                    writeln(format("line %d: prj: %s ...", lineNum, prjName));
                    pmvd(prjName, isSave, line);
                }
            }
        }
        if (options.db) {
            dbCfgPrj("lin_ss", "clb", prjNameExt);
        }

    }
    catch (ArgParseError e) {
        writeln(e.msg);
        writeln(usage);
        return 1;
    }
    catch (ArgParseHelp e) {
        // Help was requested
        writeln(usage);
        write(help);
        return 0;
    }
    catch (Exception e) {
        writeln(format("error: %s file: %s line: %s", e.msg, e.file, e.line)); // "error"|__FILE__|__LINE__
    }
    return 0;
}
