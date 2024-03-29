package at.ait.dme.magicktiler;

import java.io.File;

import junit.framework.TestCase;

import org.junit.Ignore;

/**
 * Base class for our tests
 * 
 * @author Christian Sadilek <christian.sadilek@gmail.com>
 */
@Ignore
public abstract class BaseTest extends TestCase {

	protected void deleteDir(File path) {
	    if(path.exists()) {
	      File[] files = path.listFiles();
	      for (int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	        	 deleteDir(files[i]);
	         } else {
	           files[i].delete();
	         }
	      }
	    }
	    path.delete();
	}
}
