package controllers

import javax.inject._
import play.api.mvc._
import services.ConfigService

@Singleton
class StaticController @Inject()(val controllerComponents: ControllerComponents,
                                 configService: ConfigService,
                                 val assets: Assets) extends BaseController {

  def apns(): Action[AnyContent] = Action {
    Ok(
      s"""
         |{
         |  "applinks": {
         |    "apps": [],
         |    "details": [
         |      {
         |        "appID": "${configService.appIosId}",
         |        "paths": [
         |          "/app/*"
         |        ]
         |      }
         |    ]
         |  }
         |}
         |""".stripMargin).withHeaders(
      "Content-Type" -> "text/plain",
      "Content-Disposition" -> "attachment; filename=apple-app-site-association"
    )
  }

  def assetlinks(): Action[AnyContent] = Action {
    Ok(
      s"""
         [
         |  {
         |    "relation": [
         |      "delegate_permission/common.handle_all_urls"
         |    ],
         |    "target": {
         |      "namespace": "android_app",
         |      "package_name": "${configService.appAndroidId}",
         |      "sha256_cert_fingerprints": [
         |        "${configService.appAndroidFingerprint}"
         |      ]
         |    }
         |  }
         |]
         |""".stripMargin).withHeaders(
      "Content-Type" -> "text/plain",
      "Content-Disposition" -> "attachment; filename=apple-app-site-association"
    )
  }

}
