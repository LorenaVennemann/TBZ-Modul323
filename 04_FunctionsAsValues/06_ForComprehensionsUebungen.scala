object ForComprehensionUebungen {
  def main(args: Array[String]): Unit = {
    // Aufgabe 6.1
    val squared = for {
      n <- 1 to 10
    } yield n * n
    println("Aufgabe 6.1 - Quadrierte Zahlen:")
    println(squared)

    // Aufgabe 6.2
    val evenNumbers = for {
      n <- 1 to 20
      if n % 2 == 0
    } yield n
    println("\nAufgabe 6.2 - Gerade Zahlen:")
    println(evenNumbers)

    // Aufgabe 6.3
    val colors = List("Red", "Green", "Blue")
    val fruits = List("Apple", "Banana", "Orange")
    
    val combinations = for {
      color <- colors
      fruit <- fruits
    } yield s"$color $fruit"
    println("\nAufgabe 6.3 - Farb-Frucht Kombinationen:")
    println(combinations)

    // Aufgabe 6.4
    case class User(name: String, tasks: List[String])
    
    val users = List(
      User("Alice", List("Task 1", "Task 2", "Task 3")),
      User("Bob", List("Task 1", "Task 4", "Task 5")),
      User("Charlie", List("Task 2", "Task 3", "Task 6"))
    )
    
    val tasksCompleted = List("Task 1", "Task 3", "Task 5")
    
    val pendingTasks = for {
      user <- users
      task <- user.tasks
      if !tasksCompleted.contains(task)
    } yield s"${user.name}: $task"
    
    println("\nAufgabe 6.4 - Ausstehende Aufgaben:")
    println(pendingTasks)
  }
}