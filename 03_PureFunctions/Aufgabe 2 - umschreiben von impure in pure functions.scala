//Aufgabe 1.1
def pureAddToCart(currentCart: List[String], newItem: String): List[String] = {
    currentCart :+ newItem
}

// Usage example:
val emptyCart = List[String]()
val cart1 = pureAddToCart(emptyCart, "Apple")     // Returns: List(Apple)
val cart2 = pureAddToCart(cart1, "Banana")        // Returns: List(Apple, Banana)
val cart3 = pureAddToCart(cart2, "Orange")        // Returns: List(Apple, Banana, Orange)


//Aufgabe 1.4
def pureMultiplyWithFactor(number: Double, factor: Double): Double = {
    require(factor >= 0 && factor <= 1, "Factor must be between 0 and 1")
    number * factor
}

// Usage example:
val result1 = pureMultiplyWithFactor(5, 0.5)    // Returns: 2.5
val result2 = pureMultiplyWithFactor(10, 0.3)   // Returns: 3.0


//Aufgabe 1.6
// Methode zum Ausgeben und Rückgeben einer Zeichenkette
def pureReturnString(str: String): String = str

val result = pureReturnString("Hello")    // Returns: "Hello"
