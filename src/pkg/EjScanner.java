/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

public class EjScanner {
    public static void main(String [] args) throws IOException, SolrServerException {
        String sCarpAct = System.getProperty("user.dir");
        String fileName = sCarpAct+"\\src\\corpus\\CISI.ALL.extract";
        Scanner scan = new Scanner(new File(fileName));
        String state="BEFORE";
        String index = null,title=null,author=null,text=null;
        
        //Borremos colección
        DeletingAllDocuments();
        
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
    
    public static void DeletingAllDocuments () { 
   
      //Preparing the Solr client 
      String urlString = "http://localhost:8983/solr/micoleccion"; 
      SolrClient Solr = new HttpSolrClient.Builder(urlString).build();   
      
      //Preparing the Solr document 
      SolrInputDocument doc = new SolrInputDocument();   
          
        try {
            //Deleting the documents from Solr
            Solr.deleteByQuery("*");
            Solr.commit(); 
        } catch (SolrServerException ex) {
            Logger.getLogger(EjScanner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EjScanner.class.getName()).log(Level.SEVERE, null, ex);
        }
         
      //Saving the document 
      
      System.out.println("Documents deleted"); 
   } 
}