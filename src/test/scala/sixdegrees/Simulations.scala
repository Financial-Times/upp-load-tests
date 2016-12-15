package scala.sixdegrees

/**
  * This load test measures throughput of ??? hours worth of requests to the sixdegrees backend endpoints.
  *
  * USAGE example:
  * mvn clean gatling:execute -Dgatling.simulationClass=sixdegrees.ReadSimulation -Dusers=50 -Dramp-up-minutes=2 -Dsoak-duration-minutes=5
  * -Dsixdegrees-read-hosts="https://pre-prod-up.ft.com" -Dusername="pre-prod_User" -Dpassword="pre-prod_Ppass"
  */

import com.github.nscala_time.time.Imports.{DateTime, DateTimeFormat}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.LoadTestDefaults._

import scala.concurrent.forkjoin.ThreadLocalRandom.{current => Rnd}
import scala.language.postfixOps

object ReadSimulation {
  val HttpConf = http
    .baseURLs(System.getProperty("sixdegrees-read-hosts").split(',').to[List])
    .basicAuth(System.getProperty("username", "username"), System.getProperty("password", "password"))
    .userAgentHeader("Sixdegrees/Load-test")

  val Feeder = Iterator.continually(
    Map(
      "fromDate" -> DateTime.now().minusYears(1).plusMonths(Rnd().nextInt(12)).toString(DateTimeFormat.forPattern("YYYY-MM-dd'T'HH:mm:ss.SSS'Z'")),
      "toDate" -> DateTime.now().toString(DateTimeFormat.forPattern("YYYY-MM-dd'T'HH:mm:ss.SSS'Z'"))
    )
  )

  val Scenario = scenario("Notifications Read").during(Duration minutes) {
    feed(Feeder).
      exec(
        http("Sixdegrees read request")
          .get("/__public-six-degrees/sixdegrees/mostMentionedPeople?fromDate=${fromDate}&toDate=${toDate}")
          .check(status is 200))
  }
}

class ReadSimulation extends Simulation {
  val numberOfUsers = Integer.getInteger("users", DefaultNumUsers)
  val rampUpMinutes = Integer.getInteger("ramp-up-minutes", DefaultRampUpDurationInMinutes)

  setUp(
    ReadSimulation.Scenario.inject(rampUsers(numberOfUsers) over (rampUpMinutes minutes))
  ).protocols(ReadSimulation.HttpConf)

}

class FullSimulation extends Simulation {
  val numberOfReadUsers = Integer.getInteger("users", DefaultNumUsers)
  val rampUpMinutes = Integer.getInteger("ramp-up-minutes", DefaultRampUpDurationInMinutes)

  setUp(
    ReadSimulation.Scenario.inject(rampUsers(numberOfReadUsers) over (rampUpMinutes minutes)).protocols(ReadSimulation.HttpConf)
  )
}
