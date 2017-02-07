package annotator

import java.nio.charset.Charset
import java.util.UUID

import io.gatling.core.Predef._
import io.gatling.core.feeder.{Feeder}
import io.gatling.http.Predef._
import utils.LoadTestDefaults._

import scala.util.Random
import scala.language.postfixOps

object AnnotationUtils {

  implicit class RandomList[A](val self: List[A]) extends AnyVal {
    def getRandom: A = self(Random.nextInt(self.size))
  }

  def getDataMap(uuid: String, person: String, organisation: String): Map[String, String] = {
    Map(
      "personUuid" -> UUID.nameUUIDFromBytes(("http://api.ft.com/system/FACTSET-PPL/" + person).getBytes(Charset.defaultCharset())).toString,
      "orgUuid" -> UUID.nameUUIDFromBytes(("http://api.ft.com/system/FACTSET-EDM/" + organisation).getBytes(Charset.defaultCharset())).toString,
      "uuid" -> uuid,
      "person" -> person,
      "organisation" -> organisation
    )
  }
}

object WriteSimulation {

  val Feeder = getDefaultFeeder("annotator/fake_data.csv")

  val HttpConf = getDefaultHttpConf("ContentAnnotator")

  val url = System.getProperty("content-annotator-hosts") + "/force-annotate"

  val Scenario = scenario("Content Annotate").during(Duration minutes) {
    feed(Feeder)
      .uniformRandomSwitch(
        exec { session => session.setAll(AnnotationUtils.getDataMap(session("uuid").as[String], session("person").as[String], session("organisation").as[String])) },
        exec { session => session.setAll(AnnotationUtils.getDataMap(session("uuid").as[String], session("person").as[String], session("organisation").as[String])) }
      )
      .exec(
        http("Force Annotate request")
          .post(url)
          .header(RequestIdHeader, (s: Session) => getRequestId(s, "awlt"))
          .body(ELFileBody("annotator/suggestions_template.json"))
          .asJSON)
  }
}

object SpecialAnnotationUtils {
  val Orgs: Feeder[String] = csv("organisation/organisations.uuid").random.build
  val Ppl: Feeder[String] = csv("people/people.uuid").random.build

  def apply(): Feeder[String] = {
    Iterator.continually(
      Map(
        ("orgUuid", Orgs.next()("uuid")),
        ("organisation", "LoadTestOrganisation"),
        ("personUuid", Ppl.next()("uuid")),
        ("person", "LoadTestPerson")
      )
    )
  }
}

object SpecialWriteSimulation {
  val Feeder = SpecialAnnotationUtils.apply()

  val HttpConf = getDefaultHttpConf("ContentAnnotator")

  val url = System.getProperty("content-annotator-hosts") + "/force-annotate"

  val Scenario = scenario("Content Annotate").during(Duration minutes) {
    feed(Feeder)
      .exec(
        http("Force Annotate request")
          .post(url)
          .header(RequestIdHeader, (s: Session) => getRequestId(s, "awlt"))
          .body(ELFileBody("annotator/suggestions_template.json"))
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