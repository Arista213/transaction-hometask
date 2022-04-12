package ru.tinkoff.backendacademy.petapp.repository.owner
import ru.tinkoff.backendacademy.petapp.domain.{Owner, Pet}
import zio.ZIO

object SampleOwnerRepository extends OwnerRepository {
  override def findAllPetsOfOwner(pet: Pet): ZIO[Any, String, Seq[Pet]] =
    ZIO.succeed(Seq(Pet(555, "whale", pet.ownerId)))

  override def findOwnersWithoutPets(): ZIO[Any, String, Seq[Owner]] = ???
}
