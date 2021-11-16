package ru.tinkoff.backendacademy

trait World {
  def now(): Int

  def events(): Seq[Event]
}

trait Warehouses {
  def containerDestinations(locationId: LocationId): Seq[LocationId]
}

case class WorldInMemory(now: Int, events: Seq[Event]) extends World with Warehouses {
  override def containerDestinations(locationId: LocationId): Seq[LocationId] =
    events.collect {
      case ContainerArrived(_, currentLocation, destination) if currentLocation == locationId => destination
    }
}

sealed trait Event {
  def time(): Int
}

case class ContainerArrived(time: Int, currentLocation: LocationId, destination: LocationId) extends Event

case class ShipDeparted(time: Int, currentLocation: ShipLocation, destination: ShipLocation) extends Event

case class TruckDeparted(time: Int, currentLocation: TruckLocation, destination: TruckLocation) extends Event

case class TruckArrived(time: Int, currentLocation: TruckLocation) extends Event
