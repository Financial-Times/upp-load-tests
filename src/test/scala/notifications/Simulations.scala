package notifications

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import com.github.nscala_time.time.Imports.{DateTime, DateTimeFormat}
import utils.LoadTestDefaults._

import scala.concurrent.forkjoin.ThreadLocalRandom.{current => Rnd}
import scala.language.postfixOps

object ReadSimulation {

  val HttpConf = http
    .baseURLs(System.getProperty("notifications-read-hosts").split(',').to[List])
    .basicAuth(System.getProperty("username", "username"), System.getProperty("password", "password"))
    .userAgentHeader("EnrichedContent/Load-test")

  val Feeder = Iterator.continually(Map("since" -> DateTime.now().minusMonths(3).plusMillis(Rnd().nextInt(1000)).toString(DateTimeFormat.forPattern("YYYY-MM-dd'T'HH:mm:ss.SSS'Z'"))))
  val Scenario = scenario("Notifications Read").during(Duration minutes) {
    feed(Feeder).
      exec(http("Notifications Read request")
        .get("/content/notifications?since=${since}")
        .header(RequestIdHeader, (s: Session) => getRequestId(s, "nrlt"))
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
