package ru.tinkoff.backendacademy.wordstorage.backend

trait WordRepository {

  def get(word: String): Option[String]

  def put(word: String): Unit

  def delete(word: String): Unit

}
