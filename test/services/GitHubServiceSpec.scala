package services

import baseSpec.BaseSpec
import cats.data.EitherT
import connectors.GithubConnector
import models.{APIError, GitHubUser}
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.{JsValue, Json, OFormat}

import scala.concurrent.{ExecutionContext, Future}

class GitHubServiceSpec extends BaseSpec with MockFactory with ScalaFutures with GuiceOneAppPerSuite {
  val mockConnector = mock[GithubConnector]
  implicit val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]
  val testService = new GitHubService(mockConnector)

  val user: JsValue = Json.obj(
    "_id" -> "1234",
    "login" -> "testuser",
    "location" -> "London",
    "followers" -> 10,
    "following" -> 10,
    "created_at" -> "01/01/01"
  )

  "getGithubUserByName" should {
    val url: String = "testUrl"

    "return a GitHub user" in {
      val expectedUser = user.as[GitHubUser]

      (mockConnector.get[GitHubUser](_: String)(_: OFormat[GitHubUser], _: ExecutionContext)).expects(url, *, *)
        .returning(EitherT.rightT[Future, APIError](expectedUser))
        .once()
      whenReady(testService.getGithubUserByName(urlOverride = Some(url), username = "").value) { result =>
        result shouldBe Right(expectedUser)
      }
    }
  }
}
