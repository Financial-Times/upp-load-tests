package disruption

import java.util.concurrent.TimeUnit.{MILLISECONDS, SECONDS}

import akka.actor.ActorSystem
import com.tomakehurst.crashlab.saboteur.Delay.delay
import com.tomakehurst.crashlab.saboteur.FirewallTimeout.firewallTimeout
import com.tomakehurst.crashlab.saboteur.NetworkFailure.networkFailure
import com.tomakehurst.crashlab.saboteur.PacketLoss.packetLoss
import com.tomakehurst.crashlab.saboteur.Saboteur
import com.tomakehurst.crashlab.saboteur.ServiceFailure.serviceFailure
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration
import scala.language.postfixOps

object GraphDB {

  private val Logger = LoggerFactory.getLogger(getClass)

  case class Worker(host: String, client: Saboteur) {
    def this(host: String) = this(host, Saboteur.defineClient("worker-" + host, 8080, host))
  }

  implicit class GraphDBSaboteur(val self: Worker) extends AnyVal {

    def createFlakyConnectionIn(duration: FiniteDuration)(implicit SchedulerSystem: ActorSystem) = SchedulerSystem.scheduler.scheduleOnce(duration) {
      Logger.warn("Attempting to create a flaky connection to worker '{}'", self.host)
      self.client.addFault(packetLoss("flaky-connection").probability(40).correlation(15))
    }

    def createFlakyConnectionIn(duration: FiniteDuration, lasting: FiniteDuration)(implicit SchedulerSystem: ActorSystem) = {
      SchedulerSystem.scheduler.scheduleOnce(duration) {
        Logger.warn("Attempting to create a flaky connection to worker '{}'", self.host)
        self.client.addFault(packetLoss("flaky-connection").probability(40).correlation(15))
      }
      resetIn(duration + lasting)
    }

    def createSlowConnectionIn(duration: FiniteDuration)(implicit SchedulerSystem: ActorSystem) = SchedulerSystem.scheduler.scheduleOnce(duration) {
      Logger.warn("Attempting to create a slow connection to worker '{}'", self.host)
      self.client.addFault(delay("response-slowness").delay(2, SECONDS).variance(1, SECONDS))
    }

    def createSlowConnectionIn(duration: FiniteDuration, lasting: FiniteDuration)(implicit SchedulerSystem: ActorSystem) = {
      SchedulerSystem.scheduler.scheduleOnce(duration) {
        Logger.warn("Attempting to create a slow connection to worker '{}'", self.host)
        self.client.addFault(delay("response-slowness").delay(2, SECONDS).variance(1, SECONDS))
      }
      resetIn(duration + lasting)
    }

    def createNetworkFailureIn(duration: FiniteDuration)(implicit SchedulerSystem: ActorSystem) = SchedulerSystem.scheduler.scheduleOnce(duration) {
      Logger.warn("Attempting to create a network failure to worker '{}'", self.host)
      self.client.addFault(networkFailure("network-failure"))
    }

    def createNetworkFailureIn(duration: FiniteDuration, lasting: FiniteDuration)(implicit SchedulerSystem: ActorSystem) = {
      SchedulerSystem.scheduler.scheduleOnce(duration) {
        Logger.warn("Attempting to create a network failure to worker '{}'", self.host)
        self.client.addFault(networkFailure("network-failure"))
      }
      resetIn(duration + lasting)
    }

    def createServiceFailureIn(duration: FiniteDuration)(implicit SchedulerSystem: ActorSystem) = SchedulerSystem.scheduler.scheduleOnce(duration) {
      Logger.warn("Attempting to create a service failure to worker '{}'", self.host)
      self.client.addFault(serviceFailure("service-failure"))
    }

    def createServiceFailureIn(duration: FiniteDuration, lasting: FiniteDuration)(implicit SchedulerSystem: ActorSystem) = {
      SchedulerSystem.scheduler.scheduleOnce(duration) {
        Logger.warn("Attempting to create a service failure to worker '{}'", self.host)
        self.client.addFault(serviceFailure("service-failure"))
      }
      resetIn(duration + lasting)
    }

    def createFirewallTimeoutIn(duration: FiniteDuration)(implicit SchedulerSystem: ActorSystem) = SchedulerSystem.scheduler.scheduleOnce(duration) {
      Logger.warn("Attempting to create a firewall timeout failure to worker '{}'", self.host)
      self.client.addFault(firewallTimeout("service-failure").timeout(500, MILLISECONDS))
    }

    def createFirewallTimeoutIn(duration: FiniteDuration, lasting: FiniteDuration)(implicit SchedulerSystem: ActorSystem) = {
      SchedulerSystem.scheduler.scheduleOnce(duration) {
        Logger.warn("Attempting to create a firewall timeout failure to worker '{}'", self.host)
        self.client.addFault(firewallTimeout("service-failure").timeout(500, MILLISECONDS))
      }
      resetIn(duration + lasting)
    }

    def reset() = self.client.reset()

    def resetIn(duration: FiniteDuration)(implicit SchedulerSystem: ActorSystem) = SchedulerSystem.scheduler.scheduleOnce(duration) {
      self.client.reset()
    }

  }

  val WorkerOne = new Worker("ftaps44672-law1a-eu-t")
  val WorkerTwo = new Worker("ftaps44670-law1a-eu-t")
  val WorkerThree = new Worker("ftaps44671-law1a-eu-t")
  val WorkerFour = new Worker("ftaps44839-law1a-eu-t")

  def resetAll() = {
    WorkerOne.reset()
    WorkerTwo.reset()
    WorkerThree.reset()
    WorkerFour.reset()
    WorkerFour.reset()
  }

  def resetAll(duration: FiniteDuration)(implicit SchedulerSystem: ActorSystem) = {
    WorkerOne.resetIn(duration)
    WorkerTwo.resetIn(duration)
    WorkerThree.resetIn(duration)
    WorkerFour.resetIn(duration)
  }

}

