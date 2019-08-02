import clbdb;
import clbrest;

import darg;

//import dxml.dom;
import dxml.parser;
//import dxml.util;

import std.conv;
import std.file;
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

    @Option("db", "d") @Help("DB properties configure")
    OptionFlag db;

    @Option("xml", "x") @Help("XML test")
    OptionFlag xml;
}

// Generate the usage and help string at compile time.
immutable usage = usageString!Options("clb");
immutable help = helpString!Options;

string getRespStatus(string content) {
    auto rangeAll = parseXML(content);
    auto rangeStatus = rangeAll.skipToPath("S:Body/ns2:processMultiVerbatimDocumentResponse/return/status");

    // auto elName = range.front.name;
    rangeStatus.popFront();
    auto elText = rangeStatus.front.text;
    return elText;
}

int main(string[] args) {
    auto langId = "zh"; // ar bn de en es fr hi id it ko nl pt ro ru th th tl tr vi zh @u
    auto prjNameInt = langId ~ "0int";
    auto prjNameExt = langId ~ "0ext";
    auto prjNames = [ prjNameInt , prjNameExt ];
    //auto prjNames = [ prjNameInt ];
    //auto prjNames = [ prjNameExt ];
    //auto dataFileName = langId ~ "1.txt";
    auto dataFileName = langId ~ "100.txt";

    Options options;
    try {
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
            auto prjName = prjNameExt; // int
            auto lines = File(dataFileName).byLineCopy();
            foreach (lineNum, line; lines.enumerate(1)) {
                writeln(format("process: %s line %d: prj: %s ...", isSave, lineNum, prjName));
                auto responseBody = pmvd(prjName, isSave, line);
                if (getRespStatus(responseBody) != "SUCCESS") {
                    throw new Exception("failed !!!");
                }
                //auto reqFile = File(format("req-%d.xml", lineNum), "w");
                //reqFile.write(line);
                //auto respFile = File(format("result-%d.xml", lineNum), "w");
                //respFile.write(responseBody);
            }
        }
        if (options.upload) {
            bool isSave = true;
            auto lines = File(dataFileName).byLineCopy();
            foreach (lineNum, line; lines.enumerate(1)) {
                // stdin.readln();
                foreach (prjName; prjNames) {
                    //auto prjName = prjNameExt;
                    writeln(format("line %d: prj: %s ...", lineNum, prjName));
                    auto responseBody = pmvdNatId(prjName, isSave, line, "natId" ~ lineNum.to!string);
                    if (getRespStatus(responseBody) != "SUCCESS") {
                        throw new Exception("failed !!!");
                    }
                }
            }
        }
        if (options.db) {
            dbCfgPrj("lin_ss", "clb", prjNameExt);
        }
        if (options.xml) {
            auto content = readText("./res-int.xml");
            writeln(getRespStatus(content));
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
