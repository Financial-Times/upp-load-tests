import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import utils.LoadTestDefaults._

import organisation.{ReadSimulation => OrgReadSimulation}
import people.{ReadSimulation => PplReadSimulation}
import enrichedcontent.{ReadSimulation => EnrichedContentSimulation}
import annotator.SpecialWriteSimulation

import scala.language.postfixOps

class KillerLoadSimulation extends Simulation {

  val numReadUsers = NumUsers.toInt / 3
  val numWriteUsers = NumUsers.toInt / 10

  setUp(
    EnrichedContentSimulation.Scenario.inject(rampUsers(NumUsers) over (RampUp minutes)).protocols(getDefaultHttpConf("killer")),
    OrgReadSimulation.Scenario.inject(rampUsers(numReadUsers) over (RampUp minutes)).protocols(getDefaultHttpConf("killer")),
    SpecialWriteSimulation.Scenario.inject(rampUsers(numWriteUsers) over (RampUp minutes)).protocols(getDefaultHttpConf("killer")),
    PplReadSimulation.Scenario.inject(rampUsers(numReadUsers) over (RampUp minutes)).protocols(getDefaultHttpConf("killer"))
  )

}
