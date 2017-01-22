package oriana.liquibase

import oriana._
import java.util.UUID

import com.typesafe.config.ConfigFactory
import oriana.TableAccess.SimpleTableAccess
import oriana.{SimpleDatabaseContext, TableAccess}
import slick.driver.{H2Driver, JdbcProfile}

class TestContext(db: String = UUID.randomUUID().toString) extends SimpleDatabaseContext(H2Driver, ConfigFactory.defaultApplication()) {
  import api._

  override protected def connectToDatabase() = H2Driver.backend.Database.forURL(s"jdbc:h2:mem:$db;DB_CLOSE_DELAY=-1").asInstanceOf[driver.backend.Database]

  class UserTable(tag: Tag) extends Table[(Option[Int], String, String, String)](tag, "PERSON") {
    def id = column[Option[Int]]("ID", O.PrimaryKey, O.AutoInc)
    def firstname = column[String]("FIRSTNAME", O.SqlType("VARCHAR(50)"))
    def lastname = column[String]("LASTNAME", O.SqlType("VARCHAR(50)"))
    def state = column[String]("STATE", O.SqlType("CHAR(2)"))

    def * = (id, firstname, lastname, state)
  }

  val users = TableQuery[UserTable]

  override def allTables: List[TableAccess[_]] = List(users)

}
