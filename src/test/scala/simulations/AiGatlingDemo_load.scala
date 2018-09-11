package simulations

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

////https://docs.google.com/presentation/d/1AqLjEni3k72Xuq9wcNtdx9XDyaUKA6ftVLNEa1cHJ-0/edit?usp=sharing

class AiGatlingDemo_load extends Simulation {
  //  mvn gatling:test -Dgatling.simulationClass=simulations.AiGatlingDemo_load
  //file:///Users/stuart.robinson/repos/gatling-maven-plugin-demo/target/gatling/aigatlingdemo-load-1534464076043/index.html

  val httpConf = http
    .baseURL("https://www.bing.com")

  val characterFeeder = Array(
    Map("character" -> "Patrick Star"),
    Map("character" -> "Squidward Tentacles"),
    Map("character" -> "Mr. Krabs"),
    Map("character" -> "Gary the Snail"),
    Map("character" -> "Mrs. Puff"),
    Map("character" -> "Karen")
  ).random


  val scn1 = scenario("Scenario Name 1")
    .feed(characterFeeder)
    .exec(
      http("""${character}""")
        .get("/search?q=" + """${character}""")
        .check(substring("spongebob").exists)
    )

  setUp(scn1
    .inject(
      rampUsersPerSec(1) to 150 during (5 seconds)
    )
    .protocols(httpConf)
  )
}
