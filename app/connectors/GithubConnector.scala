package connectors

import cats.data.EitherT
import com.google.inject._
import models.APIError
import play.api.libs.json.Reads
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class GithubConnector @Inject()(ws: WSClient) {

  def get[Response](url: String)(implicit rds: Reads[Response], ec: ExecutionContext): EitherT[Future, APIError, Response] = {
    val request = ws.url(url)
    val response = request.get()

    EitherT {
      response.map { result =>
        if (result.status == 200) {
          Right(result.json.as[Response])
        } else {
          Left(APIError.BadAPIResponse(result.status, result.statusText))
        }
      }.recover { case _: WSResponse =>
        Left(APIError.BadAPIResponse(500, "Could not connect to API."))
      }
    }
  }
}