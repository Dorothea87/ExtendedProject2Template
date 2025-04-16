package repositories

import com.google.inject.ImplementedBy
import models.DataModel
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
  def create(user: DataModel): Future[DataModel]

  def read(id: String): Future[DataModel]

  def update(id: String, user: DataModel): Future[result.UpdateResult]

  def delete(id: String): Future[result.DeleteResult]

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

  def create(user: DataModel): Future[DataModel] =
    collection.find(byUsername(user.username)).headOption() flatMap {
      case None => collection.insertOne(user).toFuture().map(_ => user)
      case Some(existingUser) => Future.successful(existingUser)
    }

  def read(username: String): Future[DataModel] =
    collection.find(byUsername(username)).headOption().map {
      case Some(data) => data
    }

  def update(username: String, user: DataModel): Future[result.UpdateResult] =
    collection.replaceOne(
      filter = byUsername(username),
      replacement = user,
      options = new ReplaceOptions().upsert(true) //What happens when we set this to false?
    ).toFuture()

  def delete(username: String): Future[result.DeleteResult] =
    collection.deleteOne(
      filter = byUsername(username)
    ).toFuture()

  def deleteAll(): Future[Unit] = collection.deleteMany(empty()).toFuture().map(_ => ())

}
