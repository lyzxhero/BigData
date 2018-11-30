package com.lyzx.scala

class ScalaTest {

    def f1(): Unit ={
        val t = List("e","l","l","l","e","l","e","e","l","e")
        val result = new scala.collection.mutable.ArrayBuffer[String]()
        val size = t.size

       var flag = true
       var index = 0
        while(flag){
            if(index < size-1){
                val current = t(index)
                val next = t(index+1)

                if("e".equals(current) && "l".equals(next)){
                  result += current
                  result += next
                }
            }

          index += 1
          if(index <= size){
              flag = false
          }
        }
      println(result)
    }

}


object ScalaTest{

  def main(args: Array[String]): Unit = {
      val t = new ScalaTest
          t.f1()

  }
}