package com.projektER.service;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
@Service
public class TwistedTransistor {
	
	public TwistedTransistor(){
		super();
		
	}
	
	private static File writeArray(File tmpF, String[] poNovomRedu) throws IOException {
		
		FileWriter fw = new FileWriter(tmpF);
		
		for(String redPodatka : poNovomRedu) {
			
			fw.write(redPodatka+"\n");
			fw.flush();
		}
		
		//System.out.println("ovov je: "+tmpF.length());
		fw.close();
		return tmpF;
		
	}
	
	
	
	
	
	private static File unscramble(int inJeSad, int inMoraBiti, String[] poRedovima) throws IOException {
		 
	/*	for(int i=0;i<poRedovima.length;i++) {
			System.out.println(poRedovima[i]);
		}*/
		
		
		Path izl= Files.createTempFile("myapp-", ".tmp");
		
		if(inJeSad != inMoraBiti) {
			String pom="";
			
			pom= poRedovima[inJeSad];
			//System.out.println("Dogodilo se");
			poRedovima[inJeSad] = poRedovima[inMoraBiti];
			poRedovima[inMoraBiti]= pom;
			}
		
	/*	for(int i=0;i<poRedovima.length;i++) {
			System.out.println(poRedovima[i]);
		}*/
		
		
		return writeArray(izl.toFile(), poRedovima);
	}
	
	
	public  File transfer(int indUlaz, int indIzlaz, String s) throws IOException {
		//Path izl= Files.createTempFile("myapp-", ".tmp");
		
		Scanner sc= new Scanner(Path.of(s));
		
		String mr= "";
	    while(sc.hasNext()) {
	    	
	    	mr+=sc.nextLine()+"\n";  }
		
		
		//System.out.println(mr);
		String[] listed = mr.split("\n");
		
		
		
		
		return unscramble(indUlaz,indIzlaz, listed );
		
	}
	
	
	
	
	
}
