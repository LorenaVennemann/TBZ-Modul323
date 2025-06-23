object CustomLazyLists extends App {
  // Task 1: LazyList that counts from 1 onwards
  val countingNumbers: LazyList[Int] = LazyList.from(1)
  println("Counting numbers: " + countingNumbers.take(5).toList)
 
  // Task 2: LazyList that generates powers of 2
  val powersOfTwo: LazyList[Int] = LazyList.iterate(2)(_ * 2)
  println("Powers of 2: " + powersOfTwo.take(5).toList)
}