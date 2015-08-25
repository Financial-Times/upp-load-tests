package organisation

import java.nio.charset.Charset
import java.util.UUID

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.LoadTestDefaults._

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

object OrganisationType extends Enumeration {
  val Organisation, PublicCompany = Value
}

object OrgUtils {

  implicit class RandomList[A](val self: List[A]) extends AnyVal {
    def getRandom: A = self(Random.nextInt(self.size))
  }

  val RemoveLetters = List(' ', 'a', 'e', 'i', 'o', 'u')

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

  def getOrgMap = {
    val id = "ORG-" + f"${Random.nextInt(System.getProperty("org.write.max-id").toInt)}%04d"
    val name = ValidOrgNames.getRandom
    val nameSplit = name.split(' ')
    Map(
      "uuid" -> UUID.nameUUIDFromBytes(("http://api.ft.com/system/FACTSET-LOAD-TEST/" + id).getBytes(Charset.defaultCharset())),
      "orgType" -> OrganisationType.Organisation,
      "id" -> id,
      "name" -> name,
      "shortName" -> nameSplit(0),
      "legalName" -> nameSplit(2),
      "hiddenLabel" -> name.filter(x => !(RemoveLetters contains x)),
      "industryClassification" -> "b79269c7-f8ab-38be-9ba2-35724c60af3a")
  }
}

case class OrganisationTemplate(id: String,
                                name: String,
                                orgType: OrganisationType.Value,
                                industryClassification: String) {
  val uuid = UUID.nameUUIDFromBytes(("http://api.ft.com/system/FACTSET-LOAD-TEST/" + id).getBytes(Charset.defaultCharset()))
  val shortName = name substring(0, 3)
  val legalName = name substring (name.length / 2)
  val hiddenLabel = name filter (x => !(OrgUtils.RemoveLetters contains x))

  def getOrgParams =
    (Map[String, Any]() /: this.getClass.getDeclaredFields) { (a, f) =>
      f.setAccessible(true)
      a + (f.getName -> f.get(this))
    }
}

object TransformerSimulation {

  val Feeder = csv("organisations.uuid").random

  val Duration = Integer.getInteger("soak-duration-minutes", DefaultSoakDurationInMinutes)

  val HttpConf = http
    .baseURLs("http://ftaps39403-law1a-eu-t:8080", "http://ftaps39395-law1b-eu-t:8080")
    .userAgentHeader("Organisation/Load-test")

  val Scenario = scenario("Organisation Transformer").during(Duration minutes) {
    feed(Feeder)
      .exec(
        http("Transformer request")
          .get("/transformers/organisations/${uuid}")
          .check(status is 200))
      .pause(100 microseconds, 1 second)
  }
}

object WriteSimulation {

  val Feeder = Iterator.continually(OrgUtils.getOrgMap)

  val Duration = Integer.getInteger("soak-duration-minutes", DefaultSoakDurationInMinutes)

  val HttpConf = http
    .baseURLs("http://ftaps37932-law1a-eu-t", "http://ftaps37933-law1a-eu-t")
    .userAgentHeader("Organisation/Load-test")

  val Scenario = scenario("Organisation Write").during(Duration minutes) {
    feed(Feeder)
      .exec(
        http("Write request")
          .put("/organisation/${uuid}")
          .body(ELFileBody("organisation_template.json"))
          .asJSON)
      .pause(100 microseconds, 1 second)
  }
}

object ReadSimulation {
  val Feeder = csv("organisations.uuid").random

  val Duration = Integer.getInteger("soak-duration-minutes", DefaultSoakDurationInMinutes)

  val HttpConf = http
    //        .baseURLs("http://localhost:9022", "http://localhost:9022")
    .baseURLs("http://ftaps30276-law1a-eu-t", "http://ftaps30271-law1a-eu-t")
    .userAgentHeader("Organisation/Load-test")

  val Scenario = scenario("Organisation Read").during(Duration minutes) {
    feed(Feeder)
      .exec(
        http("Read request")
          .get("/organisations/${uuid}")
          .check(status is 200, jsonPath("$.id").is("http://api.ft.com/things/${uuid}")))
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

class WriteSimulation extends Simulation {

  val numUsers = Integer.getInteger("users", DefaultNumUsers / 10)
  val rampUp = Integer.getInteger("ramp-up-minutes", 1)

  setUp(
    WriteSimulation.Scenario.inject(rampUsers(numUsers) over (rampUp minutes))
  ).protocols(WriteSimulation.HttpConf)

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
  val numWriteUsers = Integer.getInteger("write-users", numReadUsers.toInt / 10)
  val rampUp = Integer.getInteger("ramp-up-minutes", DefaultRampUpDurationInMinutes)

  setUp(
    TransformerSimulation.Scenario.inject(rampUsers(numWriteUsers) over (rampUp minutes)).protocols(TransformerSimulation.HttpConf),
    WriteSimulation.Scenario.inject(rampUsers(numWriteUsers) over (rampUp minutes)).protocols(WriteSimulation.HttpConf),
    ReadSimulation.Scenario.inject(rampUsers(numReadUsers) over (rampUp minutes)).protocols(ReadSimulation.HttpConf)
  )

}
