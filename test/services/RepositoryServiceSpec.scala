package services

import baseSpec.BaseSpec
import models.{APIError, DataModel}
import org.mongodb.scala.result
import org.openqa.selenium.json.Json
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.{JsValue, Json}
import repositories.DataRepositoryTrait

import scala.concurrent.{ExecutionContext, Future}

class RepositoryServiceSpec extends BaseSpec with MockFactory with ScalaFutures with GuiceOneAppPerSuite {
  val mockDataRepo = mock[DataRepositoryTrait]
  implicit val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]
  val testRepoService = new RepositoryService(mockDataRepo)

  val dataModel: DataModel = DataModel(
    "1234",
    "testuser",
    "London",
    10,
    10,
    "01/01/01"
  )

  "RepositoryService .create" should {
    "return a DataModel" when {
      "dataRepository create returns a DataModel" in {
        (mockDataRepo.create(_: DataModel))
          .expects(dataModel)
          .returning(Future(Right(dataModel)))
          .once()

        whenReady(testRepoService.create(dataModel)) { result =>
          result shouldBe Right(dataModel)
        }
      }
    }
    "return a Bad request error" when {
      "dataRepository create tries to create a duplicate" in {
        val apiError = APIError.BadAPIResponse(500, s"The user you tried to create, already exists.")
        (mockDataRepo.create(_: DataModel))
          .expects(dataModel)
          .returning(Future.successful(Left(apiError)))
          .once()

        whenReady(testRepoService.create(dataModel)) { result =>
          result shouldBe Left(apiError)
        }
      }
    }
  }

  "RepositoryService .read" should {
    "return a Data Model" when {
      ".read in the Data repository returns a DataModel" in {
        val username = dataModel.username
        (mockDataRepo.read(_: String))
          .expects(username)
          .returning(Future(Right(Some(dataModel))))
          .once()

        whenReady(testRepoService.read(username)) { result =>
          result shouldBe Right(Some(dataModel))
        }
      }
    }
    "return a Bad request error" when {
      "dataRepository .read tries to read an invalid entry" in {
        val username = dataModel.username

        val apiError = APIError.BadAPIResponse(500, s"An error occurred.")
        (mockDataRepo.read(_: String))
          .expects(username)
          .returning(Future.successful(Left(apiError)))
          .once()

        whenReady(testRepoService.read(username)) { result =>
          result shouldBe Left(apiError)
        }
      }
    }
  }
  "RepositoryService .update" should {
    "return a Right" when {
      "dataRepository update returns a Right" in {
        val username = dataModel.username
        val user = dataModel
        val updateResult = mock[result.UpdateResult]

        (mockDataRepo.update(_: String, _: DataModel))
          .expects(username, user)
          .returning(Future(Right(updateResult)))
          .once()

        whenReady(testRepoService.update(username, user)) { result =>
          result shouldBe Right(updateResult)
        }
      }
    }

    "return an error" when {
      "dataRepository update returns a Left" in {
        val username = dataModel.username
        val user = dataModel

        val apiError = APIError.BadAPIResponse(500, s"An error occurred when trying to update a book with id: S{dataModel._id}")
        (mockDataRepo.update(_: String, _: DataModel))
          .expects(username, user)
          .returning(Future.successful(Left(apiError)))
          .once()

        whenReady(testRepoService.update(username, user)) { result =>
          result shouldBe Left(apiError)
        }
      }
    }
  }

  " RepositoryService .delete" should {
    "return a Right" when {
      "dataRepository delete returns a Right" in {
        val username = dataModel.username
        val deleteResult = mock[result.DeleteResult]

        (mockDataRepo.delete(_: String))
          .expects(username)
          .returning(Future(Right(deleteResult)))
          .once()

        whenReady(testRepoService.delete(username)) { result =>
          result shouldBe Right(deleteResult)
        }
      }
    }

    "return a Left" when {
      "dataRepository delete returns a Left" in {
        val username = dataModel.username

        val apiError = APIError.BadAPIResponse(404, s"An error occurred when trying to delete a book with this id: S{dataModel._id}")
        (mockDataRepo.delete(_: String))
          .expects(username)
          .returning(Future.successful(Left(apiError)))
          .once()

        whenReady(testRepoService.delete(username)) { result =>
          result shouldBe Left(apiError)
        }
      }
    }
  }
}
