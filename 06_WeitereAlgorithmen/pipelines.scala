case class Course(title: String, students: List[String])

object PipelineExercises extends App {
  val courses = List(
    Course("M323", List("Peter", "Petra", "Paul", "Paula")),
    Course("M183", List("Paula", "Franz", "Franziska")),
    Course("M117", List("Paul", "Paula")),
    Course("M114", List("Petra", "Paul", "Paula")),
  )

  // Pipeline for Peter's modules
  val peterModules = courses
    .filter(_.students.contains("Peter"))
    .map(_.title)
    .mkString(", ")
  println(s"Peter besucht folgende Module: $peterModules")

  // Pipeline for Petra's modules
  val petraModules = courses
    .filter(_.students.contains("Petra"))
    .map(_.title)
    .mkString(", ")
  println(s"Petra besucht folgende Module: $petraModules")

  // Part 2: CourseSubscriptions
  case class CourseSubscriptions(title: String, totalStudents: Int)

  val courseSubscriptions = courses
    .map(course => CourseSubscriptions(course.title, course.students.size))
  
  println("\nKurseinschreibungen:")
  courseSubscriptions.foreach(cs => 
    println(s"${cs.title}: ${cs.totalStudents} Studierende"))
}