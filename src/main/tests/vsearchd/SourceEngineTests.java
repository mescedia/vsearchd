package vsearchd;

import java.io.Console;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.vsearchd.crawler.sourceengine.SourceEngineJson;
import org.vsearchd.crawler.sourceengine.SourceEngineRobots;

public class SourceEngineTests {
	

	@Test
	public void robots_txt_test() throws IOException {
		
		FileInputStream fis = new FileInputStream("src/main/tests/resources/robots.txt"); 
		
		
		SourceEngineRobots r = new SourceEngineRobots() ;
		String result = r.getSource(fis) ;
		
		System.out.println(result);
		
	}
	
	
	@Test
	public void json_2_xml_test() throws IOException {
		
		FileInputStream fis = new FileInputStream("src/main/tests/resources/test.json"); 
		
		SourceEngineJson r = new SourceEngineJson() ;
		String result = r.getSource(fis) ;
		
		System.out.println(result);
		
	}

}
