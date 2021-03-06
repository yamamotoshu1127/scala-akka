package com.example.blog.akka

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object Main extends App {
  implicit val timeout: Timeout = Timeout(5 seconds)

  val system = ActorSystem("system")
  val actor = system.actorOf(Props[HelloActor], "helloActor")

  actor ! "Hello"
  actor ! "foo"

  val reply = actor ? "How are you?"
  reply.onComplete {
    case Success(res) => println(res)
    case Failure(ex) => sys.error(ex.toString)
  }

  val supervisorActor = system.actorOf(Props[SupervisorActor], "supervisorActor")

  supervisorActor ! "Hello"
  supervisorActor ! ForChild("Hello")
  val childActor = supervisorActor ? Child

  childActor.onComplete {
    case Success(ca) => println(ca.toString)
    case Failure(ex) => sys.error(ex.toString)
  }

  system.terminate()
}
