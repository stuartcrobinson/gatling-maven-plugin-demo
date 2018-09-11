package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  //github.com/gatling/gatling-maven-plugin-demo

  val httpConf = http
    .baseURL("http://computer-database.gatling.io")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Mac OS X 10.8; rv:16.0)Firefox/16.0")

  val scn = scenario("Scenario Name") // A scenario is a chain of requests and pauses
    .exec(http("request_1")
    .get("/"))
    .pause(7)
    .exec(http("request_2")
    .get("/computers?f=macbook"))
    .pause(2)
    .exec(http("request_10")
      .post("/computers")
      .formParam("name", "Beautiful Computer")
      .formParam("introduced", "2012-05-30"))

  setUp(scn.inject(atOnceUsers(1)).protocols(httpConf))
}










//package simulations
//
//import scala.concurrent.duration._
//import scala.util.Random
//
//import io.gatling.core.Predef._
//import io.gatling.http.Predef._
//
//class BasicSimulation extends Simulation {
//
//  //variables in scenario:
//  // https://stackoverflow.com/questions/35843371/how-to-add-random-value-in-json-body-in-gatling
//  // https://groups.google.com/forum/#!topic/gatling/ycR9LtoU1_c
//
//  val httpConf = http
//    .baseURL("https://app.brontostaging.com/login/index/login/") //https://ngss.wonderville.org/asset/photosynthesis")//https://app.brontostaging.com/login/index/login/")
//    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
//    .acceptEncodingHeader("gzip, deflate")
//    .acceptLanguageHeader("en-US,en;q=0.5")
//    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
//
//  val feeder = Iterator.continually(Map("email" -> (Random.alphanumeric.take(20).mkString + "@foo.com")))
//
//  //TODO - build a feeder that maps to a random productId and a new customerId.
//  // how do we know it's a new customerId?
//  // 1.  we could: grow a set of used customerIds and ensure new one isn't there.  or
//  // 2.  we could use a map of customerId --> lastUsedTimestamp and only use a customerId if not used in past second.
//  // 2 seems better.
//  // ok scala load file values into map as keys where timestamp is 0.
//  // scala - how get current time as long.
//  //
//
//
//  val scn = scenario("Scenario Name")
//    .feed(feeder)
//    .exec(
//      http("""${email}""")
//        .get("/")
//        .queryParam("""fromPort""", """${email}""")
//    )
//    .pause(1)
//
//  setUp(scn
//    .inject(
//      nothingFor(1 seconds),
//      atOnceUsers(1),
//      rampUsersPerSec(1) to 10 during (2 seconds), //was to 800 during 10
//    )
//    //    .throttle(
//    //      reachRps(520) in (6 seconds),
//    //      holdFor(3 seconds),
//    //      jumpToRps(685),
//    //      holdFor(2 seconds)
//    //    )
//    .protocols(httpConf))
//
//
//  //  val scn = scenario("Scenario Name") // A scenario is a chain of requests and pauses
//  //    .exec(http("request_1").get("/"))
//  //    .pause(1)
//  //  //    .exec(
//  //  //      http("request_2")
//  //  //        .get("/reserve.php")
//  //  //        .queryParam("""fromPort""", """Paris""")
//  //  //        .queryParam("""toPort""", """Buenos+Aires"""))
//  //  //    .pause(3)
//  //  //    .exec(
//  //  //      http("request_3")
//  //  //        .get("/purchase.php")
//  //  //        .queryParam("""fromPort""", """Paris""")
//  //  //        .queryParam("""toPort""", """Buenos+Aires""")
//  //  //        .queryParam("""flight""", 43)
//  //  //        .queryParam("""price""", 472.56)
//  //  //    )
//  //
//  //  //https://gatling.io/docs/current/general/simulation_setup/
//  //  setUp(scn
//  //    .inject(
//  //      nothingFor(1 seconds),
//  //      atOnceUsers(1),
//  //      //    rampUsers(10) over (5 seconds),
//  //      //      rampUsersPerSec(1) to 800 during (10 seconds),
//  //      rampUsersPerSec(1) to 800 during (10 seconds),
//  //    )
//  //    //    .throttle(
//  //    //      reachRps(520) in (6 seconds),
//  //    //      holdFor(3 seconds),
//  //    //      jumpToRps(685),
//  //    //      holdFor(2 seconds)
//  //    //    )
//  //    .protocols(httpConf))
//  //
//  //
//  //
//  //
//  //
//  //
//  //  val httpConf = http
//  //    .baseURL("http://computer-database.gatling.io") // Here is the root for all relative URLs
//  //    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
//  //    .acceptEncodingHeader("gzip, deflate")
//  //    .acceptLanguageHeader("en-US,en;q=0.5")
//  //    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
//  //
//  //  val scn = scenario("Scenario Name") // A scenario is a chain of requests and pauses
//  //    .exec(http("request_1")
//  //    .get("/"))
//  //    .pause(7) // Note that Gatling has recorder real time pauses
//  //    .exec(http("request_2")
//  //    .get("/computers?f=macbook"))
//  //    .pause(2)
//  //    .exec(http("request_3")
//  //      .get("/computers/6"))
//  //    .pause(3)
//  //    .exec(http("request_4")
//  //      .get("/"))
//  //    .pause(2)
//  //    .exec(http("request_5")
//  //      .get("/computers?p=1"))
//  //    .pause(670 milliseconds)
//  //    .exec(http("request_6")
//  //      .get("/computers?p=2"))
//  //    .pause(629 milliseconds)
//  //    .exec(http("request_7")
//  //      .get("/computers?p=3"))
//  //    .pause(734 milliseconds)
//  //    .exec(http("request_8")
//  //      .get("/computers?p=4"))
//  //    .pause(5)
//  //    .exec(http("request_9")
//  //      .get("/computers/new"))
//  //    .pause(1)
//  //    .exec(http("request_10") // Here's an example of a POST request
//  //      .post("/computers")
//  //      .formParam("""name""", """Beautiful Computer""") // Note the triple double quotes: used in Scala for protecting a whole chain of characters (no need for backslash)
//  //      .formParam("""introduced""", """2012-05-30""")
//  //      .formParam("""discontinued""", """""")
//  //      .formParam("""company""", """37"""))
//  //
//  //  setUp(scn.inject(atOnceUsers(1)).protocols(httpConf))
//}
