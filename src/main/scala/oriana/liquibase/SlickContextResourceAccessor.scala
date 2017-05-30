package oriana.liquibase

import liquibase.resource.ResourceAccessor
import oriana.ExecutableDatabaseContext

trait SlickContextResourceAccessor { _: ResourceAccessor =>
  def context: ExecutableDatabaseContext
}
