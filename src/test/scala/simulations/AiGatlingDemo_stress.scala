package simulations

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

//https://docs.google.com/presentation/d/1AqLjEni3k72Xuq9wcNtdx9XDyaUKA6ftVLNEa1cHJ-0/edit?usp=sharing

class AiGatlingDemo_stress extends Simulation {
  // mvn gatling:test -Dgatling.simulationClass=simulations.AiGatlingDemo_stress -Dgatling.http.ahc.connectTimeout=20000
  // http://ec2-54-91-204-127.compute-1.amazonaws.com:3000/factorial/800

  //http://ec2-107-21-6-248.compute-1.amazonaws.com:3000/factorial/10
  val httpConf = http
    .baseURL("http://ec2-107-21-6-248.compute-1.amazonaws.com:3000/factorial/") //see bottom of file for server setup

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

  /** ******************************************************
    * *********   NOTE:  run (1) then run (2)   ************
    * ******************************************************/


  /*
  (1)
   */

  //file:///Users/stuart.robinson/repos/gatling-maven-plugin-demo/target/gatling/aigatlingdemo-stress-1534463908448/index.html
  //don't change - blows up around 260
  setUp(scn1
    .inject(
      rampUsersPerSec(1) to 500 during (10 seconds)
    )
    .protocols(httpConf)
  )


  /*
  (2)
   */

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


/** ******************************************************
  * *********       EC2 Demo Server       ****************
  * ******************************************************/

/*

ec2 factorial demo load test server

hello.js:
---------------------------------------------------
var express = require('express')
var app = express()

var factorial = require( 'big-factorial' );

app.get('/', function (req, res) {
  res.send('Hello World')
})

app.get('/factorial/:x', function (req, res) {
   req.setTimeout(500000);
    //res.send(req.params.x);
    let result = factorial(req.params.x);

    console.log(result);
    res.send(''+result);
});


app.listen(3000)
---------------------------------------------------

to run:
---------------------------------------------------
nohup forever hello.js &
---------------------------------------------------

this was running on a ubuntu t2.micro instance

to recreate, i think:

npm init

npm install express

.... that's it?

just save the file in main directory and run?

load factorial endpoint:
http://ec2-107-21-6-248.compute-1.amazonaws.com:3000/factorial/10

make sure the security group allows for access from your current location
 */