import org.scalatest.FlatSpec

class LabTests extends FlatSpec {

  "A function justNumeric" should "return words consisting just from numbers" in {
    assert(List("12345", "01011101") === mainObject.justNumeric(List("hello", "-3+4", "12345", "stop", "01011101")))
  }
  it should "return List() if no words consisting just from numbers" in {
    assert(List() === mainObject.justNumeric(List("hello", "-3+4", "stop")))
  }

  "A function averageValue" should "return average value from List[Double]" in {
    assert(3 === mainObject.averageValue(List(2.4, 5.6, 1.0)))
  }
  it should "return NaN if base list is empty" in {
    assert(Double.NaN equals mainObject.averageValue(List()))
  }

}
