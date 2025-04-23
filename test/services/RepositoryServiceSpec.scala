package services

import baseSpec.BaseSpec
import models.{APIError, DataModel}
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
          .returning(Future(dataModel))
          .once()

        whenReady(testRepoService.create(dataModel)) { result =>
          result shouldBe dataModel
        }
      }
    }
    "return a Bad request error" when {
      "dataRepository create tries to create a double entry" in {
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
}
