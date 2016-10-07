package notifications_rw

/**
  * This load test measures throughput of 3 hours worth of notifications putting requests directly through the notifications RW.
  *
  * USAGE:
  * mvn clean gatling:execute -Dgatling.simulationClass=notifications_rw.ReadSimulation -Dusers=50 -Dramp-up-minutes=2 -Dsoak-duration-minutes=5 -Dnotifications-read-hosts="https://pre-prod-up.ft.com"
  * -Dusername="pre-prod_User" -Dpassword="pre-prod_Ppass"
  */

import com.github.nscala_time.time.Imports._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory
import utils.LoadTestDefaults._

import scala.concurrent.forkjoin.ThreadLocalRandom.{current => Rnd}
import scala.language.postfixOps

object ReadSimulation {
  private val Logger = LoggerFactory.getLogger(getClass)

  val Duration = Integer.getInteger("soak-duration-minutes", DefaultSoakDurationInMinutes)

  val HttpConf = http
    .baseURLs(System.getProperty("notifications-read-hosts").split(',').to[List])
    .basicAuth(System.getProperty("username", "username"), System.getProperty("password", "password"))
    .userAgentHeader("EnrichedContent/Load-test")

  val Feeder = Iterator.continually(Map("since" -> DateTime.now().minusHours(3).plusMillis(Rnd().nextInt(1000)).toString(DateTimeFormat.forPattern("YYYY-MM-dd'T'HH:mm:ss.SSS'Z'"))))
  val Scenario = scenario("Notifications Read").during(Duration minutes) {
    feed(Feeder).
      exec(http("Notifications Read request")
        .get("/__notifications-rw/content/notifications?since=${since}")
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
