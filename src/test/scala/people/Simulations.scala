package people


import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.LoadTestDefaults._

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

object PeopleUtils {

  implicit class RandomList[A](val self: List[A]) extends AnyVal {
    def getRandom: A = self(Random.nextInt(self.size))
  }
  val ValidOrgNames = List(
    "Bucket & Code",
    "Cobalt & Vein",
    "Burlap & Twine",
    "Circle & Wish",
    "Smoke & Spindle",
    "Cable & Home",
    "Parsley & Wren",
    "Otter & Stove",
    "City & Music",
    "Circle & Cloth")
}

object TransformerSimulation {

  val Feeder = csv("people.uuid").random

  val Duration = Integer.getInteger("soak-duration-minutes", DefaultSoakDurationInMinutes)

  val HttpConf = http
    .baseURLs("http://ftaps35629-law1a-eu-t", "http://ftaps35630-law1a-eu-t")
    .userAgentHeader("People/Load-test")

  val Scenario = scenario("People Transformer").during(Duration minutes) {
    feed(Feeder)
      .exec(
        http("Transformer request")
          .get("/transformers/people/${uuid}")
          .check(status is 200))
      .pause(100 microseconds, 1 second)
  }
}

class TransformerSimulation extends Simulation {

  val numUsers = Integer.getInteger("users", DefaultNumUsers)
  val rampUp = Integer.getInteger("ramp-up-minutes", DefaultRampUpDurationInMinutes)

  setUp(
    TransformerSimulation.Scenario.inject(rampUsers(numUsers) over (rampUp minutes))
  ).protocols(TransformerSimulation.HttpConf)

}
