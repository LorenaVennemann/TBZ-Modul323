// Aufgabe 3.1: Eine Funktion, die aus einer Liste von Zahlen die Summe aller Listenelemente berechnet.
def sumList(numbers: List[Int]): Int = {
    numbers.match {
        case Nil => 0
        case head :: tail => head + sumList(tail)
    }
}

// Aufgabe 3.2: Eine Funktion, die aus einer Liste von Zahlen den Mittelwert aller Listenelemente berechnet.
def averageList(numbers: List[Double]): Double = {
    def sum(nums: List[Double]): Double = nums match{
            case Nil => 0
            case head :: tail => head + sum(tail)
    }
    if (numbers.isEmpty) 0 else sum(numbers) / numbers.length
}

// Aufgabe 3.3: Eine Funktion, die eine gegebene Liste von Strings alphabetisch sortiert.
def sortStrings(strings: List[String]): List[String] = {
  def merge(left: List[String], right: List[String]): List[String] = (left, right) match {
    case (Nil, right) => right
    case (left, Nil) => left
    case (leftHead :: leftTail, rightHead :: rightTail) =>
      if (leftHead <= rightHead) leftHead :: merge(leftTail, right)
      else rightHead :: merge(left, rightTail)
  }
  
  val n = strings.length / 2
  if (n == 0) strings
  else {
    val (left, right) = strings.splitAt(n)
    merge(sortStrings(left), sortStrings(right))
  }
}

// Aufgabe 3.4: Eine Funktion, die eine Liste von Objekten anhand einer eigenen Sort-aFunktion sortiert. Die Objekte sollen dabei die Properties Datum, Priorität und Titel enthalten. Die Funktion soll eine sortierte Liste der Objekte zurückgeben, wobei als Sortierkriterium zuerst das Datum, dann die Priorität und zum Schluss der Titel verwendet werden.
case class Task(date: java.time.LocalDate, priority: Int, title: String)

def sortTasks(tasks: List[Task]): List[Task] = {
  tasks.sortWith((a, b) => 
    if (a.date != b.date) a.date.isBefore(b.date)
    else if (a.priority != b.priority) a.priority < b.priority
    else a.title < b.title
  )
}
// Aufgabe 3.5: Eine Funktion, die aus einer Baumstruktur mit unterschiedlich tiefer Verschachtelung alle Blätter (Elemente ohne weitere Kinder) ausliest und in einer flachen Liste von Blättern zurückgibt.
sealed trait Tree[A]
case class Leaf[A](value: A) extends Tree[A]
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

def getLeaves[A](tree: Tree[A]): List[A] = tree match {
  case Leaf(value) => List(value)
  case Branch(left, right) => getLeaves(left) ++ getLeaves(right)
}