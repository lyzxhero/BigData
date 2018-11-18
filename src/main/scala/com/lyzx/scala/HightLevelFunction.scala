package com.lyzx.scala

/**
  * @author hero.li
  *  高阶函数是指能够接受函数作为参数的函数
  */
class HightLevelFunction {

  def add(a:Int,b:Int)={a+b}

  /**
    * 高阶函数的定义形式 (参数列表)=>返回值类型
    * eg:(a:Int,b:Int)=>Int
   */
  def f1(f:(Int,Int)=>Int,c:Int,d:Int):Int ={
      f(c,d)
  }

}

object HightLevelFunction{

  def main(args: Array[String]): Unit = {
    val t = new HightLevelFunction
      val v= t.f1(t.add,100,200)
        println(v)


  }
}