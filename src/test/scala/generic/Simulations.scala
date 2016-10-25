package generic

import io.gatling.core.Predef._
import io.gatling.core.feeder.FeederBuilder
import io.gatling.http.Predef._
import utils.LoadTestDefaults._

import scala.language.postfixOps

/**
  * Generic Load Test for any service endpoint.
  *
  * USAGE: mvn gatling:execute -Dgatling.simulationClass=generic.ReadSimulation -Dusers=50 -Dramp-up-minutes=5 -Dsoak-duration-minutes=10 -Dhosts="https://semantic-up.ft.com" -Dusername="user" -Dpassword="
  * pass" -Dplatform=coco -DapiKey=api_key -Dendpoint=/__content-public-read/content
  **/
object ReadSimulation {

  val Feeder = csv(System.getProperty("uuid-csv-path", "generic.content.uuid")).random

  val JsonPathForId = System.getProperty("json-path", "$.id")
  val ExpectedIdPattern = System.getProperty("expected-id-pattern", "http://www.ft.com/thing/${uuid}")
  val ServiceHeader = "Load-test"
  val ServiceEndpoint = "/" + System.getProperty("endpoint")
  val RequestUrl = ServiceEndpoint + "/${uuid}"
  val ServiceScenario = System.getProperty("endpoint") + " Read Request"

  val HttpConf = getDefaultHttpConf(ServiceHeader)
    .header("x-api-key", System.getProperty("apiKey", "apiKey"))

  val Scenario = buildScenario(Feeder, ServiceScenario, RequestUrl, JsonPathForId, ExpectedIdPattern)

  def buildScenario(feeder: FeederBuilder[_], serviceScenario: String, requestUrl: String, jsonPathForId: String, expectedIdPattern: String) =
    scenario(serviceScenario).during(Duration minutes) {
      feed(feeder)
        .exec(
          http(serviceScenario)
            .get(requestUrl)
            .header(RequestIdHeader, (s: Session) => getRequestId(s, "glt"))
            .check(
              status is 200,
              jsonPath(jsonPathForId).is(expectedIdPattern)
            )
        )
    }
}


class ReadSimulation extends Simulation {

  setUp(
    ReadSimulation.Scenario.inject(rampUsers(NumUsers) over (RampUp minutes))
  ).protocols(ReadSimulation.HttpConf)

}

class FullSimulation extends Simulation {

  setUp(
    ReadSimulation.Scenario.inject(rampUsers(NumUsers) over (RampUp minutes))
  ).protocols(ReadSimulation.HttpConf)

}
