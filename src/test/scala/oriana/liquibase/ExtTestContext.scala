package oriana.liquibase

import java.util.UUID

import com.typesafe.config.ConfigFactory
import oriana.TableAccess.SimpleTableAccess
import oriana.{SimpleDatabaseContext, TableAccess}
import slick.driver.H2Driver

class ExtTestContext(db: String = UUID.randomUUID().toString) extends SimpleDatabaseContext(H2Driver, ConfigFactory.defaultApplication()) {
  override implicit val driver: H2Driver.type = H2Driver

  import api._

  override protected def connectToDatabase() = H2Driver.backend.Database.forURL(s"jdbc:h2:mem:$db")

  class UserTable(tag: Tag) extends Table[(Option[Int], String, String, String, String)](tag, "person") {
    def id = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)
    def firstname = column[String]("firstname", O.SqlType("VARCHAR(50)"))
    def lastname = column[String]("lastname", O.SqlType("VARCHAR(50)"))
    def state = column[String]("state", O.SqlType("CHAR(2)"))
    def username = column[String]("username", O.SqlType("VARCHAR(8)"))

    def * = (id, firstname, lastname, state, username)
  }


  val users = TableQuery[UserTable]

  override def allTables: List[TableAccess[_]] = {
    List(new SimpleTableAccess(users)(driver))
  }
}
