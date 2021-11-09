package ru.tinkoff.backendacademy

trait Vehicle {
  def id: Int
}

case class Truck(id: Int) extends Vehicle
case class Ship(id: Int)  extends Vehicle
