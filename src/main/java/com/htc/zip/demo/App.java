package com.htc.zip.demo;

import net.lingala.zip4j.exception.ZipException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        WinZip wz = new WinZip();
        
        try {
			wz.createDemoZip();
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
