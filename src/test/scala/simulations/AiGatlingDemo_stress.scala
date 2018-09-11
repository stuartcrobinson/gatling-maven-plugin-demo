package simulations

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class AiGatlingDemo_stress extends Simulation {
  // mvn gatling:test -Dgatling.simulationClass=simulations.AiGatlingDemo_stress -Dgatling.http.ahc.connectTimeout=20000
  // http://ec2-54-91-204-127.compute-1.amazonaws.com:3000/factorial/800

  //http://ec2-107-21-6-248.compute-1.amazonaws.com:3000/factorial/10
  val httpConf = http
    .baseURL("http://ec2-107-21-6-248.compute-1.amazonaws.com:3000/factorial/")

  //20
  val xFeeder = Array(
    Map("x" -> "80"),
    Map("x" -> "90"),
    Map("x" -> "100")
  ).random

  val scn1 = scenario("Scenario Name 1")
    .feed(xFeeder)
    .exec(
      http("""${x}""")
        .get("""${x}""")
    )

  //file:///Users/stuart.robinson/repos/gatling-maven-plugin-demo/target/gatling/aigatlingdemo-stress-1534463908448/index.html
  //don't change - blows up around 260
  setUp(scn1
    .inject(
      rampUsersPerSec(1) to 500 during (10 seconds)
    )
    .protocols(httpConf)
  )

//  //file:///Users/stuart.robinson/repos/gatling-maven-plugin-demo/target/gatling/aigatlingdemo-stress-1534463835421/index.html
//    setUp(scn1
//      .inject(
//        constantUsersPerSec(2500) during (25 seconds),
//      )
//      .throttle(
//        reachRps(100) in (1 seconds),
//        holdFor(4 seconds),
//        reachRps(175) in (1 seconds),
//        holdFor(4 seconds),
//        reachRps(225) in (1 seconds),
//        holdFor(5 seconds),
//      )
//      .protocols(httpConf)
//    )
}
