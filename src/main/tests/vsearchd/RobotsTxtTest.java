package vsearchd;

import java.io.Console;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.vsearchd.crawler.sourceengine.SourceEngineRobots;

public class RobotsTxtTest {

	
	
	

	@Test
	public void test() throws IOException {
		
		FileInputStream fis = new FileInputStream("src/main/tests/resources/robots.txt"); 
		
		
		
		SourceEngineRobots r = new SourceEngineRobots() ;
		String result = r.getSource(fis) ;
		
		System.out.println(result);
		
	}

}
