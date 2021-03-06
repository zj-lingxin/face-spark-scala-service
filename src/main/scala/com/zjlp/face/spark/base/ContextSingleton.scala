package com.zjlp.face.spark.base

import com.zjlp.face.spark.bean.{PersonRelation, CommonFriendNum}
import org.apache.spark.sql.SQLContext
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

object SparkContextSingleton extends Logging {
  private var instance: SparkContext = _

  private def showSparkConf() = {
    instance.getConf.getAll.foreach { prop =>
      logInfo(prop.toString())
    }
  }

  private def getSparkConf = {
    val conf = new SparkConf()
    Array(
      "spark.master",
      "spark.app.name",
      "spark.sql.shuffle.partitions",
      "spark.executor.memory",
      "spark.executor.cores",
      "spark.cores.max",
      "spark.speculation",
      "spark.driver.memory",
      "spark.driver.cores",
      "spark.table.numPartitions",
      "spark.default.parallelism"
    ).foreach { prop =>
      conf.set(prop, Props.get(prop))
    }

    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    conf.registerKryoClasses(Array(
      classOf[CommonFriendNum],
      classOf[PersonRelation]
    ))

    conf
  }

  def getInstance = {
    if (instance == null) {
      instance = new SparkContext(getSparkConf)
      showSparkConf()
    }
    instance
  }
}
