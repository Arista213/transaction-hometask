package ru.tinkoff.backendacademy.wordstorage

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import ru.tinkoff.backendacademy.wordstorage.backend.{FileWordRepository, InMemoryWordRepository, WordRepository}

import java.nio.file.Files

class WordRepositorySpec extends AnyFlatSpec with Matchers with WordRepositoryBehaviors {

  behavior of "File word repository"
  it should behave like validWordRepository(new FileWordRepository(Files.createTempFile(null, null)))

  behavior of "In memory word repository"
  it should behave like validWordRepository(new InMemoryWordRepository())

}
