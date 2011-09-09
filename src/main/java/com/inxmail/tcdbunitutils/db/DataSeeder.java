package com.inxmail.tcdbunitutils.db;

/**
 * 
 * @author Bartosz Majsak
 *
 */
public interface DataSeeder {

    void prepare();
    
    void cleanup();
    
}
