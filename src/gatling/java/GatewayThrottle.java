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
                  .header("Authorization","Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJvTXlRTHB6TXJmWlJ2SkxXbGxZdXRuS1NHbFlHNE5fZ0J0SkIweHpLdHBBIn0.eyJleHAiOjE2ODE4NjAyMDMsImlhdCI6MTY4MTc3NzQwMywianRpIjoiNjBjZmRmNTYtNDMzYy00NGQ2LTk3ZTktZmNiZTQ4NTNhMzc5IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4NDQzL3JlYWxtcy9iYW5rX3JlYWxtIiwiYXVkIjpbIm9hdXRoMi1yZXNvdXJjZSIsImFjY291bnQiXSwic3ViIjoiNzY5NzE3MDQtYjdkZC00OGY2LTkyNTEtY2U0ODI1NzAzODg2IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiamhvbl9kb2UiLCJzZXNzaW9uX3N0YXRlIjoiZTRlOWFjNjctMDEzNS00NDBkLWI2M2QtNTM3M2RmNWE4MWYwIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIvKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1iYW5rX3JlYWxtIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicHJvZmlsZSByZWdpc3RyYXRpb24gYmFzaWMgYmFzaWNfcmVhZCBlbWFpbCIsInNpZCI6ImU0ZTlhYzY3LTAxMzUtNDQwZC1iNjNkLTUzNzNkZjVhODFmMCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6IkpvaG4gRG9lIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiam9obmRvZSIsImdpdmVuX25hbWUiOiJKb2huIiwiZmFtaWx5X25hbWUiOiJEb2UifQ.K1dPeqKh_xlfDIDWfRpoUS7nY8TzoP5YgEruVeApE0QclWbZp63khUDOtTGWgeCD2lejBgy5fm9KN6N7ZcOQnSrIsvCy8hSOluIFWRTJ-laT3XgHBt5GR3kRe69mbc_-YMxY5oIVwDhg8dDGtpxmNLZlGTSRmmLXEeS1BA6RFyDlBdSCsjqng9urFyEZCTeG8G69d7Cs5XuNDVkM0N8iiQ5cM0l-_YZgZry6pBLWlRh3yHr9aQbbkjuTvlX-B1amSey4soZ41gCV-bp90rRnmI5-UhxbsDywZP0fa7-oLdY2S4z1kfYoTHRXJK7bntdMFl-bbJpZgz8WhLKjlOFGJg"))
            .exec(http("slowRequest")
                  .get("/slow-service/v0")
                  .header("Authorization","Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJvTXlRTHB6TXJmWlJ2SkxXbGxZdXRuS1NHbFlHNE5fZ0J0SkIweHpLdHBBIn0.eyJleHAiOjE2ODE4NjAyMDMsImlhdCI6MTY4MTc3NzQwMywianRpIjoiNjBjZmRmNTYtNDMzYy00NGQ2LTk3ZTktZmNiZTQ4NTNhMzc5IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4NDQzL3JlYWxtcy9iYW5rX3JlYWxtIiwiYXVkIjpbIm9hdXRoMi1yZXNvdXJjZSIsImFjY291bnQiXSwic3ViIjoiNzY5NzE3MDQtYjdkZC00OGY2LTkyNTEtY2U0ODI1NzAzODg2IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiamhvbl9kb2UiLCJzZXNzaW9uX3N0YXRlIjoiZTRlOWFjNjctMDEzNS00NDBkLWI2M2QtNTM3M2RmNWE4MWYwIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIvKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1iYW5rX3JlYWxtIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicHJvZmlsZSByZWdpc3RyYXRpb24gYmFzaWMgYmFzaWNfcmVhZCBlbWFpbCIsInNpZCI6ImU0ZTlhYzY3LTAxMzUtNDQwZC1iNjNkLTUzNzNkZjVhODFmMCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6IkpvaG4gRG9lIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiam9obmRvZSIsImdpdmVuX25hbWUiOiJKb2huIiwiZmFtaWx5X25hbWUiOiJEb2UifQ.K1dPeqKh_xlfDIDWfRpoUS7nY8TzoP5YgEruVeApE0QclWbZp63khUDOtTGWgeCD2lejBgy5fm9KN6N7ZcOQnSrIsvCy8hSOluIFWRTJ-laT3XgHBt5GR3kRe69mbc_-YMxY5oIVwDhg8dDGtpxmNLZlGTSRmmLXEeS1BA6RFyDlBdSCsjqng9urFyEZCTeG8G69d7Cs5XuNDVkM0N8iiQ5cM0l-_YZgZry6pBLWlRh3yHr9aQbbkjuTvlX-B1amSey4soZ41gCV-bp90rRnmI5-UhxbsDywZP0fa7-oLdY2S4z1kfYoTHRXJK7bntdMFl-bbJpZgz8WhLKjlOFGJg"));


  {
    setUp(scn.injectOpen(constantUsersPerSec(80).during(10)).protocols(httpProtocol));
  }
}
