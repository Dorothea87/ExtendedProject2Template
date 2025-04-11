package repository

import model.DataModel
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.empty
import org.mongodb.scala.model._
import org.mongodb.scala.result
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

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
) {


  def create(user: DataModel): Future[DataModel] =
    collection.insertOne(user).toFuture().map(_ => user)

  private def byUsername(username: String): Bson =
    Filters.and(
      Filters.equal("username", username)
    )

  def read(username: String): Future[DataModel] =
    collection.find(byUsername(username)).headOption flatMap {
      case Some(data) =>
        Future(data)
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
