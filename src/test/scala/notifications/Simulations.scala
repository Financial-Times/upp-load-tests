package notifications

import com.github.nscala_time.time.Imports._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory
import utils.LoadTestDefaults._

import scala.language.postfixOps
import scala.util.Random

object ReadSimulation {
  private val Logger = LoggerFactory.getLogger(getClass)

  val Duration = Integer.getInteger("soak-duration-minutes", DefaultSoakDurationInMinutes)

  val HttpConf = http
    .baseURLs(System.getProperty("notifications-read-hosts").split(',').to[List])
    .basicAuth(System.getProperty("username", "username"), System.getProperty("password", "password"))
    .userAgentHeader("EnrichedContent/Load-test")

  val Feeder = Iterator.continually(Map("since" -> DateTime.now().minusMonths(3).plusMillis(Random.nextInt(1000)).toString(DateTimeFormat.forPattern("YYYY-MM-dd'T'HH:mm:ss.SSS'Z'"))))
  val Scenario = scenario("Notifications Read").during(Duration minutes) {
    feed(Feeder).
      exec(http("Notifications Read request")
        .get("/content/notifications?since=${since}")
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
