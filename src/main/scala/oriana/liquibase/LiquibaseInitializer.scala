package oriana.liquibase

import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.logging.LogFactory
import liquibase.resource.{ClassLoaderResourceAccessor, ResourceAccessor}
import org.slf4j.LoggerFactory
import oriana.DatabaseActor.InitComplete
import oriana.{DatabaseCommandExecution, DatabaseContext}

import scala.concurrent.{ExecutionContext, Future}

class LiquibaseInitializer(resourceName: String = "db-changelog.xml")(implicit ec: ExecutionContext) extends oriana.DBInitializer[DatabaseContext with DatabaseCommandExecution] {
  private val logger = LoggerFactory.getLogger(classOf[LiquibaseInitializer])

  override def apply(ctx: DatabaseContext with DatabaseCommandExecution): Future[InitComplete.type] = {
    LogFactory.setInstance(SLF4JLoggingBridge)

    Future {
      val connection = ctx.database.source.createConnection()
      try {
        val liquibase = new Liquibase(resourceName,
          resourceAccessor,
          new JdbcConnection(connection))

        liquibase.update("")

        InitComplete
      } finally {
        connection.close()
      }
    }
  }

  protected def resourceAccessor: ResourceAccessor = new ClassLoaderResourceAccessor(classOf[LiquibaseInitializer].getClassLoader)

}
