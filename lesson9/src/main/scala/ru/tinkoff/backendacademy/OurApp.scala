package ru.tinkoff.backendacademy

object OurApp {
  case class AvailabilityTime(truck1: Int, truck2: Int, ship: Int)

  def main(args: Array[String]): Unit = {

    val line  = "BBABAABA"
    val lenSA = 5
    val lenSP = 1
    val lenPB = 4

    val result = line.foldLeft(AvailabilityTime(0, 0, 0)) {
      case (t @ AvailabilityTime(truck1, truck2, ship), 'A') if truck1 > truck2 =>
        t.copy(truck2 = truck2 + lenSA * 2)
      case (t @ AvailabilityTime(truck1, truck2, ship), 'A') if truck1 <= truck2 =>
        t.copy(truck1 = truck1 + lenSA * 2)

      case (t @ AvailabilityTime(truck1, truck2, ship), 'B') if truck1 > truck2 =>
        t.copy(truck2 = truck2 + lenSP * 2, ship = Math.max(truck2 + lenSP, ship) + lenPB * 2)
      case (t @ AvailabilityTime(truck1, truck2, ship), 'B') if truck1 <= truck2 =>
        t.copy(truck1 = truck1 + lenSP * 2, ship = Math.max(truck1 + lenSP, ship) + lenPB * 2)
    }

    val time = List(result.truck1 - lenSA, result.truck2 - lenSA, result.ship - lenPB).max

    println(time)
  }
}
