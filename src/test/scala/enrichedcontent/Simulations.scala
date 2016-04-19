package enrichedcontent

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.LoadTestDefaults._

import scala.language.postfixOps

object ReadSimulation {

  val Feeder = csv("enrichedcontent/content.uuid." + System.getProperty("platform", "platform")).random

  val Duration = Integer.getInteger("soak-duration-minutes", DefaultSoakDurationInMinutes)

  val HttpConf = http
    .baseURLs(System.getProperty("enriched-content-read-hosts").split(',').to[List])
    .basicAuth(System.getProperty("username", "username"), System.getProperty("password", "password"))
    .userAgentHeader("EnrichedContent/Load-test")
    .header("x-api-key", System.getProperty("apiKey", "apiKey"))

  val Scenario = scenario("EnrichedContent Read").during(Duration minutes) {
    feed(Feeder)
      .exec(session => {
        http("EnrichedContent Read request")
          .get("/enrichedcontent/${uuid}")
          .check(status is 200)
          .check(jsonPath("$.id") in("http://api.ft.com/things/" + session("uuid").as[String], "http://www.ft.com/thing/" + session("uuid").as[String]))
        session
      })
  }
}


class ReadSimulation extends Simulation {

  val numUsers = Integer.getInteger("users", DefaultNumUsers)
  val rampUp = Integer.getInteger("ramp-up-minutes", DefaultRampUpDurationInMinutes)

  setUp(
    ReadSimulation.Scenario.inject(rampUsers(numUsers) over (rampUp minutes))
  ).protocols(ReadSimulation.HttpConf)

}

class FullSimulation extends Simulation {

  val numReadUsers = Integer.getInteger("users", DefaultNumUsers)
  val rampUp = Integer.getInteger("ramp-up-minutes", DefaultRampUpDurationInMinutes)

  setUp(
    ReadSimulation.Scenario.inject(rampUsers(numReadUsers) over (rampUp minutes)).protocols(ReadSimulation.HttpConf)
  )

}
