package simulations

import scala.concurrent.duration._
import scala.io.Source
import scala.util.Random

import io.gatling.core.Predef._
import io.gatling.http.Predef._

/** to run: mvn gatling:test -Dgatling.simulationClass=simulations.BasicSimulationWebRecsWorking */
class BasicSimulationWebRecsWorking extends Simulation {
  //variables in scenario:
  // https://stackoverflow.com/questions/35843371/how-to-add-random-value-in-json-body-in-gatling
  // https://groups.google.com/forum/#!topic/gatling/ycR9LtoU1_c

  //  val contactIdsFile = "src/test/resources/data/crmt_201_site_36830_contacts_on_list_607910_contactIdsOnly.csv"

  val colligoSiteId = "???????????"
  val recommendationId = "????????"
  val contactIdsFile = "src/test/resources/data/contactIdsDemo.csv"
  val customerIds = Source.fromFile(contactIdsFile).getLines.toList
  //  var mymap = listOfLines.map(t => t -> 0).toMap

  val m_customerId_timeLastUsed = collection.mutable.Map(customerIds.map(id => id -> 0l): _*)

  //  println(m_customerId_timeLastUsed)

  val productIdsFile = "src/test/resources/data/productIdsDemo.csv"
  val productIds = Source.fromFile(productIdsFile).getLines.toList


  val httpConf = http
    .baseURL("https://ngss.wonderville.org/asset/photosynthesis") //https://app.brontostaging.com/login/index/login/") //https://ngss.wonderville.org/asset/photosynthesis")//https://app.brontostaging.com/login/index/login/")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  //  https://stackoverflow.com/questions/35730086/how-can-i-turn-a-simple-iterative-loop-into-a-feeder-in-gatling
  val rand = new Random(System.currentTimeMillis())

  val productFeeder = Iterator.continually(Map("productId" -> productIds(rand.nextInt(productIds.length))))
  val customerFeeder = Iterator.continually(Map("customerId" -> {

    //how to get a random element of map? keep getting until timestamp less than current time minus a second or something

    var time_last_used = 0l
    var customerId = ""
    var iter = 0
    do {
      customerId = customerIds(rand.nextInt(customerIds.length))
      time_last_used = m_customerId_timeLastUsed.get(customerId).get
      iter += 1
      println(iter + ". " + customerId + " " + time_last_used + " helloooooohelloooooohelloooooohelloooooo")
    } while (System.currentTimeMillis() - time_last_used < 500)

    //now update customerIds timestamps
    m_customerId_timeLastUsed(customerId) = System.currentTimeMillis()
    customerId
  }))

  //TODO - build a feeder that maps to a random productId and a new customerId.
  // how do we know it's a new customerId?
  // 1.  we could: grow a set of used customerIds and ensure new one isn't there.  or
  // 2.  we could use a map of customerId --> lastUsedTimestamp and only use a customerId if not used in past second.
  // 2 seems better.
  // ok scala load file values into map as keys where timestamp is 0.
  // scala - how get current time as long.
  //

  //load file lines to map keys

  //https://rest.bronto.com
  val webrecsScn = scenario("Webrecs")
    .feed(productFeeder)
    .feed(customerFeeder)
    .exec(
      http("""${productId}     ${customerId}""")
        .get(
          s"/products/sites/${colligoSiteId}/product_queries/web/execute_batch?" +
            s"productQueryData=placeholder1|${recommendationId}|0|1&" +
            "fieldNames=title&" +
            """referenceProductIds=${productId}&""" +
            """customerId=${customerId}&""" +
            "recentlyBrowsedProductIds=|&" +
            "identifierUuid=14e5553f-07ff-4d72-93f7-34be7ca1d2ac&" +
            "preview=false")
        .queryParam("""fromPort""", """${customerId}""")
    )
    .pause(1)


  val scn = scenario("Scenario Name")
    .feed(productFeeder)
    .feed(customerFeeder)
    .exec(
      http("""${productId}     ${customerId}""")
        .get("/")
        .queryParam("""fromPort""", """${customerId}""")
    )
    .pause(1)


  //  String asdfasdf = "https://rest.bronto.com/products/sites/{{colligoSiteId}}/product_queries/web/execute_batch?" +
  //    "productQueryData=placeholder1|{{recommendationId}}|0|1&" +
  //    "fieldNames=title&" +
  //    "referenceProductIds={{myReferenceProductIds}}&" +
  //    "customerId={{myCustomerId}}&" +
  //    "recentlyBrowsedProductIds=2017-08-02T00:00:00.000Z|prod_48&" +
  //    "identifierUuid=14e5553f-07ff-4d72-93f7-34be7ca1d2ac&" +
  //    "preview=false";


  setUp(scn
    .inject(
      nothingFor(1 seconds),
      atOnceUsers(1),
      rampUsersPerSec(1) to 10 during (2 seconds), //was to 800 during 10
    )
    //    .throttle(
    //      reachRps(520) in (6 seconds),
    //      holdFor(3 seconds),
    //      jumpToRps(685),
    //      holdFor(2 seconds)
    //    )
    .protocols(httpConf))


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
