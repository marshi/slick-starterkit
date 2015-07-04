package main.scala

import entity.Tables
import slick.driver.PostgresDriver.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object App {

  /**
   * resources/application.conf内の設定と対応.
   */
  val db = Database.forConfig("db")

  def main(args: Array[String]): Unit = {
    select
    update
    insert(30)
    delete(16)
  }

  def insert(id: Int) = {
    val q = Tables.HostMachine.map(h => (h.name)) += (Some("test_name"))
    val future = db.run(q)
    Await.ready(future, Duration.Inf)
  }

  def delete(id: Int) = {
    val q = Tables.HostMachine.filter(_.id === id).delete
    val future = db.run(q)
    Await.ready(future, Duration.Inf)
  }

  def update = {
    // filter ≒ where.
    // map(m => m.◯◯) ≒ "set ◯◯ = "
    // update(□□) ≒ (set ◯◯ =) □□
    val q = Tables.HostMachine.filter(_.id === 3).map(h => (h.id, h.name)).update((3, Some("dev-pc03")))
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
}
