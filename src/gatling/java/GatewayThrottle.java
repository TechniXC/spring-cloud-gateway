/*
 * Copyright 2011-2022 GatlingCorp (https://gatling.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

public class GatewayThrottle extends Simulation {

  HttpProtocolBuilder httpProtocol =
      http
          // Here is the root for all relative URLs
          .baseUrl("http://localhost:8082")
          // Here are the common headers
          .acceptHeader("*/*")
          .acceptEncodingHeader("gzip, deflate, br")
          .userAgentHeader(
              "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0");

  // A scenario is a chain of requests and pauses
  ScenarioBuilder scn = scenario("Scenario Name")
            .exec(http("fastRequest")
                  .get("/fast-service/")
                  .header("x-client-version", "0.0.1")
                  .header("Authorization","Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJvTXlRTHB6TXJmWlJ2SkxXbGxZdXRuS1NHbFlHNE5fZ0J0SkIweHpLdHBBIn0.eyJleHAiOjE2ODE4NTIyNjEsImlhdCI6MTY4MTc2OTQ2MSwianRpIjoiZTJjYmU1NTMtMWEyZi00NTM4LWI2N2MtZTczNGMzMzVlYzE1IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4NDQzL3JlYWxtcy9iYW5rX3JlYWxtIiwiYXVkIjpbIm9hdXRoMi1yZXNvdXJjZSIsImFjY291bnQiXSwic3ViIjoiNzY5NzE3MDQtYjdkZC00OGY2LTkyNTEtY2U0ODI1NzAzODg2IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiamhvbl9kb2UiLCJzZXNzaW9uX3N0YXRlIjoiYTEyMGRiYTEtNDUxZC00ZmFiLThmZGUtNTYxZGVlNjRiMDM4IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIvKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1iYW5rX3JlYWxtIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicHJvZmlsZSByZWdpc3RyYXRpb24gYmFzaWMgYmFzaWNfcmVhZCBlbWFpbCIsInNpZCI6ImExMjBkYmExLTQ1MWQtNGZhYi04ZmRlLTU2MWRlZTY0YjAzOCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6IkpvaG4gRG9lIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiam9obmRvZSIsImdpdmVuX25hbWUiOiJKb2huIiwiZmFtaWx5X25hbWUiOiJEb2UifQ.B9eKlPB7VUuY4djDQEqgYE5NuFOb8ayvFB-CazSBqaopeOVm50Emn59g953OFKoczP_kURCyrqUgT6Q5rdoA6rMGxfq25mdEtjEfp7NaM1hZFhsnsbKvDqloCyoWG9vCf0aMrRGw0pdUJxoWiLvkxOON7RE_ouaZ1fi8dDfiCk0IQxDq91wajOzR1-cZFR_cECpl28exXs9FX1sFVJiGvsu3M2hBsQoQpYiiE8gWpiXnDPELm7jqYDx4SIVBxqpxqmWXKEgKJ22_aNw2yo4wtU_RYLTLRD7L0jAwNRz85KEcwra1veJNJ1uWDR_Y-q7C5jgvuwMf4ow4uMXyeJBvmw"))
            .exec(http("slowRequest")
                  .get("/slow-service/v0")
                  .header("Authorization","Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJvTXlRTHB6TXJmWlJ2SkxXbGxZdXRuS1NHbFlHNE5fZ0J0SkIweHpLdHBBIn0.eyJleHAiOjE2ODE4NTIyNjEsImlhdCI6MTY4MTc2OTQ2MSwianRpIjoiZTJjYmU1NTMtMWEyZi00NTM4LWI2N2MtZTczNGMzMzVlYzE1IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4NDQzL3JlYWxtcy9iYW5rX3JlYWxtIiwiYXVkIjpbIm9hdXRoMi1yZXNvdXJjZSIsImFjY291bnQiXSwic3ViIjoiNzY5NzE3MDQtYjdkZC00OGY2LTkyNTEtY2U0ODI1NzAzODg2IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiamhvbl9kb2UiLCJzZXNzaW9uX3N0YXRlIjoiYTEyMGRiYTEtNDUxZC00ZmFiLThmZGUtNTYxZGVlNjRiMDM4IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIvKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1iYW5rX3JlYWxtIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicHJvZmlsZSByZWdpc3RyYXRpb24gYmFzaWMgYmFzaWNfcmVhZCBlbWFpbCIsInNpZCI6ImExMjBkYmExLTQ1MWQtNGZhYi04ZmRlLTU2MWRlZTY0YjAzOCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6IkpvaG4gRG9lIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiam9obmRvZSIsImdpdmVuX25hbWUiOiJKb2huIiwiZmFtaWx5X25hbWUiOiJEb2UifQ.B9eKlPB7VUuY4djDQEqgYE5NuFOb8ayvFB-CazSBqaopeOVm50Emn59g953OFKoczP_kURCyrqUgT6Q5rdoA6rMGxfq25mdEtjEfp7NaM1hZFhsnsbKvDqloCyoWG9vCf0aMrRGw0pdUJxoWiLvkxOON7RE_ouaZ1fi8dDfiCk0IQxDq91wajOzR1-cZFR_cECpl28exXs9FX1sFVJiGvsu3M2hBsQoQpYiiE8gWpiXnDPELm7jqYDx4SIVBxqpxqmWXKEgKJ22_aNw2yo4wtU_RYLTLRD7L0jAwNRz85KEcwra1veJNJ1uWDR_Y-q7C5jgvuwMf4ow4uMXyeJBvmw"));


  {
    setUp(scn.injectOpen(constantUsersPerSec(80).during(10)).protocols(httpProtocol));
  }
}
