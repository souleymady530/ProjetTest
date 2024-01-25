package com.climax.demo.Services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.xml.stream.XMLStreamReader;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.fasterxml.jackson.databind.ObjectMapper;


import org.springframework.asm.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.swing.text.html.HTMLDocument.Iterator;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import  com.fasterxml.jackson.databind.ObjectMapper;
import com.climax.demo.Models.Client;
import com.climax.demo.Models.RowClient;
import com.climax.demo.Repositories.ClientRepository;
import com.fasterxml.jackson.databind.JsonNode;
  
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

@Service
public class ImportFileService implements FileStorageService{

    @Autowired
    ClientRepository clients_repos;
    
    private final Path root=Paths.get("uploads");
private final ObjectMapper objMp;
    public ImportFileService(ObjectMapper obj)
    {
        this.objMp=obj;
    }


    @Override
    public void init() {
       try {
        Files.createDirectories(root);
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
       
    }

    @Override
    public void save(MultipartFile file) {
        //on test voir si le repertoire existe, sinon on cree le repertoire dans la methode init
        init();
        
        
        //on fait une copie du fichier dans le repertoire
        Calendar cal=Calendar.getInstance();
        String make_unique_name=cal.getTimeInMillis()+"_";
        try {
            
            Files.copy(file.getInputStream(),this.root.resolve(make_unique_name+file.getOriginalFilename()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //lecture du fichier

        //enregistrement dans la bd via le model
        
        String nom=make_unique_name+file.getOriginalFilename();
       
        String chemin=this.root.toString()+"/"+nom;
        System.out.println("le chemin est "+chemin);
        String extension=nom.substring(nom.indexOf(".")+1);
        List<Client> all_clients=new ArrayList<>();
        
        //lecture de fichier xlsx
        if(extension.equals("txt"))
        {
            System.out.println("Fichie text");
            try {
                // Création d'un fileReader pour lire le fichier
                FileReader fileReader = new FileReader(chemin);
                
                // Création d'un bufferedReader qui utilise le fileReader
                BufferedReader reader = new BufferedReader(fileReader);
                
                // une fonction à essayer pouvant générer une erreur
                String line = reader.readLine();
                
                while (line != null) {
                    // affichage de la ligne
                   // all_clients.add(new Client(line.toString().split(",")[0],line.toString().split(",")[1],line.toString().split(",")[3],Float.valueOf(line.toString().split(",")[2]),Float.valueOf(line.toString().split(",")[4])));
                    clients_repos.save(new Client(line.toString().split(",")[0],line.toString().split(",")[1],line.toString().split(",")[3],Float.valueOf(line.toString().split(",")[2]),Float.valueOf(line.toString().split(",")[4])));
                    // lecture de la prochaine ligne
                    line = reader.readLine();
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    
            System.err.println(all_clients);
            
        }
        else
            {
                //lecture de fichie json
                if(extension.equals("json"))
                {
                      System.out.println("Fichier Json");
                      JsonNode json;
                        try
                        (InputStream input_stream=file.getInputStream()) 
                        {
                        
                             json=this.objMp.readValue(input_stream, JsonNode.class);
                                                         

                        } catch (Exception e) {
                            e.printStackTrace();
                           throw new RuntimeException("Failed to read json file");
                        }                               
                        JsonNode clients=getClientFromJson(json);
                        for(JsonNode client:clients)
                            {
                                clients_repos.save(new Client(client.get("nom").asText(),client.get("prenom").asText(),client.get("profession").asText(),client.get("age").asLong(),client.get("salaire").asLong()));
                               // all_clients.add(new Client(client.get("nom").asText(),client.get("prenom").asText(),client.get("profession").asText(),client.get("age").asLong(),client.get("salaire").asLong()));
                            }
                            
                }
                
                else
                    {
                        //lecture de fichier txt
                        if(extension.equals("csv"))
                        {
                            System.out.println("Fichier Csv");
                             
                            try (BufferedReader br = new BufferedReader(new FileReader(chemin))) 
                            {
                                String line;
                                while( (line=br.readLine())!=null){
                                    System.out.println(line);
                                   
                                     String []tab=line.split(";");
                                    this.clients_repos.save(new Client(String.valueOf(tab[0]), String.valueOf(tab[1]),String.valueOf(tab[3]),Float.valueOf(tab[2]),Float.valueOf(tab[4])));
                                    
                                }
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                System.err.println("Erreur de lecture de fichier");
                            }

                        }
                        //lecture de fichier xmls
                        else  if(extension.equals("xml"))
                        {
                            System.out.println("Fichier XMLS");
                            
                            
                            FileInputStream fileIS;
							try {
								fileIS = new FileInputStream(chemin);
								 DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		                            DocumentBuilder builder = builderFactory.newDocumentBuilder();
		                            Document xmlDocument = builder.parse(fileIS);
		                            XPath xPath = XPathFactory.newInstance().newXPath();
		                            
		                           
		                            NodeList les_noms = (NodeList) xPath.compile("/app/client/nom").evaluate(xmlDocument, XPathConstants.NODESET);
		                            NodeList les_prenoms = (NodeList) xPath.compile("/app/client/prenom").evaluate(xmlDocument, XPathConstants.NODESET);
		                            NodeList les_ages = (NodeList) xPath.compile("/app/client/age").evaluate(xmlDocument, XPathConstants.NODESET);
		                            NodeList les_professions = (NodeList) xPath.compile("/app/client/profession").evaluate(xmlDocument, XPathConstants.NODESET);
		                            NodeList les_salaires = (NodeList) xPath.compile("/app/client/salaire").evaluate(xmlDocument, XPathConstants.NODESET);
		                            
		                            for(int i=0;i<les_noms.getLength();i++) {
		                            	
		                            	 Node nom_client = les_noms.item(i);
		                            	 Node prenom_client = les_prenoms.item(i);
		                            	 Node age_client = les_ages.item(i);
		                            	 Node profession_client = les_professions.item(i);
		                            	 Node salaire_client = les_salaires.item(i);
		                            	 Client client=new Client(nom_client.getTextContent(),
		                            			 prenom_client.getTextContent(),
		                            			 profession_client.getTextContent()
		                            			 ,Float.valueOf(age_client.getTextContent()),Float.valueOf(salaire_client.getTextContent()));
		                            	// System.out.print(nom_client.getTextContent()+" "+prenom_client.getTextContent()+" "+age_client.getTextContent()+" "+profession_client.getTextContent()+" "+salaire_client.getTextContent()+"");
		                              //   System.out.print(node.getTextContent());
		                            	 this.clients_repos.save(client);
		                                 
		                            }
		                          //  System.out.print(nodeList.getLength());
							} 
							catch (Exception e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                           
                            
                             
                          /*

                            XPathFactory xpathFactory = XPathFactory.newInstance();
                            XPath xpath = xpathFactory.newXPath();

                            InputSource source = new InputSource(chemin);
                            String status;
							try 
							{
								 
								status = xpath.evaluate("/resp/msg", source);
								 
							} catch (XPathExpressionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

                           
                            
                           RowClient row=new RowClient();
                            
                          List<Client> ls= row.read((new File(chemin)));
                            */
                            
                            
                            
                        }
                    }
            }
    }
    private JsonNode getClientFromJson(JsonNode json){
        return Optional.ofNullable(json)
        .map(client->client.get("client")).orElseThrow(()->new IllegalArgumentException("Invalide Json Format"));
        
    }

    @Override
    public Resource load(String filename) {
        
        Path file=root.resolve(filename);
        Resource resource=null;
        try {
            resource = new UrlResource(file.toUri());
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(resource.exists() || resource.isReadable()){
             
             return resource;
        }
        else{
            throw new RuntimeException("This files is not readable");
        }
        
    
    }

   
    

    public Iterable<Client> getClients(){
        return clients_repos.findAll();
    }

    public String getMoyenne(){
        String stat="";
    Iterable<Client>les_clients=clients_repos.findAll();
    
    List<String>liste_prof=new ArrayList<>();
    for (Client client : les_clients) {
        if(!liste_prof.contains(client.getProfession().trim())){
             liste_prof.add(client.getProfession());
        }
       

    }
 
     for (String profession : liste_prof) {
        float som_salaire=0;
        int nbre=0;
        for (Client client : les_clients){
            if(client.getProfession().equals(profession)){
                som_salaire+=client.getSalaire();
                nbre++;
            }
         }
         Float moyenne=som_salaire/nbre;
        stat+=profession+"--->"+moyenne+"\n";
    }
    System.out.println(stat);
     return stat;
    }


	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Stream<Path> loadAll() {
		// TODO Auto-generated method stub
		return null;
	}
    
}
