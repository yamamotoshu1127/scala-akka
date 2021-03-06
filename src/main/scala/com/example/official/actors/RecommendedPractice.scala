package com.example.official.actors

import akka.actor._

object DemoActor {
  def props(magicNumber: Int) = Props(new DemoActor(magicNumber))
}

class DemoActor(magicNumber: Int) extends Actor {

  def receive = {
    case x: Int => sender ! (magicNumber + x)
  }
}

object PActor {
  def props = Props[PActor]

  case class Greeting(from: String)

  case object GoodBye

}

class PActor extends Actor with ActorLogging {

  import PActor._

  val initialGreet = ""
  var greet = initialGreet

  def receive = {
    case Greeting(greeter) =>
      log.info(s"I was greeted by ${greeter}")
      greet = greeter
    case GoodBye =>
      log.info("someone said goodbye to me")
      greet = initialGreet
    case _ => sys.error("unknown message")
  }
}

class RecommendedPractice extends App {

  import PActor._

  val system = ActorSystem("testSystem")
  val demoActor = system.actorOf(DemoActor.props(10))
  demoActor ! 5
  val myActor = system.actorOf(PActor.props)
  myActor ! Greeting("foo")
  myActor ! GoodBye
  system.terminate()
}
