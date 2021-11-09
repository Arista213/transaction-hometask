package ru.tinkoff.backendacademy.factorial

import org.scalatest.Inspectors
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class FactorialSpec extends AnyFlatSpec with Matchers with Inspectors {
  def factorialBehaviour(f: Factorial): Unit = {
    it should "calc factorial 4" in {
      f.get(4) shouldBe 24
    }

    it should "calc factorial until 30" in {
      0 to 30 map (BigDecimal(_: Int)) foreach  f.get
    }
    it should "calc factorial until 300" in {
      forAll(30 to 300) { n =>
        try {
          f.get(n)  > 1
        } catch {
          case _: StackOverflowError => fail()
        }
      }

    }
    it should "calc factorial 0" in {
      f.get(1) shouldBe 1
    }
  }

  "LazyFactorial" should behave like factorialBehaviour(LazyFactorial)
  "RecursiveFactorial" should behave like factorialBehaviour(RecursiveFactorial)
}
