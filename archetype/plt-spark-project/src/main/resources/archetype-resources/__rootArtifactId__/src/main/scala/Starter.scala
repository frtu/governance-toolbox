package ${groupId}

import java.io.FileNotFoundException

import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{SQLContext, SaveMode, SparkSession}
import org.apache.spark.sql.functions._
 
object Starter {
  def main(args: Array[String]): Unit = {
    val appName = "${artifactId}"
    val sparkConf = new SparkConf().setAppName(appName)

    // MODE LOCAL or YARN
    if (args.isEmpty) sparkConf.setMaster("local[*]")

    // INPUT FILE : Put your test data file here (CSV, ...)
    // => You can get some sample data at : https://support.spatialkey.com/spatialkey-sample-csv-data/
    var data = if (!args.isEmpty) args(0) else "src/test/resources/data"

    val sc: SparkContext = new SparkContext(sparkConf)
    val spark = SparkSession.builder()
      .appName(appName)
      // https://spark.apache.org/docs/2.3.0/api/java/org/apache/spark/sql/SparkSession.Builder.html#enableHiveSupport()
      //      .enableHiveSupport()
      .getOrCreate()

    println(spark.conf.get("spark.sql.catalogImplementation"))

    //======================
    // Read file
    //======================
    //    val df = spark.read.
    //      //      option("header", "true").
    //      option("inferSchema", "true").
    //      option("delimiter", "~").
    ////      csv(data + "csv/file.csv")
    //      json(data + "json/file.json")
    //
    //    df.printSchema()
    //    df.show(10)

    //======================
    // Write Table & Query
    //======================
    //    val tableName = "mytable"
    //
    //    df.write.mode(SaveMode.Overwrite).saveAsTable(tableName)
    //    spark.sql("SELECT * FROM " + tableName).show(10)

    //======================
    // Read folder
    //======================
    val dataPath = new Path(data)

    val system = FileSystem.get(sc.hadoopConfiguration)
    try {
    val files: Seq[String] = for {
      folder <- system.listStatus(dataPath);
      file <- system.listStatus(folder.getPath)
    } yield file.getPath.toString

    println("Scanning folder " + dataPath.toUri + " files founds : " + files.size)


    // Use RDD to read file
    // https://spark.apache.org/docs/latest/rdd-programming-guide.html#external-datasets
    files.foreach(sourceFile => {
      println("= FILE PATH:" + sourceFile)
      val textFile = sc.textFile(sourceFile)
      textFile.foreach(line => println("==" + line))
    })
    } catch {
      case ex: FileNotFoundException => {
        println("Folder '" + dataPath.toUri + "' not found ! Please check you're running the application in the RIGHT location !")
        ex.printStackTrace()
      }
    }

    sc.stop
  }
}