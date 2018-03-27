module clbutils;

import requests;

import diet.html;

import std.stdio;
import std.format;
import std.array; // appender

import core.time;

//extern(C) void test();

string wsdl(string name, string data) {
    auto rq = Request();
    rq.verbosity = 2;
    rq.timeout = 5.minutes;
    rq.authenticator = new BasicAuthentication("admin", "admin");
    string url = format("http://localhost:18080/cbapi/%s?wsdl", name);
    auto rs = rq.post(url, data, "text/xml");
    writeln(format("code: %d", rs.code));
    writeln(format("body: %d", rs.responseBody));
    return rs.responseBody.toString();
}

void prjCreate(string langId, string prjName) {
    auto app = appender!string;
    app.compileHTMLDietFile!("prjCreate.dt", langId, prjName);
    wsdl("project", app.data);
}

string pmvd(string prjName, bool isSave, string verbatimText) {
    auto app = appender!string;
    app.compileHTMLDietFile!("pmvd.dt", prjName, isSave, verbatimText);
    writeln(format("compiled to: %s", app.data));
    return wsdl("realtime", app.data);
}
