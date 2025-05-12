object TipCalculator {
  def calculateTip(names: List[String]): Int = {
    names.length match {
      case n if n > 5 => 20
      case n if n > 0 => 10
      case _ => 0
    }
  }
}

object TipCalculatorDemo {
  def main(args: Array[String]): Unit = {
    val smallGroup = List("Alice", "Bob", "Charlie")
    val largeGroup = List("Alice", "Bob", "Charlie", "Dave", "Eve", "Frank")
    val emptyGroup = List.empty[String]

    println(s"Tip for small group (${smallGroup.length} people): ${TipCalculator.calculateTip(smallGroup)}%")
    println(s"Tip for large group (${largeGroup.length} people): ${TipCalculator.calculateTip(largeGroup)}%")
    println(s"Tip for empty group: ${TipCalculator.calculateTip(emptyGroup)}%")
  }
}