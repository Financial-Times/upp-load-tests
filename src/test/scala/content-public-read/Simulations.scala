package enrichedcontent

import java.util.concurrent.atomic.AtomicBoolean

import io.gatling.core.Predef._
import io.gatling.core.result.message.KO
import io.gatling.http.Predef._
import utils.LoadTestDefaults._

import scala.language.postfixOps

/*
// StressLimitSimulation is designed as a tool to find out the limit of requests/second that one
// endpoint is capable of supporting (it stops the requests when it starts to receive error statuses).
// The reason for stopping is that the purpose of the simulation is reached (i.e. finding the req/sec capability of the endpoint),
// so there is no need to crush the cluster, which means this simulation is also safe.
//
// This simulation considers OK the following HTTP response status codes: 200, 404.
// Any other status code is consider KO, and the simulation stops sending out request when detected.
// Note: the simulation is tolerant to accidental error responses, and stops only when
// the error responses keep coming constantly in a short time.
//
// Example of running it from the command line:
// mvn clean gatling:execute -Dgatling.simulationClass=enrichedcontent.StressLimitSimulation -Dduration-seconds=60 -Dstarting-req-per-sec=100 -Dpeek-req-per-sec=1000
// -Dhosts="https://pre-prod-up.ft.com" -Dusername="pre-prod" -Dpassword="pre-prod-password"
*/
object StressLimitSimulation {
  val Feeder = csv("enrichedcontent/content.uuid.coco").random

  val Duration = Integer.getInteger("duration-seconds", DefaultSoakDurationInSeconds)
  val StartingReqPerSecond = Integer.getInteger("starting-req-per-sec", DefaultStartingReqPerSec).toDouble
  val PeekReqPerSecond = Integer.getInteger("peek-req-per-sec", DefaultPeekReqPerSec).toDouble

  val HttpConf = getDefaultHttpConf("CPR")
    .header("x-policy", "INTERNAL_UNSTABLE,RICH_CONTENT")

  val continue = new AtomicBoolean(true)

  val Scenario = scenario("CPR Read").exec(
    doIf(session => continue.get) {
      feed(Feeder)
        .exec(
          http("CPR Read request")
            .get("/__content-public-read/content/${uuid}")
            .check(status.in(200, 404))
        )
        .exec((session: Session) => {
          if (session.status == KO) {
            continue.set(false)
          }
          session
        })
    })
}

class StressLimitSimulation extends Simulation {
  setUp(
    StressLimitSimulation.Scenario.inject(
      rampUsersPerSec(StressLimitSimulation.StartingReqPerSecond)
        to StressLimitSimulation.PeekReqPerSecond
        during StressLimitSimulation.Duration)
      .protocols(StressLimitSimulation.HttpConf)
  )
}