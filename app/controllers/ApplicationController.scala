package controllers

import play.api.libs.json.JsValue
import play.api.mvc.{BaseController, ControllerComponents, Results}

import javax.inject.{Inject, Singleton}


@Singleton
class ApplicationController @Inject() (val controllerComponents: ControllerComponents) extends BaseController {

def create(): Action[JsValue] = Action.async(){ implicit request =>
 body.validate[DataModel] match {
   case Created(200 -> OK)
 }}

def read(username: String) = TODO
def update(username: String) = TODO
def delete(username: String) = TODO

}
