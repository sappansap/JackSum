import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

import jonelo.jacksum.JacksumAPI;

import org.junit.Before;
import org.junit.Test;

public class JacksumZTest {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	
	@Before
	public void setUpStreams() throws IOException {
	System.setOut(new PrintStream(outContent));
	System.setErr(new PrintStream(errContent));
	}
	
	@Test
	public void MD5CheckPass() throws IOException {
		String [] args1 ={"-a", "md5","-c", "list.jacksum"};
		JacksumAPI.runCLI(args1);
		String output=outContent.toString();
		assertTrue(!(output.contains("Fail")));
		
	}
}
