# API Load Test

## What is this project about?

This is a simple Maven project to produce predictable load on our API's micro-services.

## Motivation

* Simple load tests that capture the behaviour of our endpoints
* Easy to configure load tests so different load scenarios can be played
* Straight forward results that show us how our endpoints behaved

## Why use Gatling?

We used Gatling as it allowed us write minimal amount of code to produce and validate load against our API endpoint. It also produces easy to understand reports on the events that take place during a load test. 

JMeter was the other option but it was ruled out due to the overhead (and somewhat complexity of using JMeter) of running and writing scenarios. It is a viable alternative and no reason why if people want, the simple scenarios created can be translated into a JMeter load test. The scenarios after all are very simple as they only hit a handfull endpoints.

## Running load tests

The basic way to run these tests is via the command line, using Maven:

```bash
$ mvn gatling:execute -Dgatling.simulationClass=organisation.TransformerSimulation -Dusers=10 -Dramp-up-minutes=2 -Dsoak-duration-minutes=6
```

Where:

* __users__ is the number of concurrent users the load test should run with
* __ramp-up-minutes__ is the duration (in minutes) the load test should ramp up, i.e. how long it should take to get to the total user count specified
* __soak-duration-minutes__ is the duration (in minutes) each "user" should run for
* __gatling.simulationClass__ is the name of the class (full package path) that represents the "simulation" you wish to run

So in the above case, the load test being run is the class "organisation.TransformerSimulation". It will use a maximum of 10 users. We've also specified a ramp up time of 2 minutes. This is the duration over which the users will be linearly started during the test. Each user will then run through the scenario for 6 minutes. With the above config the load test will run at "full load" for only 4 minutes before it begins to ramp down. If the soak duration is less than the ramp up time then the test will never run at full load.

Most of the load test scenarios follow the above pattern. There are times though that the scenario can take in extra parameters. When running the large full scale load tests (the ones that hit multiple microservices) you'll be able to configure the users for each scenario. For those it is best to look at the scenarios to see what can be configured.

The available simulations are:

1. FullLoadSimulation
2. membership.FullSimulation
3. membership.ReadSimulation
4. membership.TransformerSimulation
5. membership.WriteSimulation
6. organisation.FullSimulation
7. organisation.ReadSimulation
8. organisation.TransformerSimulation
9. organisation.WriteSimulation
10. people.FullSimulation
11. people.ReadSimulation
12. people.TransformerSimulation
13. people.WriteSimulation
14. enrichedContent.ReadSimulation
15. generic.ReadSimulation
16. sixdegrees.ReadSimulation
17. publicannotations.ReadSimulation

If you want to run the tests from your IDE, then all you need to do is run the class Engine. This will create a prompt menu giving you the option to select a gatling test to run. Doing it this way will mean you run with all the default values (unless you run it with the JVM arguments to override them).

## Jenkins Jobs

Enriched-content-api:
* http://ftjen10085-lvpr-uk-p:8181/view/API-load-test/job/enriched-content-api-load-test-ucs-gateway/
* http://ftjen10085-lvpr-uk-p:8181/view/API-load-test/job/enriched-content-api-load-test-coco/
* http://ftjen10085-lvpr-uk-p:8181/view/API-load-test/job/enriched-content-api-load-test-ucs/

Public-six-degrees-api:
* http://ftjen10085-lvpr-uk-p:8181/view/API-load-test/job/six-degrees-load-test/

public-annotations-load-trst
* http://ftjen10085-lvpr-uk-p:8181/view/API-load-test/job/public-annotations-api-load-test

## Generate valid Uuids

Run the generate scripts to retrieve a valid list of uuids for transformation.
