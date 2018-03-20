module clbutils;

import requests;

import diet.html;

import std.stdio;
import std.format;
import std.array; // appender

import core.time;

//extern(C) void test();

string compile(string templateName, string langId, string prjName) {
    auto result = appender!string;
    result.compileHTMLDietFile!(templateName ~ ".dt", langId, prjName);
    return result.data;
}

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
    auto data = compile("prjCreate", langId, prjName);
    wsdlProject(data);
}
