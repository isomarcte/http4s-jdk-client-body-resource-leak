Demonstrates a resource leak in the response body handling of the [http4s-jdk-client][http4s-jdk-client] present in version <= 0.3.3. This issue is _particularly_ difficult to demonstrate in a unit test, which is why I wrote this toy project.

It stands up a http4s ember server and then using the JDK client to repeated run `client.status` against it. It prints out the number of requests sent every 1000 requests.

When I give the JVM ~200MiB of heap space it starts failing due to out of memory errors between 6000 and 7000 requests, which takes about 15 seconds on my computer.

To run,
```
sbt clean compile
sbt -J-Xmx200M run
```

Or just run `./run-leak-test.sh` which does the exact same thing.

[http4s-jdk-client]: https://github.com/http4s/http4s-jdk-http-client "http4s JDK Client"
