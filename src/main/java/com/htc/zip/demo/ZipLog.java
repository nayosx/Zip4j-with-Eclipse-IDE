package com.htc.zip.demo;

import java.lang.System.Logger.Level;
import java.util.logging.Logger;

public class ZipLog {
	private static final String TAG = "ZipLog";
	private static boolean DEBUG = true;
	
	final static Logger LOG = Logger.getLogger("paquete.NombreClase");
	
	public static void config(boolean debug) {
        DEBUG = debug;
    }

    public static void debug(String msg) {
        if (DEBUG) LOG.info(msg);
    }

}
