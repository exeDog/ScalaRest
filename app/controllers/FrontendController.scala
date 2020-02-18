package controllers

import javax.inject.{Inject, Singleton}

import play.api.Configuration
import play.api.http.HttpErrorHandler
import play.api.mvc._

@Singleton
class FrontendController @Inject()(assets: Assets, errorHandler: HttpErrorHandler, configuration: Configuration, controllerComponents: ControllerComponents) extends AbstractController(controllerComponents = controllerComponents) {

  def index: Action[AnyContent] = assets.at("index.html")

  def assetOrDefault(resource: String): Action[AnyContent] =  if(resource.startsWith(configuration.get[String]("apiPrefix"))) {
    Action.async(r => errorHandler.onClientError(r, NOT_FOUND, "Not found"))
  } else {
    if(resource.contains(".")) assets.at(resource) else index
  }
}
