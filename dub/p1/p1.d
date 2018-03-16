#!/usr/bin/env dub
/+ dub.sdl:
    name "p1"
    targetType "executable"
    dependency "entity" version="~>1.2.1"
    dependency "database" version="~>0.1.0"
    configuration "default" {
        subConfiguration "database" "postgresql"
    }
+/
import std.stdio; // : writeln;
import database;

void main() {
    writeln("begin");
    Database db = new Database("postgresql://postgres:PGSQL-123@localhost:5432/postgres?charset=utf-8");
    Statement statement = db.prepare("select * from my");
    ResultSet rs = statement.query();
    foreach(row; rs)
    {
        writeln(row);
    }
    db.close();
    writeln("end");
}
