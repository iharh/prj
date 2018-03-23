import std.format;
import std.range;
import std.stdio;

import clbutils;

void createProjects(string langId) {
    prjCreate(langId, langId ~ "0int");
    prjCreate(langId, langId ~ "0ext");
}

void uploadByLine(string prjName, string fileName) {
    auto f = File(fileName);
    auto lines = f.byLineCopy();
    foreach(lineNum, line; lines.enumerate(1)) {
        writeln(format("line %d: %s", lineNum, line));
        pmvd(prjName, true, line);
    }
}

void main() {
    auto langId = "en";
    // createProjects(langId);

    string prjSuffix = "0ext";
    string prjName = langId ~ prjSuffix;
    uploadByLine(prjName, langId ~ "100.txt");
/*

    bool isSave = false;
    string responseBody = pmvd(prjName, isSave, "I like my round dreamliner");
    auto respFile = File("result.xml", "w");
    respFile.write(responseBody);
*/
}
