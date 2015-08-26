import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import organisation.{ReadSimulation, WriteSimulation, TransformerSimulation}
import utils.LoadTestDefaults._
import scala.language.postfixOps

class FullLoadSimulation extends Simulation {

  val numReadUsers = Integer.getInteger("users", DefaultNumUsers)
  val numWriteUsers = Integer.getInteger("write-users", numReadUsers.toInt / 10)
  val rampUp = Integer.getInteger("ramp-up-minutes", DefaultRampUpDurationInMinutes)

  setUp(
    TransformerSimulation.Scenario.inject(rampUsers(numWriteUsers) over (rampUp minutes)).protocols(TransformerSimulation.HttpConf),
    WriteSimulation.Scenario.inject(rampUsers(numWriteUsers) over (rampUp minutes)).protocols(WriteSimulation.HttpConf),
    ReadSimulation.Scenario.inject(rampUsers(numReadUsers) over (rampUp minutes)).protocols(ReadSimulation.HttpConf)
  )

}