module clbdb;

import std.conv;
import std.format;
import std.stdio;

import database;

void dbCfgPrj(string usr, string pwd, string prjName) {
    auto connectStr = format("postgresql://%s:%s@localhost:5432/postgres?charset=utf-8", usr, pwd);
    Database db = new Database(connectStr);

    // "select * from cb_properties where prop_name='VERSION'"
    auto sqlQuery = // format("select id from cb_project where name = '%s'", prjName); // lin_ss.
        db.createSqlBuilder()
        .select("id")
        .from("lin_ss.cb_project")
        .eq("name", "'" ~ prjName ~ "'")
        .build()
        .toString();

    writeln(format("sqlQuery: %s", sqlQuery));

    Statement statement = db.prepare(sqlQuery);
    // stmt.setParameter(":username","viile");

    // ResultSet rs = .query()
    // Row row = rs.front();
    //foreach(row; rs) { writeln(row); }

    Row row = statement.fetch();
    int prjId = row[0].to!int;
    writeln(format("prjId: %d", prjId));

    //string sqlIns = "insert into public.test(id, name) VALUES (1, 1);"
    //int result = db.execute(sqlIns);

    db.close();
}
