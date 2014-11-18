package _

import spock.lang.Specification

class CollectionsTestSpec extends Specification {
  def "Iterate collection with closure"() {
    given: "defined list"
    def list = ["a", "b", "c"]
    def res = []

    when: "call each operator"
    _Collections.each(list, {item, index ->
      println "Delegate class: ${delegate.getClass()}"
      println "This class: ${this.getClass()}"
      add("${item+index}")
    },
    res)

    then:
    list.eachWithIndex{ item, idx ->
      res[idx] == "${item+idx}"
    }
    res[0] == "a0"
  }

  def "Iterate map with closure"() {
    given: "defined map"
    def map = [a: "1", b: "2", c: "3"]
    def res = []

    when: "call each operator"
    _Collections.each(map, {key, value, idx ->
      add("${key+value}")
    }, res)

    then:
    map.eachWithIndex{key, value, idx ->
      res[idx] == "${key+value}"
    }
  }

  def "Iterate class instance with closure"() {
    given:  "defined object"
    def testObject = new Expando()
    testObject.attr1= "a"
    testObject.attr2 = "b"
    def res = []

    when: "call each operator"
    _Collections.each(testObject, {key, val, idx ->
      println("VAL: ${key}, ${val}, ${idx}")
      add("${key+val+idx}")
    }, res)

    then:
    testObject.properties.eachWithIndex{key, val, idx ->
      String test = "${key+val+idx}"
      res.find{ it == test } != null
    }
  }

  def "Map array to another array with context object and its method"() {
    given: "defined array of elements"
    def arr = ["a", "b", "c"]
    def ctx = new Expando() {
      int idx = 0
      String calc(val) { return val + idx++ }
    }
    
    when: "map array with closure and ctx's calc method"
    def res = _Collections.map(arr, {val ->
      return calc(val)
    }, ctx)

    then:
    res[0] == "a0"
    res[1] == "b1"
    res[2] == "c2"
  }

  def "Map map to array with context object and its method"() {
    given: "defined map"
    def map = [a: "1", b: "2", c: "3"]
    def ctx = new Expando() {
      int ctx = 0
      String calc(String key, String val) {
        return key+val+"_"+ctx++
      }
    }

    when: "map to array with ctx delegate"
    def res = _Collections.map(map, {key, val ->
      return calc(key, val)
    }, ctx)

    then:
    res[0] == "a1_0"
    res[1] == "b2_1"
    res[2] == "c3_2"
  }

  def "Map object's attributes to array with context object"() {
    given: "defined object"
    def obj = new Expando()
    obj.attr1 = "a"
    obj.attr2 = "b"
    def ctx = new Expando() {
      int idx = 0
      String calc(key, val) {
        return key+val+"_"+idx++
      }
    }

    when: "map object properties to array"
    def res = _Collections.map(obj, {key, val ->
      return calc(key, val)
    }, ctx)

    then:
    obj.properties.eachWithIndex{key, val, idx ->
      String test = "${key+val}_${idx}"
      res.find{ it == test } != null
    }
  }
}
