/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791881.applicationLogic;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author Isch
 */
public class XMLFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
        String lowercaseName = name.toLowerCase();
        if (lowercaseName.endsWith(".xml") || lowercaseName.endsWith(".")) {
                return true;
        } else {
                return false;
        }
    }
    
}
