package com.lyzx.scala

/**
  * @author hero.li
  * 本例演示scala的基本语法用法
  */
class BaseVarTest {

  /**
    * scala中没有基本类型,所谓的基本类型也是引用
    * 每一种基本类型都有一个对应的Rich类型
    * eg: Int有RichInt
    *   val n:Int = 10
    *   printf(1.toString)
    *   其中的String方法就是RichInt的，定义在preDef中隐式转换
    */
  def f1(): Unit ={
    val i = 1
    println(Seq(i))
  }

  /**
    * scala除了提供方法还提供了函数，如数学函数,但是需要引入
    * java中通常使用静态类来模拟函数
    *
    */
  def f2(): Unit ={
    import scala.math._
    println(sqrt(9))      //开平方
    println(abs(-119))    //绝对值
    println(floor(4.58))  //
  }

  /**
    * Option是一个包装类表示里面有一个值，但是不确定存在不存在
    * 这样就避免了null，空字符串等问题
    * 注意null和空字符串等都标识没有即opt.isEmpty为true
    */
  def f3(): Unit ={
    val t = Map("k1"->"v1","k2"->"v2","k3"->"","k4"->null)
    println("==>"+t.size)
    val opt = t.get("k3")
    if(opt.isEmpty){
        println(opt.get)
    }else{
      println("木有值")
    }
  }

  /**
    * scala中使用if-else结构代替三目运算符
    */
  def f4(): Unit ={
     val v =  10
    val t = if(v >= 10) 100 else 99
    println(t)
  }


  /**
    * for推导,话说这才叫真正的增强的for循环
    * 下面的for循环相当于java中的2层循环
    *  1 to 10 表示1到10的循环  if表示过滤条件
    *  2 until 6 表示1到5的循环 by表示步长
    */
  def f5(): Unit ={
    for(i <- (1 to 10) if( i != 4);j <- (2 until 6 by 2) ){
      println("i="+i,"j="+j)
    }
  }


  /**
    * for-yield的返回值有循环本身决定
    */
  def f6(): Unit ={
    val tt = for(i <- 1 to 5 if(i != 4) ;j <- 2 to 6 by 2) yield (i,j)
    println(tt)
  }


  /**
    * scala中的异常通过case的方式匹配
    */
  def f7(): Unit ={
     try{
//      val t = 1/0
       val arr = Array(1,2,3)
       println(arr(10))
     }catch {
       case e:ArithmeticException => println("算数错误:")
       case e:ArrayIndexOutOfBoundsException=>println("数组下标越界")
       case e :Exception=>println("未知异常!")
     }
  }
}

object BaseVarTest{
  def main(args: Array[String]): Unit = {
    val bv = new BaseVarTest
//    bv.f1()
//    bv.f2()
//    bv.f3()
//    bv.f4()
//    bv.f5()
//    bv.f6()
bv.f7()

  }
}