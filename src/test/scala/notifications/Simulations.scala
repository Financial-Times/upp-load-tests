package notifications

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.LoadTestDefaults._

import scala.language.postfixOps

object ReadSimulation {

  val Duration = Integer.getInteger("soak-duration-minutes", DefaultSoakDurationInMinutes)

  val HttpConf = http
    .baseURLs(System.getProperty("notifications-read-hosts").split(',').to[List])
    .basicAuth(System.getProperty("username", "username"), System.getProperty("password", "password"))
    .userAgentHeader("EnrichedContent/Load-test")

  val Scenario = scenario("Notifications Read").during(Duration minutes) {
    exec(http("Notifications Read request")
      //since should be randomly generated going back 3 month
      .get("/content/notifications?since=2016-08-13T08:18:40.630Z")
      .check(status is 200))
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
