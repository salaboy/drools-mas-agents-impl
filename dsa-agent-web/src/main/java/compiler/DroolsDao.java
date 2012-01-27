/*
 * TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION
 * Copyright (c) 2008, Nationwide Health Information Network (NHIN) Connect.
 * All rights reserved.Redistribution and use in source and binary forms,
 * with or without modification, are permitted provided that the following
 * conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * - Neither the name of the NHIN Connect Project nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *    END OF TERMS AND CONDITIONS
 */

package compiler;

import org.drools.WorkingMemory;
import org.drools.reteoo.ReteooRuleBase;
import org.hibernate.ejb.Ejb3Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class DroolsDao {

    private EntityManagerFactory factory;
    private EntityManager em;

    private Set<String> klasses = new HashSet<String>();


    public DroolsDao() {

    }


    public void register(String className) {
        klasses.add(className);
    }


    public void initEntityManager(String persistenceUnitName, WorkingMemory wm) {

        Map<String,Object> props = new HashMap<String,Object>();
        ClassLoader drlKlassLoader = ((ReteooRuleBase) wm.getRuleBase()).getRootClassLoader();

        if (factory == null) {
            Ejb3Configuration config = new Ejb3Configuration();
                config.configure(persistenceUnitName,props);

            try {
                for (String klassName : klasses) {
                    Class klazz = drlKlassLoader.loadClass(klassName);
                    config.addAnnotatedClass(klazz);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            ClassLoader defaultLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(drlKlassLoader);
                factory = config.buildEntityManagerFactory();
                    em = factory.createEntityManager();
            Thread.currentThread().setContextClassLoader(defaultLoader);
        }

    }


    public void dispose() {
        if ( em != null ) {
            em.close();
        }
        if ( factory != null ) {
            factory.close();
        }
    }




    public void save(Object o) {
        try {
            EntityTransaction tx = em.getTransaction();

            if (!tx.isActive()) {
                tx.begin();
            }
            try {
                em.persist(o);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }










}
