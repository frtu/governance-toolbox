package ${groupId}

import java.io.FileNotFoundException
import java.util.concurrent.TimeUnit

import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{SQLContext, SaveMode, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.types.{FloatType, LongType, StringType, StructType}

object StarterStreaming {
  def main(args: Array[String]): Unit = {
    val appName = "${artifactId}"
    val sparkConf = new SparkConf().setAppName(appName)

    // MODE LOCAL or YARN
    if (args.isEmpty) sparkConf.setMaster("local[*]")

    // INPUT Kafka
    val kafkaTopicName = ???

    var inputData = if (!args.isEmpty) args(0) else "localhost:9092"
    var inputTopic = if (args.length > 1) args(1) else kafkaTopicName

    val sc: SparkContext = new SparkContext(sparkConf)
    val spark = SparkSession.builder()
      .appName(appName)
      .getOrCreate()

    //------------------------------------
    // https://spark.apache.org/docs/latest/structured-streaming-kafka-integration.html
    //------------------------------------
    // Register all serdes & type cast
    import spark.implicits._
    val schema = new StructType()
      .add("id", StringType)
      .add("name", StringType)
      .add("value", FloatType)
      .add("eventTime", LongType)
    //------------------------------------
    val df = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", inputData)
      .option("subscribe", inputTopic)
      .load()
      .select(from_json(col("value").cast("string"), schema) as 'data, $"*")
      .select($"data.id", $"data.name", $"data.value",
        from_unixtime(col("data.eventTime")/1000,"yyyy-MM-dd'T'HH:mm:ssZ").cast("timestamp")
          as "event_time",
        $"*"
      )

    df.printSchema();

    val job = df
      .writeStream
      .format("console")
      .start()

    job.awaitTermination()
  }
}