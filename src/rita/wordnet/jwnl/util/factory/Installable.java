/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.util.factory;


import java.util.Map;

import rita.wordnet.jwnl.JWNLException;

/**
 * An <code>Installable</code> is an object that defines a framework for allowing
 * subclasses to define an instance of themselves as the single static
 * instance of the superclass. It is required that subclasses implement
 * the install() method which creates an instance of the class from
 * property file parameters (<code>Param</code>s) and installs it.
 * <p>
 * For example:
 * <pre>
 * public abstract class Super implements Installable {
 *      private static Super INSTANCE;
 * 		private String str;
 *
 * 		protected void setInstance(Super instance) {
 * 			INSTANCE = instance;
 * 		}
 *
 * 		public Super getInstance() {
 * 			return INSTANCE;
 * 		}
 *
 * 		protected Super() {
 * 		}
 *
 * 		protected Super(String str) {
 * 			this.str = str;
 * 		}
 *
 * 		// other methods go here
 * }
 *
 * public class Sub extends Super {
 * 		public Sub() {
 * 		}
 *
 * 		protected Sub(String s) {
 *      	super(s);
 * 		}
 *
 * 		public void install(Map params) {
 * 			Param p = params.get("string");
 * 			Sub sub = new Sub(p.getValue());
 * 			setInstance(sub);
 * 		}
 *
 * 		// other methods go here
 * }
 *
 * public static void main(String[] args) {
 * 		Map params = getParams();
 * 		Sub.class.newInstance().install(params);
 * }
 * </pre>
 * A class that implements this interface must also define a no-arg constructor.
 */
public interface Installable {
	public void install(Map params) throws JWNLException;
}
