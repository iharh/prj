#!/usr/bin/env dub

/+ dub.sdl:
    name "p1"
    targetType "executable"
    dependency "diet-ng" version="~>1.4.5"
    dependency "requests" version="~>0.7.2"
+/

import requests;

import diet.html;

import std.stdio;
import std.format;

import core.time;

void compile(string langId, string prjName) {
    auto file = File("t.xml", "wt");
    auto dst = file.lockingTextWriter;
    dst.compileHTMLDietFile!("t.dt", langId, prjName);
}

void main() {
    string langId = "en";
    string prjName = langId ~ "2ext";
    compile(langId, prjName);

    auto file = File("t.xml", "rt");

    auto rq = Request();
    rq.verbosity = 2;
    rq.timeout = 5.minutes;
    rq.authenticator = new BasicAuthentication("admin", "admin");
    auto rs = rq.post("http://localhost:18080/cbapi/project?wsdl", file.byChunk(5), "text/xml");

    writeln(format("code: %d", rs.code));
    writeln(format("resp: %s", rs.responseBody));
}
