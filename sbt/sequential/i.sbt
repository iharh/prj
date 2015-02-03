import complete.DefaultParsers._
//import complete.Parser._

lazy val i1 = inputKey[Unit]("Some input task")

val argParser = OptSpace ~> token(StringBasic, "<arg>") // = spaceDelimited("<arg>")
// StringBasic.examples("<arg>") // : Parser[String]

// sbt i1"abc"
// sbt i1"""abc  def"""
// sbt i1"D:\dev\bin\dotfiles"
i1 := {
    println("before")
    val args = argParser.parsed // : Seq[String]
    println(args)
    println("after")
}

