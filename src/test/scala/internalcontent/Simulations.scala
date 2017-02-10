package internalcontent

/**
  * This is a load test for /internalcontent endpoint.
  *
  * USAGE example:
  * mvn clean gatling:execute -Dgatling.simulationClass=internalcontent.ReadSimulation -Dusers=50 -Dramp-up-minutes=2 -Dsoak-duration-minutes=5
  * -Dhosts="https://pre-prod-up.ft.com" -Dusername="pre-prod_User" -Dpassword="pre-prod_Ppass" -DapiKey="theApiKey"
  */

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.LoadTestDefaults._

import scala.language.postfixOps

object ReadSimulation {

  val Feeder = csv("internalcontent/content.uuid." + System.getProperty("platform", "platform")).random

  val Duration = Integer.getInteger("soak-duration-minutes", DefaultSoakDurationInMinutes)

  val HttpConf = getDefaultHttpConf("InternalContent")
    .header("x-api-key", System.getProperty("apiKey", "apiKey"))

  val Scenario = scenario("InternalContent Read").during(Duration minutes) {
    feed(Feeder)
      .exec(
        http("InternalContent Read request")
          .get("/internalcontent/${uuid}")
          .header(RequestIdHeader, (s: Session) => getRequestId(s, "eclt"))
          .check(status is 200))
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
