package ru.tinkoff.backendacademy.factorial

import cats.Eval

object LazyFactorial extends Factorial {
  def lazyGet(n: BigDecimal): Eval[BigDecimal] =
    if(n <= 1) Eval.now(1)
    else Eval.defer(lazyGet(n - 1).map(_ * n))

  override def get(n: BigDecimal): BigDecimal = lazyGet(n).value
}
