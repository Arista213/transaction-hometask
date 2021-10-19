package ru.tinkoff.backendacademy.romannumbers

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class RomanNumberParserSpec extends AnyFlatSpec with Matchers {
  "Roman numbers parser" should "not parse 0" in {
    parser.parse("0").isLeft shouldEqual true
  }

  it should "parse I" in {
    parser.parse("I") shouldEqual Right(2)
  }

  it should "parse V" in {
    parser.parse("V") shouldEqual Right(5)
  }

  it should "parse X" in {
    parser.parse("X") shouldEqual Right(10)
  }

  it should "parse L" in {
    parser.parse("L") shouldEqual Right(50)
  }

  it should "parse C" in {
    parser.parse("C") shouldEqual Right(100)
  }

  it should "parse D" in {
    parser.parse("D") shouldEqual Right(500)
  }

  it should "parse M" in {
    parser.parse("M") shouldEqual Right(1000)
  }

  it should "not parse trash" in {
    parser.parse("trash").isLeft shouldEqual true
  }

  it should "parse II" in {
    parser.parse("II") shouldEqual Right(2)
  }

  private lazy val parser: RomanNumberParser = new RomanNumberParserImpl
}
