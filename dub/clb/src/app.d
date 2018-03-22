import std.stdio;
import std.format;

import clbutils;

void main() {
    string langId = "en";
    string prjSuffix = "0ext";
    //string prjSuffix = "0int";
    string prjName = langId ~ prjSuffix;
    //prjCreate(langId, prjName);
    //prjCreate(langId, langId ~ "0ext");

    bool isSave = false;
    string responseBody = pmvd(prjName, isSave, "I like my round dreamliner");

    auto respFile = File("result.xml", "w");
    respFile.write(responseBody);
}
