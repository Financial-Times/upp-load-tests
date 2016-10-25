package extended

import generic.{ReadSimulation => GenSim}
import io.gatling.core.Predef._
import people.ReadSimulation
import utils.LoadTestDefaults._

import scala.language.postfixOps

class BigPeopleSimulations extends Simulation {

  val numReadUsers = DefaultNumUsers / 4
  val ConcordancesSim = GenSim.buildScenario(getDefaultFeeder("concordance/prod.concordances.uuid"), "Concordance Read Request", "/concordances/${uuid}", "$.concordances[0].concept.id", "http://api.ft.com/things/${uuid}")
  val BrandsSim = GenSim.buildScenario(getDefaultFeeder("brands/prod.brands.uuid"), "Brands Read Request", "/brands/${uuid}", "$.id", "http://api.ft.com/things/${uuid}")
  val httpConf = getDefaultHttpConf("BigPeople")

  setUp(
    ReadSimulation.Scenario.inject(rampUsers(numReadUsers * 2) over (RampUp minutes)).protocols(httpConf),
    ConcordancesSim.inject(rampUsers(numReadUsers) over (RampUp minutes)).protocols(httpConf),
    BrandsSim.inject(rampUsers(numReadUsers) over (RampUp minutes)).protocols(httpConf)
  )
}
