package com.zjlp.face.spark.service

import com.zjlp.face.spark.bean.{PersonRelation, CommonFriendNum}
import org.apache.spark.sql.DataFrame
import java.util

trait IBusinessCircle {
  /**
   * 一度好友查询
   * @param loginAccount 用户ID
   * @return 返回结果集
   */
  def searchFriendFrame(loginAccount: String): DataFrame

  /**
   * 二度好友查询
   * @param loginAccount 用户ID
   * @return 返回结果集
   */
  def searchTwoFriendFrame(loginAccount: String): DataFrame

  /**
   * 根据当前登录用户id和用户id列表查询共同好友数
   *
   * @param userNames 附近店铺用户集
   * @param loginAccount 登入账号
   * @return 返回结果集
   */
  def searchCommonFriendNum(userNames: util.List[String], loginAccount: String): util.List[CommonFriendNum]

  /**
   * 根据当前登录用户id和用户id列表返回人脉关系类型列表
   *
   * @param userIds 用户集
   * @param loginAccount 登入账号
   * @return
   */
  def searchPersonRelation(userIds: util.List[String], loginAccount: String): util.List[PersonRelation]

  /**
   * 更新数据源 定时任务调取
   * @return 返回执行状态
   */
  def updateDBSources: Boolean
}
