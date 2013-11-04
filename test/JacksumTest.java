import static org.junit.Assert.*;
import jonelo.jacksum.JacksumAPI;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import static org.hamcrest.CoreMatchers.*;

public class JacksumTest {
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	
	@Before
	public void setUpStreams() {
	System.setOut(new PrintStream(outContent));
	System.setErr(new PrintStream(errContent));
	
	}
	
	@Test
	public void AlgoWithoutAltPass() throws IOException {
		//arguments for checksum
		int i=0, count=0, j=0;
		
		String [] algos = {"sha1+crc32", "adler32"};
		
		while(j < algos.length){
		String [] args1 ={"-f", "-a", "{place}", "-A", "./testResource" };
		args1[2] = algos[j];
		String content = "Hello World";
		File file = new File("./testResource/testDoc.txt");
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		JacksumAPI.runCLI(args1);
		
		File dir = new File("./testResource/");
	    int numbOfFiles = dir.listFiles().length;
	    
		String delims = "\\r?\\n";
		String output=outContent.toString();
		String [] tokens = output.split(delims);
		
		//changing the contents of the file 
		content = "Hello Word";
		fw = new FileWriter(file.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		
		JacksumAPI.runCLI(args1);
		output=outContent.toString();
		tokens = output.split(delims);
		while(i < numbOfFiles){
			if((tokens[i].equals(tokens[numbOfFiles+i])))
	 			count++;
			i++;
		}
		
		if(count == numbOfFiles)
			fail("CRC same for all files");
		i=0;
		count = 0;
		outContent.reset();
		j++;
	}
}
	@Test
	public void AllalgoPass() throws IOException {
		//arguments for checksum
		int i=0,count=0, j=0;
		String [] algos = { "Crc64" , "Crc8", "Crc16", "crc32", "edonkey", "fcs16", "elf", "gost", "has160", "haval", "md2", "md4", "md5" 
				, "read", "rmd128", "rmd160", "rmd256", "rmd320", "sha0", "sha", "sha224", "sha256", "sha384", "sha512", "sum8", "sum16", "sum24", "sum32"
				,"sysv", "tiger128", "tiger160", "tiger", "tiger2","whirlpool0", "whirlpool1", "whirlpool", "fcs32", "xor8", "Cksum", "adler32", "crc32_mpeg2",
				"bsd"};
			
			while(j < algos.length){
			String [] args1 ={"-f", "-a", "{place}", "-A", "./testResource" };
			args1[2] = algos[j];
			String content = "Hello World";
			File file = new File("./testResource/testDoc.txt");
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
		
			JacksumAPI.runCLI(args1);
		
			File dir = new File("./testResource/");
			int numbOfFiles = dir.listFiles().length;
	    
		String delims = "\\r?\\n";
		String output=outContent.toString();
		String [] tokens = output.split(delims);
		
		//changing the contents of the file 
		content = "Corrupt";
		fw = new FileWriter(file.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
		JacksumAPI.runCLI(args1);
		output=outContent.toString();
		tokens = output.split(delims);
		
		if(output.isEmpty()){
			fail("Failed to Hash: " + args1[2]);
		}
		
		while(i < numbOfFiles){
			if((tokens[i].equals(tokens[numbOfFiles+i])))
	 			count++;
	 		i++;
		}
		
		if(count == numbOfFiles){
			fail("CRC same for all files" + args1[2] );
		}
		i=0;
		count = 0;
		outContent.reset();
		j++;
	}
}	
		
		@Test
		public void CRCGenericTestPass() throws IOException {
			//arguments for checksum
			int i=0,count=0;
			String [] args1 ={"-f", "-a", "crc:16,1021,FFFF,false,false,0","./testResource" };
			
			String content = "Hello World";
			File file = new File("./testResource/testDoc.txt");
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			
			JacksumAPI.runCLI(args1);
			
			File dir = new File("./testResource/");
		    int numbOfFiles = dir.listFiles().length;
		    
			String delims = "\\r?\\n";
			String output=outContent.toString();
			String [] tokens = output.split(delims);
			
			//changing the contents of the file 
			content = "Corrupt";
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			
			
			JacksumAPI.runCLI(args1);
			output=outContent.toString();
			tokens = output.split(delims);
			while(i < numbOfFiles){
				if((tokens[i].equals(tokens[numbOfFiles+i])))
		 			count++;
		 		i++;
			}
			if(count == numbOfFiles)
				fail("CRC same for all files");
			}
	
		
		@Test
		public void MD5OutputToFile() throws IOException {
			String [] args1 ={"-a", "md5", "-f", "-m", "-O" ,"list.jacksum", "-w", "." };
			
			JacksumAPI.runCLI(args1);
			
			File f = new File("./list.jacksum");
			if(!f.exists()){
				fail("File not created with -m option");
			}
		}
			
		@Test //testing the -q option 
		public void CRCPlainText() throws IOException {
			String [] args1 ={"-a", "crc32", "-q", "txt:Hello World!" };
			JacksumAPI.runCLI(args1);
				
			String delims = "\\r?\\n";
			String output=outContent.toString();
			String [] tokens = output.split(delims);
			
			String [] args2 ={"-a", "crc32", "-q", "txt:Hella World!" };
			JacksumAPI.runCLI(args2);
			
			 output=outContent.toString();
			 tokens = output.split(delims);
			 assertThat(tokens[0], is(not(tokens[1])));
			 }
		
		
		@Test  //testing -r option
		public void CRCRecursiveCall() throws IOException {
			
			String [] args1 ={"-a", "cksum", "-r", "./testResource2" };
			JacksumAPI.runCLI(args1);
			String output=outContent.toString();
			
			assertTrue(output.contains("testDoc2.txt"));
			}
		
		
		@Test  //testing -V option
		public void verboseModeTest() throws IOException {
			String [] args1 ={"-a", "md5", "-A", "-V", "summary", "./testResource2/KAPGRE.iso" };
			JacksumAPI.runCLI(args1);
			String output=outContent.toString();
			output = output + errContent.toString();
	
			assertTrue(output.contains("Jacksum:"));
			}
		
		@Test  //testing -t option
		public void customDateFormat() throws IOException {
			String [] args1 ={"-a", "sha1", "-s", "\t", "-t", "EEE, MMM d, yyyy 'at' h:mm a", "./testResource/testDoc.txt" };
			JacksumAPI.runCLI(args1);
			String output=outContent.toString();
			File f = new File("./testResource/testDoc.txt");
			SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy 'at' h:mm a");
			String Dateformat = sdf.format(f.lastModified());
			if(!output.contains(Dateformat)){
				fail("Wrong date format");
			}
		}
		
		@Test  //testing -f option
		public void DirIgnoreTest() throws IOException {
			String [] args1 ={"-a", "crc32", "-f", "-t", "default", "." };
			JacksumAPI.runCLI(args1);
			String output=outContent.toString();
			output = output + errContent.toString();
			assertFalse(output.contains("Is a directory"));
		}
		
		@Test  //testing hex option
		public void HexInputTest() throws IOException {
			String [] args1 ={"-a", "tree:tiger", "-F", "urn:#ALGONAME:#FINGERPRINT", "-q", "hex:48656C6C6F20576F726C6421"};
			JacksumAPI.runCLI(args1);
			
			String delims = "\\r?\\n";
			String output=outContent.toString();
			String [] tokens = output.split(delims);
			
   			String [] args2 ={"-a", "tree:tiger", "-F", "urn:#ALGONAME:#FINGERPRINT", "-q", "hex:48656C6F20576F726C6421"};
			JacksumAPI.runCLI(args2);
			
			 output=outContent.toString();
			 tokens = output.split(delims);
			 assertThat(tokens[0], is(not(tokens[1])));
		}
		
		
		@Test  //testing file size when no algo given
		public void NoAlgoTest() throws IOException {
			String [] args1 ={"-a", "none", "-f", "./testResource/testDoc.txt"};
			JacksumAPI.runCLI(args1);
			
			String delims = "\t";
			String output=outContent.toString();
			String [] tokens = output.split(delims);
			
   			File f = new File("./testResource/testDoc.txt");
   			int size = (int) f.length();
   			int size2 =	Integer.parseInt(tokens[0]);
   			assertEquals(size, size2);
		}
}

