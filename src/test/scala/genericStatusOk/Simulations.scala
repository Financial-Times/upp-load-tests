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

  val Feeder = csv(System.getProperty("uuid-csv-path", "lists/sample.lists.uuid")).random

  val JsonPathForId = System.getProperty("json-path", "$.id")
  val ExpectedIdPattern = System.getProperty("expected-id-pattern", "http://www.ft.com/things/${uuid}")

  val Duration = Integer.getInteger("soak-duration-minutes", DefaultSoakDurationInMinutes)

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
          .check(status is 200)
          )
  }
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
  val rampUp = Integer.getInteger("ramp-up-minutes", DefaultRampUpDurationInMinutes)

  setUp(
    ReadSimulation.Scenario.inject(rampUsers(numReadUsers) over (rampUp minutes)).protocols(ReadSimulation.HttpConf)
  )

}
