package ru.tinkoff.backendacademy.factorial

object RecursiveFactorial extends Factorial {
  override def get(n: BigDecimal): BigDecimal = {
    if(n<=1) 1
    else get(n - 1) * n
  }
}
