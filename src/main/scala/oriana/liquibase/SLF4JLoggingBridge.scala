package oriana.liquibase

import liquibase.logging.LogFactory

object SLF4JLoggingBridge extends LogFactory {
  override def getLog(name: String) = new SLF4JLogAdapter(s"liquibase.$name")
}
