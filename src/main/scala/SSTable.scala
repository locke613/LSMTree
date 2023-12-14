import java.io.{File, PrintWriter}

/**
 * SSTable is a file containing a sorted collection of key-value pairs.
 */
case class SSTable(data: Map[String, String], filePath: String) {
  /**
   * Saves the SSTable to disk.
   */
  def saveToDisk(): Unit = {
    val file = new File(filePath)
    val writer = new PrintWriter(file)
    try {
      data.foreach { case (key, value) =>
        writer.println(s"$key:$value")
      }
    } finally {
      writer.close()
    }
  }

  /**
   * Gets the size of the SSTable in bytes.
   */
  def size: Long = {
    data.map { case (key, value) =>
      key.getBytes("UTF-8").length + value.getBytes("UTF-8").length
    }.sum
  }
}