package ingester

import java.util.UUID

import com.github.nscala_time.time.Imports.{DateTime, DateTimeZone}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.joda.time.format.ISODateTimeFormat
import utils.LoadTestDefaults._

import scala.language.postfixOps

object BinaryWriterSimulation {
  val size = 1024 * 1024 * 16
  val binaryBlob: Array[Byte] = new Array(size)
  val r = scala.util.Random
  r.nextBytes(binaryBlob)

  val HttpConf = getDefaultHttpConf("BinaryWriter/Load-test")

  val sentHeaders = Map(
    "Content-Type" -> "application/octet-stream",
    "Content-Length" -> String.valueOf(size))

  val Scenario = scenario("Binary Ingestion").during(Duration minutes) {
    exec(_.set("file_uuid", "load-" + UUID.randomUUID().toString))
      .exec(
        http("Binary Writer request")
          .put("/__binary-writer/binary/${file_uuid}")
          .headers(sentHeaders)
          .header(RequestIdHeader, (s: Session) => getRequestId(s, "ibw"))
          .body(ByteArrayBody(binaryBlob))
          .check(status is 200))
  }

}

class BinaryWriterSimulation extends Simulation {

  setUp(
    BinaryWriterSimulation.Scenario.inject(rampUsers(NumUsers) over (RampUp minutes))
  ).protocols(BinaryWriterSimulation.HttpConf)

}

object BinaryIngesterSimulation {

  val Feeder = getDefaultFeeder("ingester/image.uuid")

  val formatter = ISODateTimeFormat.dateTime().withZone(DateTimeZone.getDefault)

  val HttpConf = getDefaultHttpConf("BinaryIngester/Load-test")

  val sentHeaders = Map(
    "Content-Type" -> "application/json",
    "Accept" -> "application/json")

  val Scenario = scenario("Binary Ingestion").during(Duration minutes) {
    feed(Feeder)
      .exec {
        session => session.set("date", getIsoDate)
      }
      .exec(
        http("Binary Ingestion request")
          .post("/__binary-ingester/ingest")
          .headers(sentHeaders)
          .header(RequestIdHeader, (s: Session) => getRequestId(s, "ibi"))
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

  def getIsoDate: String = {
    formatter.print(DateTime.now())
  }
}

class BinaryIngesterSimulation extends Simulation {

  setUp(
    BinaryIngesterSimulation.Scenario.inject(rampUsers(NumUsers) over (RampUp minutes))
  ).protocols(BinaryIngesterSimulation.HttpConf)

}
