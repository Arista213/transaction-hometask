package ru.tinkoff.backendacademy

import cats.Eval
import ru.tinkoff.backendacademy.factorial.{LazyFactorial, RecursiveFactorial}

import scala.annotation.tailrec

object Demo {

  case class MegaTicket(name: String)

  class Cinema(name: String) {
    class Ticket {
      val cinema = name
    }

    def buyTicket: Ticket                  = new Ticket
    def refundTicket(ticket: Ticket): Unit = ()
  }

  val venom   = new Cinema("venom")
  val bond007 = new Cinema("venom")

//  bond007.refundTicket(venom.buyTicket)
  venom.refundTicket(venom.buyTicket)

  class Cinema2(name: String) {
    type Ticket = MegaTicket
    def buyTicket: Ticket                  = MegaTicket(name)
    def refundTicket(ticket: Ticket): Unit = ()
  }

  val avengers = new Cinema2("avengers")
  val dune     = new Cinema2("dune")

  dune.refundTicket(avengers.buyTicket)

  abstract class CustomCinema(val name: String) {
    type Ticket
    def buyTicket: Ticket
    def refundTicket(ticket: Ticket): Unit = ()
  }

  object CustomCinema {
    def make[T](name: String, controller: CustomCinema => T): CustomCinema = new CustomCinema(
      name
    ) {
      override type Ticket = T

      override def buyTicket: T = controller(this)
    }
  }

  val adams     = CustomCinema.make("Adams family", c => MegaTicket(c.name))
  val superstar = CustomCinema.make("superstar", c => MegaTicket(c.name))

//  superstar.refundTicket(adams.buyTicket)

  def main(args: Array[String]): Unit = {
    val foo: String => Int  = _.length
    val foo2: String => Int = (str) => str.length
//
//    println(List("1234", "123456").map(foo).map(_ * 2).toVector)
//
//    "123" :: "3123" :: Nil match {
//      case head :: tail =>
//      println(head)
//      println(tail)
//      case Nil =>
//    }

    @tailrec
    def recPrint[A](list: List[A], printer: A => Unit): Unit = {
      list match {
        case head :: next =>
          printer(head)
          recPrint(next, printer)
        case Nil =>
      }
    }

    def prefixPrint[A](prefix: String)(item: A): Unit =
      println(s"$prefix $item")

    val f1: String => String = _ + " >= "
    val f2: String => String = _ + "  <="

    val f3: String => String = f1.compose(f2)

    val f = f3.andThen(println)

    recPrint(LazyList.iterate(0)(_ + 1).take(1).map(_.toString).toList, f)

    trait Range

    object EmptyRange     extends Range
    class NonEmptyRange() extends Range

    trait Tree

    case class Node(left: Tree, right: Tree) extends Tree
    case class Leaf(value: Int)              extends Tree

    println(RecursiveFactorial.get(10))

    println("1:")
    val lazyValue = LazyFactorial.lazyGet(10).map(println).memoize
    println("2:")
    lazyValue.value
    println("3:")
    lazyValue.value

    val c: Eval[BigDecimal] = for {
      a <- LazyFactorial.lazyGet(6)
      b <- LazyFactorial.lazyGet(4)
    } yield a * b

  }
}
