package genericStatusOk

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.LoadTestDefaults._

import scala.language.postfixOps

/**
  * Generic Load Test for any service path & query param only checks that response status is OK.
  *
  *
  * CONCORDANCES Example:
  * mvn clean gatling:execute -Dgatling.simulationClass=genericStatusOk.ReadSimulation -Dusers=1 -Dramp-up-minutes=1
  * -Dsoak-duration-minutes=1 -Dhosts="https://pre-prod-up-read.ft.com" -Dusername="pre-prod"
  * -Dpassword="pre-prod_pass" -Dplatform=coco -DapiKey="test_mashery_api_key"
  * -Dendpoint="concordances" -Duuid-csv-path="concordance/prod.concordances.uuid"
  * -Djson-path="$.concept" -Dexpected-id-pattern="concordances.${concept}"
  *
  * LISTS Example
  * mvn clean gatling:execute -Dgatling.simulationClass=genericStatusOk.ReadSimulation -Dusers=1 -Dramp-up-minutes=1
  * -Dsoak-duration-minutes=1 -Dhosts="https://prod-coco-up-read.ft.com" -Dusername="coco_prod_user"
  * -Dpassword="coco_prod_pass" -Dplatform=coco -DapiKey="prod_mashery_api_key" -Dendpoint="lists"
  * -Duuid-csv-path="lists/prod.lists.uuid" -Djson-path="$.id" -Dexpected-id-pattern="http://api.ft.com/things/${id}"
  *
  */
object ReadSimulation {

  val Feeder = getDefaultFeeder("lists/sample.lists.uuid")

  val JsonPathForId = System.getProperty("json-path", "$.id")
  val ExpectedIdPattern = System.getProperty("expected-id-pattern", "http://www.ft.com/things/${uuid}")

  val ServiceHeader = "Load-test"
  val ServiceEndpoint = "/" + System.getProperty("endpoint")
  val RequestUrl = ServiceEndpoint + "${uuid}"
  val ServiceScenario = System.getProperty("endpoint") + " Read Request"

  val HttpConf = http
    .baseURLs(System.getProperty("hosts").split(',').to[List])
    .basicAuth(System.getProperty("username", "useanyrname"), System.getProperty("password", "password"))
    .userAgentHeader(ServiceHeader)
    .header("x-api-key", System.getProperty("apiKey", "apiKey"))

  val Scenario = scenario(ServiceScenario).during(Duration minutes) {
    feed(Feeder)
      .exec(
        http(ServiceScenario)
          .get(RequestUrl)
          .header(RequestIdHeader, (s: Session) => getRequestId(s, "grok"))
          .check(status is 200)
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
    ReadSimulation.Scenario.inject(rampUsers(NumUsers) over (RampUp minutes)).protocols(ReadSimulation.HttpConf)
  )

}
