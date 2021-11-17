package com.lymytz.entitymanager.tools.xml;

import android.text.Html;
import android.util.Log;

import com.lymytz.entitymanager.bean.Columns;
import com.lymytz.entitymanager.bean.Rows;
import com.lymytz.entitymanager.bean.Tables;
import com.lymytz.entitymanager.tools.Utils;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author Lymytz Dowes
 */
public class DataBaseHandler extends DefaultHandler {

    List<Tables> result = new ArrayList<>();
    Tables table = new Tables("");

    List<Rows> rows = new ArrayList<>();
    Rows row = new Rows(new ArrayList<Columns>());

    List<Columns> columns = new ArrayList<>();
    Columns column = new Columns("", "");

    boolean is_table = false;
    boolean is_row = false;
    boolean is_column = false;

    String tmpValue;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
        tmpValue = "";
        int position = com.lymytz.entitymanager.tools.Constantes.PERSISTENCE.getPosition(qName);
        if (position > -1) {
            is_table = true;

            table = com.lymytz.entitymanager.tools.Constantes.PERSISTENCE.getTable(position);
            rows = new ArrayList<>();
        } else {
            if (qName.equals("row")) {
                is_row = true;

                row = new Rows();
                columns = new ArrayList<>();
            } else {
                position = table.getColumns().indexOf(new Columns(qName, table.getName()));
                if (position > -1) {
                    is_column = true;

                    column = new Columns(qName, table.getName());
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        int position = com.lymytz.entitymanager.tools.Constantes.PERSISTENCE.getPosition(qName);
        if (position > -1) {
            is_table = false;
            table.setValues(rows);
            result.add(table);

            table = new Tables("");
            rows = new ArrayList<>();
        } else {
            if (qName.equals("row")) {
                is_row = false;
                row.setColumns(columns);
                rows.add(row);

                row = new Rows(new ArrayList<Columns>());
                columns = new ArrayList<>();
            } else {
                position = table.getColumns().indexOf(new Columns(qName, table.getName()));
                if (position > -1) {
                    is_column = false;
                    columns.add(column);

                    column = new Columns("", "");
                }
            }
        }
    }

    @Override
    public void characters(char[] data, int start, int end) {
        tmpValue += new String(data, start, end);
        if (is_column) {
            tmpValue = tmpValue.replace("Â°", "°");
            column.setValue(Utils.asString(tmpValue) ? Html.fromHtml(tmpValue).toString() : tmpValue);
        } else if (is_row) {

        } else if (is_table) {

        }
    }

    public List<Tables> getResult() {
        return result;
    }

    public DataBaseHandler create(File file) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            InputSource input = new InputSource(new FileInputStream(file));
            input.setEncoding("UTF-8");
            parser.parse(input, this);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return this;
    }

}