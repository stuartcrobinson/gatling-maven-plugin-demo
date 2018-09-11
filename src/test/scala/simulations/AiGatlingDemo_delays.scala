package simulations

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._


class AiGatlingDemo_delays extends Simulation {

  //  mvn gatling:test -Dgatling.simulationClass=simulations.AiGatlingDemo_delays

  //  file:///Users/stuart.robinson/repos/gatling-maven-plugin-demo/target/gatling/aigatlingdemo-delays-1534464250799/index.html

  val httpConf = http
    .baseURL("https://reqres.in/api/")

  val delaysFeeder = Array(
    Map("delay" -> "0"),
    Map("delay" -> "0.3"),
    Map("delay" -> "0.6")
  ).random

  val scn1 = scenario("Scenario Name 1")
    .feed(delaysFeeder)
    .exec(
      http("""delay: ${delay} seconds""")
        .get("/users?delay=" +"""${delay}""")
    )

  setUp(scn1
    .inject(
      constantUsersPerSec(6) during (10 seconds),
    )
    .protocols(httpConf)
  )
}
