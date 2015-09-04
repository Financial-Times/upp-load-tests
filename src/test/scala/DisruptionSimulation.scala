import akka.actor.ActorSystem
import disruption.GraphDB
import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import organisation.{ReadSimulation => OrgReadSimulation, WriteSimulation => OrgWriteSimulation}
import people.{ReadSimulation => PplReadSimulation, WriteSimulation => PplWriteSimulation}
import utils.LoadTestDefaults._
import scala.concurrent.duration._

import scala.language.postfixOps

class DisruptionSimulation extends Simulation {

  val numReadUsers = Integer.getInteger("users", DefaultNumUsers)
  val rampUp = Integer.getInteger("ramp-up-minutes", DefaultRampUpDurationInMinutes)
  implicit val actorSystem = ActorSystem()

  before {
    import disruption.GraphDB._
    GraphDB.resetAll()
    GraphDB.WorkerOne.createFlakyConnectionIn(15 minutes)
    GraphDB.WorkerOne.resetIn(25 minutes)
    GraphDB.WorkerTwo.createSlowConnectionIn(40 minutes)
    GraphDB.WorkerTwo.resetIn(50 minutes)
    GraphDB.WorkerThree.createNetworkFailureIn(65 minutes)
    GraphDB.WorkerThree.resetIn(75 minutes)
    GraphDB.WorkerFour.createServiceFailureIn(90 minutes)
    GraphDB.WorkerFour.resetIn(100 minutes)
    GraphDB.WorkerOne.createFirewallTimeoutIn(115 minutes)
    GraphDB.WorkerOne.resetIn(125 minutes)
  }

  setUp(
    OrgWriteSimulation.Scenario.inject(constantUsersPerSec(6) during(150 minutes)).protocols(OrgWriteSimulation.HttpConf)
      .throttle(reachRps(3) in (5 minutes), holdFor(145 minutes)),
    PplWriteSimulation.Scenario.inject(constantUsersPerSec(6) during(150 minutes)).protocols(PplWriteSimulation.HttpConf)
      .throttle(reachRps(3) in (5 minutes), holdFor(145 minutes)),
    OrgReadSimulation.Scenario.inject(constantUsersPerSec(30) during(150 minutes)).protocols(OrgReadSimulation.HttpConf)
      .throttle(reachRps(15) in (5 minutes), holdFor(145 minutes)),
    PplReadSimulation.Scenario.inject(constantUsersPerSec(30) during(150 minutes)).protocols(PplReadSimulation.HttpConf)
      .throttle(reachRps(15) in (5 minutes), holdFor(145 minutes))
  )

  after(
    GraphDB.resetAll()
  )

}