package kengine;

/**
 * @overview An exception that is thrown when there there are two identical items 
 *           found in a given collection. 
 *            
 * @author dmle
 *
 */
public class DuplicateException extends RuntimeException {
  public DuplicateException(String s) {
    super(s);
  }
}
