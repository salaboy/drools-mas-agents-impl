/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.guvnorassetsscanner;

import java.util.List;
import org.drools.grid.api.ResourceDescriptor;

/**
 *
 * @author esteban
 */
public interface AssetScannerListener {
    public void onAssetsScanned(List<ResourceDescriptor> assets);
}
