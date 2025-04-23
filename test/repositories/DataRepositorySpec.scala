package repositories

import baseSpec.BaseSpec
import models.{APIError, DataModel}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import play.api.test.Injecting

import scala.concurrent.ExecutionContext

class DataRepositorySpec extends BaseSpec with Injecting with GuiceOneAppPerSuite with BeforeAndAfterEach {

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

  val repository: DataRepository = inject[DataRepository]

  override def beforeEach(): Unit = {
    await(repository.deleteAll())
    super.beforeEach()
  }

  "DataRepository .create" should {
    "create a Data Model" in {

      val dataModel = DataModel (
        "1234",
        "testUserName",
        "London",
        50,
        25,
        "08/08/2024"
      )

      val createResult = await(repository.create(dataModel))
      createResult mustBe Right(dataModel)

      val readResult = await(repository.read("test_user"))
      readResult mustBe Right(Some(dataModel))
    }
  }
}
