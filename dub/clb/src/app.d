import std.format;
import std.range;
import std.stdio;

import darg;

import clbutils;

struct Options {
    @Option("help", "h") @Help("Prints this help.")
    OptionFlag help;

    @Option("prj", "p") @Help("Creates projects")
    OptionFlag prj;

    @Option("realtime", "r") @Help("Realtime request")
    OptionFlag realtime;

    @Option("upload", "u") @Help("Upload data")
    OptionFlag upload;
}

// Generate the usage and help string at compile time.
immutable usage = usageString!Options("clb");
immutable help = helpString!Options;

void uploadByLine(string prjName, string fileName) {
    auto f = File(fileName);
    auto lines = f.byLineCopy();
    foreach (lineNum, line; lines.enumerate(1)) {
        writeln(format("line %d: %s", lineNum, line));
        pmvd(prjName, true, line);
    }
}

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
        foreach (prjName; prjNames) {
            writeln(format("uploading data to %s ...", prjName));
            // uploadByLine(prjName, langId ~ "100.txt");
        }
    }
    return 0;
}
