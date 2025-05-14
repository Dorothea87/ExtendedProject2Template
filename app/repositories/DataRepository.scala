package repositories

import com.google.inject.ImplementedBy
import models.{APIError, DataModel}
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.empty
import org.mongodb.scala.model._
import org.mongodb.scala.result
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}


@ImplementedBy(classOf[DataRepository])
trait DataRepositoryTrait {
  def create(user: DataModel): Future[Either[APIError, DataModel]]

  def read(id: String): Future[Either[APIError, Option[DataModel]]]

  def update(id: String, user: DataModel): Future[Either[APIError, result.UpdateResult]]

  def delete(id: String): Future[Either[APIError, result.DeleteResult]]

  def deleteAll(): Future[Unit]
}


@Singleton
class DataRepository @Inject()(
                                mongoComponent: MongoComponent
                              )(implicit ec: ExecutionContext) extends PlayMongoRepository[DataModel](
  collectionName = "dataModels",
  mongoComponent = mongoComponent,
  domainFormat = DataModel.formats,
  indexes = Seq(IndexModel(
    Indexes.ascending("_id")
  )),
  replaceIndexes = false
) with DataRepositoryTrait {

  private def byUsername(username: String): Bson =
    Filters.and(
      Filters.equal("username", username)
    )

  def create(user: DataModel): Future[Either[APIError.BadAPIResponse, DataModel]] = {
    collection.find(byUsername(user.username)).headOption().flatMap {
      case Some(existingUser) => Future.successful(Left(APIError.BadAPIResponse(409, "Username already exists")))
      case None => collection.insertOne(user).toFuture().map(_ => Right(user)).recover {
        case ex: Exception => Left(APIError.BadAPIResponse(500, s"An error occurred when trying to ad the user with the username ${user.username}."))
      }
    }
  }

  def read(username: String): Future[Either[APIError.BadAPIResponse, Option[DataModel]]] =
    collection.find(byUsername(username)).headOption().map(data => Right(data)).recover {
      case ex: Exception => Left(APIError.BadAPIResponse(500, s"An error occurred: ${ex.getMessage}"))
    }

  def update(username: String, user: DataModel): Future[Either[APIError.BadAPIResponse, result.UpdateResult]] =
    collection.replaceOne(
      filter = byUsername(username),
      replacement = user,
      options = new ReplaceOptions().upsert(false) //What happens when we set this to false?
    ).toFuture().map { result =>
      if (result.getModifiedCount > 0) Right(result)
      else Left(APIError.BadAPIResponse(404, s"No user found with username: ${user.username}"))
    }.recover {
      case _ => Left(APIError.BadAPIResponse(500, "An error occurred when trying to find this user."))
    }

  def delete(username: String): Future[Either[APIError.BadAPIResponse, result.DeleteResult]] =
    collection.deleteOne(
      filter = byUsername(username)
    ).toFuture().map { deleted =>
      if (deleted.getDeletedCount > 0) Right(deleted)
      else Left(APIError.BadAPIResponse(400, s"No user was found with the username: $username"))
    }.recover {
      case ex: Exception => Left(APIError.BadAPIResponse(500, s"An error occurred: ${ex.getMessage}"))
    }

  def deleteAll(): Future[Unit] = collection.deleteMany(empty()).toFuture().map(_ => ())

}
