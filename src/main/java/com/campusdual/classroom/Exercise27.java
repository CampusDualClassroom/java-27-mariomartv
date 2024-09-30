package com.campusdual.classroom;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Exercise27 {
    public static void main(String[] args) {

        // Lista de productos con cantidad y nombre
        List<Product> products = Arrays.asList(
                new Product("2", "Manzana"),
                new Product("1", "Leche"),
                new Product("3", "Pan"),
                new Product("2", "Huevo"),
                new Product("1", "Queso"),
                new Product("1", "Cereal"),
                new Product("4", "Agua"),
                new Product("6", "Yogur"),
                new Product("2", "Arroz")
        );
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            DOMImplementation implementation = builder.getDOMImplementation();

            Document documento = implementation.createDocument(null,"shoppinglist",null);
            documento.setXmlVersion("1.0");

            Element items = documento.createElement("items");



            for (Product product : products) {
                Element item = createItemElement(documento, product);
                items.appendChild(item);
            }

            documento.getDocumentElement().appendChild(items);

            Source source = new DOMSource(documento);
            Result result = new StreamResult(new File("src/main/resources/shoppingList.xml"));

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source,result);










        } catch (ParserConfigurationException | TransformerException e) {
            throw new RuntimeException(e);
        }

        
        createJSON(products);



    }

    private static Element createItemElement(Document documento, Product product) {
        Element item = documento.createElement("item");
        item.setAttribute("quantity", product.getQuantity());
        item.setTextContent(product.getName());
        return item;
    }

    private static void createJSON(List<Product> products) {
        JsonObject root = new JsonObject();
        JsonArray itemsArray = new JsonArray();

        for (Product product : products) {
            JsonObject item = new JsonObject();
            item.addProperty("quantity", Integer.parseInt(product.getQuantity()));
            item.addProperty("text", product.getName());
            itemsArray.add(item);
        }

        root.add("items", itemsArray);

        try (FileWriter file = new FileWriter("src/main/resources/shoppingList.json")) {
            Gson gson = new Gson();
            gson.toJson(root, file);
        } catch (IOException e) {
            throw new RuntimeException("Error writing JSON file: " + e.getMessage());
        }
    }

    static class Product {
        private final String quantity;
        private final String name;

        public Product(String quantity, String name) {
            this.quantity = quantity;
            this.name = name;
        }

        public String getQuantity() {
            return quantity;
        }

        public String getName() {
            return name;
        }
    }

    // Clase que representa la lista de compras
    static class ShoppingList {

        private List<Product> items;

        public ShoppingList(List<Product> items) {
            this.items = items;
        }

        public List<Product> getItems() {
            return items;
        }

        public void setItems(List<Product> items) {
            this.items = items;
        }
    }
}
