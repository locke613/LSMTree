case class LSMTree(memTable: MemTable, sstables: List[SSTable], basePath: String) {
  val maxDataSize: Long = 10 * 1024 * 1024 // 10 MB in bytes

  def put(key: String, value: String): Unit = {
    memTable.put(key, value)
    val memTableSize = memTable.table.map { case (k, v) =>
      k.getBytes("UTF-8").length + v.map(_.getBytes("UTF-8").length).getOrElse(0)
    }.sum

    if (memTableSize + totalSSTableSize() >= maxDataSize) {
      mergeMemTable()
    }
  }

  def get(key: String): Option[String] = {
    memTable.get(key) match {
      case Some(value) => Some(value)
      case None =>
        sstables.reverse.flatMap(_.data.get(key)).headOption
    }
  }

  private def mergeMemTable(): Unit = {
    val newData = memTable.table.collect { case (k, Some(v)) => k -> v }.toMap
    val filePath = s"$basePath/SSTable_${System.currentTimeMillis()}.txt"
    val newSSTable = SSTable(newData, filePath)
    newSSTable.saveToDisk()
    val updatedSSTables = sstables :+ newSSTable
    memTable.table.clear()
    // Reassign the updated list of SSTables
    this.copy(sstables = updatedSSTables)
  }

  private def totalSSTableSize(): Long = {
    sstables.map(_.size).sum
  }

  def delete(key: String): Unit = {
    memTable.delete(key)
  }
}
