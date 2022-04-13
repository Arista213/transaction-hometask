package ru.tinkoff.backendacademy.wordstorage.console

import ru.tinkoff.backendacademy.wordstorage.backend.WordRepository

import scala.io.StdIn.readLine

class WordStorageConsoleFrontend(backend: WordRepository) {

  private val commandAndWord = "^(get|put|delete)\\s+(.*)$".r

  def start(): Unit =
    while (true) {
      val str = readLine()
      str match {
        case commandAndWord("get", word)    => println(backend.get(word))
        case commandAndWord("put", word)    => backend.put(word)
        case commandAndWord("delete", word) => backend.delete(word)
        case "quit"                         => return
        case _ =>
          println(
            "Could not recognize command, please use one of " +
              """"get", "put" or "delete" and then write your word, or write "quit""""
          )
      }
    }
}
