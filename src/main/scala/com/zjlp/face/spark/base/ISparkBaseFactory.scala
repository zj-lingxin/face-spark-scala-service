package com.zjlp.face.spark.base

import org.apache.spark.sql.SQLContext

/**
 * Created by root on 6/6/16.
 */
trait ISparkBaseFactory {
  /**
   * 获取sqlContext
   * @return 返回值
   */
  def getSQLContext: SQLContext

  /**
   * 更新数据源
   */
  def updateSQLContext
}
