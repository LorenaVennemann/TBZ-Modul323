object Combined {
  def calculateScoreImperative(word: String): Int = {
    var score = 0
    for (c <- word) {
      if (c != 'a') {
        score += 1
      }
    }
    score
  }

  def calculateScoreDeclarative(word: String): Int = {
    word.count(_ != 'a')
  }

  def main(args: Array[String]): Unit = {
    val testWords = Array("imperative", "no", "declarative", "yes")
    
    println("Imperative results:")
    for (word <- testWords) {
      val score = calculateScoreImperative(word)
      println(s"Score for '$word': $score")
    }

    println("\nDeclarative results:")
    for (word <- testWords) {
      val score = calculateScoreDeclarative(word)
      println(s"Score for '$word': $score")
    }
  }
}