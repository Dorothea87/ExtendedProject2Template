package controllers

import model.DataModel
import play.api.libs.json.{JsError, JsSuccess, JsValue}
import play.api.mvc._
import service.RepositoryService

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ApplicationController @Inject() (val controllerComponents: ControllerComponents, val repositoryService: RepositoryService)(implicit val ec: ExecutionContext) extends BaseController {

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        repositoryService.create(dataModel).map(_ => Created)
      case JsError(_) => Future(BadRequest)
    }
  }


def read(username: String) = TODO
def update(username: String) = TODO
def delete(username: String) = TODO

}
