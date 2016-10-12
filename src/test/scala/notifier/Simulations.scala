package notifier

import java.nio.charset.Charset
import java.util.UUID

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.LoadTestDefaults._

import scala.concurrent.forkjoin.ThreadLocalRandom.{current => Rnd}
import scala.language.postfixOps

/**
  * USAGE: mvn gatling:execute -Dgatling.simulationClass=notifier.WriteSimulation -Dusers=1 -Dramp-up-minutes=1
  * -Dsoak-duration-minutes=1 -Dhosts="https://semantic-up.ft.com" -Dusername="mashery" -Dpassword="mashery_pass"
  * -Dplatform=coco
  */
object NotifierSimulation {

  val Feeder = csv(System.getProperty("uuid-csv-path", "notifier/one_article.uuid")).random

  val Duration = Integer.getInteger("soak-duration-minutes", DefaultSoakDurationInMinutes)

  val ServiceHeader = "Notifier/Load-test"
  val ServiceEndpoint = "/notify"
  val RequestUrl = ServiceEndpoint
  val ServiceScenario = System.getProperty("endpoint") + " Publish Request"
  val transactionId = "gatling_" + "${uuid}"
  val HttpConf = http
    .baseURLs(System.getProperty("hosts").split(',').to[List])
    .basicAuth(System.getProperty("username", "username"), System.getProperty("password", "password"))
    .userAgentHeader(ServiceHeader)

  val sentHeaders = Map(
    "Content-Type" -> "application/json",
    "X-Origin-System-Id" -> "methode-web-pub",
    "X-Request-Id" -> transactionId)

    val Scenario = scenario("Notifier Publish").during(Duration minutes) {
    val b = ELFileBody("notifier/raw_methode_content.json")

    feed(Feeder)
      .exec(
        http("Notifier Write request")
          .post("/notify")
          .headers(sentHeaders)
          .body(ELFileBody("notifier/raw_methode_content.json"))
          .asJSON.check(status is 200))
  }
}

class WriteSimulation extends Simulation {

  val numUsers = Integer.getInteger("users", DefaultNumUsers)
  val rampUp = Integer.getInteger("ramp-up-minutes", DefaultRampUpDurationInMinutes)

  setUp(
    NotifierSimulation.Scenario.inject(rampUsers(numUsers) over (rampUp minutes)).protocols(NotifierSimulation.HttpConf)

  )
}

