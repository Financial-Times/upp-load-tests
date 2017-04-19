package publicannotations

import io.gatling.core.Predef._
import io.gatling.core.feeder.FeederBuilder
import io.gatling.http.Predef._
import utils.LoadTestDefaults._

import scala.language.postfixOps

/**
  * Generic Load Test for any service endpoint.
  *
  * USAGE: mvn gatling:execute -Dgatling.simulationClass=publicannotations.ReadSimulation -Dusers=5 -Dramp-up-minutes=1 -Dsoak-duration-minutes=2 -Dhosts="https://semantic-up.ft.com" -Dusername="user" -Dpassword="
  * pass" -Dplatform=coco -DapiKey=api_key -Dendpoint=__public-annotations-api/content
  **/
object ReadSimulation {

  val Feeder = csv(System.getProperty("uuid-csv-path", "publicannotations/content.uuid")).random

  val ServiceHeader = "Load-test"
  val ServiceEndpoint = "/__public-annotations-api/content"
  val RequestUrl = ServiceEndpoint + "/${uuid}/annotations"
  val ServiceScenario = "/" + System.getProperty("endpoint") + " Read Request"

  val HttpConf = getDefaultHttpConf(ServiceHeader)
    .header("x-api-key", System.getProperty("apiKey", "apiKey"))

  val Scenario = buildScenarioThatExpectsOK(Feeder, ServiceScenario, RequestUrl)


  def buildScenarioThatExpectsOK(feeder: FeederBuilder[_], serviceScenario: String, requestUrl: String) =
    scenario(serviceScenario).during(Duration minutes) {
      feed(feeder)
        .exec(
          http(serviceScenario)
            .get(requestUrl)
            .header(RequestIdHeader, (s: Session) => getRequestId(s, "glt"))
            .check(
              status is 200
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
