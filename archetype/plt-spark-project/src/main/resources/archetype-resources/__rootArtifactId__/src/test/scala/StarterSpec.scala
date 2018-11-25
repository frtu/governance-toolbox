package ${groupId}

import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.FlatSpec
import scala.io.Source
import java.io.{File => JFile}

class StarterSpec extends FlatSpec {
  private val sc = new SparkContext(new SparkConf().setMaster("local[2]").setAppName(this.getClass.getCanonicalName).set("spark.ui.enabled", "false"))
  //  http://www.scalatest.org/user_guide/writing_your_first_test

  "main" should "return find the file with one line" in {
    val lines = Source.fromFile(new JFile("src/test/resources/simple.txt")).getLines()
    assert(lines.length == 1)
  }
}