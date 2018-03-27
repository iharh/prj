import std.format;
import std.range;
import std.stdio;

import darg;

import clbdb;
import clbrest;

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
    string[] prjNames = [ langId ~ "0int", langId ~ "0ext" ];

    Options options;
    try
    {
        options = parseArgs!Options(args[1 .. $]);
    }
    catch (ArgParseError e)
    {
        writeln(e.msg);
        writeln(usage);
        return 1;
    }
    catch (ArgParseHelp e)
    {
        // Help was requested
        writeln(usage);
        write(help);
        return 0;
    }

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
        string responseBody = pmvd(prjNames[0], isSave, "I like my round dreamliner");
        auto respFile = File("result.xml", "w");
        respFile.write(responseBody);
    }
    if (options.upload) {
        auto fileName = langId ~ "100.txt";
        auto f = File(fileName);
        auto lines = f.byLineCopy();
        foreach (lineNum, line; lines.enumerate(1)) {
            foreach (prjName; prjNames) {
                writeln(format("line %d: prj: %s ...", lineNum, prjName));
                pmvd(prjName, true, line);
            }
        }
    }
    if (options.db) {
        dbTest();
    }
    return 0;
}
