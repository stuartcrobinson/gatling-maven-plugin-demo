package simulations

import scala.concurrent.duration._
import scala.io.Source
import scala.util.Random

import io.gatling.core.Predef._
import io.gatling.http.Predef._

/** to run: mvn gatling:test -Dgatling.simulationClass=computerdatabase.WebRecsSimulation */
class WebRecsSimulation extends Simulation {

  val colligoSiteId = "???????????"
  val recommendationId = "????????"
  val contactIdsFile = "src/test/resources/data/contactIdsDemo.csv"
  val customerIds = Source.fromFile(contactIdsFile).getLines.toList

  /** map of customerId -> last usage timestamp.  for ensuring that new random customerId wasn't used within the past second or so. */
  val m_customerId_timeLastUsed = collection.mutable.Map(customerIds.map(id => id -> 0l): _*)

  val productIdsFile = "src/test/resources/data/productIdsDemo.csv"
  val productIds = Source.fromFile(productIdsFile).getLines.toList

  val httpConf = http
    .baseURL("https://rest.bronto.com")
    .acceptHeader("application/json")

  val rand = new Random(System.currentTimeMillis())

  val productFeeder = Iterator.continually(Map("productId" -> productIds(rand.nextInt(productIds.length))))
  val customerFeeder = Iterator.continually(Map("customerId" -> {

    var time_last_used = 0l
    var customerId = ""
    do {
      customerId = customerIds(rand.nextInt(customerIds.length))
      time_last_used = m_customerId_timeLastUsed.get(customerId).get
    } while (System.currentTimeMillis() - time_last_used < 500)

    //now update customerIds timestamps
    m_customerId_timeLastUsed(customerId) = System.currentTimeMillis()
    customerId
  }))

  //https://rest.bronto.com
  val webrecsScn = scenario("Webrecs")
    .feed(productFeeder)
    .feed(customerFeeder)
    .exec(
      http("request1")
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

  setUp(webrecsScn
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
}
