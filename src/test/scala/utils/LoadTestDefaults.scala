package utils

import java.util.UUID
import java.util.concurrent.atomic.AtomicLong

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object LoadTestDefaults {

  val DefaultSoakDurationInMinutes = 5
  val DefaultRampUpDurationInMinutes = 1
  val DefaultNumUsers = 10
  val RequestIdHeader = "X-Request-Id"
  val NumUsers = Integer.getInteger("users", DefaultNumUsers)
  val RampUp = Integer.getInteger("ramp-up-minutes", DefaultRampUpDurationInMinutes)
  val Duration = Integer.getInteger("soak-duration-minutes", DefaultSoakDurationInMinutes)
  val Counter: ThreadLocal[AtomicLong] = new ThreadLocal[AtomicLong]() {
    override protected def initialValue: AtomicLong = {
      new AtomicLong(0)
    }
  }

  def getDefaultHttpConf(loadTestName: String) = {
    val userAgent = s"$loadTestName/Load-test"
    http
      .baseURLs(System.getProperty("hosts").split(',').to[List])
      .basicAuth(System.getProperty("username", "username"), System.getProperty("password", "password"))
      .userAgentHeader(userAgent)
  }

  def getDefaultFeeder(defaultCSVPath: String) = csv(System.getProperty("uuid-csv-path", defaultCSVPath)).random

  def getRandomUUIDFeeder = Iterator.continually(Map(
    "uuid" -> UUID.randomUUID()
  ))

  def getRequestId(s: Session, identifier: String) = "tid_" + identifier + s.userId.toString + Counter.get().getAndIncrement()
}
