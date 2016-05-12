package configreader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
 
/**
 * @TuomasH
 * 
 * This class reads file (project root) "/resources/config.properties" and returns it as a map
 * Properties are key/value pairs stored as string, 
 * so to assign the value to a variable following needs to be done:
 * 
 * String:
 * 	String totalfile = prop.getProperty("totalfile");
 * 
 * Double:
 * 	double maxresearchresources = Double.parseDouble(prop.getProperty("maxresearchresources"));
 * 
 * Integer:
 * 	int populationsize = Integer.parseInt((prop.getProperty("populationsize")));
 * 
 * And so forth
 * 
 */

public class getPropertyValues {
	 
		InputStream inputStream;
		String propFilename;
		
		//Default constructor
		public getPropertyValues() {
			this.propFilename = "config.properties";
		}
		
		//Constructor if filename exists
		public getPropertyValues(String filename) {
			this.propFilename = filename;
		}
	 
		public Properties getPropValues() throws IOException {
	 
			Properties prop = new Properties();
			try {
				
				String propFileName = this.propFilename;
	 
				inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
	 
				if (inputStream != null) {
					prop.load(inputStream);
				} else {
					throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
				}
	 
			} catch (Exception e) {
				System.out.println("Exception: " + e);
			} finally {
				inputStream.close();
			}
			return prop;
		}
	}