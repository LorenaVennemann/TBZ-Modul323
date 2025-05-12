object FunctionalShoppingCart {
  def getDiscountPercentage(items: List[String]): Int = 
    if (items.map(_.toLowerCase).exists(_.contains("book"))) 5 else 0

  def main(args: Array[String]): Unit = {
    val cart1 = List("Book 1", "Pen")
    val cart2 = List("Pen", "Paper")
    
    println(s"Cart with book: $cart1")
    println(s"Discount: ${getDiscountPercentage(cart1)}%")
    
    println(s"Cart without book: $cart2")
    println(s"Discount: ${getDiscountPercentage(cart2)}%")
  }
}