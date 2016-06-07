package com.zjlp.face.spark.base.factory

import com.zjlp.face.spark.base.{ISparkBaseFactory, JdbcDF, SQLContextSingleton}
import org.apache.spark.sql.SQLContext

/**
 * Created by root on 6/6/16.
 */
class SparkBaseFactoryImpl extends ISparkBaseFactory{
  /**
   * 获取sqlContext
   * @return 返回值
   */
  def getSQLContext: SQLContext = {
    SQLContextSingleton.getInstance()
  }

  /**
   * 更新数据源
   */
  def updateSQLContext: Unit = {
    JdbcDF.load("(select username,loginAccount,userId from view_ofroster where sub=3) ofRoster")
      .registerTempTable("ofRoster")
  }
}
