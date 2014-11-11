/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791881.model;

import java.io.File;
import javax.xml.bind.JAXB;

/**
 *
 * @author Isch
 */
public class TestUnmarshalling {
    
    public static void main(String[] args) {
        File file = new File("K:\\MedienInformatik\\FPA\\Workspace\\FPA Mailer\\Account\\Eingang\\Private\\mail100.xml");
        Email testMail = JAXB.unmarshal(file, Email.class);
        System.out.println(testMail);      

    }
    
}
