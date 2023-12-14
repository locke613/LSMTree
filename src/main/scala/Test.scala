import java.nio.file.{Files, Paths}

object Test {
  def main(args: Array[String]): Unit = {
    val memTable = MemTable()
    val sstables = List.empty[SSTable]
    val basePath = Paths.get(".").toAbsolutePath.normalize.toString // Sets base path to current folder

    val lsmTree = LSMTree(memTable, sstables, basePath)

    // Example data insertion
    lsmTree.put("key1", "value1")
    lsmTree.put("key2", "value2")
    lsmTree.delete("key2")

    println(lsmTree.get("key1")) // Output: Some(value1)
    println(lsmTree.get("key2")) // Output: None
  }
}
