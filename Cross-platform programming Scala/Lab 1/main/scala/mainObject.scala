//Task 1 - 1
//Вывести все целые значения между двумя числами
//Task 2 - 7
//Составить список, содержащий только положительные числа из исходного списка

object mainObject {

  def main(args: Array[String]): Unit = {
    checkInt(0, 7)
    checkTailInt(0, 7)
    foundPositive(List(-1, 0, 34, -2, 9))
    foundTailPositive(List(-1, 0, 34, -2, 9))
  }
  
  def checkInt(start: Int, finish: Int): List[Int] = {
    def run(start: Int, finish: Int): List[Int] =
      if (start >= finish) List()
      else start :: run(start + 1, finish)
    run(start + 1, finish)
  }

  def checkTailInt(start: Int, finish: Int) = {
    def loop(a: Int, b: Int, list: List[Int]): List[Int] = {
      if (a >= b) list
      else loop(a + 1, b, list :+ a)
    }
    loop(start + 1, finish, List())
  }

  def foundPositive(list: List[Int]): List[Int] = {
    list.filter(_ > 0)
  }

  def foundTailPositive(list: List[Int]) = {
    def loop(in: List[Int], out: List[Int]): List[Int] = in match {
      case Nil => out
      case head :: tail => loop(tail, if (head <= 0) out else head :: out)
    }
    loop(list, Nil).reverse
  }
}

