module clbdb;

import std.stdio;

import database;

void dbTest() {
    writeln("begin");
    // postgres:PGSQL-123
    Database db = new Database("postgresql://lin_ss:clb@localhost:5432/postgres?charset=utf-8");
    Statement statement = db.prepare("select * from cb_properties where prop_name='VERSION'");
    ResultSet rs = statement.query();
    foreach(row; rs)
    {
        writeln(row);
    }
    db.close();
    writeln("end");
}
