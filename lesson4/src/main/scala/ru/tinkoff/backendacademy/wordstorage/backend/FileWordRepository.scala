package ru.tinkoff.backendacademy.wordstorage.backend

import java.nio.file.{Files, Path}
import scala.jdk.CollectionConverters.{CollectionHasAsScala, IterableHasAsJava}
import scala.jdk.OptionConverters.RichOptional

class FileWordRepository(file: Path) extends WordRepository {
  override def get(word: String): Option[String] =
    Files.lines(file).filter(word.equals).findAny().toScala

  override def put(word: String): Unit = {
    val _ = Files.write(file, (allLines.toSeq :+ word).asJava)
  }

  override def delete(word: String): Unit = {
    val _ = Files.write(file, allLines.filterNot(word.equals).asJava)
  }

  private def allLines: Iterable[String] =
    Files.readAllLines(file).asScala
}
