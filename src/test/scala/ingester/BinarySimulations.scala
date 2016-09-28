package ingester

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.joda.time.format.ISODateTimeFormat
import org.joda.time.{DateTime, DateTimeZone}
import utils.LoadTestDefaults._

import scala.language.postfixOps

object BinarySimulation {

  val Feeder = csv("ingester/image.uuid").random

  val formatter = ISODateTimeFormat.dateTime().withZone(DateTimeZone.getDefault())

  val Duration = Integer.getInteger("soak-duration-minutes", DefaultSoakDurationInMinutes)

  val HttpConf = http
    .baseURLs(System.getProperty("binary-ingester-hosts").split(',').to[List])
    .basicAuth(System.getProperty("username", "username"), System.getProperty("password", "password"))
    .userAgentHeader("BinaryIngester/Load-test")

  val sentHeaders = Map("Content-Type" -> "application/json", "Accept" -> "application/json")

  val Scenario = scenario("Binary Ingestion").during(Duration minutes) {
    feed(Feeder)
      .exec {
        session => session.set("date", getIsoDate())
      }
      .exec(
        http("Binary Ingestion request")
          .post("/__binary-ingester/ingest")
          .headers(sentHeaders)
          .body(StringBody(
            """{
              |  "contentUri": "http://methode-image-model-transformer-pr-uk-int.svc.ft.com/image/model/${uuid}",
              |  "uuid": "${uuid}",
              |  "destination": "methode-image-model-transformer",
              |  "relativeUrl": "/image/model/${uuid}",
              |  "payload": {
              |    "uuid": "${uuid}",
              |    "title": null,
              |    "titles": null,
              |    "byline": null,
              |    "brands": null,
              |    "identifiers": [
              |      {
              |        "authority": "http://api.ft.com/system/FTCOM-METHODE",
              |        "identifierValue": "${uuid}"
              |      }
              |    ],
              |    "publishedDate": "${date}",
              |    "body": null,
              |    "description": null,
              |    "mediaType": "image/jpeg",
              |    "pixelWidth": null,
              |    "pixelHeight": null,
              |    "internalBinaryUrl": "http://ip-172-24-113-70.eu-west-1.compute.internal:8080/image/binary/${uuid}",
              |    "externalBinaryUrl": "http://com.ft.coco-imagepublish.pre-prod.s3.amazonaws.com/${uuid}",
              |    "members": null,
              |    "mainImage": null,
              |    "comments": null,
              |    "copyright": null,
              |    "publishReference": "tid_qbljizi463",
              |    "lastModified": "${date}"
              |  },
              |  "lastModified": "${date}"
              |}
            """.stripMargin)).asJSON
          .check(status is 200))
  }

  def getIsoDate(): String = {
    formatter.print(DateTime.now())
  }
}

class BinarySimulation extends Simulation {

  val numUsers = Integer.getInteger("users", DefaultNumUsers)
  val rampUp = Integer.getInteger("ramp-up-minutes", DefaultRampUpDurationInMinutes)

  setUp(
    BinarySimulation.Scenario.inject(rampUsers(numUsers) over (rampUp minutes))
  ).protocols(BinarySimulation.HttpConf)

}
