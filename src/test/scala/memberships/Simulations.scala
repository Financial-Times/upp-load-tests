package membership

import java.nio.charset.Charset
import java.util.UUID

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.LoadTestDefaults._

import scala.concurrent.forkjoin.ThreadLocalRandom.{current => Rnd}
import scala.language.postfixOps

object MembershipUtils {

  implicit class RandomList[A](val self: List[A]) extends AnyVal {
    def getRandom: A = self(Rnd().nextInt(self.size))
  }

  val creativeJobTitles = List(
    "Sandwich Artist",
    "Shopping Centre Santa",
    "Level 80 Paladin",
    "Cat Whisperer",
    "Personal Shopper"
  )

  def getMembershipMap = {
    val identifier = 100000 + Rnd().nextInt(900000)
    val title = creativeJobTitles.getRandom
    Map(
      "uuid" -> UUID.nameUUIDFromBytes(("http://api.ft.com/system/FACTSET/" + identifier).getBytes(Charset.defaultCharset())).toString,
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

  val HttpConf = http
    .baseURLs(System.getProperty("memberships-write-hosts").split(',').to[List])
    .userAgentHeader("Membership/Load-test")

  val Scenario = scenario("Membership Write").during(Duration minutes) {
    feed(Feeder)
      .exec(
        http("Membership Write request")
          .put("/memberships/${uuid}")
          .header(RequestIdHeader, (s: Session) => getRequestId(s, "mtw"))
          .body(ELFileBody("memberships/membership_template.json"))
          .asJSON)
  }
}

class WriteSimulation extends Simulation {
  val numUsers = Integer.getInteger("users", DefaultNumUsers / 10)

  setUp(
    WriteSimulation.Scenario.inject(rampUsers(numUsers) over (RampUp minutes))
  ).protocols(WriteSimulation.HttpConf)
}

object TransformerSimulation {
  val Feeder = getDefaultFeeder("memberships/memberships.uuid")

  val HttpConf = http
    .baseURLs(System.getProperty("memberships-transformer-hosts").split(',').to[List])
    .userAgentHeader("Membership/Load-test")

  val Scenario = scenario("Membership Transformer").during(Duration minutes) {
    feed(Feeder)
      .exec(
        http("Membership Transformer request")
          .get("/transformers/memberships/${uuid}")
          .header(RequestIdHeader, (s: Session) => getRequestId(s, "mtr"))
          .check(status is 200))
  }
}

class TransformerSimulation extends Simulation {

  setUp(
    TransformerSimulation.Scenario.inject(rampUsers(NumUsers) over (RampUp minutes))
  ).protocols(TransformerSimulation.HttpConf)

}

class FullSimulation extends Simulation {
  val numWriteUsers = Integer.getInteger("write-users", NumUsers.toInt / 10)

  setUp(
    TransformerSimulation.Scenario.inject(rampUsers(numWriteUsers) over (RampUp minutes)).protocols(TransformerSimulation.HttpConf),
    WriteSimulation.Scenario.inject(rampUsers(numWriteUsers) over (RampUp minutes)).protocols(WriteSimulation.HttpConf)
  )
}