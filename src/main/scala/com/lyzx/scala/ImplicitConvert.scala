package com.lyzx.scala

case class X_Man(firstName:String){}
case class Super_Man(code:String){
  implicit def toDF(): Int ={
    128
  }
}

class ImplicitConvert {

  def introduce(implicit name:String,age:Int,introduce:String,intro:String="很高心和大家一起工作!"): Unit ={
      println("我的名字叫:"+name+",今年"+age+"岁,"+intro)
  }

  /**
    * 一、隐式转换的小例子之参数申明为隐式的
    * 当一个方法中的参数申明为隐式的时候
    * 如果在调用这个方法时没有传入该隐式参数，那么scala编译器就会在
    * 当前的作用域中寻找匹配的隐式变量来作为默认替补值
    * 1、如果在当前作用域中找到了2个或者以上类型匹配的参数时会报错
    * 2、当前作用域中可以有多个隐式变量但只能有一个匹配该方法参数才不会报错
    * 3、当方法有隐式参数和非隐式参数时，直接不加参数其余的参数依然会被当做隐式参数一样自动在作用域中匹配值
    * 建议设置隐式参数的时候避免使用基本类型,这样凑巧被匹配的概率太大
    * @param v
    */
  def f1(): Unit ={
    implicit val name = "李耀辉！"
    implicit val age = 100
//    implicit val name1 = "李耀辉1！"

    val t1 = new ImplicitConvert
    t1.introduce
  }


  /**
    * 2、隐式转换之调用本不存在的方法
    * scala可以使用隐式抓换调用本不存在与自己对象的方法
    * scala编译的时候发现，t这个X_Man根本就没有toDF()这个方法
    * 那么toDF()时谁的方法呢？它是Super_Man的，所以scala编译器就想
    * 怎么样能够把X_Man转换为Super_Man呢？
    * 它就在当前的作用域中找，而import implicitConversion.implicitConversion._
    * 正好把定义好的自包中的函数引入当前的作用域，而这些函数中正好有一个能把x2s函数能把X_Man转换为Super_Man
    * 所以就在当前的作用域中找到了能把X_Man转换为Super_Man的函数，即使用这个函数进行隐式转换
    */
  def f2(): Unit ={
    val t = new X_Man("金刚狼")
    import implicitConversion.implicitConversion._
    val v = t.toDF()
    println("=="+v)
  }

}

package implicitConversion{
  object implicitConversion{
    implicit def x2s(x_Man: X_Man):Super_Man={
      new Super_Man(x_Man.firstName)
    }
  }
}

object ImplicitConvert{

  def main(args: Array[String]): Unit = {
    val t = new ImplicitConvert
    t.f2()
  }
}