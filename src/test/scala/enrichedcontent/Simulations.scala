package enrichedcontent

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.LoadTestDefaults._

import scala.language.postfixOps

object ReadSimulation {

  val Feeder = csv("enrichedcontent/content.uuid." + System.getProperty("platform", "platform")).random

  val Duration = Integer.getInteger("soak-duration-minutes", DefaultSoakDurationInMinutes)

  val HttpConf = getDefaultHttpConf("EnrichedContent")
    .header("x-api-key", System.getProperty("apiKey", "apiKey"))

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
