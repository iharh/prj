import org.scalatest._
import Matchers._ // org.scalatest.Matchers._
import org.scalactic._
import StringNormalizations._ // org.scalactic.StringNormalizations._
//import Explicitly._
//import NormMethods._

import pkg.User

import org.antlr.stringtemplate.StringTemplate

import java.util.Arrays

import org.slf4j.Logger
import org.slf4j.LoggerFactory

//import scala.collection.JavaConversions._

class ST3Spec extends FlatSpec {
    private val log = LoggerFactory.getLogger(this.getClass.getName)

    implicit val strNormalization = trimmed.toEquality

    "ST3" should "instantiate simple template to" in {
        val t = new StringTemplate("""
            name: $name$
        """);

        t.setAttribute("name", "val")

        val v = t.toString()
        log.info("v1: {}", v)

        v shouldEqual "name: val" // (after being trimmed)
    }
    it should "instantiate loop template to" in {
        val t = new StringTemplate("""$users:{u|-$u$}; separator="\n"$""");

        t.setAttribute("users", Arrays.asList("val1", "val2"))

        val v = t.toString()
        log.info("v2: {}", v);

        v shouldEqual """-val1
-val2"""
    }
    it should "instantiate loop user-based template to" in {
        val t = new StringTemplate("""$users:{u|-$u.name$, $u.displayName$}; separator="\n"$""");

        t.setAttribute("users", Arrays.asList(
              new User("u1", "d1")
            , new User("u2", "d2")
        ))

        val v = t.toString()
        log.info("v3: {}", v)

        v shouldEqual """-u1, d1
-u2, d2"""
    }
}
