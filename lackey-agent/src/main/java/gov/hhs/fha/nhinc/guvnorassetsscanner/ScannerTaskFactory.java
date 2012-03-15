/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.guvnorassetsscanner;

import gov.hhs.fha.nhinc.guvnorassetsscanner.io.URLResourceReaderStrategy;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author salaboy
 */
public class ScannerTaskFactory {
    public static ScannerTask newScannerTask(String url) throws MalformedURLException{
        return new ScannerTask(new URLResourceReaderStrategy(new URL(url)));
        
    }
}
