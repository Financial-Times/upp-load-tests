package membership

import java.nio.charset.Charset
import java.util.UUID

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.LoadTestDefaults._

import scala.util.Random

object MembershipUtils {
  implicit class RandomList[A](val self: List[A]) extends AnyVal {
    def getRandom: A = self(Random.nextInt(self.size))
  }

  val creativeJobTitles = List(
    "Sandwich Artist",
    "Shopping Centre Santa",
    "Level 80 Paladin",
    "Cat Whisperer",
    "Personal Shopper"
  )

  def getMembershipMap = {
    val identifier = (100000 + Random.nextInt(900000))
    val title = creativeJobTitles.getRandom
    Map(
      "uuid" -> UUID.nameUUIDFromBytes(("http://api.ft.com/system/FACTSET-LOAD-TEST/" + identifier).getBytes(Charset.defaultCharset())).toString,
      "prefLabel" -> title,
      "personUuid" -> UUID.randomUUID(),
      "orgUuid" -> UUID.randomUUID(),
      "identifier" -> identifier,
      "agentRoleUuid" -> UUID.fromString("025a9fb8-f89f-47c7-b3b3-56b23faebe1e"),
      "membershipRoleUuid" -> UUID.randomUUID(),
      "roleUuid" -> UUID.randomUUID()
    )
  }
}

object WriteSimulation {
  val Feeder = Iterator.continually(MembershipUtils.getMembershipMap)

  val Duration = Integer.getInteger("soak-durtation-minutes", DefaultSoakDurationInMinutes)

  val HttpConf = http
    .baseURLs(System.getProperty("memberships-write-hosts").split(',').to[List])
    .userAgentHeader("Membership/Load-test")

  val Scenario = scenario("Membership Write").during(Duration minutes) {
    feed(Feeder)
      .exec(
        http("Membership Write request")
          .put("/memberships/${uuid}")
          .body(ELFileBody("memberships/membership_template.json"))
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

object TransformerSimulation {
  val Feeder = csv("memberships/memberships.uuid").random

  val Duration = Integer.getInteger("soak-duration-minutes", DefaultSoakDurationInMinutes)

  val HttpConf = http
    .baseURLs(System.getProperty("memberships-transformer-hosts").split(',').to[List])
    .userAgentHeader("Membership/Load-test")

  val Scenario = scenario("Membership Transformer").during(Duration minutes) {
    feed(Feeder)
      .exec(
        http("Membership Transformer request")
          .get("/transformers/memberships/${uuid}")
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

class FullSimulation extends Simulation {
  val numReadUsers = Integer.getInteger("users", DefaultNumUsers)
  val numWriteUsers = Integer.getInteger("write-users", numReadUsers.toInt / 10)
  val rampUp = Integer.getInteger("ramp-up-minutes", DefaultRampUpDurationInMinutes)

  setUp(
    TransformerSimulation.Scenario.inject(rampUsers(numWriteUsers) over (rampUp minutes)).protocols(TransformerSimulation.HttpConf),
    WriteSimulation.Scenario.inject(rampUsers(numWriteUsers) over (rampUp minutes)).protocols(WriteSimulation.HttpConf)
  )
}