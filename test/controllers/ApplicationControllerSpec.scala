package controllers

import baseSpec.BaseSpecWithApplication
import play.api.test.FakeRequest
import play.api.http.Status
import play.api.test.Helpers._


class ApplicationControllerSpec extends BaseSpecWithApplication {

  val TestApplicationController = new ApplicationController(
    component
  )
  "ApplicationController .create()" should {
    val result = TestApplicationController.create()(FakeRequest())

  }

  "ApplicationController .update(username: String)" should {

    "return a 200 OK" in {

    }
  }

  "ApplicationController .read(username: String)" should {

  }

  "ApplicationController .delete(username: String)" should {

  }

}
