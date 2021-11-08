/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg;

import java.io.*;
import java.util.*;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

public class EjScanner {
    public static void main(String [] args) throws IOException, SolrServerException {
        String fileName = ".\\collection\\CISI.ALL.extract";
        Scanner scan = new Scanner(new File(fileName));
        String state="BEFORE";
        String index = null,title=null,author=null,text=null;
        while(scan.hasNextLine()){
            state="PROCESS";
            String line = scan.nextLine();
            System.out.println(line);
            if(line.startsWith(".I", 0)){  //indice del documento
                
                indexaDocumento(index, title, author, text);
                String[] parts = line.split(" ");
                index = parts[1];
                System.out.println(index);
            }
            if(line.startsWith(".T", 0)){  //titulo del documento     
                title=scan.nextLine();
            }       
            if(line.startsWith(".A", 0)){  //autor del documento     
                author=scan.nextLine();
            }
            if(line.startsWith(".W", 0) || state=="READING"){  //texto del documento     
                text=scan.nextLine();
                state="READING";
            }
            if(state=="READING"){  //texto del documento 
                String part=scan.nextLine();
                if(part.startsWith(".X",0)){
                    state="IGNORING";
                }else{
                    text=text+part;
                }
            if((line.startsWith(".X", 0))||state=="IGNORING"){  //referencias cruzadas del documento     
                //no hago nada
            }else{
                //aquí tampoco
            }
        }
    }
        //last document
        indexaDocumento(index, title, author, text);
    }
    
    public static void indexaDocumento(String index, String title,String author, String text) throws SolrServerException, IOException{
        HttpSolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/solr/micoleccion").build();
		
		//Create solr document
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("index", index);
		doc.addField("title", title);
                doc.addField("author", author);
                doc.addField("text", text);
		solr.add(doc);
		solr.commit();
    }
}