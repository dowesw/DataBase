package com.lymytz.entitymanager;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class PersistenceHandler extends DefaultHandler {
    List<Class> classList = new ArrayList<>();
    boolean addClass = false;

    public List<Class> getClassList() {
        return classList;
    }

    public void setClassList(List<Class> classList) {
        this.classList = classList;
    }

    public void parse(InputStream input) throws ParserConfigurationException, SAXException, IOException {
        // creation du parser SAX
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        SAXParser parser = factory.newSAXParser();
        // lancement du parsing en précisant le flux et le "handler"
        // l'instance qui implémente les méthodes appelées par le parser
        // dans notre cas: this
        parser.parse(input, this);
    }

    public void parse(String filename) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
        parse(new FileInputStream(filename));
    }

    @Override
    public void startDocument() throws SAXException {
        // initialisation
        classList = new ArrayList<Class>();
    }

    @Override
    public void endDocument() throws SAXException {

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("persistence")) {
        } else if ("class".equals(localName)) {
            addClass = false;
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws
            SAXException {
        if ("persistence".equals(localName)) {

        } else if ("class".equals(localName)) {
            addClass = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // TODO Auto-generated method stub
        String value = new String(ch, start, length);
        if (addClass) {
            try {
                classList.add(Class.forName(value));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
}
