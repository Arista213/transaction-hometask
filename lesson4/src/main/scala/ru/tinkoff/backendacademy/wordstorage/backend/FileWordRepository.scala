package ru.tinkoff.backendacademy.wordstorage.backend

import java.nio.file.{Files, Path, StandardOpenOption}
import scala.jdk.CollectionConverters.{CollectionHasAsScala, IterableHasAsJava}
import scala.jdk.OptionConverters.RichOptional

class FileWordRepository(file: Path) extends WordRepository {
  override def get(word: String): Option[String] = Files.lines(file).filter(word.equals).findAny().toScala

  override def put(word: String): Unit =
    Files.writeString(file, word + System.lineSeparator(), StandardOpenOption.APPEND)

  override def delete(word: String): Unit = Files.write(file, allLines.filterNot(word.equals).asJava)

  private def allLines: Iterable[String] =
    Files.readAllLines(file).asScala
}
