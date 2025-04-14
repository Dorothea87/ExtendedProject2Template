package controllers

import baseSpec.BaseSpecWithApplication
import model.DataModel
import play.api.test.FakeRequest
import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Result
import play.api.test.Helpers._

import scala.concurrent.Future


class ApplicationControllerSpec extends BaseSpecWithApplication {

  val TestApplicationController = new ApplicationController(
    component, repoService
  )(executionContext)

  private val dataModel: DataModel = DataModel(
    "abcd",
    "test name",
    "test date",
    "London",
    100,
    100
  )

  "ApplicationController .create" should {

    "create a user in the database" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)

      status(createdResult) shouldBe Status.CREATED
      afterEach()
    }


  }

//  "ApplicationController .update(username: String)" should {
//  }
//
//  "ApplicationController .read(username: String)" should {
//
//  }
//
//  "ApplicationController .delete(username: String)" should {
//
//  }


  override def beforeEach(): Unit = await(repository.deleteAll())

  override def afterEach(): Unit = await(repository.deleteAll())

}
