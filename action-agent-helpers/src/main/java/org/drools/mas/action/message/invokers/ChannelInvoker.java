/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drools.mas.action.message.invokers;

/**
 *
 * @author salaboy
 */
public interface ChannelInvoker<T> {
    public String getType();
    public T invoke(String endpoint, T object);
}
