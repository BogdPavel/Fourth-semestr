import org.scalatest.FlatSpec

class LabTests extends FlatSpec {

  "A function checkInt" should "return integer values between numbers" in {
    val list = List.range(1, 7)
    assert(list === mainObject.checkInt(0, 7))
  }
  it should "return empty list if borders are violated" in {
    assert(List() === mainObject.checkInt(7, 0))
  }

  "A function checkTailInt" should "return integer values between numbers" in {
    val list = List.range(1, 7)
    assert(list === mainObject.checkTailInt(0, 7))
  }
  it should "return empty list if borders are violated" in {
    assert(List() === mainObject.checkTailInt(7, 0))
  }

  "A function foundPositive" should "return list with positive numbers from base list" in {
    assert(List(34, 9) === mainObject.foundPositive(List(-1, 0, 34, -2, 9)))
  }
  it should "return empty list if no positive numbers or if base list is equal" in {
    assert(List() === mainObject.foundPositive(List()))
  }

  "A function foundTailPositive" should "return list with positive numbers from base list" in {
    assert(List(34, 9) === mainObject.foundTailPositive(List(-1, 0, 34, -2, 9)))
  }
  it should "return empty list if no positive numbers or if base list is equal" in {
    assert(List() === mainObject.foundTailPositive(List()))
  }

}
