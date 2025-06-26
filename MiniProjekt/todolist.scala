import scala.annotation.tailrec
import java.time.LocalDate

sealed trait Category {
  def name: String
  def subcategories: List[Category]
}

case class SimpleCategory(name: String, subcategories: List[Category] = Nil) extends Category

case class TodoItem(
  id: Int,
  title: String,
  category: Category,
  deadline: LocalDate,
  done: Boolean = false
)


object TodoApp extends App {
  type TodoList = List[TodoItem]
  type CategoryList = List[Category]

  // Category operations
  def addCategory(categories: CategoryList, name: String, parentName: Option[String] = None): CategoryList = 
    parentName match {
      case None =>
        if (categories.exists(_.name == name)) categories
        else categories :+ SimpleCategory(name)
      case Some(parent) =>
        categories.map {
          case c: SimpleCategory if c.name == parent =>
            c.copy(
              subcategories = if (c.subcategories.exists(_.name == name)) c.subcategories 
              else c.subcategories :+ SimpleCategory(name)
            )
          case c => c
        }
    }

  def deleteCategory(categories: CategoryList, name: String): CategoryList = 
    categories.filterNot(_.name == name)

  def findCategory(categories: CategoryList, name: String): Option[Category] = 
    categories.find(_.name.equalsIgnoreCase(name))
}