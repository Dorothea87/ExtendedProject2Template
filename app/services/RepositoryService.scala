package services

import models.{APIError, DataModel}
import org.mongodb.scala.result
import repositories.DataRepositoryTrait

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RepositoryService @Inject()(dataRepository: DataRepositoryTrait)(implicit ec: ExecutionContext) {

  def create(user: DataModel): Future[Either[APIError, DataModel]] =
    dataRepository.create(user)

  def read(username: String): Future[Either[APIError, Option[DataModel]]] =
    dataRepository.read(username)

  def update(username: String, user: DataModel): Future[Either[ APIError, result.UpdateResult]] =
    dataRepository.update(username, user)

  def delete(username: String): Future[Either[APIError, result.DeleteResult]] =
    dataRepository.delete(username)
}
