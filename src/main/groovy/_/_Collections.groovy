package _

import groovy.util.logging.Slf4j

@Slf4j
class _Collections {
  private static boolean isCollection(obj) {
    log.debug("IS COLLECTION: ${obj.getClass()}")
    return obj && (obj instanceof Collection ||
                   obj instanceof List ||
                   obj instanceof Map || 
                   obj.getClass().isArray())
  }

  // Iterate over list (either array or map with key/value, or object properties). Pass element to closure iteratee that is bound to
  // context object, if one is passed iteratee, is delegated to it
  static Object each(def obj, Closure iteratee, def context) {
    if (!obj || !iteratee) {
      return null
    }
    if (context != null) {
      iteratee.delegate = context
    }
    if (isCollection(obj)){
      return obj.eachWithIndex(iteratee)
    }
    else {
      return obj.properties.eachWithIndex(iteratee)
    }
  }

  // Return new array of values by mapping each value in input object though a transformation function
  // context object, if one is passed, iteratee is delegated to it
  static Collection map(def obj, Closure iteratee, def context) {
    def arr = []
    if (!obj || !iteratee) {
      return arr
    }
    if (context) {
      iteratee.delegate = context
    }
    if (isCollection(obj)){
      log.debug("MAP COLLECTION")
      return obj.collect(arr, iteratee)
    }
    else {
      log.debug("MAP OBJECT: ${obj.properties}")
      return obj.properties.collect(arr, iteratee)
    }
  }
}
