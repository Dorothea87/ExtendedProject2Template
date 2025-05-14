package controllers

import models.{DataModel, GitHubUser, APIError}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc._
import services.{GitHubService, RepositoryService}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ApplicationController @Inject()(val controllerComponents: ControllerComponents, val repositoryService: RepositoryService, val service: GitHubService)(implicit val ec: ExecutionContext) extends BaseController {

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        repositoryService.create(dataModel).map(_ => Created)
      case JsError(_) => Future(BadRequest)
    }
  }

  def read(username: String): Action[AnyContent] = Action.async { implicit request =>
    repositoryService.read(username).map {
      case Left(APIError.BadAPIResponse(statusCode, message)) =>
        Status(statusCode)(Json.toJson(message))
      case Right(None) => NotFound(Json.toJson("No data found"))
      case Right(Some(item: DataModel)) => Ok(Json.toJson(item))
    }
  }

  def update(username: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        repositoryService.update(username, dataModel).map(_ => Accepted {
          request.body
        })
      case JsError(_) => Future(BadRequest)
    }
  }


  def delete(username: String): Action[AnyContent] = Action.async { implicit request =>
    repositoryService.delete(username).map {
      case Left(APIError.BadAPIResponse(statusCode, message)) =>
        Status(statusCode)(Json.toJson(message))
      case Right(result) => Accepted(Json.toJson(s"Successfully deleted the user: $username"))
    }

  }

  def getGithubUserByName(username: String): Action[AnyContent] = Action.async { implicit request =>
    service.getGithubUserByName(username = username).value.map {
      case Left(error) => NotFound
      case Right(user) => Ok {
        Json.toJson(user)
      }
    }
  }
}
