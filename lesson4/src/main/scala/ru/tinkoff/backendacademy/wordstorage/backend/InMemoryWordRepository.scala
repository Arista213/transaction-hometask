package ru.tinkoff.backendacademy.wordstorage.backend

import scala.collection.mutable

class InMemoryWordRepository extends WordRepository {
  private val words: mutable.Set[String] = mutable.Set()

  override def get(word: String): Option[String] = words.find(word.equals)

  override def put(word: String): Unit = words.add(word)

  override def delete(word: String): Unit = words.remove(word)

}
