package com.zjlp.face.spark.test

import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by root on 6/6/16.
 */
class TestSpark {
  def test = {
    val conf = new SparkConf().setAppName(" ").setMaster("local[4]")
    val sc = new SparkContext(conf)
    sc.makeRDD(Array(1,2,3)).collect().foreach(println)
  }
}
object TestSpark {
  def main(args: Array[String]) {
    val ts = new TestSpark()
    ts.test
  }
}