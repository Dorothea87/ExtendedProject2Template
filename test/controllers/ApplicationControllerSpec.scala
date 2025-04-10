package controllers

import baseSpec.BaseSpecWithApplication


class ApplicationControllerSpec extends BaseSpecWithApplication {

  val TestApplicationController = new ApplicationController(
    component
  )
  "ApplicationController .create()" should {

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
