package generic

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.LoadTestDefaults._

import scala.language.postfixOps

/**
  * Generic Load Test for any service endpoint.
  *
  * USAGE: mvn gatling:execute -Dgatling.simulationClass=generic.ReadSimulation -Dusers=50 -Dramp-up-minutes=5 -Dsoak-duration-minutes=10 -Dhosts="https://semantic-up.ft.com" -Dusername="user" -Dpassword="
pass" -Dplatform=coco -DapiKey=api_key -Dendpoint=/__content-public-read/content
  */
object ReadSimulation {

  val Feeder = csv(System.getProperty("UUID_CSV_PATH", "generic.content.uuid")).random

  val Duration = Integer.getInteger("soak-duration-minutes", DefaultSoakDurationInMinutes)

  val ServiceHeader = "Load-test"
  val ServiceEndpoint = "/" + System.getProperty("endpoint")
  val RequestUrl = ServiceEndpoint + "/${uuid}"
  val ServiceScenario = System.getProperty("endpoint") + " Read Request"

  val HttpConf = http
    .baseURLs(System.getProperty("hosts").split(',').to[List])
    .basicAuth(System.getProperty("username", "username"), System.getProperty("password", "password"))
    .userAgentHeader(ServiceHeader)
    .header("x-api-key", System.getProperty("apiKey", "apiKey"))

  val Scenario = scenario(ServiceScenario).during(Duration minutes) {
    feed(Feeder)
      .exec(
        http(ServiceScenario)
          .get(RequestUrl)
          .check(
            status is 200,
            jsonPath("$.id").is("http://www.ft.com/thing/${uuid}")
          )
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
