package ru.tinkoff.backendacademy


sealed trait LocationId
sealed trait ShipLocation extends LocationId
sealed trait TruckLocation extends LocationId

case object A extends TruckLocation
case object B extends ShipLocation
case object P extends TruckLocation with ShipLocation
case object S extends TruckLocation
