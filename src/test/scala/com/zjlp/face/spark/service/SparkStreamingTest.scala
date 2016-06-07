package com.zjlp.face.spark.service

import java.util

import com.zjlp.face.spark.base._
import com.zjlp.face.spark.bean.PersonRelation
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.JavaConversions._

object SparkStreamingTest {
  def searchPersonRelation(loginAccount: String) = {
    //测试数据，以后传入
    val userIds = new util.ArrayList[String]()
    userIds.add("3184")
    userIds.add("3186")
    userIds.add("3322")
    userIds.add("3183")
    userIds.add("2807")
    userIds.add("1080")

    val beginTime = System.currentTimeMillis()
    val userIdsLoginAccount =
      SQLContextSingleton.getInstance().sql(s" select distinct loginAccount,userId from ofRoster where userId in ('${userIds.mkString("','")}') and loginAccount != '${loginAccount}'")
        .map(a => a.toSeq.toArray.map(a => a.toString)).persist()

    val userIdsMap = userIdsLoginAccount.map(a => (a(0), a(1))).collectAsMap()

    val othersUserName: RDD[String] = userIdsLoginAccount.map(a => a(0)).persist()

    //我的所有的一度好友
    val myFriends =
      SQLContextSingleton.getInstance().sql(s"select distinct loginAccount from ofRoster where username = '${loginAccount}' and username <> loginAccount")
        .map(a => a.toSeq.toArray.map(a => a.toString)).map(_ (0)).collect()

    //是我的一度好友的用户集
    val othersBelongToOneLevelFriends = othersUserName.filter(a => myFriends.contains(a)).persist()

    //我的所有的二度好友
    val myTwoLevelFriends =
      SQLContextSingleton.getInstance().sql(s"select distinct loginAccount from ofRoster where username in ('${myFriends.mkString("','")}') ")
        .map(a => a.toSeq.toArray.map(a => a.toString)).map(a => a(0))

    //是我的二度好友的用户集
    val othersBelongToTwoLevelFriends = myTwoLevelFriends.intersection(othersUserName).subtract(othersBelongToOneLevelFriends).persist()
    val oneLevel = othersBelongToOneLevelFriends.collect()
    val twoLevel = othersBelongToTwoLevelFriends.collect()
    val stranger = othersUserName.collect().filter(a => !(oneLevel.contains(a) || twoLevel.contains(a)))

    val list = new util.ArrayList[PersonRelation]()
    oneLevel.foreach(a => list.add(new PersonRelation(userIdsMap(a), 1)))
    twoLevel.foreach(a => list.add(new PersonRelation(userIdsMap(a), 2)))
    stranger.foreach(a => list.add(new PersonRelation(userIdsMap(a), 3)))


    println(loginAccount + "的好友：" + list.toString)
    val endTime = System.currentTimeMillis()
    println("耗时：" + ((endTime - beginTime) / 1000) + "s")
  }

  def searchPersonRelationTest(loginAccount: String) = {
    SQLContextSingleton.getInstance().sql(s" select distinct userId from ofRoster where loginAccount ='${loginAccount}'")
      .map(a => a.toSeq.toArray.map(a => a.toString)).map(a => (a(0)))
      .foreach(userId => println(s"$loginAccount 的userId是:$userId"))
  }


  def main(args: Array[String]) {
    JdbcDF.load("(select username,loginAccount,userId from view_ofroster where sub=3) ofRoster")
      .registerTempTable("ofRoster")


    val ssc = new StreamingContext(SparkContextSingleton.getInstance, Seconds(8))
    val loginAccountsRDDs = ssc.socketTextStream("192.168.175.12", 9999, StorageLevel.MEMORY_AND_DISK_SER)

    loginAccountsRDDs.foreachRDD {
      loginAccountsRDD =>
        loginAccountsRDD.collect().foreach {
          loginAccount => searchPersonRelationTest(loginAccount)
        }
    }

    ssc.start()
    ssc.awaitTermination()
  }
}

