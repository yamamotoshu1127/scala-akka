package com.example.akkainaction.gotickets

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import com.typesafe.config.ConfigFactory

object FrontendRemoteDeployMain extends App with StartUp {

  val config = ConfigFactory.load("frontend-remote-deploy")
  implicit val system = ActorSystem("frontend", config)
  val api = new RestApi() {
    val log = Logging(system.eventStream, "frontend-remote")
    implicit val requestTimeout = configuredRequestTimeout(config)

    implicit def executionContext = system.dispatcher

    // boxOffice deploy to Remote.
    def createBoxOffice: ActorRef = system.actorOf(BoxOffice.props, BoxOffice.name)
  }

  startUp(api.routes)

}