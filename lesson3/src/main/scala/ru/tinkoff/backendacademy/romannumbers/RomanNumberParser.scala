package ru.tinkoff.backendacademy.romannumbers

trait RomanNumberParser {
  def parse(romanNumber: String): Either[String, Int]
}

class RomanNumberParserImpl extends RomanNumberParser {
  override def parse(romanNumber: String): Either[String, Int] = romanNumber
    .map(digit)
    .foldLeft[Either[String, Int]](Right(0))((acc, value) =>
      for {
        accInt   <- acc
        valueInt <- value
      } yield accInt + valueInt
    )

  private def digit(romanNumber: Char): Either[String, Int] =
    romanNumber match {
      case 'I' => Right(1)
      case 'V' => Right(5)
      case 'X' => Right(10)
      case 'L' => Right(50)
      case 'C' => Right(100)
      case 'D' => Right(500)
      case 'M' => Right(1000)
      case _   => Left("")
    }
}
