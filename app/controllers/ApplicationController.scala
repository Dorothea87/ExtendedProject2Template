package controllers

import model.DataModel
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, _}
import service.RepositoryService

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ApplicationController @Inject()(val controllerComponents: ControllerComponents, val repositoryService: RepositoryService)(implicit val ec: ExecutionContext) extends BaseController {

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        repositoryService.create(dataModel).map(_ => Created)
      case JsError(_) => Future(BadRequest)
    }
  }

  def read(username: String): Action[AnyContent] = Action.async { implicit request =>
    repositoryService.read(username).map(user => Ok {
      Json.toJson(user)
    })
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
    repositoryService.delete(username).map(_ => Accepted)

  }
}
