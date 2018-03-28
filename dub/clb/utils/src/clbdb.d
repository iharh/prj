module clbdb;

import std.conv;
import std.format;
import std.stdio;

import database;

string sysTbl(string usr, string tableName) {
    return usr ~ "." ~ tableName;
}

string qS(string strVal) {
    return "'" ~ strVal ~ "'";
}

void dbCfgPrj(string usr, string pwd, string prjName) {
    auto connectStr = format("postgresql://%s:%s@localhost:5432/postgres?charset=utf-8", usr, pwd);
    Database db = new Database(connectStr);

    // "select * from cb_properties where prop_name='VERSION'"
    auto sqlQuery = db.createSqlBuilder()
        .select("id")
        .from(sysTbl(usr, "cb_project"))
        .eq("name", qS(prjName))
        .build()
        .toString();

    Statement statement = db.prepare(sqlQuery);

    Row row = statement.fetch();
    int prjId = row[0].to!int;
    writeln("prjId: " ~ prjId.to!string);

    string[] flagNames = [ "fx_use_fxservice", "fx_use_jdbc_mapping" ];
    foreach (flagName; flagNames) {
        // insert into cb_properties (prop_name, prop_value, id_project) values ('flagName', 'true', prjId);
        string sqlIns = 
            db.createSqlBuilder()
            .insert(sysTbl(usr, "cb_properties"))
            .values([
                "prop_name" : qS(flagName),
                "prop_value": qS("true"),
                "id_project": qS(prjId.to!string)])
            .build()
            .toString();
        writeln("sql: " ~ sqlIns);
        int numInserted = db.execute(sqlIns);
        writeln("numInserted: " ~ numInserted.to!string);
    }
    db.close();
}
