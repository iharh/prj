import std.stdio;
import std.format;

import clbutils;

void main() {
    string langId = "en";
    string prjName = langId ~ "0ext";
    // prjCreate(langId, prjName);
    bool isSave = false;
    pmvd(prjName, isSave, "I like my round dreamliner");
}
