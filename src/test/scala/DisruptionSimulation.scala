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

    /*
      create two flaky connections that last a few seconds
     */
    GraphDB.WorkerOne.createFlakyConnectionIn(15 minutes, 10 seconds)
    GraphDB.WorkerOne.createFlakyConnectionIn(25 minutes, 15 seconds)

    /*
      create slow connections lasting a few seconds
     */
    GraphDB.WorkerTwo.createSlowConnectionIn(40 minutes, 60 seconds)
    GraphDB.WorkerTwo.createSlowConnectionIn(45 minutes, 90 seconds)
    GraphDB.WorkerTwo.createSlowConnectionIn(50 minutes, 120 seconds)

    /*
      create network failure lasting a few seconds
     */
    GraphDB.WorkerThree.createNetworkFailureIn(65 minutes, 10 seconds)
    GraphDB.WorkerThree.createNetworkFailureIn(70 minutes, 5 seconds)
    GraphDB.WorkerThree.createNetworkFailureIn(80 minutes, 10 seconds)

    /*
    create network failure lasting a few seconds
    */
    GraphDB.WorkerFour.createServiceFailureIn(90 minutes, 5 seconds)
    GraphDB.WorkerFour.createServiceFailureIn(100 minutes, 10 seconds)

    /*
     create firewall timeouts lasting a few seconds
     */
    GraphDB.WorkerOne.createFirewallTimeoutIn(115 minutes, 5 seconds)
    GraphDB.WorkerOne.createFirewallTimeoutIn(125 minutes, 5 seconds)
    GraphDB.WorkerOne.createFirewallTimeoutIn(135 minutes, 5 seconds)
  }

  setUp(
    OrgWriteSimulation.Scenario.inject(constantUsersPerSec(6) during (150 minutes)).protocols(OrgWriteSimulation.HttpConf)
      .throttle(reachRps(3) in (4 minutes), holdFor(145 minutes)),
    PplWriteSimulation.Scenario.inject(constantUsersPerSec(6) during (150 minutes)).protocols(PplWriteSimulation.HttpConf)
      .throttle(reachRps(3) in (4 minutes), holdFor(145 minutes)),
    OrgReadSimulation.Scenario.inject(constantUsersPerSec(30) during (150 minutes)).protocols(OrgReadSimulation.HttpConf)
      .throttle(reachRps(15) in (5 minutes), holdFor(145 minutes)),
    PplReadSimulation.Scenario.inject(constantUsersPerSec(30) during (150 minutes)).protocols(PplReadSimulation.HttpConf)
      .throttle(reachRps(15) in (5 minutes), holdFor(145 minutes))
  )

  after(
    GraphDB.resetAll()
  )

}