object ShoppingCartDemo {
  class ImperativeShoppingCart {
    private var items = List[String]()
    private var bookAdded = false

    def addItem(item: String): Unit = {
      items = item :: items
      if (item.toLowerCase.contains("book")) {
        bookAdded = true
      }
    }

    def removeItem(item: String): Unit = {
      items = items.filter(_ != item)
      bookAdded = items.exists(_.toLowerCase.contains("book"))
    }

    def getItems(): List[String] = items

    def getDiscount(): Int = {
      if (bookAdded) 5 else 0
    }
  }

  object FunctionalShoppingCart {
    def getDiscountPercentage(items: List[String]): Int = {
      if (items.exists(_.toLowerCase.contains("book"))) 5 else 0
    }
  }

  def main(args: Array[String]): Unit = {
    println("Testing Imperative Approach:")
    val cart = new ImperativeShoppingCart()
    cart.addItem("Book 1")
    cart.addItem("Pen")
    println(s"Items: ${cart.getItems()}")
    println(s"Discount: ${cart.getDiscount()}%")
    cart.removeItem("Book 1")
    println(s"After removing book - Discount: ${cart.getDiscount()}%")

    println("\nTesting Functional Approach:")
    val items = List("Book 1", "Pen")
    println(s"Items: $items")
    println(s"Discount: ${FunctionalShoppingCart.getDiscountPercentage(items)}%")
    val itemsWithoutBook = List("Pen")
    println(s"Items without book: $itemsWithoutBook")
    println(s"Discount: ${FunctionalShoppingCart.getDiscountPercentage(itemsWithoutBook)}%")
  }
}