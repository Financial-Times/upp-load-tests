import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import organisation.{ReadSimulation => OrgReadSimulation, WriteSimulation => OrgWriteSimulation, TransformerSimulation => OrgTransformerSimulation}
import people.{ReadSimulation => PplReadSimulation, WriteSimulation => PplWriteSimulation, TransformerSimulation => PplTransformerSimulation}
import utils.LoadTestDefaults._
import scala.language.postfixOps

class FullLoadSimulation extends Simulation {

  val numReadUsers = Integer.getInteger("users", DefaultNumUsers)
  val numWriteUsers = Integer.getInteger("write-users", numReadUsers.toInt / 10).toInt / 2
  val rampUp = Integer.getInteger("ramp-up-minutes", DefaultRampUpDurationInMinutes)

  setUp(
    OrgTransformerSimulation.Scenario.inject(rampUsers(numWriteUsers) over (rampUp minutes)).protocols(OrgTransformerSimulation.HttpConf),
    OrgWriteSimulation.Scenario.inject(rampUsers(numWriteUsers) over (rampUp minutes)).protocols(OrgWriteSimulation.HttpConf),
    OrgReadSimulation.Scenario.inject(rampUsers(numReadUsers) over (rampUp minutes)).protocols(OrgReadSimulation.HttpConf),
    PplTransformerSimulation.Scenario.inject(rampUsers(numWriteUsers) over (rampUp minutes)).protocols(PplTransformerSimulation.HttpConf),
    PplWriteSimulation.Scenario.inject(rampUsers(numWriteUsers) over (rampUp minutes)).protocols(PplWriteSimulation.HttpConf),
    PplReadSimulation.Scenario.inject(rampUsers(numReadUsers) over (rampUp minutes)).protocols(PplReadSimulation.HttpConf)
  )

}