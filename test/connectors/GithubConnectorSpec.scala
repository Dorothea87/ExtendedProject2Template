package connectors

import baseSpec.BaseSpec
import models.{APIError, GitHubUser}
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import play.api.libs.json.{Json, OFormat}
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import scala.concurrent.{ExecutionContext, Future}

class GithubConnectorSpec extends BaseSpec with ScalaFutures with MockFactory {
  val mockWsClient: WSClient = mock[WSClient]
  val mockRequest: WSRequest = mock[WSRequest]
  val connector: GithubConnector = new GithubConnector(mockWsClient)
  implicit val ec: ExecutionContext = ExecutionContext.global

  val testUser: GitHubUser = GitHubUser(
    "1234",
    "testUserName",
    Some("London"),
    50,
    25,
    "08/08/2024"
  )

  implicit val githubUserFormat: OFormat[GitHubUser] = Json.format[GitHubUser]

  "get" should {
    val url = "https://api.github.com/users/username"

    "return a Right" when {
      "the API response status is 200" in {
        val mockResponse = mock[WSResponse]
        (mockResponse.status _).expects().returning(200).once()
        (mockResponse.json _).expects().returning(Json.toJson(testUser)).once()

        (mockWsClient.url(_: String)).expects(url).returning(mockRequest).once()
        (mockRequest.get _).expects().returning(Future.successful(mockResponse)).once()

        whenReady(connector.get[GitHubUser](url).value) { result =>
          result shouldBe Right(testUser)
        }
      }
    }

    "return a Left" when {
      "the API response status is not 200" in {
        val mockResponse: WSResponse = mock[WSResponse]
        val error = APIError.BadAPIResponse(404, "Not Found")

        (mockResponse.status _).expects().returning(404).twice()
        (mockResponse.statusText _).expects().returning("Not Found").once()

        (mockWsClient.url(_: String)).expects(url).returning(mockRequest).once()
        (mockRequest.get _).expects().returning(Future.successful(mockResponse)).once()

        whenReady(connector.get[GitHubUser](url).value) { result =>
          result shouldBe Left(error)
        }
      }


      "the API request fails with an exception" in {
        (mockWsClient.url(_: String)).expects(url).returning(mockRequest).once()
        (mockRequest.get _).expects().returning(Future.failed(new RuntimeException("Network Error"))).once()

        whenReady(connector.get[GitHubUser](url).value.failed) { ex =>
          ex shouldBe a[RuntimeException]
          ex.getMessage shouldEqual "Network Error"
        }
      }
    }
  }
}

