package wordpress

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.joda.time.format.ISODateTimeFormat
import org.joda.time.{DateTime, DateTimeZone}
import utils.LoadTestDefaults._

import scala.language.postfixOps

object ImageTransformerSimulation {

  val formatter = ISODateTimeFormat.dateTime().withZone(DateTimeZone.getDefault)
  val Duration = Integer.getInteger("soak-duration-minutes", DefaultSoakDurationInMinutes)
  val HttpConf = http
    .baseURLs(System.getProperty("hosts").split(',').to[List])
    .userAgentHeader("People/Load-test")

  val Scenario = scenario("Wordpress Image Transformer").during(Duration minutes) {
    exec(_.set("modified", getIsoDate))
      .exec(
        http("Wordpress Image Transformer request")
          .post("/__wordpress-image-mapper/import")
          .header("X-Request-Id", (s: Session) => "tid_wip" + s.userId.toString)
          .body(StringBody(
            """{
              |  "apiUrl": "http://blogs.ft.com/brusselsblog/api/get_post/?id=65281",
              |  "lastModified": "${modified}",
              |  "next_url": "http://blogs.ft.com/brusselsblog/2016/02/08/brussels-briefing-back-to-turkey/",
              |  "post": {
              |    "attachments": [
              |      {
              |        "alt": "",
              |        "caption": "Mr Renzi, left, during his visit last week with Germany's Angela Merkel in Berlin",
              |        "description": "Italian Prime Minister Matteo Renzi (L) and German Chancellor Angela Merkel (R) address a press conference following talks at the chancellery in Berlin on January 29, 2016. / AFP / John MACDOUGALL        (Photo credit should read JOHN MACDOUGALL/AFP/Getty Images)",
              |        "id": 65286,
              |        "image_source": "",
              |        "images": {
              |          "full": {
              |            "height": 3840,
              |            "url": "http://test.www.ft.com/fastft/files/2016/10/230_fractal.jpg",
              |            "width": 2160
              |          },
              |          "large": {
              |            "height": 396,
              |            "url": "http://blogs.ft.com/brusselsblog/files/2016/02/Renzi-Merkel-590x396.jpg",
              |            "width": 590
              |          },
              |          "medium": {
              |            "height": 182,
              |            "url": "http://blogs.ft.com/brusselsblog/files/2016/02/Renzi-Merkel-272x182.jpg",
              |            "width": 272
              |          },
              |          "participant-headshot": {
              |            "height": 45,
              |            "url": "http://blogs.ft.com/brusselsblog/files/2016/02/Renzi-Merkel-35x45.jpg",
              |            "width": 35
              |          },
              |          "thumbnail": {
              |            "height": 45,
              |            "url": "http://blogs.ft.com/brusselsblog/files/2016/02/Renzi-Merkel-35x45.jpg",
              |            "width": 35
              |          },
              |          "top-story": {
              |            "height": 96,
              |            "url": "http://blogs.ft.com/brusselsblog/files/2016/02/Renzi-Merkel-167x96.jpg",
              |            "width": 167
              |          }
              |        },
              |        "mime_type": "image/jpeg",
              |        "parent": 65281,
              |        "slug": "germany-italy-diplomacy-politics-and-maine-coon-kitten",
              |        "title": "GERMANY-ITALY-DIPLOMACY-POLITICS-AND-MAINE-COON-KITTEN",
              |        "url": "http://test.www.ft.com/fastft/files/2016/10/230_fractal.jpg"
              |      },
              |      {
              |        "alt": "",
              |        "caption": "",
              |        "description": "Italy's Prime minister Matteo Renzi arrives before a meeting with his Irish counterpart Enda Kenny on July 10, 2015 at the Chigi Palace in Rome. AFP PHOTO / ALBERTO PIZZOLI        (Photo credit should read ALBERTO PIZZOLI/AFP/Getty Images)",
              |        "id": 65325,
              |        "image_source": "",
              |        "images": {
              |          "full": {
              |            "height": 1152,
              |            "url": "http://blogs.ft.com/brusselsblog/files/2016/02/New-story-of-MAS_PM9843918@ITALY-IRELAND-REN_20160118171546_2.jpg",
              |            "width": 2048
              |          },
              |          "large": {
              |            "height": 331,
              |            "url": "http://blogs.ft.com/brusselsblog/files/2016/02/New-story-of-MAS_PM9843918@ITALY-IRELAND-REN_20160118171546_2-590x331.jpg",
              |            "width": 590
              |          },
              |          "medium": {
              |            "height": 153,
              |            "url": "http://blogs.ft.com/brusselsblog/files/2016/02/New-story-of-MAS_PM9843918@ITALY-IRELAND-REN_20160118171546_2-272x153.jpg",
              |            "width": 272
              |          },
              |          "participant-headshot": {
              |            "height": 45,
              |            "url": "http://blogs.ft.com/brusselsblog/files/2016/02/New-story-of-MAS_PM9843918@ITALY-IRELAND-REN_20160118171546_2-35x45.jpg",
              |            "width": 35
              |          },
              |          "thumbnail": {
              |            "height": 45,
              |            "url": "http://blogs.ft.com/brusselsblog/files/2016/02/New-story-of-MAS_PM9843918@ITALY-IRELAND-REN_20160118171546_2-35x45.jpg",
              |            "width": 35
              |          },
              |          "top-story": {
              |            "height": 96,
              |            "url": "http://blogs.ft.com/brusselsblog/files/2016/02/New-story-of-MAS_PM9843918@ITALY-IRELAND-REN_20160118171546_2-167x96.jpg",
              |            "width": 167
              |          }
              |        },
              |        "mime_type": "image/jpeg",
              |        "parent": 65281,
              |        "slug": "italy-ireland-renzi-kenny",
              |        "title": "Italy's Prime minister Matteo Renzi",
              |        "url": "http://blogs.ft.com/brusselsblog/files/2016/02/New-story-of-MAS_PM9843918@ITALY-IRELAND-REN_20160118171546_2.jpg"
              |      }
              |    ],
              |    "authors": [
              |      {
              |        "description": "",
              |        "first_name": "Peter",
              |        "id": 1421,
              |        "last_name": "Spiegel",
              |        "name": "Peter Spiegel",
              |        "nickname": "peterspiegel",
              |        "slug": "peterspiegel",
              |        "url": ""
              |      }
              |    ],
              |    "blog_id": 3,
              |    "blog_uid": "FT-LABS-WP-1-3",
              |    "categories": [
              |      {
              |        "description": "",
              |        "id": 29094,
              |        "parent": 0,
              |        "post_count": 18,
              |        "slug": "brussels-briefing",
              |        "title": "Brussels Briefing"
              |      },
              |      {
              |        "description": "",
              |        "id": 6,
              |        "parent": 0,
              |        "post_count": 889,
              |        "slug": "eu",
              |        "title": "EU"
              |      }
              |    ],
              |    "comment_count": 0,
              |    "comment_status": "open",
              |    "comments": [],
              |    "content": "<p><strong><em>This is Friday&#8217;s edition of our new Brussels Briefing. To receive it every morning in your email in-box, <a title=\"FT.com - Alerts Hub\" href=\"http://nbe.ft.com/nbe/profile.cfm?brussels=Y\" target=\"_blank\">sign up here</a>.</em></strong></p>\n<div id=\"attachment_65286\" class=\"wp-caption alignleft\" style=\"width: 282px\"><a href=\"http://blogs.ft.com/brusselsblog/files/2016/02/Renzi-Merkel.jpg\"><img class=\"size-medium wp-image-65286\" src=\"http://blogs.ft.com/brusselsblog/files/2016/02/Renzi-Merkel-272x182.jpg\" alt=\"\" width=\"272\" height=\"182\" /></a><p class=\"wp-caption-text\" data-img-id=\"65286\">Mr Renzi, left, during his visit last week with Germany&#39;s Angela Merkel in Berlin</p></div>\n<p>Sometimes it seems not a day goes by without Matteo Renzi, the Italian prime minister, <a href=\"http://click.link.ft.com/e/YSwiNDZjAhEucNMsHAfQow~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERRaHR0cDovL3d3dy5mdC5jb20vY21zL3MvMC9lNTgwYzQ2NC1iZGYzLTExZTUtOWZkYi04N2I4ZDE1YmFlYzIuaHRtbCNheHp6M3l2aVB2N2NjR_Z7InByb2R1Y3QiOiJFbUUiLCJyZXF1ZXN0ZWRTZW5kVGltZSI6IjE0NTQ2NTk0NjIiLCJ0ZW1wbGF0ZUlkIjoiNTY1NWQwMDhjYjU2ZTYwZmM2NDQ3ZTIyIiwidXNlclV1aWQiOiI3MmNhZWZhMy05YTg3LTRkMDctYTYxMy1lZGExNWRkYjFiOGUiLCJlbWFpbElkIjoiNTZiM2NmNzA4NWQ5MjkwMzAwNWM3MzMzIiwidHJhbnNhY3Rpb25JZCI6IjB4NTRhYzFkNzhkNzAwMDAwMCIsInNvdXJjZSI6InNpbXBsZS1lbWFpbC1zZXJ2aWNlIn0~\" target=\"_blank\">picking a fight with Brussels</a>. For a while, it was his <a href=\"http://click.link.ft.com/e/DaiOlhWioDuZBeHJqMuzRw~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERRaHR0cDovL3d3dy5mdC5jb20vY21zL3MvMC80ZDJlZjI0Mi0xYzFhLTExZTUtYTEzMC0yZTdkYjcyMWY5OTYuaHRtbCNheHp6M3l2aVB2N2NjR_Z7InNvdXJjZSI6InNpbXBsZS1lbWFpbC1zZXJ2aWNlIiwiZW1haWxJZCI6IjU2YjNjZjcwODVkOTI5MDMwMDVjNzMzMyIsInByb2R1Y3QiOiJFbUUiLCJ1c2VyVXVpZCI6IjcyY2FlZmEzLTlhODctNGQwNy1hNjEzLWVkYTE1ZGRiMWI4ZSIsInRlbXBsYXRlSWQiOiI1NjU1ZDAwOGNiNTZlNjBmYzY0NDdlMjIiLCJyZXF1ZXN0ZWRTZW5kVGltZSI6IjE0NTQ2NTk0NjIiLCJ0cmFuc2FjdGlvbklkIjoiMHg1NGFjMWQ3OGQ3MDAwMDAwIn0~\" target=\"_blank\">angry denunciation</a> of its slow response to the refugee crisis. Then he accused the EU <a href=\"http://click.link.ft.com/e/EcH6PJcJCcW65ATNxY4g3Q~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERRaHR0cDovL3d3dy5mdC5jb20vY21zL3MvMC9jZWJkNjc5Yy1hMjgxLTExZTUtOGQ3MC00MmI2OGNmYWU2ZTQuaHRtbCNheHp6M3l2aVB2N2NjR_Z7InNvdXJjZSI6InNpbXBsZS1lbWFpbC1zZXJ2aWNlIiwidGVtcGxhdGVJZCI6IjU2NTVkMDA4Y2I1NmU2MGZjNjQ0N2UyMiIsInJlcXVlc3RlZFNlbmRUaW1lIjoiMTQ1NDY1OTQ2MiIsInByb2R1Y3QiOiJFbUUiLCJlbWFpbElkIjoiNTZiM2NmNzA4NWQ5MjkwMzAwNWM3MzMzIiwidXNlclV1aWQiOiI3MmNhZWZhMy05YTg3LTRkMDctYTYxMy1lZGExNWRkYjFiOGUiLCJ0cmFuc2FjdGlvbklkIjoiMHg1NGFjMWQ3OGQ3MDAwMDAwIn0~\" target=\"_blank\">of a Ã¢â‚¬Å“double standardÃ¢â‚¬Â</a> on Russian gas pipelines. More recently, <a href=\"http://click.link.ft.com/e/syPtm_cKJzysMivQ4ohPBg~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERRaHR0cDovL3d3dy5mdC5jb20vY21zL3MvMC9jMGE5NmFmMi1iYjhiLTExZTUtYmY3ZS04YTMzOWI2ZjIxNjQuaHRtbCNheHp6M3l2aVB2N2NjR_Z7InJlcXVlc3RlZFNlbmRUaW1lIjoiMTQ1NDY1OTQ2MiIsImVtYWlsSWQiOiI1NmIzY2Y3MDg1ZDkyOTAzMDA1YzczMzMiLCJ1c2VyVXVpZCI6IjcyY2FlZmEzLTlhODctNGQwNy1hNjEzLWVkYTE1ZGRiMWI4ZSIsInByb2R1Y3QiOiJFbUUiLCJ0cmFuc2FjdGlvbklkIjoiMHg1NGFjMWQ3OGQ3MDAwMDAwIiwic291cmNlIjoic2ltcGxlLWVtYWlsLXNlcnZpY2UiLCJ0ZW1wbGF0ZUlkIjoiNTY1NWQwMDhjYjU2ZTYwZmM2NDQ3ZTIyIn0~\" target=\"_blank\">he held up a Ã¢â€šÂ¬3bn EU aid package</a> to Turkey. And heÃ¢â‚¬â„¢s been <a href=\"http://click.link.ft.com/e/IDebGSbBS4va3ex_cA9LjA~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERRaHR0cDovL3d3dy5mdC5jb20vY21zL3MvMC84MDEwOWZjYy1hOGUxLTExZTUtOTcwMC0yYjY2OWE1YWViODMuaHRtbCNheHp6M3l2aVB2N2NjR_Z7InRyYW5zYWN0aW9uSWQiOiIweDU0YWMxZDc4ZDcwMDAwMDAiLCJzb3VyY2UiOiJzaW1wbGUtZW1haWwtc2VydmljZSIsImVtYWlsSWQiOiI1NmIzY2Y3MDg1ZDkyOTAzMDA1YzczMzMiLCJwcm9kdWN0IjoiRW1FIiwidGVtcGxhdGVJZCI6IjU2NTVkMDA4Y2I1NmU2MGZjNjQ0N2UyMiIsInJlcXVlc3RlZFNlbmRUaW1lIjoiMTQ1NDY1OTQ2MiIsInVzZXJVdWlkIjoiNzJjYWVmYTMtOWE4Ny00ZDA3LWE2MTMtZWRhMTVkZGIxYjhlIn0~\" target=\"_blank\">blaming new EU rules</a> for his countryÃ¢â‚¬â„¢s mounting banking crisis. But the most critical fight heÃ¢â‚¬â„¢s been waging was on full display yesterday: his attempt <a href=\"http://click.link.ft.com/e/fJBW8T4UY8DMvVjEf0LvhA~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERRaHR0cDovL3d3dy5mdC5jb20vY21zL3MvMC8wOGJhNzhmOC1hODA1LTExZTUtOTU1Yy0xZTFkNmRlOTQ4NzkuaHRtbCNheHp6M3l2aVB2N2NjR_Z7InNvdXJjZSI6InNpbXBsZS1lbWFpbC1zZXJ2aWNlIiwidXNlclV1aWQiOiI3MmNhZWZhMy05YTg3LTRkMDctYTYxMy1lZGExNWRkYjFiOGUiLCJyZXF1ZXN0ZWRTZW5kVGltZSI6IjE0NTQ2NTk0NjIiLCJ0ZW1wbGF0ZUlkIjoiNTY1NWQwMDhjYjU2ZTYwZmM2NDQ3ZTIyIiwidHJhbnNhY3Rpb25JZCI6IjB4NTRhYzFkNzhkNzAwMDAwMCIsInByb2R1Y3QiOiJFbUUiLCJlbWFpbElkIjoiNTZiM2NmNzA4NWQ5MjkwMzAwNWM3MzMzIn0~\" target=\"_blank\">to get more wiggle room</a> for Italy&#8217;s 2016 budget.</p>\n<p>Pierre Moscovici, the European CommissionÃ¢â‚¬â„¢s economic chief, was the man in the firing line this time, since yesterday was his semi-regular appearance to unveil <a href=\"http://click.link.ft.com/e/d0slKk6Cg3C9fW9QCvyZIQ~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERRaHR0cDovL3d3dy5mdC5jb20vY21zL3MvMC81MWY4MmMxNi1jYjFiLTExZTUtYThlZi1lYTY2ZTk2N2RkNDQuaHRtbCNheHp6M3l2aVB2N2NjR_Z7InRyYW5zYWN0aW9uSWQiOiIweDU0YWMxZDc4ZDcwMDAwMDAiLCJyZXF1ZXN0ZWRTZW5kVGltZSI6IjE0NTQ2NTk0NjIiLCJ0ZW1wbGF0ZUlkIjoiNTY1NWQwMDhjYjU2ZTYwZmM2NDQ3ZTIyIiwic291cmNlIjoic2ltcGxlLWVtYWlsLXNlcnZpY2UiLCJlbWFpbElkIjoiNTZiM2NmNzA4NWQ5MjkwMzAwNWM3MzMzIiwidXNlclV1aWQiOiI3MmNhZWZhMy05YTg3LTRkMDctYTYxMy1lZGExNWRkYjFiOGUiLCJwcm9kdWN0IjoiRW1FIn0~\" target=\"_blank\">the EUÃ¢â‚¬â„¢s latest economic forecasts</a>. In the run-up to Mr MoscoviciÃ¢â‚¬â„¢s announcement, Pier Carlo Padoan, the Italian finance minister, laid down his marker: he wanted a decision quickly that would allow Rome more flexibility <a href=\"http://click.link.ft.com/e/6xiq7uWNarvjc-hk7DWJmw~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAESFaHR0cDovL3d3dy5hbnNhLml0L2VuZ2xpc2gvbmV3cy9wb2xpdGljcy8yMDE2LzAyLzAzL3BhZG9hbi1zYXlzLWl0YWx5LWhhcy1mbGV4aWJpbGl0eS1yaWdodF81ZTAzMzliMi02YmE1LTQ4MWUtYTVhZC0xOGY2NTRlYWQ3MDEuaHRtbEf2eyJlbWFpbElkIjoiNTZiM2NmNzA4NWQ5MjkwMzAwNWM3MzMzIiwicmVxdWVzdGVkU2VuZFRpbWUiOiIxNDU0NjU5NDYyIiwidGVtcGxhdGVJZCI6IjU2NTVkMDA4Y2I1NmU2MGZjNjQ0N2UyMiIsInByb2R1Y3QiOiJFbUUiLCJ1c2VyVXVpZCI6IjcyY2FlZmEzLTlhODctNGQwNy1hNjEzLWVkYTE1ZGRiMWI4ZSIsInNvdXJjZSI6InNpbXBsZS1lbWFpbC1zZXJ2aWNlIiwidHJhbnNhY3Rpb25JZCI6IjB4NTRhYzFkNzhkNzAwMDAwMCJ9\" target=\"_blank\">to spend a bit more</a> than EU rules normally allow. But Mr Moscovici was having none of it. Mr Padoan would have to wait until May for a decision, along with every other eurozone minister.</p>\n<p>In what appeared a fit of mild Gallic pique, Mr Moscovici also noted that Ã¢â‚¬Å“Italy is the only country in the EUÃ¢â‚¬Â that had already been given special dispensation under new budget flexibility guidelines Ã¢â‚¬â€œ it is able to miss its structural deficit target by 0.4 per cent in order to implement Brussels-approved economic reforms Ã¢â‚¬â€œ and it was now coming back repeatedly for more.</p>\n<p><span id=\"more-65281\"></span>Ã¢â‚¬Å“In October, that 0.4 per cent aside, they asked for more flexibility regarding their budgetary plan for other structural reforms and investment,Ã¢â‚¬Â Mr Moscovici said. Ã¢â‚¬Å“The Italians also asked for additional flexibility for costs related to the migrant crisis. There was further flexibility requested to cope with the terrorist threat, and to improve education.Ã¢â‚¬Â Quite a litany.</p>\n<p>The EU budget rules are fiendishly complicated, but commission officials have already made clear there is no way Italy will get everything it wants. To understand whatÃ¢â‚¬â„¢s at stake, the key figure to understand is what Brussels calls Ã¢â‚¬Å“structural effortÃ¢â‚¬Â Ã¢â‚¬â€œ basically a measure of how much a countryÃ¢â‚¬â„¢s economic reforms will permanently lower its deficit. For 2016, Italy was supposed to make a Ã¢â‚¬Å“structural effortÃ¢â‚¬Â equal to 0.5 per cent of gross domestic product, so it can more quickly reduce its sovereign debt, which is second highest in the eurozone, behind only Greece.</p>\n<p>With the 0.4 per cent of flexibility granted last year, Rome now only has to make an effort of 0.1 per cent. But since then, Mr Padoan has requested another 0.4 per cent of space, arguing they should get more credit for new economic reforms, and some of his spending should be qualified as Ã¢â‚¬Å“investmentÃ¢â‚¬Â, which can be exempted from the deficit calculations. According to EU rules, though, nobody is allowed that much Ã¢â‚¬Å“flexibilityÃ¢â‚¬Â. And Mr MoscoviciÃ¢â‚¬â„¢s forecast yesterday showed it would take even more flexibility than that to get Italy into compliance, because RomeÃ¢â‚¬â„¢s spending is already way past those maximum limits.</p>\n<p>As we said, fiendishly complicated. And we arenÃ¢â‚¬â„¢t even delving into Mr RenziÃ¢â‚¬â„¢s requests for yet more flexibility to spend on refugees (probably justified) and counterterrorism (a bit more squishy, since much of that spending <a href=\"http://click.link.ft.com/e/iuKjmIY7ID14-MhVs967_A~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERRaHR0cDovL3d3dy5mdC5jb20vY21zL3MvMC8zMmQ0YWZjNi05MmQ2LTExZTUtYmQ4Mi1jMWZiODdiZWY3YWYuaHRtbCNheHp6M3l2aVB2N2NjR_Z7InVzZXJVdWlkIjoiNzJjYWVmYTMtOWE4Ny00ZDA3LWE2MTMtZWRhMTVkZGIxYjhlIiwidGVtcGxhdGVJZCI6IjU2NTVkMDA4Y2I1NmU2MGZjNjQ0N2UyMiIsInByb2R1Y3QiOiJFbUUiLCJlbWFpbElkIjoiNTZiM2NmNzA4NWQ5MjkwMzAwNWM3MzMzIiwicmVxdWVzdGVkU2VuZFRpbWUiOiIxNDU0NjU5NDYyIiwic291cmNlIjoic2ltcGxlLWVtYWlsLXNlcnZpY2UiLCJ0cmFuc2FjdGlvbklkIjoiMHg1NGFjMWQ3OGQ3MDAwMDAwIn0~\" target=\"_blank\">is actually going to cultural programmes</a>).</p>\n<p>If one were to read between Mr MoscoviciÃ¢â‚¬â„¢s lines, itÃ¢â‚¬â„¢s becoming increasingly clear Mr Renzi will have yet another thing to complain about come May.</p>\n<p><strong>What weÃ¢â‚¬â„¢re reading</strong></p>\n<p>The European Commission has been called in for a special meeting today to deal with yet another budget problem child: Portugal. The new Socialist government in Lisbon <a href=\"http://click.link.ft.com/e/fetEVzVDkW6grfyF6o_HjA~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERRaHR0cDovL3d3dy5mdC5jb20vY21zL3MvMC9iY2ZiYzM0Yy1jYjM5LTExZTUtYThlZi1lYTY2ZTk2N2RkNDQuaHRtbCNheHp6M3l2aVB2N2NjR_Z7ImVtYWlsSWQiOiI1NmIzY2Y3MDg1ZDkyOTAzMDA1YzczMzMiLCJyZXF1ZXN0ZWRTZW5kVGltZSI6IjE0NTQ2NTk0NjIiLCJzb3VyY2UiOiJzaW1wbGUtZW1haWwtc2VydmljZSIsInVzZXJVdWlkIjoiNzJjYWVmYTMtOWE4Ny00ZDA3LWE2MTMtZWRhMTVkZGIxYjhlIiwidHJhbnNhY3Rpb25JZCI6IjB4NTRhYzFkNzhkNzAwMDAwMCIsInRlbXBsYXRlSWQiOiI1NjU1ZDAwOGNiNTZlNjBmYzY0NDdlMjIiLCJwcm9kdWN0IjoiRW1FIn0~\" target=\"_blank\">has until this afternoon</a> to overhaul its spending plan to get it into compliance with eurozone rules. The talks are expected to go down to the wire, but if the two sides canÃ¢â‚¬â„¢t come to an agreement, Portugal would be the first country in the eurozone to have a budget rejected outright under the new crisis-era fiscal rules. The Portuguese business daily DiÃƒÂ¡rio EconÃƒÂ³mico reports that the cabinet of prime minister AntÃƒÂ³nio Costa <a href=\"http://click.link.ft.com/e/T8sXvn1G8OdBQvH6GCAZHg~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERRaHR0cDovL2Vjb25vbWljby5zYXBvLnB0L25vdGljaWFzL29yY2FtZW50by1kby1lc3RhZG8tamEtZm9pLWFwcm92YWRvXzI0MTcyNS5odG1sR_Z7InRlbXBsYXRlSWQiOiI1NjU1ZDAwOGNiNTZlNjBmYzY0NDdlMjIiLCJwcm9kdWN0IjoiRW1FIiwidXNlclV1aWQiOiI3MmNhZWZhMy05YTg3LTRkMDctYTYxMy1lZGExNWRkYjFiOGUiLCJlbWFpbElkIjoiNTZiM2NmNzA4NWQ5MjkwMzAwNWM3MzMzIiwidHJhbnNhY3Rpb25JZCI6IjB4NTRhYzFkNzhkNzAwMDAwMCIsInNvdXJjZSI6InNpbXBsZS1lbWFpbC1zZXJ2aWNlIiwicmVxdWVzdGVkU2VuZFRpbWUiOiIxNDU0NjU5NDYyIn0~\" target=\"_blank\">approved the budget</a> last night even before a sign-off from Brussels.</p>\n<p>Parisis all atwitter about a possible reshuffle in President FranÃƒÂ§ois HollandeÃ¢â‚¬â„¢s cabinet, likely to come sometime after next Wednesday. Paris Match this week broke the biggest overhaul news: that SÃƒÂ©golÃƒÂ¨ne Royal, the current environment minister (and mother to Mr HollandeÃ¢â‚¬â„¢s four children), will become <a href=\"http://click.link.ft.com/e/2jLSPfrD44n54wKdyWvxPA~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERnaHR0cDovL3d3dy5wYXJpc21hdGNoLmNvbS9BY3R1L1BvbGl0aXF1ZS9TZWdvbGVuZS1Sb3lhbC1iaWVudG90LW1pbmlzdHJlLWRlcy1BZmZhaXJlcy1ldHJhbmdlcmVzLTkwNzMzN0f2eyJ0cmFuc2FjdGlvbklkIjoiMHg1NGFjMWQ3OGQ3MDAwMDAwIiwidXNlclV1aWQiOiI3MmNhZWZhMy05YTg3LTRkMDctYTYxMy1lZGExNWRkYjFiOGUiLCJzb3VyY2UiOiJzaW1wbGUtZW1haWwtc2VydmljZSIsInJlcXVlc3RlZFNlbmRUaW1lIjoiMTQ1NDY1OTQ2MiIsInRlbXBsYXRlSWQiOiI1NjU1ZDAwOGNiNTZlNjBmYzY0NDdlMjIiLCJlbWFpbElkIjoiNTZiM2NmNzA4NWQ5MjkwMzAwNWM3MzMzIiwicHJvZHVjdCI6IkVtRSJ9\" target=\"_blank\">the new foreign minister</a>. Since then, LibÃƒÂ©ration has reported that Nicolas Hulot, one of FranceÃ¢â‚¬â„¢s most prominent environmentalists who ran for the Green nomination in the 2012 presidential race, <a href=\"http://click.link.ft.com/e/0OAIbkrENrY71wCIcE1PdQ~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERiaHR0cDovL3d3dy5saWJlcmF0aW9uLmZyL2ZyYW5jZS8yMDE2LzAyLzA0L25pY29sYXMtaHVsb3QtcmVmdXNlLWxlLXBvc3RlLWRlLXN1cGVyLW1pbmlzdHJlXzE0MzEwNDlH9nsidHJhbnNhY3Rpb25JZCI6IjB4NTRhYzFkNzhkNzAwMDAwMCIsInByb2R1Y3QiOiJFbUUiLCJ1c2VyVXVpZCI6IjcyY2FlZmEzLTlhODctNGQwNy1hNjEzLWVkYTE1ZGRiMWI4ZSIsInJlcXVlc3RlZFNlbmRUaW1lIjoiMTQ1NDY1OTQ2MiIsInNvdXJjZSI6InNpbXBsZS1lbWFpbC1zZXJ2aWNlIiwidGVtcGxhdGVJZCI6IjU2NTVkMDA4Y2I1NmU2MGZjNjQ0N2UyMiIsImVtYWlsSWQiOiI1NmIzY2Y3MDg1ZDkyOTAzMDA1YzczMzMifQ~~\" target=\"_blank\">turned down an offer</a> to head a Ã¢â‚¬Å“superÃ¢â‚¬Â ministry overseeing ecology. Le Monde reports that Jean-Christophe CambadÃƒÂ©lis, the SocialistsÃ¢â‚¬â„¢ party chief, is urging Mr Hollande to <a href=\"http://click.link.ft.com/e/4j8H0refYlHbLE59eLp-fA~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAES1aHR0cDovL3d3dy5sZW1vbmRlLmZyL3BvbGl0aXF1ZS9hcnRpY2xlLzIwMTYvMDIvMDQvbS1jYW1iYWRlbGlzLXZldXQtdW4tcmVtYW5pZW1lbnQtbWluaXN0ZXJpZWwtcXVpLWVsYXJnaXNzZS1sYS1tYWpvcml0ZS1nb3V2ZXJuZW1lbnRhbGVfNDg1ODg5OV84MjM0NDguaHRtbD94dG1jPXJlbWFuaWVtZW50Jnh0Y3I9Mkf2eyJ0cmFuc2FjdGlvbklkIjoiMHg1NGFjMWQ3OGQ3MDAwMDAwIiwicHJvZHVjdCI6IkVtRSIsInJlcXVlc3RlZFNlbmRUaW1lIjoiMTQ1NDY1OTQ2MiIsInVzZXJVdWlkIjoiNzJjYWVmYTMtOWE4Ny00ZDA3LWE2MTMtZWRhMTVkZGIxYjhlIiwiZW1haWxJZCI6IjU2YjNjZjcwODVkOTI5MDMwMDVjNzMzMyIsInRlbXBsYXRlSWQiOiI1NjU1ZDAwOGNiNTZlNjBmYzY0NDdlMjIiLCJzb3VyY2UiOiJzaW1wbGUtZW1haWwtc2VydmljZSJ9\" target=\"_blank\">look beyond the usual crop of candidates</a> and reach into Ã¢â‚¬Å“all the talents on the left or beyondÃ¢â‚¬Â for reinforcements.</p>\n<p>World leaders were in London yesterday for a donorsÃ¢â‚¬â„¢ conference to help countries in the Middle East overwhelmed by refugees fleeing Syria, and Ahmet Davutoglu, the Turkish prime minister, used the opportunity to warn that <a href=\"http://click.link.ft.com/e/ovwfVKvEu-ShXERDxD7-uQ~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERuaHR0cDovL3d3dy5ibG9vbWJlcmcuY29tL25ld3MvYXJ0aWNsZXMvMjAxNi0wMi0wNC90dXJrZXktYnJhY2VzLWZvci1uZXctd2F2ZS1vZi1zeXJpYW4tcmVmdWdlZXMtZmxlZWluZy1hbGVwcG9H9nsic291cmNlIjoic2ltcGxlLWVtYWlsLXNlcnZpY2UiLCJwcm9kdWN0IjoiRW1FIiwicmVxdWVzdGVkU2VuZFRpbWUiOiIxNDU0NjU5NDYyIiwidXNlclV1aWQiOiI3MmNhZWZhMy05YTg3LTRkMDctYTYxMy1lZGExNWRkYjFiOGUiLCJ0ZW1wbGF0ZUlkIjoiNTY1NWQwMDhjYjU2ZTYwZmM2NDQ3ZTIyIiwiZW1haWxJZCI6IjU2YjNjZjcwODVkOTI5MDMwMDVjNzMzMyIsInRyYW5zYWN0aW9uSWQiOiIweDU0YWMxZDc4ZDcwMDAwMDAifQ~~\" target=\"_blank\">as many as 70,000</a> were on their way from Aleppo amid intensifying fighting. The Wall Street Journal reports that the massive evacuation is due to signs a Russia-backed offensive supporting the Assad regime <a href=\"http://click.link.ft.com/e/3FSiM3XzzZAT5CdHLnhH4A~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERmaHR0cDovL3d3dy53c2ouY29tL2FydGljbGVzL3NhdWRpLWFyYWJpYS1nZXJtYW55LWhpdC1zeXJpYW4tZ292ZXJubWVudC1mb3ItdGFsa3Mtc3VzcGVuc2lvbi0xNDU0NTk1MTM0R_Z7InJlcXVlc3RlZFNlbmRUaW1lIjoiMTQ1NDY1OTQ2MiIsInByb2R1Y3QiOiJFbUUiLCJ0cmFuc2FjdGlvbklkIjoiMHg1NGFjMWQ3OGQ3MDAwMDAwIiwidXNlclV1aWQiOiI3MmNhZWZhMy05YTg3LTRkMDctYTYxMy1lZGExNWRkYjFiOGUiLCJ0ZW1wbGF0ZUlkIjoiNTY1NWQwMDhjYjU2ZTYwZmM2NDQ3ZTIyIiwic291cmNlIjoic2ltcGxlLWVtYWlsLXNlcnZpY2UiLCJlbWFpbElkIjoiNTZiM2NmNzA4NWQ5MjkwMzAwNWM3MzMzIn0~\" target=\"_blank\">may be close to taking the city</a>, SyriaÃ¢â‚¬â„¢s second largest and long the most contested city during the bloody civil war. Divisions within Germany&#8217;s ruling coalition, deepened by the rising refugee tide, continued to widen with Horst Seehofer, head of the Bavarian sister party to Chancellor Angela MerkelÃ¢â‚¬â„¢s Christian Democrats, wrapping up a two-day visit to Moscow by <a href=\"http://click.link.ft.com/e/HvXAyKRzaM-j899mpiP_5Q~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAER3aHR0cDovL3d3dy5mYXoubmV0L2FrdHVlbGwvcG9saXRpay9hdXNsYW5kL2V1cm9wYS9ob3JzdC1zZWVob2Zlci1uYWNoLWJlc3VjaC1iZWktd2xhZGltaXItcHV0aW4taW4tbW9za2F1LTE0MDUxNjU2Lmh0bWxH9nsicHJvZHVjdCI6IkVtRSIsImVtYWlsSWQiOiI1NmIzY2Y3MDg1ZDkyOTAzMDA1YzczMzMiLCJzb3VyY2UiOiJzaW1wbGUtZW1haWwtc2VydmljZSIsInRyYW5zYWN0aW9uSWQiOiIweDU0YWMxZDc4ZDcwMDAwMDAiLCJyZXF1ZXN0ZWRTZW5kVGltZSI6IjE0NTQ2NTk0NjIiLCJ0ZW1wbGF0ZUlkIjoiNTY1NWQwMDhjYjU2ZTYwZmM2NDQ3ZTIyIiwidXNlclV1aWQiOiI3MmNhZWZhMy05YTg3LTRkMDctYTYxMy1lZGExNWRkYjFiOGUifQ~~\" target=\"_blank\">openly breaking with Ms MerkelÃ¢â‚¬â„¢s Russia policy</a> by suggesting the EU drop sanctions against the Kremlin. The statement came a day after Mr Seehofer, who has been Ms MerkelÃ¢â‚¬â„¢s most ardent critic during the refugee crisis, met with Vladimir Putin.</p>\n<p>The fallout from the <a href=\"http://click.link.ft.com/e/sY3khv-3Bg8aGKqJy9hlDQ~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERRaHR0cDovL3d3dy5mdC5jb20vY21zL3MvMC8yMDFkZTJiMC1jYTkxLTExZTUtYmUwYi1iN2VjZTRlOTUzYTAuaHRtbCNheHp6M3lvS0ZlUHJMR_Z7InRlbXBsYXRlSWQiOiI1NjU1ZDAwOGNiNTZlNjBmYzY0NDdlMjIiLCJ0cmFuc2FjdGlvbklkIjoiMHg1NGFjMWQ3OGQ3MDAwMDAwIiwidXNlclV1aWQiOiI3MmNhZWZhMy05YTg3LTRkMDctYTYxMy1lZGExNWRkYjFiOGUiLCJyZXF1ZXN0ZWRTZW5kVGltZSI6IjE0NTQ2NTk0NjIiLCJzb3VyY2UiOiJzaW1wbGUtZW1haWwtc2VydmljZSIsImVtYWlsSWQiOiI1NmIzY2Y3MDg1ZDkyOTAzMDA1YzczMzMiLCJwcm9kdWN0IjoiRW1FIn0~\" target=\"_blank\">resignation of Aivaras Abromavicius</a>, UkraineÃ¢â‚¬â„¢s reform-minded economy minister, continued to reverberate in Kiev, where the speaker of the parliament warned the country was now <a href=\"http://click.link.ft.com/e/mOqbDg3vCCc2thB-eBCoWQ~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERwaHR0cDovL3d3dy5ibG9vbWJlcmcuY29tL25ld3MvYXJ0aWNsZXMvMjAxNi0wMi0wNC91a3JhaW5lLWVudGVyaW5nLXNlcmlvdXMtcG9saXRpY2FsLWNyaXNpcy1hZnRlci1taW5pc3Rlci1xdWl0c0f2eyJ0cmFuc2FjdGlvbklkIjoiMHg1NGFjMWQ3OGQ3MDAwMDAwIiwicmVxdWVzdGVkU2VuZFRpbWUiOiIxNDU0NjU5NDYyIiwic291cmNlIjoic2ltcGxlLWVtYWlsLXNlcnZpY2UiLCJ1c2VyVXVpZCI6IjcyY2FlZmEzLTlhODctNGQwNy1hNjEzLWVkYTE1ZGRiMWI4ZSIsInByb2R1Y3QiOiJFbUUiLCJlbWFpbElkIjoiNTZiM2NmNzA4NWQ5MjkwMzAwNWM3MzMzIiwidGVtcGxhdGVJZCI6IjU2NTVkMDA4Y2I1NmU2MGZjNjQ0N2UyMiJ9\" target=\"_blank\">entering a Ã¢â‚¬Å“serious crisisÃ¢â‚¬Â</a> and accused politicians close to President Petro Poroshenko with corruption. Mr Abromavicius quit after accusing the government of impropriety, and the English-language Kyiv Post reported there were calls yesterday <a href=\"http://click.link.ft.com/e/An7qZlWMS7I-TG4T4VJtJw~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAESfaHR0cHM6Ly93d3cua3lpdnBvc3QuY29tL2FydGljbGUvY29udGVudC91a3JhaW5lLXBvbGl0aWNzL2Ficm9tYXZpY2l1c3MtcmVzaWduYXRpb24tcHJvbXB0cy1jYWxscy1mb3ItbmV3LXBhcmxpYW1lbnQtY29hbGl0aW9uLWdvdmVybm1lbnQtcmVzaHVmZmxlLTQwNzM0MC5odG1sR_Z7InVzZXJVdWlkIjoiNzJjYWVmYTMtOWE4Ny00ZDA3LWE2MTMtZWRhMTVkZGIxYjhlIiwidHJhbnNhY3Rpb25JZCI6IjB4NTRhYzFkNzhkNzAwMDAwMCIsImVtYWlsSWQiOiI1NmIzY2Y3MDg1ZDkyOTAzMDA1YzczMzMiLCJzb3VyY2UiOiJzaW1wbGUtZW1haWwtc2VydmljZSIsInByb2R1Y3QiOiJFbUUiLCJyZXF1ZXN0ZWRTZW5kVGltZSI6IjE0NTQ2NTk0NjIiLCJ0ZW1wbGF0ZUlkIjoiNTY1NWQwMDhjYjU2ZTYwZmM2NDQ3ZTIyIn0~\" target=\"_blank\">for the government to resign</a> and be replaced by technocrats.</p>\n<p>In popular imagination, Scandinavia is often seen as a near-utopian region of strapping but kind-hearted outdoorsman, complete with a social conscience, strong WiFi reception, and female prime ministers who juggle family life and African peacemaking in <a href=\"http://click.link.ft.com/e/soCJIrXgwGQ7zYfJnxcIcA~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAEQ0aHR0cHM6Ly93d3cuZHIuZGsvdHYvc2UvYm9yZ2VuLXNhZXNvbi0yL2Jvcmdlbi0xMS0yMEf2eyJzb3VyY2UiOiJzaW1wbGUtZW1haWwtc2VydmljZSIsImVtYWlsSWQiOiI1NmIzY2Y3MDg1ZDkyOTAzMDA1YzczMzMiLCJ0ZW1wbGF0ZUlkIjoiNTY1NWQwMDhjYjU2ZTYwZmM2NDQ3ZTIyIiwicmVxdWVzdGVkU2VuZFRpbWUiOiIxNDU0NjU5NDYyIiwidXNlclV1aWQiOiI3MmNhZWZhMy05YTg3LTRkMDctYTYxMy1lZGExNWRkYjFiOGUiLCJwcm9kdWN0IjoiRW1FIiwidHJhbnNhY3Rpb25JZCI6IjB4NTRhYzFkNzhkNzAwMDAwMCJ9\" target=\"_blank\">neat one-hour episodes</a>. But has the refugee crisis spoiled that reputation Ã¢â‚¬â€œ and revealed a dark underbelly of Scandinavian culture? The FTÃ¢â‚¬â„¢s Oslo-based correspondent Richard Milne asks if the Nordics have <a href=\"http://click.link.ft.com/e/PuMRTpZAn8KiSkHiF1qssQ~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERRaHR0cDovL3d3dy5mdC5jb20vY21zL3MvMC9jYTAzNmU5ZS1jOWE2LTExZTUtYThlZi1lYTY2ZTk2N2RkNDQuaHRtbCNheHp6M3l2aVB2N2NjR_Z7ImVtYWlsSWQiOiI1NmIzY2Y3MDg1ZDkyOTAzMDA1YzczMzMiLCJ0cmFuc2FjdGlvbklkIjoiMHg1NGFjMWQ3OGQ3MDAwMDAwIiwicmVxdWVzdGVkU2VuZFRpbWUiOiIxNDU0NjU5NDYyIiwic291cmNlIjoic2ltcGxlLWVtYWlsLXNlcnZpY2UiLCJ0ZW1wbGF0ZUlkIjoiNTY1NWQwMDhjYjU2ZTYwZmM2NDQ3ZTIyIiwicHJvZHVjdCI6IkVtRSIsInVzZXJVdWlkIjoiNzJjYWVmYTMtOWE4Ny00ZDA3LWE2MTMtZWRhMTVkZGIxYjhlIn0~\" target=\"_blank\">Ã¢â‚¬Å“turned nastyÃ¢â‚¬Â</a> after Denmark moved to seize asylum seekers&#8217; cash and jewelry; Norwegian officials deported migrants over the freezing Arctic border; and mobs of Swedish vigilantes began chasing down immigrants.</p>\n<p>&nbsp;</p>\n",
              |    "custom_fields": {
              |      "assanka_atompush": [
              |        "yes_atompush"
              |      ],
              |      "assanka_mockingbird": [
              |        "yes_mockingbird"
              |      ],
              |      "ftblogs_access": [
              |        "1"
              |      ]
              |    },
              |    "date": "2016-02-05 08:18:51",
              |    "date_gmt": "2016-02-05 08:18:51",
              |    "excerpt": "<p><strong><em>This is Friday&#8217;s edition of our new Brussels Briefing. To receive it every morning in your email in-box, <a title=\"FT.com - Alerts Hub\" href=\"http://nbe.ft.com/nbe/profile.cfm?brussels=Y\" target=\"_blank\">sign up here</a>.</em></strong></p>\n<div id=\"attachment_65286\" class=\"wp-caption alignleft\" style=\"width: 282px\"><a href=\"http://blogs.ft.com/brusselsblog/files/2016/02/Renzi-Merkel.jpg\"><img class=\"size-medium wp-image-65286\" src=\"http://blogs.ft.com/brusselsblog/files/2016/02/Renzi-Merkel-272x182.jpg\" alt=\"\" width=\"272\" height=\"182\" /></a>\n<p class=\"wp-caption-text\" data-img-id=\"65286\">Mr Renzi, left, during his visit last week with Germany&#39;s Angela Merkel in Berlin</p>\n</div>\n<p>Sometimes it seems not a day goes by without Matteo Renzi, the Italian prime minister, <a href=\"http://click.link.ft.com/e/YSwiNDZjAhEucNMsHAfQow~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERRaHR0cDovL3d3dy5mdC5jb20vY21zL3MvMC9lNTgwYzQ2NC1iZGYzLTExZTUtOWZkYi04N2I4ZDE1YmFlYzIuaHRtbCNheHp6M3l2aVB2N2NjR_Z7InByb2R1Y3QiOiJFbUUiLCJyZXF1ZXN0ZWRTZW5kVGltZSI6IjE0NTQ2NTk0NjIiLCJ0ZW1wbGF0ZUlkIjoiNTY1NWQwMDhjYjU2ZTYwZmM2NDQ3ZTIyIiwidXNlclV1aWQiOiI3MmNhZWZhMy05YTg3LTRkMDctYTYxMy1lZGExNWRkYjFiOGUiLCJlbWFpbElkIjoiNTZiM2NmNzA4NWQ5MjkwMzAwNWM3MzMzIiwidHJhbnNhY3Rpb25JZCI6IjB4NTRhYzFkNzhkNzAwMDAwMCIsInNvdXJjZSI6InNpbXBsZS1lbWFpbC1zZXJ2aWNlIn0~\" target=\"_blank\">picking a fight with Brussels</a>. For a while, it was his <a href=\"http://click.link.ft.com/e/DaiOlhWioDuZBeHJqMuzRw~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERRaHR0cDovL3d3dy5mdC5jb20vY21zL3MvMC80ZDJlZjI0Mi0xYzFhLTExZTUtYTEzMC0yZTdkYjcyMWY5OTYuaHRtbCNheHp6M3l2aVB2N2NjR_Z7InNvdXJjZSI6InNpbXBsZS1lbWFpbC1zZXJ2aWNlIiwiZW1haWxJZCI6IjU2YjNjZjcwODVkOTI5MDMwMDVjNzMzMyIsInByb2R1Y3QiOiJFbUUiLCJ1c2VyVXVpZCI6IjcyY2FlZmEzLTlhODctNGQwNy1hNjEzLWVkYTE1ZGRiMWI4ZSIsInRlbXBsYXRlSWQiOiI1NjU1ZDAwOGNiNTZlNjBmYzY0NDdlMjIiLCJyZXF1ZXN0ZWRTZW5kVGltZSI6IjE0NTQ2NTk0NjIiLCJ0cmFuc2FjdGlvbklkIjoiMHg1NGFjMWQ3OGQ3MDAwMDAwIn0~\" target=\"_blank\">angry denunciation</a> of its slow response to the refugee crisis. Then he accused the EU <a href=\"http://click.link.ft.com/e/EcH6PJcJCcW65ATNxY4g3Q~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERRaHR0cDovL3d3dy5mdC5jb20vY21zL3MvMC9jZWJkNjc5Yy1hMjgxLTExZTUtOGQ3MC00MmI2OGNmYWU2ZTQuaHRtbCNheHp6M3l2aVB2N2NjR_Z7InNvdXJjZSI6InNpbXBsZS1lbWFpbC1zZXJ2aWNlIiwidGVtcGxhdGVJZCI6IjU2NTVkMDA4Y2I1NmU2MGZjNjQ0N2UyMiIsInJlcXVlc3RlZFNlbmRUaW1lIjoiMTQ1NDY1OTQ2MiIsInByb2R1Y3QiOiJFbUUiLCJlbWFpbElkIjoiNTZiM2NmNzA4NWQ5MjkwMzAwNWM3MzMzIiwidXNlclV1aWQiOiI3MmNhZWZhMy05YTg3LTRkMDctYTYxMy1lZGExNWRkYjFiOGUiLCJ0cmFuc2FjdGlvbklkIjoiMHg1NGFjMWQ3OGQ3MDAwMDAwIn0~\" target=\"_blank\">of a Ã¢â‚¬Å“double standardÃ¢â‚¬Â</a> on Russian gas pipelines. More recently, <a href=\"http://click.link.ft.com/e/syPtm_cKJzysMivQ4ohPBg~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERRaHR0cDovL3d3dy5mdC5jb20vY21zL3MvMC9jMGE5NmFmMi1iYjhiLTExZTUtYmY3ZS04YTMzOWI2ZjIxNjQuaHRtbCNheHp6M3l2aVB2N2NjR_Z7InJlcXVlc3RlZFNlbmRUaW1lIjoiMTQ1NDY1OTQ2MiIsImVtYWlsSWQiOiI1NmIzY2Y3MDg1ZDkyOTAzMDA1YzczMzMiLCJ1c2VyVXVpZCI6IjcyY2FlZmEzLTlhODctNGQwNy1hNjEzLWVkYTE1ZGRiMWI4ZSIsInByb2R1Y3QiOiJFbUUiLCJ0cmFuc2FjdGlvbklkIjoiMHg1NGFjMWQ3OGQ3MDAwMDAwIiwic291cmNlIjoic2ltcGxlLWVtYWlsLXNlcnZpY2UiLCJ0ZW1wbGF0ZUlkIjoiNTY1NWQwMDhjYjU2ZTYwZmM2NDQ3ZTIyIn0~\" target=\"_blank\">he held up a Ã¢â€šÂ¬3bn EU aid package</a> to Turkey. And heÃ¢â‚¬â„¢s been <a href=\"http://click.link.ft.com/e/IDebGSbBS4va3ex_cA9LjA~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERRaHR0cDovL3d3dy5mdC5jb20vY21zL3MvMC84MDEwOWZjYy1hOGUxLTExZTUtOTcwMC0yYjY2OWE1YWViODMuaHRtbCNheHp6M3l2aVB2N2NjR_Z7InRyYW5zYWN0aW9uSWQiOiIweDU0YWMxZDc4ZDcwMDAwMDAiLCJzb3VyY2UiOiJzaW1wbGUtZW1haWwtc2VydmljZSIsImVtYWlsSWQiOiI1NmIzY2Y3MDg1ZDkyOTAzMDA1YzczMzMiLCJwcm9kdWN0IjoiRW1FIiwidGVtcGxhdGVJZCI6IjU2NTVkMDA4Y2I1NmU2MGZjNjQ0N2UyMiIsInJlcXVlc3RlZFNlbmRUaW1lIjoiMTQ1NDY1OTQ2MiIsInVzZXJVdWlkIjoiNzJjYWVmYTMtOWE4Ny00ZDA3LWE2MTMtZWRhMTVkZGIxYjhlIn0~\" target=\"_blank\">blaming new EU rules</a> for his countryÃ¢â‚¬â„¢s mounting banking crisis. But the most critical fight heÃ¢â‚¬â„¢s been waging was on full display yesterday: his attempt <a href=\"http://click.link.ft.com/e/fJBW8T4UY8DMvVjEf0LvhA~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERRaHR0cDovL3d3dy5mdC5jb20vY21zL3MvMC8wOGJhNzhmOC1hODA1LTExZTUtOTU1Yy0xZTFkNmRlOTQ4NzkuaHRtbCNheHp6M3l2aVB2N2NjR_Z7InNvdXJjZSI6InNpbXBsZS1lbWFpbC1zZXJ2aWNlIiwidXNlclV1aWQiOiI3MmNhZWZhMy05YTg3LTRkMDctYTYxMy1lZGExNWRkYjFiOGUiLCJyZXF1ZXN0ZWRTZW5kVGltZSI6IjE0NTQ2NTk0NjIiLCJ0ZW1wbGF0ZUlkIjoiNTY1NWQwMDhjYjU2ZTYwZmM2NDQ3ZTIyIiwidHJhbnNhY3Rpb25JZCI6IjB4NTRhYzFkNzhkNzAwMDAwMCIsInByb2R1Y3QiOiJFbUUiLCJlbWFpbElkIjoiNTZiM2NmNzA4NWQ5MjkwMzAwNWM3MzMzIn0~\" target=\"_blank\">to get more wiggle room</a> for Italy&#8217;s 2016 budget.</p>\n<p>Pierre Moscovici, the European CommissionÃ¢â‚¬â„¢s economic chief, was the man in the firing line this time, since yesterday was his semi-regular appearance to unveil <a href=\"http://click.link.ft.com/e/d0slKk6Cg3C9fW9QCvyZIQ~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAERRaHR0cDovL3d3dy5mdC5jb20vY21zL3MvMC81MWY4MmMxNi1jYjFiLTExZTUtYThlZi1lYTY2ZTk2N2RkNDQuaHRtbCNheHp6M3l2aVB2N2NjR_Z7InRyYW5zYWN0aW9uSWQiOiIweDU0YWMxZDc4ZDcwMDAwMDAiLCJyZXF1ZXN0ZWRTZW5kVGltZSI6IjE0NTQ2NTk0NjIiLCJ0ZW1wbGF0ZUlkIjoiNTY1NWQwMDhjYjU2ZTYwZmM2NDQ3ZTIyIiwic291cmNlIjoic2ltcGxlLWVtYWlsLXNlcnZpY2UiLCJlbWFpbElkIjoiNTZiM2NmNzA4NWQ5MjkwMzAwNWM3MzMzIiwidXNlclV1aWQiOiI3MmNhZWZhMy05YTg3LTRkMDctYTYxMy1lZGExNWRkYjFiOGUiLCJwcm9kdWN0IjoiRW1FIn0~\" target=\"_blank\">the EUÃ¢â‚¬â„¢s latest economic forecasts</a>. In the run-up to Mr MoscoviciÃ¢â‚¬â„¢s announcement, Pier Carlo Padoan, the Italian finance minister, laid down his marker: he wanted a decision quickly that would allow Rome more flexibility <a href=\"http://click.link.ft.com/e/6xiq7uWNarvjc-hk7DWJmw~~/AAAFwAA~/RgRYltyOP0EIAGtZC-iWwHNCCgAB-1e0VpgbhwVSFHBldGVyLnNwaWVnZWxAZnQuY29tCVEEAAAAAESFaHR0cDovL3d3dy5hbnNhLml0L2VuZ2xpc2gvbmV3cy9wb2xpdGljcy8yMDE2LzAyLzAzL3BhZG9hbi1zYXlzLWl0YWx5LWhhcy1mbGV4aWJpbGl0eS1yaWdodF81ZTAzMzliMi02YmE1LTQ4MWUtYTVhZC0xOGY2NTRlYWQ3MDEuaHRtbEf2eyJlbWFpbElkIjoiNTZiM2NmNzA4NWQ5MjkwMzAwNWM3MzMzIiwicmVxdWVzdGVkU2VuZFRpbWUiOiIxNDU0NjU5NDYyIiwidGVtcGxhdGVJZCI6IjU2NTVkMDA4Y2I1NmU2MGZjNjQ0N2UyMiIsInByb2R1Y3QiOiJFbUUiLCJ1c2VyVXVpZCI6IjcyY2FlZmEzLTlhODctNGQwNy1hNjEzLWVkYTE1ZGRiMWI4ZSIsInNvdXJjZSI6InNpbXBsZS1lbWFpbC1zZXJ2aWNlIiwidHJhbnNhY3Rpb25JZCI6IjB4NTRhYzFkNzhkNzAwMDAwMCJ9\" target=\"_blank\">to spend a bit more</a> than EU rules normally allow. But Mr Moscovici was having none of it. Mr Padoan would have to wait until May for a decision, along with every other eurozone minister.</p>\n<p>In what appeared a fit of mild Gallic pique, Mr Moscovici also noted that Ã¢â‚¬Å“Italy is the only country in the EUÃ¢â‚¬Â that had already been given special dispensation under new budget flexibility guidelines Ã¢â‚¬â€œ it is able to miss its structural deficit target by 0.4 per cent in order to implement Brussels-approved economic reforms Ã¢â‚¬â€œ and it was now coming back repeatedly for more.&nbsp;<a href=\"http://blogs.ft.com/brusselsblog/2016/02/05/brussels-briefing-the-italian-job/\" rel=\"65281\" title=\"Continue reading: Brussels Briefing: The Italian job\" class=\"more-link\">Read more</a></p>\n",
              |    "id": 65281,
              |    "main_image": {
              |      "alt": "",
              |      "caption": "",
              |      "description": "Italy's Prime minister Matteo Renzi arrives before a meeting with his Irish counterpart Enda Kenny on July 10, 2015 at the Chigi Palace in Rome. AFP PHOTO / ALBERTO PIZZOLI        (Photo credit should read ALBERTO PIZZOLI/AFP/Getty Images)",
              |      "id": 65325,
              |      "image_source": "",
              |      "images": {
              |        "full": {
              |            "height": 3840,
              |            "url": "http://test.www.ft.com/fastft/files/2016/10/230_fractal.jpg",
              |            "width": 2160
              |        },
              |        "large": {
              |          "height": 331,
              |          "url": "http://blogs.ft.com/brusselsblog/files/2016/02/New-story-of-MAS_PM9843918@ITALY-IRELAND-REN_20160118171546_2-590x331.jpg",
              |          "width": 590
              |        },
              |        "medium": {
              |          "height": 153,
              |          "url": "http://blogs.ft.com/brusselsblog/files/2016/02/New-story-of-MAS_PM9843918@ITALY-IRELAND-REN_20160118171546_2-272x153.jpg",
              |          "width": 272
              |        },
              |        "participant-headshot": {
              |          "height": 45,
              |          "url": "http://blogs.ft.com/brusselsblog/files/2016/02/New-story-of-MAS_PM9843918@ITALY-IRELAND-REN_20160118171546_2-35x45.jpg",
              |          "width": 35
              |        },
              |        "thumbnail": {
              |          "height": 45,
              |          "url": "http://blogs.ft.com/brusselsblog/files/2016/02/New-story-of-MAS_PM9843918@ITALY-IRELAND-REN_20160118171546_2-35x45.jpg",
              |          "width": 35
              |        },
              |        "top-story": {
              |          "height": 96,
              |          "url": "http://blogs.ft.com/brusselsblog/files/2016/02/New-story-of-MAS_PM9843918@ITALY-IRELAND-REN_20160118171546_2-167x96.jpg",
              |          "width": 167
              |        }
              |      },
              |      "mime_type": "image/jpeg",
              |      "parent": 65281,
              |      "slug": "italy-ireland-renzi-kenny",
              |      "title": "Italy's Prime minister Matteo Renzi",
              |      "url": "http://test.www.ft.com/fastft/files/2016/10/230_fractal.jpg"
              |    },
              |    "modified": "${modified}",
              |    "modified_gmt": "${modified}",
              |    "slug": "brussels-briefing-the-italian-job",
              |    "status": "publish",
              |    "tags": [
              |      {
              |        "description": "",
              |        "id": 132,
              |        "post_count": 43,
              |        "slug": "italy",
              |        "title": "Italy"
              |      },
              |      {
              |        "description": "",
              |        "id": 29032,
              |        "post_count": 2,
              |        "slug": "matteo-renzi",
              |        "title": "Matteo Renzi"
              |      }
              |    ],
              |    "taxonomy_author": [
              |      {
              |        "description": "",
              |        "id": 29074,
              |        "post_count": 359,
              |        "slug": "peterspiegel",
              |        "title": "peterspiegel"
              |      }
              |    ],
              |    "thumbnail": null,
              |    "title": "Brussels Briefing: The Italian job",
              |    "title_plain": "Brussels Briefing: The Italian job",
              |    "type": "post",
              |    "url": "http://blogs.ft.com/brusselsblog/2016/02/05/brussels-briefing-the-italian-job/",
              |    "uuid": "49ef75ec-9213-36c8-92f5-f5d9e80edabc"
              |  },
              |  "previous_url": "http://blogs.ft.com/brusselsblog/2016/02/04/brussels-briefing-ireland-and-the-bailout-jinx/",
              |  "status": "ok"
              |}
            """.stripMargin)).asJSON
          .check(status is 200)
      )
  }

  def getIsoDate: String = {
    formatter.print(DateTime.now())
  }
}

class ImageTransformerSimulation extends Simulation {
  val numUsers = Integer.getInteger("users", DefaultNumUsers)
  val rampUp = Integer.getInteger("ramp-up-minutes", DefaultRampUpDurationInMinutes)

  setUp(
    ImageTransformerSimulation.Scenario.inject(rampUsers(numUsers) over (rampUp minutes))
  ).protocols(ImageTransformerSimulation.HttpConf)

}