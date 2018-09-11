package other

import scala.concurrent.duration._
import scala.util.Random

import io.gatling.core.Predef._
import io.gatling.http.Predef._

//https://www.bing.com/search?q=dog%20facts

/** to run:
  * mvn gatling:test -Dgatling.simulationClass=simulations.AiGatlingDemo
  * */


class AiGatlingDemo_trash extends Simulation {

  //variables in scenario:
  // https://stackoverflow.com/questions/35843371/how-to-add-random-value-in-json-body-in-gatling
  // https://groups.google.com/forum/#!topic/gatling/ycR9LtoU1_c

  // http://slowwly.robertomurray.co.uk/delay/0000/url/https://en.wikipedia.org/wiki/Spoon

  val httpConf = http
    .baseURL("https://www.bing.com")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  //20
  val characterFeeder = Array(
    Map("character" -> "Patrick Star"),
    Map("character" -> "Squidward Tentacles"),
    Map("character" -> "Mr. Krabs"),
    Map("character" -> "Plankton and Karen"),
    Map("character" -> "Sandy Cheeks"),
    Map("character" -> "Mrs. Puff"),
    Map("character" -> "Pearl Krabs"),
    Map("character" -> "Gary the Snail"),
    Map("character" -> "Patchy the Pirate"),
    Map("character" -> "Potty the Parrot"),
    Map("character" -> "French Narrator"),
    Map("character" -> "Flying Dutchman"),
    Map("character" -> "Mermaid Man"),
    Map("character" -> "Barnacle Boy"),
    Map("character" -> "King Neptune"),
    Map("character" -> "Perch Perkins"),
    Map("character" -> "Betsy Krabs"),
    Map("character" -> "Bubble Bass"),
    Map("character" -> "Purple Doctorfish"),
    Map("character" -> "Man Ray"),
  ).random

  val testList = List("Patrick Star",
    "Squidward Tentacles",
    "Mr. Krabs",
    "Plankton and Karen",
    "Sandy Cheeks",
    "Mrs. Puff",
    "Pearl Krabs",
    "Gary the Snail",
    "Patchy the Pirate",
    "Potty the Parrot",
    "French Narrator",
    "Flying Dutchman",
    "Mermaid Man",
    "Barnacle Boy",
    "King Neptune",
    "Perch Perkins",
    "Betsy Krabs",
    "Bubble Bass",
    "Purple Doctorfish",
    "Man Ray")

  val rand = new Random(System.currentTimeMillis())


  /** Feeds a random product into the scenario endpoint per query. */
  val productFeeder = Iterator.continually(Map("productId" -> testList(rand.nextInt(testList.length))))

  def randChar() = productFeeder.next().get("productId").get

//  val queryFeeder = Array(
//    Map("queryLoad" -> "1", "query" -> characterFeeder.next,
////    Map("queryLoad" -> "5", "query" -> s"/${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar() }"),
////    Map("queryLoad" -> "10", "query" -> s"/${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar()} "),
////    Map("queryLoad" -> "20", "query" -> s"/${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar()} ${randChar()} "),
//  ).random

  //King Neptune

  val scn1 = scenario("Scenario Name 1")
    .feed(characterFeeder)
    .exec(
      http("""1""")
        .get("/search?q=" + """${character}""")
    )

  val scn5 = scenario("Scenario Name 5")
    .feed(characterFeeder)
    .exec(
      http("""5""")
        .get("/search?q=" + """${character} ${character} ${character} ${character} ${character} """)
    )

  val scn10 = scenario("Scenario Name 10")
    .feed(characterFeeder)
    .exec(
      http("""10""")
        .get("/search?q=" + """${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} """)
    )

  val scn20 = scenario("Scenario Name 20")
    .feed(characterFeeder)
    .exec(
      http("""20""")
        .get("/search?q=" + """${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} """)
    )

  val scn80 = scenario("Scenario Name 80")
    .feed(characterFeeder)
    .exec(
      http("""80""")
        .get("/search?q=" + """${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} ${character} """)
    )

  setUp(scn1
    .inject(
      rampUsersPerSec(1) to 5 during (5 seconds) //was to 800 during 10
    )
    .protocols(httpConf),

    scn5
      .inject(
        rampUsersPerSec(1) to 5 during (5 seconds) //was to 800 during 10
      )
      .protocols(httpConf),

    scn10
      .inject(
        rampUsersPerSec(1) to 5 during (5 seconds) //was to 800 during 10
      )
      .protocols(httpConf),


    scn20
      .inject(
        rampUsersPerSec(1) to 5 during (5 seconds) //was to 800 during 10
      )
      .protocols(httpConf),

    scn80
      .inject(
        rampUsersPerSec(1) to 5 during (5 seconds) //was to 800 during 10
      )
      .protocols(httpConf)

  )

//  setUp(scn
//    .inject(
//      //      nothingFor(1 seconds),
//      //      atOnceUsers(1),
//      rampUsersPerSec(1) to 10 during (5 seconds), //was to 800 during 10
//    )
//    //        .throttle(
//    //          reachRps(200) in (2 seconds),
//    //          holdFor(3 seconds),
//    //          jumpToRps(400),
//    //          holdFor(3 seconds)
//    //        )
//    .protocols(httpConf))


  //  val scn = scenario("Scenario Name") // A scenario is a chain of requests and pauses
  //    .exec(http("request_1").get("/"))
  //    .pause(1)
  //  //    .exec(
  //  //      http("request_2")
  //  //        .get("/reserve.php")
  //  //        .queryParam("""fromPort""", """Paris""")
  //  //        .queryParam("""toPort""", """Buenos+Aires"""))
  //  //    .pause(3)
  //  //    .exec(
  //  //      http("request_3")
  //  //        .get("/purchase.php")
  //  //        .queryParam("""fromPort""", """Paris""")
  //  //        .queryParam("""toPort""", """Buenos+Aires""")
  //  //        .queryParam("""flight""", 43)
  //  //        .queryParam("""price""", 472.56)
  //  //    )
  //
  //  //https://gatling.io/docs/current/general/simulation_setup/
  //  setUp(scn
  //    .inject(
  //      nothingFor(1 seconds),
  //      atOnceUsers(1),
  //      //    rampUsers(10) over (5 seconds),
  //      //      rampUsersPerSec(1) to 800 during (10 seconds),
  //      rampUsersPerSec(1) to 800 during (10 seconds),
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
  //
  //
  //
  //
  //  val httpConf = http
  //    .baseURL("http://computer-database.gatling.io") // Here is the root for all relative URLs
  //    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
  //    .acceptEncodingHeader("gzip, deflate")
  //    .acceptLanguageHeader("en-US,en;q=0.5")
  //    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
  //
  //  val scn = scenario("Scenario Name") // A scenario is a chain of requests and pauses
  //    .exec(http("request_1")
  //    .get("/"))
  //    .pause(7) // Note that Gatling has recorder real time pauses
  //    .exec(http("request_2")
  //    .get("/computers?f=macbook"))
  //    .pause(2)
  //    .exec(http("request_3")
  //      .get("/computers/6"))
  //    .pause(3)
  //    .exec(http("request_4")
  //      .get("/"))
  //    .pause(2)
  //    .exec(http("request_5")
  //      .get("/computers?p=1"))
  //    .pause(670 milliseconds)
  //    .exec(http("request_6")
  //      .get("/computers?p=2"))
  //    .pause(629 milliseconds)
  //    .exec(http("request_7")
  //      .get("/computers?p=3"))
  //    .pause(734 milliseconds)
  //    .exec(http("request_8")
  //      .get("/computers?p=4"))
  //    .pause(5)
  //    .exec(http("request_9")
  //      .get("/computers/new"))
  //    .pause(1)
  //    .exec(http("request_10") // Here's an example of a POST request
  //      .post("/computers")
  //      .formParam("""name""", """Beautiful Computer""") // Note the triple double quotes: used in Scala for protecting a whole chain of characters (no need for backslash)
  //      .formParam("""introduced""", """2012-05-30""")
  //      .formParam("""discontinued""", """""")
  //      .formParam("""company""", """37"""))
  //
  //  setUp(scn.inject(atOnceUsers(1)).protocols(httpConf))
}

//
//package simulations
//
//import scala.concurrent.duration._
//
//import io.gatling.core.Predef._
//import io.gatling.http.Predef._
//
////https://www.bing.com/search?q=dog%20facts
//
///** to run:
//  * mvn gatling:test -Dgatling.simulationClass=simulations.AiGatlingDemo
//  * */
//
//
//class AiGatlingDemo_stress extends Simulation {
//
//  //variables in scenario:
//  // https://stackoverflow.com/questions/35843371/how-to-add-random-value-in-json-body-in-gatling
//  // https://groups.google.com/forum/#!topic/gatling/ycR9LtoU1_c
//
//  // http://slowwly.robertomurray.co.uk/delay/0000/url/https://en.wikipedia.org/wiki/Spoon
//
//  //http://ec2-54-91-204-127.compute-1.amazonaws.com:3000/factorial/800
//
//  val httpConf = http
//    .baseURL("http://ec2-54-91-204-127.compute-1.amazonaws.com:3000/factorial/")
//
//  //20
//  val xFeeder = Array(
//    Map("x" -> "80"),
//    Map("x" -> "90"),
//    Map("x" -> "100")
//  ).random
//
//
//  val scn1 = scenario("Scenario Name 1")
//    .feed(xFeeder)
//    .exec(
//      http("""${x}""")
//        .get("""${x}""")
//    )
//
//  //  //don't change - blows up around 260
//  //  setUp(scn1
//  //    .inject(
//  //      rampUsersPerSec(1) to 500 during (10 seconds)
//  //    )
//  //    .protocols(httpConf)
//  //  )
//
//
//  //  setUp(scn1
//  //    .inject(
//  //      constantUsersPerSec(300) during (110 seconds),
//  //    )
//  //    .throttle(
//  //      reachRps(150) in (1 seconds),
//  //      holdFor(3 seconds),
//  //      jumpToRps(200),
//  //      holdFor(3 seconds),
//  //      jumpToRps(250),
//  //      holdFor(3 seconds),
//  //      jumpToRps(300),
//  //      holdFor(3 seconds),
//  //    )
//  //    .protocols(httpConf)
//  //  )
//
//
//  setUp(scn1
//    .inject(
//      constantUsersPerSec(300) during (25 seconds),
//      //          rampUsersPerSec(200) to 300 during (25 seconds)
//    )
//    .throttle(
//      reachRps(100) in (1 seconds),
//      holdFor(4 seconds),
//      //        reachRps(150) in (1 seconds),
//      //        holdFor(3 seconds),
//      reachRps(175) in (1 seconds),
//      holdFor(4 seconds),
//      //        reachRps(200) in (1 seconds),
//      //        holdFor(3 seconds),
//      reachRps(225) in (1 seconds),
//      holdFor(5 seconds),
//      //          jumpToRps(200),
//      //          holdFor(2 seconds)
//    )
//    .protocols(httpConf)
//  )
//}

