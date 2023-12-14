import scala.collection.mutable

// Define a class for the MemTable
case class MemTable() {
  val table: mutable.Map[String, Option[String]] = mutable.Map[String, Option[String]]()

  def put(key: String, value: String): Unit = {
    table.put(key, Some(value))
  }

  def get(key: String): Option[String] = {
    table.get(key).flatten
  }

  def delete(key: String): Unit = {
    table.put(key, None)
  }
}