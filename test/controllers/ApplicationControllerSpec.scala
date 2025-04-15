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

    "return BadRequest if JSON is invalid" in {
      beforeEach()

      val invalidRequest = buildPost("/api").withBody[JsValue](Json.obj("invalidField" -> "value"))
      val result = TestApplicationController.create()(invalidRequest)

      status(result) shouldBe Status.BAD_REQUEST

      afterEach()
    }

  }

  "ApplicationController .update(username: String)" should {
    "update an existing data set" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)

      status(createdResult) shouldBe Status.CREATED

      val updatedModel = dataModel.copy(username = "Updated Username")
      val updateRequest: FakeRequest[JsValue] = buildPut("/api/${dataModel.username}").withBody[JsValue](Json.toJson(updatedModel))


      val updatedResult: Future[Result] = TestApplicationController.update(dataModel.username)(updateRequest)
      status(updatedResult) shouldBe Status.ACCEPTED


      val updatedContent = contentAsJson(updatedResult).as[DataModel]
      updatedContent.username shouldBe "Updated Username"

      afterEach()
    }

    "return 400 BadRequest if JSON is invalid (non-existent key)" in {
      beforeEach()

      val createRequest: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
      val createResult: Future[Result] = TestApplicationController.create()(createRequest)

      status(createResult) shouldBe Status.CREATED

      val invalidRequest = buildPost("/api/testId").withBody[JsValue](Json.obj("invalidField" -> "value"))
      val updateResult: Future[Result] = TestApplicationController.update("test name")(invalidRequest)

      status(updateResult) shouldBe Status.BAD_REQUEST

      afterEach()
    }

    "Return a bad request, 400 if JSON invalid (value wrong type)" in {
      beforeEach()

      val createRequest: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
      val createResult: Future[Result] = TestApplicationController.create()(createRequest)

      status(createResult) shouldBe Status.CREATED

      val invalidRequest: FakeRequest[JsValue] = buildPut("/api/${dataModel.username}").withBody(Json.obj("created_at" -> 2))
      val updatedResult: Future[Result] = TestApplicationController.update(dataModel.username)(invalidRequest)

      status(updatedResult) shouldBe Status.BAD_REQUEST

      afterEach()}
  }

  "ApplicationController .read(username: String)" should {
    "find a user in the database by username" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildGet("/api/${dataModel.username}").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)

      status(createdResult) shouldBe Status.CREATED


      val readResult: Future[Result] = TestApplicationController.read("test name")(FakeRequest())

      status(readResult) shouldBe Status.OK
      contentAsJson(readResult).as[DataModel] shouldBe dataModel

      afterEach()
    }

   /* "return 404 NotFound for invalid username" in {
      beforeEach()

      val readResult: Future[Result] = TestApplicationController.read("invalidUsername")(FakeRequest())

      status(readResult) shouldBe Status.NOT_FOUND

      afterEach()
    }*/

  }

  "ApplicationController .delete(username: String)" should {

    "delete a user in the database" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)

      status(createdResult) shouldBe Status.CREATED

      val deleteRequest: FakeRequest[JsValue] = buildDelete("/api").withBody[JsValue](Json.toJson(dataModel))
      val deletedResult: Future[Result] = TestApplicationController.delete(dataModel.username)(FakeRequest())

      status(createdResult) shouldBe Status.CREATED
      afterEach()
    }

  }


  override def beforeEach(): Unit = await(repository.deleteAll())

  override def afterEach(): Unit = await(repository.deleteAll())

}
