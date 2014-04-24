lazy val cleanwds = taskKey[Unit]("Clean up webdav-sync caches")

cleanwds := {
    val src = Path.userHome / ".be" / "re" / "webdav" / "cmd" / "syncdb"
    IO.delete(src)
    //(src ***).get foreach { println(_) }
}

