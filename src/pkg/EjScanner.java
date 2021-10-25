/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg;

import java.io.*;
import java.util.*;

public class EjScanner {
    public static void main(String [] args) throws IOException {
        String fileName = ".\\collection\\CISI.ALL.extract";
        Scanner scan = new Scanner(new File(fileName));
        while(scan.hasNextLine()){
            String line = scan.nextLine();
            System.out.println(line);
        }
    }
}