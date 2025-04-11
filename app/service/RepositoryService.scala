package service

import model.DataModel
import repository.DataRepositoryTrait

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RepositoryService @Inject()(dataRepository: DataRepositoryTrait)(implicit ec: ExecutionContext) {

  def create(user: DataModel): Future[DataModel] =
    dataRepository.create(user)

  def read(username: String): Future[DataModel] =
    dataRepository.read(username)

  def update(username: String, user: DataModel): Future[DataModel] =
    dataRepository.update(username, user)

  def delete(username: String): Future[String] =
    dataRepository.delete(username)
}
