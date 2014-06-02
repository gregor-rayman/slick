package com.typesafe.slick.testkit.tests

import org.junit.Assert._
import java.sql.{Time, Date, Timestamp}
import com.typesafe.slick.testkit.util.{JdbcTestDB, TestkitTest}
import scala.slick.lifted.Executable

class JdbcScalarFunctionTest extends TestkitTest[JdbcTestDB] {
  import tdb.profile.simple._

  def test {
    def check[T <: Rep[_], U](rep: T, exp: U)(implicit exe: Executable[T, U]) = assertEquals(exp, rep.run)
    def checkLit[T : ColumnType](v: T) = check(LiteralColumn(v): Column[T], v)

    checkLit(Date.valueOf("2011-07-15"))
    checkLit(Time.valueOf("15:53:21"))
    checkLit(Timestamp.valueOf("2011-07-15 15:53:21"))

    val myExpr = SimpleExpression.binary[Int, Int, Int] { (l, r, qb) =>
      qb.sqlBuilder += '('
      qb.expr(l)
      qb.sqlBuilder += '+'
      qb.expr(r)
      qb.sqlBuilder += "+1)"
    }
    check(myExpr(4, 5), 10)
  }
}
