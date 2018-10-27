package ${groupId}

import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.{SparkConf, SparkContext}

object Starter {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("${artifactId}")

    // MODE LOCAL or YARN
    if (args.isEmpty) sparkConf.setMaster("local[*]")

    // INPUT FILE : Put your test data file here (CSV, ...)
    // => You can get some sample data at : https://support.spatialkey.com/spatialkey-sample-csv-data/
    var data = if (!args.isEmpty) args(0) else "${artifactId}/src/test/resources/"

    val sc: SparkContext = new SparkContext(sparkConf)
    val system = FileSystem.get(sc.hadoopConfiguration)

    val files: Seq[String] = for {
      folder <- system.listStatus(new Path(data));
      file <- system.listStatus(folder.getPath)
    } yield file.getPath.toString

    files.foreach(sourceFile => {
      println("PATH:" + sourceFile)
    })

    sc.stop
  }
}