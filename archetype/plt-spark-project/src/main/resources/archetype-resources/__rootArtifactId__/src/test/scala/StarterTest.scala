package ${groupId}

import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.FlatSpec

object StarterSpec extends FlatSpec {
  private val sc = new SparkContext(new SparkConf().setMaster("local[2]").setAppName(this.getClass.getCanonicalName).set("spark.ui.enabled", "false"))
  //  http://www.scalatest.org/user_guide/writing_your_first_test

}