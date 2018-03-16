import requests;

import diet.html;

import std.stdio;
import std.format;
import std.array; // appender

import core.time;

string compile(string langId, string prjName) {
    auto result = appender!string;
    result.compileHTMLDietFile!("t.dt", langId, prjName);
    return result.data;
}

void prjCreate(string data) {
    auto rq = Request();
    rq.verbosity = 2;
    rq.timeout = 5.minutes;
    rq.authenticator = new BasicAuthentication("admin", "admin");
    auto rs = rq.post("http://localhost:18080/cbapi/project?wsdl", data, "text/xml");
    writeln(format("code: %d", rs.code));
    writeln(format("resp: %s", rs.responseBody));
}

void main() {
    string langId = "en";
    string prjName = langId ~ "2";
    auto data = compile(langId, prjName);
    writeln(format("generated data: %s", data));
    prjCreate(data);
}
