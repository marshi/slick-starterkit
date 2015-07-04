package main.scala

import entity.Tables
import slick.driver.PostgresDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object App {

  /**
   * resources/application.conf内の設定と対応.
   */
  val db = Database.forConfig("db")

  def main(args: Array[String]): Unit = {
    select
    insertHost(30, "test01")
    insertBranch(30, "test01-branch01")
    selectJoin
    update(30, "test01-update")
    deleteHost(30)
    deleteBranch(30)
  }

  def insertHost(id: Int, hostName: String) = {
    // forceInsertではauto incrementなカラムであっても指定した値を挿入することができる.
    val q = Tables.HostMachine.map(h => (h.id, h.name)).forceInsert(id, Some(hostName))
    val future = db.run(q)
    Await.ready(future, Duration.Inf)
  }

  def insertBranch(hostId: Int, branchName: String) = {
    // insertは += を使うことも可能.
    val q = Tables.HostBranch.map(b => (b.hostMachineId, b.branchName)) += (Some(hostId), Some(branchName))
    val future = db.run(q)
    Await.ready(future, Duration.Inf)
  }

  def deleteHost(id: Int) = {
    val q = Tables.HostMachine.filter(_.id === id).delete
    val future = db.run(q)
    Await.ready(future, Duration.Inf)
  }
  
  def deleteBranch(hostId: Int) = {
    val q = Tables.HostBranch.filter(_.hostMachineId === hostId).delete
    val future = db.run(q)
    Await.ready(future, Duration.Inf)
  }

  def update(id: Int, hostName: String) = {
    // filter ≒ where.
    // map(m => m.◯◯) ≒ "set ◯◯ = "
    // update(□□) ≒ (set ◯◯ =) □□
    val q = Tables.HostMachine.filter(_.id === 3).map(h => (h.id, h.name)).update((id, Some("dev-pc03")))
    val future = db.run(q)
    Await.ready(future, Duration.Inf)
  }

  def select = {
    val q = Tables.HostMachine.filter(_.id === 3).result.head
    val future = db.run(q)
    future.onComplete {
      case Failure(r) => println("failure")
      case Success(r) => println(r.id + " " + r.name)
      case _ => println("a")
    }
    Await.ready(future, Duration.Inf)
  }

  /**
   * host_machineテーブルのIDとhost_branchテーブルのhost_machine_idが一致するレコードをselectする
   * @return
   */
  def selectJoin = {
    val q = Tables.HostMachine.join(Tables.HostBranch).on(_.id === _.hostMachineId).result
    val future = db.run(q)
    future.onComplete {
      case Failure(_) => Nil
      case Success(r) => r.foreach(t => println(t._1.name + " " + t._2.branchName))
    }
    Await.ready(future, Duration.Inf)
  }

}
