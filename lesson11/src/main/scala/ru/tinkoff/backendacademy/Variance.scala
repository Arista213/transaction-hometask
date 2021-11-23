package ru.tinkoff.backendacademy

import scala.collection.mutable

object Variance extends App {
  trait Device

  trait Keyboard extends Device {
    def keysCount: Int
  }

  trait Mouse extends Device

  val k = new Keyboard {

    override def keysCount: Int = 104

    override def toString: String = "keyboard"
  }
  val m = new Mouse {
    override def toString: String = "mouse"
  }

  val mutableKeyboardSet: mutable.Set[Keyboard] = scala.collection.mutable.Set.empty
  mutableKeyboardSet.add(k)super.toString
  println(mutableKeyboardSet)
  //  val mutableDeviceSet: mutable.Set[Device] = mutableKeyboardSet
  //  mutableDeviceSet.add(m)

  val keyboardSeq: Seq[Keyboard] = Seq.empty[Keyboard]
  val filledKeyboardSeq = keyboardSeq :+ (k)
  println(filledKeyboardSeq)
  val deviceSeq: Seq[Device] = filledKeyboardSeq
  println(deviceSeq :+ m)

  trait Order[-T] {
    def order(t: T): Int
  }

  val keyboardOrder: Order[Keyboard] = new Order[Keyboard] {
    override def order(t: Keyboard): Int = t.keysCount
  }
  val mouseOrder: Order[Mouse] = new Order[Mouse] {
    override def order(t: Mouse): Int = 0
  }
  val deviceOrder: Order[Device] = new Order[Device] {
    override def order(t: Device): Int = 0
  }

  val device: Device = k

  keyboardOrder.order(k)
//  keyboardOrder.order(device)

  deviceOrder.order(device)
  deviceOrder.order(k)
  deviceOrder.order(m)

  val defaultDeviceOrder: Order[Keyboard] = deviceOrder
//  val deviceOrder2: Order[Device] = keyboardOrder

  deviceOrder.order(m)

  val f1: Device => Device = ???
  f1(device)
  f1(k)

  val f2: Keyboard => Device = f1

  val f3: Device => Keyboard = ???
  val f4: Keyboard => Keyboard = f3
}

