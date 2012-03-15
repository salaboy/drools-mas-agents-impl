/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.guvnorassetsscanner;

import gov.hhs.fha.nhinc.guvnorassetsscanner.authenticator.GuvnorAuthenticatorProvider;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.drools.grid.api.CompositeResourceDescriptor;
import org.drools.grid.api.ResourceDescriptor;

/**
 *
 * @author esteban
 */
public class AssetsScanner {
    private int interval;
    
    private ScannerTask scannerTask;
    private final ScheduledExecutorService scheduler;
    
    public AssetsScanner(String guvnorURL, GuvnorAuthenticatorProvider authenticatorProvider, int interval) {
        this.interval = interval;
        
        this.scheduler = Executors.newScheduledThreadPool(1);
        
        this.scannerTask = new ScannerTask(guvnorURL, authenticatorProvider);
    }

    
    public void hydrateInternalResources(CompositeResourceDescriptor resourceDesriptor){
        this.scannerTask.hydrateInternalResources(resourceDesriptor);
    }
    
    public List<ResourceDescriptor> getResourceDescriptors(){
        return this.scannerTask.getResourceDescriptors();
    }
    
    public void start(){
        this.start(0);
    }
    
    public void start(int delay){
        scheduler.scheduleAtFixedRate(this.scannerTask, delay, this.interval, TimeUnit.SECONDS);
    }
    
    public void stop(){
        if (this.scheduler != null){
            this.scheduler.shutdownNow();
        }
    }

    public void removeListener(AssetScannerListener listener) {
        scannerTask.removeListener(listener);
    }

    public void addListeners(List<AssetScannerListener> listeners) {
        scannerTask.addListeners(listeners);
    }

    public void addListener(AssetScannerListener listener) {
        scannerTask.addListener(listener);
    }
    
}
