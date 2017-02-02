import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Vijaya Yeruva on 01-Feb-17.
  **/
object SparkEvenOdd {

  def main(args: Array[String]) {

    System.setProperty("hadoop.home.dir","C:\\Users\\Vijaya Yeruva\\Documents\\winutils");

    val sparkConf = new SparkConf().setAppName("SparkEvenOdd").setMaster("local[*]")

    val sc=new SparkContext(sparkConf)

    val input=sc.textFile("inputnumbers")

    val wc=input.flatMap(line=>{line.split(" ")}).map(word=>(word,1)).cache()

    val num=wc.map{x=>x._1.toInt}

    val even = num.filter(x => x % 2 == 0) // all even numbers
    val odd = num.filter(x => x % 2 != 0) // all odd numbers

    val edup = even.map(num=>(num,1)).cache()
    val evdup =edup.reduceByKey(_+_,1)

    val odup = odd.map(num=>(num,1)).cache()
    val oddup =odup.reduceByKey(_+_,1)

    evdup.saveAsTextFile("Even")
    oddup.saveAsTextFile("Odd")
  }
}