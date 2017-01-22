package oriana.liquibase

import akka.actor.{ActorSystem, PoisonPill}
import akka.util.Timeout
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import oriana._
import org.scalatest.concurrent.PatienceConfiguration.{Timeout => TestTimeout}
import org.scalatest.time.{Minute, Span}

import scala.concurrent.duration._

class LiqibaseInitilizerSpec extends FlatSpec with Matchers with BeforeAndAfterAll with ScalaFutures {
  implicit val actorSystem = ActorSystem("test")
  implicit val timeout = Timeout(1.minute - 5.seconds)
  import actorSystem.dispatcher

  "The liquibase initializer" should "correctly set up a database with its default settings" in {
    val db = actorSystem.actorOf(DatabaseActor.props(new TestContext with DatabaseCommandExecution), "testdb1")
    implicit val name = DatabaseName(db.path)
    db ! new LiquibaseInitializer()
    db ! NoRetrySchedule
    db ! DatabaseActor.Init

    val result = executeDBTransaction { ctx: TestContext =>
      import ctx.api._
      val insert = ctx.users returning ctx.users.map(_.id) into ((user, id) => (id, user._2, user._3, user._4))
      insert += (None, "Andreas", "Schweiner", "DC")
    }

    whenReady(result, TestTimeout(Span(1, Minute))) { user =>
    val (id, first, last, state) = user

      id should not be empty
      first shouldEqual "Andreas"
      last shouldEqual "Schweiner"
      state shouldEqual "DC"
    }
  }

  it should "be able to upgrade a db version" in {
    val db0 = actorSystem.actorOf(DatabaseActor.props(new TestContext with DatabaseCommandExecution), "testdb2")
    db0 ! new LiquibaseInitializer()
    db0 ! NoRetrySchedule
    db0 ! DatabaseActor.Init

    val db1 = actorSystem.actorOf(DatabaseActor.props(new TestContext with DatabaseCommandExecution), "testdb3")
    db1 ! new LiquibaseInitializer("db-changelog-2.xml")
    db1 ! NoRetrySchedule
    db1 ! DatabaseActor.Init


    val result = executeDBTransaction { ctx: ExtTestContext =>
      import ctx.api._
      val insert = ctx.users returning ctx.users.map(_.id) into ((_, id) => id)
      insert += (None, "Alexander", "Schweiner", "DC", "asch")
    }


    whenReady(result, TestTimeout(Span(1, Minute))) { id =>
      id should not be empty
    }
  }

  override protected def afterAll(): Unit = actorSystem.terminate()
}
