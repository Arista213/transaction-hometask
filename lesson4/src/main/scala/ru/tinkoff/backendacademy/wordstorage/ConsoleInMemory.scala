package ru.tinkoff.backendacademy.wordstorage

import ru.tinkoff.backendacademy.wordstorage.backend.{FileWordRepository, InMemoryWordRepository}
import ru.tinkoff.backendacademy.wordstorage.console.WordStorageConsoleFrontend

import java.nio.file.Files

object ConsoleInMemory extends App {
  new WordStorageConsoleFrontend(
    new FileWordRepository(Files.createTempFile("backendacademy", null))
  ).start()
//  new WordStorageConsoleFrontend(new InMemoryWordRepository()).start()
}
