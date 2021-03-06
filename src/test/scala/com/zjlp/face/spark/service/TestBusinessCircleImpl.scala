package com.zjlp.face.spark.service

import java.util
import com.zjlp.face.spark.base.factory.SparkBaseFactoryImpl
import com.zjlp.face.spark.service.impl.BusinessCircleImpl

object TestBusinessCircleImpl {
  val bc = new BusinessCircleImpl()

  def testSearchCommonFriendNum() = {

    val userNames = new java.util.ArrayList[String]()
    userNames.add("13000000001")
    userNames.add("13000000003")
    userNames.add("13000000004")
    userNames.add("13000000005")
    userNames.add("13000000006")
    userNames.add("1300000xx06")
    val loginAccount = "13000000002"
    val result = bc.searchCommonFriendNum(userNames, loginAccount)
    println(result)
  }

  def testSearchPersonRelation() = {
    val userIds = new util.ArrayList[String]()
    userIds.add("3184")
    userIds.add("3186")
    userIds.add("3322")
    userIds.add("3183")
    userIds.add("2807")
    userIds.add("1080")
    val  list =  bc.searchPersonRelation(userIds, "13000000005")
    println(list)
  }


  def main(args: Array[String]) {
    val beginTime = System.currentTimeMillis()
    println("###################开始计时###################")
    //testSearchPersonRelation
    bc.setSparkBaseFactory(new SparkBaseFactoryImpl)
    bc.updateDBSources
    testSearchCommonFriendNum
    println("################### 计时结束。耗时：" + ((System.currentTimeMillis() - beginTime)/1000 + "秒"))
  }
}
