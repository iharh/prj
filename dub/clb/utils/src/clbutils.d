module clbutils;

import requests;

import diet.html;

import std.stdio;
import std.format;
import std.array; // appender

import core.time;

//extern(C) void test();

void wsdlProject(string data) {
    auto rq = Request();
    rq.verbosity = 2;
    rq.timeout = 5.minutes;
    rq.authenticator = new BasicAuthentication("admin", "admin");
    auto rs = rq.post("http://localhost:18080/cbapi/project?wsdl", data, "text/xml");
    writeln(format("code: %d", rs.code));
    writeln(format("resp: %s", rs.responseBody));
}

void prjCreate(string langId, string prjName) {
    auto app = appender!string;
    app.compileHTMLDietFile!("prjCreate.dt", langId, prjName);
    wsdlProject(app.data);
}

void pmvd(string prjName, string verbatimText) {
    auto app = appender!string;
    app.compileHTMLDietFile!("pmvd.dt", prjName, verbatimText);

    writeln(format("compiled to: %s", app.data));
    // wsdlRealtime(app.data);
}
