package ru.tinkoff.backendacademy

sealed trait Edge {
  def to: Int
  def weight: Int
}

case class WaterEdge(to: Int, weight: Int)  extends Edge
case class GroundEdge(to: Int, weight: Int) extends Edge
