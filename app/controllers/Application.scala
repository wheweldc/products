package controllers

import javax.inject.{Inject, Singleton}

import play.api._
import play.api.mvc._

@Singleton
class Application @Inject() extends Controller {

  def index = Action { implicit request =>
    Redirect(routes.Products.list())
  }

}
