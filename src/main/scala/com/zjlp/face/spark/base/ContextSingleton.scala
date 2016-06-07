package com.zjlp.face.spark.base

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{Logging, SparkConf, SparkContext}

object SQLContextSingleton {
  private var instance: SQLContext = _
  def getInstance(sparkContext: SparkContext = SparkContextSingleton.getInstance): SQLContext = {
    if (instance == null) {
      instance = new SQLContext(sparkContext)
    }
    instance
  }
}

object SparkContextSingleton {
  private var instance: SparkContext = _
  def getInstance = {
    if (instance == null) {
      val conf = new SparkConf().setAppName("").setMaster("local[10]")
      conf.set("spark.sql.shuffle.partitions","48")
      conf.set("spark.executor.memory","8g")
      conf.set("spark.executor.cores","2")
      conf.set("spark.cores.max","9")
      conf.set("spark.speculation","true")
      conf.set("spark.driver.memory","4g")
      conf.set("spark.driver.cores","2")
      conf.set("spark.table.numPartitions","48")
      conf.set("spark.default.parallelism","48")

      instance = new SparkContext(conf)
    }
    instance
  }
}
