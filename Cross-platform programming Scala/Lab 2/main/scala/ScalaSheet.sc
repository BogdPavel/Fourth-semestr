def justNumeric(arr : List[String]) : List[String] =
  arr.filter(str => augmentString(str).filter(c => c >= '0' && c <= '9') == str)

def averageValue(arr : List[Double]) : Double =
  arr.foldLeft(0.0)((i, acc) => acc + i ) / arr.foldLeft(0)((i,_) => i + 1)

def cubes(arr : List[Int]) : List[Int] =
  arr.foldRight(List[Int]())((i,acc) => i*i*i::acc)

def cubesMap(arr: List[Int]) : List[Int] =
  arr.map(X => X*X*X)

justNumeric(List("hello", "-3+4", "12345", "stop", "01011101"))

averageValue(List(2.4, 5.6, 1.0))

cubes(List(1, 2, 3))

cubesMap(List(1, 2, 3))