package content

import java.util.UUID

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.LoadTestDefaults._

import scala.language.postfixOps

object MethodeImporterSimulation {

  val Feeder = Iterator.continually(Map(
    "uuid" -> UUID.randomUUID()
  ))

  val HttpConf = getDefaultHttpConf("ContentImporter")

  val Scenario = scenario("Methode Content Importer").during(Duration minutes) {
    feed(Feeder)
      .exec(
        http("Methode Content Importer request")
          .put("/__content-ingester/import")
          .header(RequestIdHeader, (s: Session) => getRequestId(s, "mcilt"))
          .body(ELFileBody("notifier/raw_methode_content.json")).asJSON
          .check(status is 200)
      )
  }
}

class MethodeImporterSimulation extends Simulation {

  setUp(
    MethodeImporterSimulation.Scenario.inject(rampUsers(NumUsers) over (RampUp minutes)).protocols(MethodeImporterSimulation.HttpConf)

  )
}

object WordpressImporterSimulation {

  val Feeder = Iterator.continually(Map(
    "uuid" -> UUID.randomUUID()
  ))

  val HttpConf = getDefaultHttpConf("ContentImporter")

  val Scenario = scenario("Wordpress Content Importer").during(Duration minutes) {
    feed(Feeder)
      .exec(
        http("Wordpress Content Importer request")
          .put("/__content-ingester/import-wordpress")
          .header(RequestIdHeader, (s: Session) => getRequestId(s, "wcilt"))
          .body(ELFileBody("notifier/raw_methode_content.json")).asJSON
          .check(status is 200)
      )
  }
}

class WordpressImporterSimulation extends Simulation {

  setUp(
    WordpressImporterSimulation.Scenario.inject(rampUsers(NumUsers) over (RampUp minutes)).protocols(WordpressImporterSimulation.HttpConf)

  )
}
