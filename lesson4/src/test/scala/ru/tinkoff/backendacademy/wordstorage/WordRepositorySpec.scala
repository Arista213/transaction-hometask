package ru.tinkoff.backendacademy.wordstorage

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import ru.tinkoff.backendacademy.wordstorage.backend.{FileWordRepository, InMemoryWordRepository, WordRepository}

import java.nio.file.Files

class WordRepositorySpec extends AnyFlatSpec with Matchers {

  "Empty word storage" should "return no word" in new TestWiring {
    repository.get("some word") shouldBe empty
  }

  "Non-empty word repository" should "return added word" in new TestWiring {
    repository.put("added word")
    repository.get("added word") shouldEqual Some("added word")
  }

  it should "delete added word" in new TestWiring {
    repository.put("deleted word")
    repository.delete("deleted word")
    repository.get("deleted word") shouldBe empty
  }

  it should "save 2 words" in new TestWiring {
    repository.put("word1")
    repository.put("word2")
    repository.get("word1") shouldEqual Some("word1")
  }

  private trait TestWiring {
//    lazy val repository: InMemoryWordRepository = new InMemoryWordRepository()
    lazy val repository: WordRepository = new FileWordRepository(Files.createTempFile(null, null))
  }

}
