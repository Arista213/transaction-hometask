package ru.tinkoff.backendacademy.wordstorage

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import ru.tinkoff.backendacademy.wordstorage.backend.WordRepository

trait WordRepositoryBehaviors {
  this: AnyFlatSpec with Matchers =>

  def validWordRepository(testedRepository: => WordRepository): Unit = {

    trait TestWiring {
      val repository: WordRepository = testedRepository
    }

    it should "return no word if empty" in new TestWiring {
      repository.get("some word") shouldBe empty
    }

    it should "return added word" in new TestWiring {
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
  }

}
