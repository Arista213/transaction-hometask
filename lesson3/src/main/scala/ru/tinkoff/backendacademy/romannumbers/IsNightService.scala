package ru.tinkoff.backendacademy.romannumbers

import java.time.{Clock, LocalTime}

class IsNightService(clock: Clock) {

  def isNight(): Boolean = {
    val time = LocalTime.now(clock)
    time.isAfter(LocalTime.parse("21:00")) || time.isBefore(LocalTime.parse("06:00"))
  }

}
