package enrichedcontent

import java.util.concurrent.atomic.AtomicBoolean

import io.gatling.core.Predef._
import io.gatling.core.result.message.KO
import io.gatling.http.Predef._
import utils.LoadTestDefaults._

import scala.language.postfixOps

object ReadSimulation {

  val Feeder = csv("enrichedcontent/content.uuid." + System.getProperty("platform", "platform")).random

  val Duration = Integer.getInteger("soak-duration-minutes", DefaultSoakDurationInMinutes)

  val HttpConf = getDefaultHttpConf("EnrichedContent")
    .header("x-api-key", System.getProperty("apiKey", "apiKey"))

  val continue = new AtomicBoolean(true)

  val Scenario = scenario("EnrichedContent Read").during(Duration minutes) {
    feed(Feeder)
      .exec(
        http("EnrichedContent Read request")
          .get("/enrichedcontent/${uuid}")
          .header(RequestIdHeader, (s: Session) => getRequestId(s, "eclt"))
          .check(status is 200)
          .check(regex("\"id\":\"(http://api.ft.com/things/|http://www.ft.com/thing/)${uuid}\"")))
  }
}

object StressSimulation {

  val Feeder = csv("enrichedcontent/content.uuid." + System.getProperty("platform", "platform")).random

  val Duration = Integer.getInteger("soak-duration-minutes", DefaultSoakDurationInMinutes)
  val StartingUsersPerSecond = Integer.getInteger("starting-users-per-sec", DefaultNumUsers).toDouble
  val PeekUsersPerSecond = Integer.getInteger("peek-users-per-sec", DefaultNumUsers).toDouble

  val HttpConf = getDefaultHttpConf("EnrichedContent")

  val continue = new AtomicBoolean(true)

  val Scenario = scenario("EnrichedContent Read").exec(
    doIf(session => continue.get) {
    feed(Feeder)
      .exec(
        http("EnrichedContent Read request")
          .get("/enrichedcontent/${uuid}")
          .header(RequestIdHeader, (s: Session) => getRequestId(s, "eclt"))
          .check(status.is(200) or status.is(404))
      )
      .exec((s: Session) => {
        if (s.status == KO) {
          continue.set(false)
        }
      })
  }
}

class ReadSimulation extends Simulation {

  setUp(
    ReadSimulation.Scenario.inject(rampUsers(NumUsers) over (RampUp minutes))
  ).protocols(ReadSimulation.HttpConf)

}

class FullSimulation extends Simulation {

  setUp(
    ReadSimulation.Scenario.inject(rampUsers(NumUsers) over (RampUp minutes)).protocols(ReadSimulation.HttpConf)
  )

}

class StressSimulation extends Simulation {
  setUp(
    StressSimulation.Scenario.inject(
      rampUsersPerSec(StressSimulation.StartingUsersPerSecond)
        to StressSimulation.PeekUsersPerSecond
        during StressSimulation.Duration)
      .protocols(StressSimulation.HttpConf)
  )
}