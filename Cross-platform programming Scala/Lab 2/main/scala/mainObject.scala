//Task 1 - 26
//Найти в коллекции символов все слова, состоящие только из цифр
//Task 2 - 9
//Подсчитать среднее значение элементов List[Double]
//Task 3 - 10
//Используя числа из List[Int] сгенерировать список кубов этих чисел

object mainObject {

  def main(args: Array[String]): Unit = {

  }

  def justNumeric(arr : List[String]) : List[String] =
    arr.filter(str => augmentString(str).filter(c => c >= '0' && c <= '9') == str)

  def averageValue(arr : List[Double]) : Double =
    arr.foldLeft(0.0)((i, acc) => acc + i ) / arr.foldLeft(0)((i,_) => i + 1)

  def cubes(arr : List[Int]) : List[Int] =
    arr.foldRight(List[Int]())((i,acc) => i*i*i::acc)

  def cubesMap(arr: List[Int]) : List[Int] =
    arr.map(X => X*X*X)
}
