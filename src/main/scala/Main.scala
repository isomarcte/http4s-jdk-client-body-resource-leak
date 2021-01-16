
import cats.data._
import cats.effect._
import cats.effect.concurrent._
import cats.syntax.all._
import fs2._
import org.http4s._
import org.http4s.client._
import org.http4s.client.jdkhttpclient._
import org.http4s.ember.server._
import org.http4s.syntax.all._

object Main extends IOApp {

  val app: HttpApp[IO] =
    Kleisli((_: Request[IO]) => IO.pure(Response[IO]().withEntity("Hello, HTTP")))

  def runRequest(client: Client[IO], counter: Ref[IO, Long]): IO[Unit] =
    client.status(
      Request[IO](method = Method.GET, uri = uri"http://127.0.0.1:8080")
    ) *> counter.updateAndGet(_ + 1L).flatMap(value =>
      if (value % 1000L === 0L) {
        IO(println(s"Request count: ${value}"))
      } else {
        IO.unit
      }
    )

  def runRequestsForever(client: Client[IO], counter: Ref[IO, Long]): Stream[IO, Unit] =
    Stream.repeatEval(
      runRequest(client, counter)
    )

  override def run(args: List[String]): IO[ExitCode] = {
    val builder: EmberServerBuilder[IO] =
      EmberServerBuilder.default[IO].withHttpApp(
        app
      ).withHost(
        "127.0.0.1"
      ).withPort(
        8080
      )

    (for {
      server <- Stream.resource(builder.build)
      client <- Stream.eval(JdkHttpClient.simple[IO])
      counter <- Stream.eval(Ref.of[IO, Long](0L))
      _ <- runRequestsForever(client, counter)
    } yield ExitCode.Success).drain.compile.lastOrError
  }
}
