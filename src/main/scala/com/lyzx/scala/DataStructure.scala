package com.lyzx.scala

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * @author hero.li
  *  本类演示scala的数据结构
  *  scala提供了2大类集合
  *  可变  :scala.collection.mutable._
  *  不可变:scala.collection.immutable._
  *
  *  scala优先采用不可变集合，不可变集合可以被线程安全的并发访问
  *  scala的集合类型
  *   1、序列(Seq)类似于java中的List
  *   2、集(Set) 类似于java中的Set
  *   3、映射(Map)类似于java中的Map
  *   几乎所有的集合都继承自Iterable特质
  *
  */
class DataStructure {


  /**
    * ArrayBuffer类似于java中的ArrayList
    */
  def f1(): Unit ={
    //arr使用java中的数组实现
    val arr = Array(1,2,4)
    println("arr="+arr)

    val ab = new ArrayBuffer[Int]()
    ab += 1
    ab += 2
    ab += 3
    println("ab="+ab)

//    for(item <- ab;item2 <- arr){
//      println("=>"+item,"    "+item2)
//    }

    val arr2arrbuffer = arr.toBuffer
    arr2arrbuffer += 100
    println(arr2arrbuffer)
//    ab.toArray
  }


  /**
    * scala和java数据结构的相互转换
    */
  def f2(): Unit ={
    val v1 = ArrayBuffer(1,2,3,4)
    import scala.collection.JavaConverters._
    val javaArr = v1.asJava
    val scalaArr = javaArr.asScala

    println("javaArr.getClass===="+javaArr.getClass)
    println("scalaArr.getClass==="+scalaArr.getClass)
  }


  /**
    * 对于所有映射 直接通过k(key)的方式获取值，如果key不存在则抛出异常
    *
    */
  def f3(): Unit ={
    val score = Map("A"->100,"B"->99,"C"->98)
    println(score("B"))

    val s = scala.collection.mutable.Map("A"->100,"B"->99,"C"->98)
    s += ("D"->4)
    println(s("D"))

    s -= "C"
    println("s="+s)
  }


  /**
    * 数组的拉链操作
    */
  def f4(): Unit ={
    val t1 = Array(1,2,3)
    val t2 = Array("<","-",">")

    val t3 = t2.zip(t1)
    t3.foreach(println)
  }


  /**
    * 先进先出的队列
    */
  def f5(): Unit ={
    val q = new mutable.Queue[Int]()
    q.enqueue(99)
    q.enqueue(97)
    q.enqueue(0)

    println(q.head+"   "+q.tail)
    println(q.size+"   "+q.dequeue())
    println(q.size+"   "+q.dequeue())
    println(q.size+"   "+q.dequeue())
    if(!q.isEmpty){
      println(q.size+"   "+q.dequeue())
    }
  }

  /**
    *
    */
  def f6(): Unit ={
      val t1 = List(1,2,3,4,5)
      val t2 = new ArrayBuffer[Int]()
          t2 += 100
          t2 += 300
      println(t1+","+t2)
  }


  /**
    * map和flatMap
    * map是对每一个item做映射即做map函数中的操作
    * flatMap是做完Map后再做一个压扁的操作
    */
  def f7(): Unit ={
    val t1 = List("a","b","c","E","F")
        t1.map(_.toUpperCase)
      .foreach(println)

    println("========================")
    val t2 = Vector(t1.map(_.toUpperCase),t1.map(_.toLowerCase()))
      t2.flatMap(_.map(_.toLowerCase))
      .foreach(println)
  }


  /**
    * reduceLeft(_ - _)从做开始归并 即1-2-3-4-5为最后的值
    *
    */
  def f8(): Unit ={
    val t = List(1,7,2,9,99)
    val c = t.reduceLeft((x,y)=>y-x)
    println(c)

    val c2 = t.reduceRight(_ - _)
    println(c2)

  }


}

object DataStructure{

  def main(args: Array[String]): Unit = {
    val t = new DataStructure
//      t.f1()
//      t.f2()
//      t.f3()
//      t.f4()
//      t.f5()
//        t.f6()
//    t.f7()
    t.f8()
  }
}