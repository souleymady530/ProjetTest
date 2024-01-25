package com.climax.demo.Models;

 
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

 import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import lombok.Data;

@Data
public class RowClient {
    private List<Client> rows;

    public List<Client> read(File file){
         // Crée un tableau dynamique.
        List<Client> personList = new ArrayList<>();

        // Crée un objet Document qui représente les données du fichier XML
        // sous la forme d’une hiérarchie d’objets de type Node. Un objet de
        // type nœud peut représenter aussi bien un élément, qu’un nœud
        // de texte ou un attribut.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        
        
        try {
            builder = factory.newDocumentBuilder();
            Document doc;
            doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            // Recherche tous les elements <person>
            NodeList personNodeList = doc.getElementsByTagName("person");

            // Pour chaque élément XML de la liste
            for (int i = 0; i < personNodeList.getLength(); i = i + 1) {
                Node node = personNodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                
                    // Récupère l'élément
                    Element personElement = (Element)node;

                    // Crée un nouvel objet de type Person
                    Client person = new Client();
System.out.println("Hello2");
                System.out.println(personElement.getElementsByTagName("Data").item(0).getTextContent());
                    person.nom = personElement.getElementsByTagName("nom").item(0).getTextContent();
                    person.prenom = personElement.getElementsByTagName("prenom").item(0).getTextContent();
                    person.profession = personElement.getElementsByTagName("profession").item(0).getTextContent();
                    person.salaire = Float.valueOf(personElement.getElementsByTagName("salaire").item(0).getTextContent().toString());
                    person.age = Float.valueOf(personElement.getElementsByTagName("age").item(0).getTextContent().toString());
                    
                    System.out.println(person.toString());
                    // Ajoute la personne à la liste
                    personList.add(person);
        
                }
        }

        // Renvoie la référence à l’objet personList
       
        } catch (SAXException | ParserConfigurationException| IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         return personList;
    }
}
