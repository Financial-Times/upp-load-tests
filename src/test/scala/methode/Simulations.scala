package methode

import io.gatling.core.Predef._
import io.gatling.http.HeaderNames.ContentType
import io.gatling.http.Predef._
import utils.LoadTestDefaults._

import scala.language.postfixOps

object ImageModelTransformerSimulation {

  val Feeder = getDefaultFeeder("methode/methode-image-model-transformer.uuid")

  val HttpConf = getDefaultHttpConf("MethodeImageModelTransformer")

  val Scenario = scenario("Methode Image Model Transformer").during(Duration minutes) {
    feed(Feeder)
      .exec(
        http("Methode Image Model Transformer request")
          .get("/__methode-image-model-transformer/${type}/model/${uuid}")
          .header(RequestIdHeader, (s: Session) => getRequestId(s, "mimt"))
          .check(status is 200,
            jsonPath("$.uuid") is "${uuid}"
          ))
  }
}

class ImageModelTransformerSimulation extends Simulation {

  setUp(
    ImageModelTransformerSimulation.Scenario.inject(rampUsers(NumUsers) over (RampUp minutes))
  ).protocols(ImageModelTransformerSimulation.HttpConf)

}

object ImageBinaryTransformerSimulation {

  val Feeder = getDefaultFeeder("methode/methode-image-binary-transformer.uuid")

  val HttpConf = getDefaultHttpConf("MethodeImageBinaryTransformer")

  val Scenario = scenario("Methode Image Binary Transformer").during(Duration minutes) {
    feed(Feeder)
      .exec(
        http("Methode Image Binary Transformer request")
          .get("/__methode-image-binary-transformer/image/binary/${uuid}")
          .header(RequestIdHeader, (s: Session) => getRequestId(s, "mibt"))
          .check(status is 200,
            header(ContentType) is "image/jpeg"
          ))
  }
}

class ImageBinaryTransformerSimulation extends Simulation {

  setUp(
    ImageBinaryTransformerSimulation.Scenario.inject(rampUsers(NumUsers) over (RampUp minutes))
  ).protocols(ImageBinaryTransformerSimulation.HttpConf)

}
