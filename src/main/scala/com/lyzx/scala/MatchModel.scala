package com.lyzx.scala

class MatchModel {


  /**
    * scala的match结构，类似于java中switch结构
    * 不用使用break
    */
  def f1(): Unit ={
    val n:Int =10
    val result =
      n match {
        case 1 => "A"
        case 10 => "B"
        case _ => "NULL"
      }

    println("result = "+result)
  }


  /**
    * match也可以使用守卫功能
    *
    */
  def f2(): Unit ={
    for(item <- "abcd9efg8"){
      item match {
        case 'a' => println("==>"+item)
        case 'b' => println("==>"+item)
        case 'g' => println("==>"+item)
        case ch if(Character.isDigit(item)) => println("是数字"+item+"   "+ch)
        case _ => println("other....")
      }
//      println(ch)
    }
  }


  /**
    * match的类型匹配
    */
  def f3(): Unit ={
    val o:Any = Array(10)

    val result = o match {
        case item:Int           => println("int");        -1
        case item:String        => println("String");     -2
        case item:Map[_,_]      => println("Map[_,_]");   -6
        case item:Array[Int]    => println("Array[Int]"); -7
        case _                  => println("no");         -9
    }
    println(result)
  }


  /**
    * scala的数组匹配
    */
  def f4(): Unit ={
      for(item <- Array(Array(0),Array(0,1),Array(0,1,2),Array(1,1,2))){
          item match {
            case Array(0) => println("元素中只有一个0的数组")
            case Array(x,y) => println("只有两个元素的数组"+x,y)
            case Array(0,_*) => println("以0开头的数组")
            case _ => println("没有匹配上的")
          }
      }
  }


  /**
    *
    */
  def f5(): Unit ={
    for(item <- Array(List(0),List(99,88),List(77,88,99),List(77,88,99,100),List(70,88,99))){
        item match {
          case 0 :: Nil => println("这个List只有一个元素0")
          case x :: y :: Nil => println("这个List有两个元素",x,y)
          case 77 :: tail => println("这个List以0开头",tail)
          case _ => println("没有匹配上的元素")
        }
    }
  }

  /**
    * scala匹配元组
    *
    */
  def f6(): Unit ={
    for(item <- Array((0,1),(0,0),(100,99),(1,2,3))){
      item match {
        case (1,y) => println("(1,"+y+")")
        case (0,y) => println("(0,"+y+")")
        case (x,y) => println("("+x+","+y+")")
        case (x,y,z) => println("3元组",x,y,z)
        case _ => println("no match")
      }
    }
  }

  def f7(): Unit ={
    import scala.collection.JavaConverters._
    val kv = System.getProperties
    for((key,value) <- kv.asScala if(!"".equals(value))){
      println("key="+key+"       value="+value)
    }
  }


  /**
    * 样例类用case修饰，最主要的作用是为了匹配模式而生
    *
    */
  def f8(): Unit ={
      abstract class Animal(name:String)
      case class Dog(name:String) extends Animal(name:String)
      case class Cat(name:String) extends Animal(name:String)

      for(item <- Array(Dog("大黄"),Dog("xiao黄"),Cat("喵喵"))){
        item match {
          case Dog(name) => println("这是一只狗:"+name)
          case Cat(name) => println("这是一只猫:"+name)
          case _ => println("======>")
        }
      }

      val d1 = Dog("v1")
      val d2 = d1.copy()

      println(d1+"         "+d2+"      "+(d1 == d2))
  }


  /**
    * 偏函数是指处理一部分输入的函数即下面的函数
    * 只处理输入时+-* / 这几种类型，企业的不处理
    * 当输入不是这几种时会抛出scala.MatchError错误
    * @param item
    * @return
    */
  def f9(item:Char):Int ={
    val result =
        item match {
          case '+' => 0
          case '-' => 1
          case '*' => 2
          case '/' => 3
        }
    result
  }

}

object MatchModel{

  def main(args:Array[String]): Unit = {
    val t = new MatchModel
//    t.f1()
//    t.f2()
//    t.f3()
//    t.f4()
//    t.f5()
//    t.f6()
//    t.f7()
//    t.f8()

    println(t.f9('0'))



  }
}