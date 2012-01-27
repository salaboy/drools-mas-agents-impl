/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.drools.mas.action.helpers;

import java.util.ArrayList;
import java.util.List;
import org.drools.mas.action.message.LDAPEntity;

/**
 *
 * @author salaboy
 */
public class RecipientSplitterHelper {
    public static List<LDAPEntity> splitRecipients(String recipients){
        String[] ids = recipients.split(",");
        List<LDAPEntity> entities = new ArrayList<LDAPEntity>();
        for(String id : ids){
            entities.add(new LDAPEntity(id));
        }
        return entities;
    }
}
