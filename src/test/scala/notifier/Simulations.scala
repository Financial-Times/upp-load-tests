package notifier

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.LoadTestDefaults._

import scala.language.postfixOps

/**
  * USAGE: mvn gatling:execute -Dgatling.simulationClass=notifier.WriteSimulation -Dusers=1 -Dramp-up-minutes=1
  * -Dsoak-duration-minutes=1 -Dhosts="https://semantic-up.ft.com" -Dusername="mashery" -Dpassword="mashery_pass"
  * -Dplatform=coco
  */
object WriteSimulation {

  val Feeder = getRandomUUIDFeeder
  val ServiceHeader = "Notifier"
  val HttpConf = getDefaultHttpConf(ServiceHeader)

  val Scenario = scenario("Notifier Publish").during(Duration minutes) {
    feed(Feeder)
      .exec(
        http("Notifier Write request")
          .post("/notify")
          .header("X-Origin-System-Id", "methode-web-pub")
          .header(RequestIdHeader, (s: Session) => getRequestId(s, "nwlt"))
          .body(ELFileBody("notifier/raw_methode_content.json")).asJSON
          .check(status is 200))
  }
}

class WriteSimulation extends Simulation {

  setUp(
    WriteSimulation.Scenario.inject(rampUsers(NumUsers) over (RampUp minutes)).protocols(WriteSimulation.HttpConf)

  )
}

object LargeWriteSimulation {

  val Feeder = getRandomUUIDFeeder
  val ServiceHeader = "NotifierLarge"
  val HttpConf = getDefaultHttpConf(ServiceHeader)

  val Scenario = scenario("Large Notifier Publish").during(Duration minutes) {
    feed(Feeder)
      .exec(
        http("Large Notifier Write request")
          .post("/notify")
          .header("X-Origin-System-Id", "methode-web-pub")
          .header(RequestIdHeader, (s: Session) => getRequestId(s, "nlwlt"))
          .body(ELFileBody("notifier/large_methode_content.json")).asJSON
          .check(status is 200))
  }
}

class LargeWriteSimulation extends Simulation {

  setUp(
    LargeWriteSimulation.Scenario.inject(rampUsers(NumUsers) over (RampUp minutes)).protocols(LargeWriteSimulation.HttpConf)

  )
}
