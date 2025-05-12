class TipCalculator {
  private var names = List[String]()
  private var tipPercentage = 0

  def addPerson(name: String): Unit = {
    names = names :+ name
    tipPercentage = names.size match {
      case size if size > 5 => 20
      case size if size > 0 => 10
      case _ => 0
    }
  }

  def getNames: List[String] = names

  def getTipPercentage: Int = tipPercentage
}