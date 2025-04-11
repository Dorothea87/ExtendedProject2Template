package controllers

import play.api.mvc.{BaseController, ControllerComponents}

import javax.inject.Inject



class ApplicationController @Inject() (val controllerComponents: ControllerComponents) extends BaseController {

def create() = TODO
def read(username: String) = TODO
def update(username: String) = TODO
def delete(username: String) = TODO

}
