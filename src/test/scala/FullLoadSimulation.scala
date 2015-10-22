import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import membership.{WriteSimulation => MbrWriteSimulation, TransformerSimulation => MbrTransformerSimulation}
import organisation.{ReadSimulation => OrgReadSimulation, TransformerSimulation => OrgTransformerSimulation, WriteSimulation => OrgWriteSimulation}
import people.{ReadSimulation => PplReadSimulation, TransformerSimulation => PplTransformerSimulation, WriteSimulation => PplWriteSimulation}
import utils.LoadTestDefaults._

import scala.language.postfixOps

class FullLoadSimulation extends Simulation {

  val numReadUsers = Integer.getInteger("users", DefaultNumUsers)
  val numWriteUsers = Integer.getInteger("write-users", numReadUsers.toInt / 10).toInt / 3
  val rampUp = Integer.getInteger("ramp-up-minutes", DefaultRampUpDurationInMinutes)

  setUp(

    OrgTransformerSimulation.Scenario.inject(rampUsers(numWriteUsers) over (rampUp minutes)).protocols(OrgTransformerSimulation.HttpConf),
    OrgWriteSimulation.Scenario.inject(rampUsers(numWriteUsers) over (rampUp minutes)).protocols(OrgWriteSimulation.HttpConf),
    OrgReadSimulation.Scenario.inject(rampUsers(numReadUsers) over (rampUp minutes)).protocols(OrgReadSimulation.HttpConf),

    PplTransformerSimulation.Scenario.inject(rampUsers(numWriteUsers) over (rampUp minutes)).protocols(PplTransformerSimulation.HttpConf),
    PplWriteSimulation.Scenario.inject(rampUsers(numWriteUsers) over (rampUp minutes)).protocols(PplWriteSimulation.HttpConf),
    PplReadSimulation.Scenario.inject(rampUsers(numReadUsers) over (rampUp minutes)).protocols(PplReadSimulation.HttpConf),
  
    MbrTransformerSimulation.Scenario.inject( rampUsers(numWriteUsers) over ( rampUp minutes)).protocols(MbrTransformerSimulation.HttpConf),
    MbrWriteSimulation.Scenario.inject(rampUsers(numWriteUsers) over (rampUp minutes)).protocols(MbrWriteSimulation.HttpConf)
  )

}


