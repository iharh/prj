import std.stdio;
import std.format;

import clbutils;

void main() {
    string langId = "en";
    string prjName = langId ~ "0ext";
    // prjCreate(langId, prjName);
    pmvd(prjName, "I like my round table");
}
