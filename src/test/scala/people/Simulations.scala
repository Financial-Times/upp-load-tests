package people


import java.nio.charset.Charset
import java.util.UUID

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

  def getPeopleMap(name: String): Map[String, String] = {
    val id = "PPL-" + f"${Random.nextInt(Integer.getInteger("org.write.max-id", 1000))}%04d"
    Map(
      "uuid" -> UUID.nameUUIDFromBytes(("http://api.ft.com/system/FACTSET-LOAD-TEST-PPL/" + id).getBytes(Charset.defaultCharset())).toString,
      "name" -> name,
      "id" -> id
    )
  }
}

object TransformerSimulation {

  val Feeder = csv("people/people.uuid").random

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
  }
}

class TransformerSimulation extends Simulation {

  val numUsers = Integer.getInteger("users", DefaultNumUsers)
  val rampUp = Integer.getInteger("ramp-up-minutes", DefaultRampUpDurationInMinutes)

  setUp(
    TransformerSimulation.Scenario.inject(rampUsers(numUsers) over (rampUp minutes))
  ).protocols(TransformerSimulation.HttpConf)

}

object ReadSimulation {
  val Feeder = csv("people/people.uuid").random

  val Duration = Integer.getInteger("soak-duration-minutes", DefaultSoakDurationInMinutes)

  val HttpConf = http
    .baseURLs("http://ftaps30270-law1a-eu-t", "http://ftaps30275-law1a-eu-t")
    .userAgentHeader("People/Load-test")

  val Scenario = scenario("People Read").during(Duration minutes) {
    feed(Feeder)
      .exec(
        http("Read request")
          .get("/people/${uuid}")
          .check(status is 200, jsonPath("$.id").is("http://api.ft.com/things/${uuid}")))
  }
}


class ReadSimulation extends Simulation {

  val numUsers = Integer.getInteger("users", DefaultNumUsers)
  val rampUp = Integer.getInteger("ramp-up-minutes", DefaultRampUpDurationInMinutes)

  setUp(
    ReadSimulation.Scenario.inject(rampUsers(numUsers) over (rampUp minutes))
  ).protocols(ReadSimulation.HttpConf)

}


object WriteSimulation {

  val Feeder = csv("people/fake_names.csv").random

  val Duration = Integer.getInteger("soak-duration-minutes", DefaultSoakDurationInMinutes)

  val HttpConf = http
    .baseURLs("http://ftaps35659-law1a-eu-t", "http://ftaps35660-law1a-eu-t")
    .userAgentHeader("People/Load-test")

  val Scenario = scenario("People Write").during(Duration minutes) {
    feed(Feeder)
      .uniformRandomSwitch(
        exec { session => session.setAll(PeopleUtils.getPeopleMap(session("actor").as[String])) },
        exec { session => session.setAll(PeopleUtils.getPeopleMap(session("character").as[String])) }
      )
      .exec(
        http("Write request")
          .put("/people/${uuid}")
          .body(ELFileBody("people/people_template.json"))
          .asJSON)
  }
}

class WriteSimulation extends Simulation {

  val numUsers = Integer.getInteger("users", DefaultNumUsers / 10)
  val rampUp = Integer.getInteger("ramp-up-minutes", 1)

  setUp(
    WriteSimulation.Scenario.inject(rampUsers(numUsers) over (rampUp minutes))
  ).protocols(WriteSimulation.HttpConf)

}

class FullSimulation extends Simulation {

  val numReadUsers = Integer.getInteger("users", DefaultNumUsers)
  val numWriteUsers = Integer.getInteger("write-users", numReadUsers.toInt / 10)
  val rampUp = Integer.getInteger("ramp-up-minutes", DefaultRampUpDurationInMinutes)

  setUp(
    TransformerSimulation.Scenario.inject(rampUsers(numWriteUsers) over (rampUp minutes)).protocols(TransformerSimulation.HttpConf),
    WriteSimulation.Scenario.inject(rampUsers(numWriteUsers) over (rampUp minutes)).protocols(WriteSimulation.HttpConf),
    ReadSimulation.Scenario.inject(rampUsers(numReadUsers) over (rampUp minutes)).protocols(ReadSimulation.HttpConf)
  )

}
