package com.randez_trying.merlin.Util;

import com.kursx.parser.fb2.FictionBook;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Book extends FictionBook {

    private File file;
    private Element root;

    private int pageWidth;
    private int pageHeight;
    private int textSize = 23;

    private ArrayList<String> pages;

    public Book(File file, int pageWidth, int pageHeight) throws ParserConfigurationException, IOException, SAXException, OutOfMemoryError {
        super(file);
        this.file = file;
        refresh(file,pageWidth,pageHeight,textSize);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(file);
            root = document.getDocumentElement();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public void displaySize(int pageWidth, int pageHeight) {
        refresh(file,pageWidth,pageHeight,textSize);
    }

    public void setTextSize(int textSize) {
        refresh(file,pageWidth,pageHeight,textSize);
    }

    private void refresh(File file, int pageWidth, int pageHeight, int textSize) {
        pages = new ArrayList<>();
        this.file = file;
        this.pageHeight = pageHeight;
        this.pageWidth = pageWidth;
        this.textSize = textSize;
        parseLines();
    }

    private void parseLines() {
        List<String> paragraphs = getXMLStrings();

        int maxCharsInLine = pageWidth / textSize;
        int maxLinesInPage = (int) (pageHeight / (textSize * 2.0));

        List<String> textLines = new ArrayList<>();
        for (int i = 0; i < paragraphs.size(); i++) {
            String paragraph = "    " + paragraphs.get(i);
            boolean paragraphLessMaxChars = paragraph.length() <= maxCharsInLine;
            StringBuilder lineBuilder = new StringBuilder();

            if (paragraphLessMaxChars) {
                textLines.add(paragraph + "\n");
            } else {
                lineBuilder.append("    ");
                String[] words = paragraphs.get(i).split(" ");

                for (String word : words) {
                    if (lineBuilder.length() > maxCharsInLine) {
                        textLines.add(lineBuilder + "\n");
                        lineBuilder = new StringBuilder();
                    }
                    if (lineBuilder.length() > 0) lineBuilder.append(" ");
                    lineBuilder.append(word);
                }
                lineBuilder.append("\n");
                textLines.add(lineBuilder.toString());
            }
        }

        List<String> pageLines = new ArrayList<>();
        for (int i = 0; i < textLines.size(); i++) {
            if (i > textLines.size() - 50) System.out.println(textLines.get(i).length() + " " + textLines.get(i));

            if (textLines.get(i).toLowerCase().contains("глава")
                    || textLines.get(i).toLowerCase().contains("chapter")) {
                StringBuilder buffer = new StringBuilder();
                for (int a = 0; a < pageLines.size(); a++) {
                    buffer.append(pageLines.get(a));
                }
                pages.add(buffer.toString());
                pageLines.clear();
            }

            if (pageLines.size() > maxLinesInPage) {
                StringBuilder buffer = new StringBuilder();
                for (int a = 0; a < pageLines.size(); a++) {
                    buffer.append(pageLines.get(a));
                }
                pages.add(buffer.toString());
                pageLines.clear();
                pageLines.add(textLines.get(i));
            } else pageLines.add(textLines.get(i));

            if (textLines.size() - 1 == i) {
                StringBuilder buffer = new StringBuilder();
                for (int a = 0; a < pageLines.size(); a++) {
                    buffer.append(pageLines.get(a));
                }
                pages.add(buffer.toString());
                pageLines.clear();
            }
        }
    }

    private List<String> getXMLStrings() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            Element root = document.getDocumentElement();
            NodeList nodes = root.getElementsByTagName("p");
            List<String> strings = new ArrayList<>();

            for (int i = 0; i < nodes.getLength(); i++){
                strings.add(nodes.item(i).getTextContent());
            }
            return strings;
        } catch (IOException | ParserConfigurationException | SAXException ignored) {}
        return Collections.singletonList("Can't read this file");
    }

    public String getPage(int index) {
        if (pages.size() <= index) return "";
        return pages.get(index);
    }

    public int getPageCount() {
        return pages.size();
    }

    public String getGenre(){
        NodeList nodes = root.getElementsByTagName("genre");
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }else{
            return null;
        }
    }

    public String getFirstName(){
        NodeList nodes = root.getElementsByTagName("first-name");
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }else{
            return null;
        }
    }

    public String getLastName(){
        NodeList nodes = root.getElementsByTagName("last-name");
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }else{
            return null;
        }
    }

    public String getBookID(){
        NodeList nodes = root.getElementsByTagName("id");
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }else{
            return null;
        }
    }

    public String getBookTitle(){
        NodeList nodes = root.getElementsByTagName("book-title");
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }else{
            return null;
        }
    }

    public String getProgramUsed(){
        NodeList nodes = root.getElementsByTagName("program-used");
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }else{
            return null;
        }
    }

    public String getBookAnnotation(){
        NodeList nodes = root.getElementsByTagName("annotation");
        if (nodes.getLength() > 0) return nodes.item(0).getTextContent();
        else return "";
    }

    public File getFile() {
        return file;
    }
}