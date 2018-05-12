package app.server.TableDemoStocks_SwingTests;

//ALLIKAS: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/TableDemoProject/src/components/TableDemo.java

//ALGSET NÄIDET ON VEIDI MUUDETUD, UPDATE'IMISE OSA ON ISE LISATUD, TABLE MODEL TÕSTETUD ERALDI KLASSI.

//Allika copyright notice:
//----------------------------------------------------------------------
/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
//----------------------------------------------------------------------

import app.server.Iu;
import app.server.Portfolio;
import app.server.UpdatingPrices;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * TableDemo is just like SimpleTableDemo, except that it
 * uses a custom TableModel.
 */
public class TableDemo extends JPanel { //

    private static JTable table; //LISATUD
    private static MyTableModel model; //LISATUD


    //TABLE MODEL ISENDI LOOMINE:
    public TableDemo(Object[][] data) { //arg. lisatud
        super(new GridLayout(1,0));

        //JTable table = new JTable(new MyTableModel());
        this.model=new MyTableModel(data);
        this.table=new JTable(model); //LISATUD
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        add(scrollPane);
    }


    //ALGNE TABEL:
    public static void createAndShowGUI(JsonObject masterAsJson) throws InterruptedException {


        JsonObject portfObj=masterAsJson.get("portfolioStocks").getAsJsonObject();
        int noOfStocks=portfObj.size();
        int noOfColumns=portfObj.get("AAPL").getAsJsonObject().size();

        Object[][] data = new Object[noOfStocks][noOfColumns];

        int i=0;
        for (String symbol : portfObj.keySet()) {
            JsonObject stockObj=portfObj.get(symbol).getAsJsonObject();
            data [i][0]=stockObj.get("symbol").getAsString();
            data [i][1]=stockObj.get("currentPrice").getAsDouble();
            data [i][2]=stockObj.get("previousClose").getAsDouble();
            data [i][3]=stockObj.get("change1Year").getAsDouble();
            data [i][4]=stockObj.get("change1Month").getAsDouble();
            data [i][5]=stockObj.get("change3Month").getAsDouble();
            data [i][6]=stockObj.get("dividendYield").getAsDouble();
            data [i][7]=stockObj.get("eps").getAsDouble();
            data [i][8]=stockObj.get("peRatio").getAsDouble();
            data [i][9]=stockObj.get("marketCap").getAsInt();
            data [i][10]=stockObj.get("shortRatio").getAsDouble();

            i++;
        }

        //Create and set up the window.
        JFrame frame = new JFrame("AVAILABLE STOCKS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        TableDemo newContentPane = new TableDemo(data);

        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);

    }

    //UPDATE'ITUD TABEL:
    public static void updateGUI(JsonObject priceUpdateObj){ //'double change' KUSATUD VAID TESTIMISEKS

        Object[][] newData= model.getData();

        for (int i = 0; i < newData.length; i++) {
            String symbol= (String) newData[i][0];
            double newPrice=priceUpdateObj.get(symbol).getAsDouble();

            //---------------------------------------
            //NEED KAKS RIDA LISATUD TESTIMISEKS!!  //SEE RIDA LISATUD TESTIMISEKS!!
             // (ET SAAKS HINDADE MUUTUMIST TESTIDA KA SIIS, KUI BÖRS ON KINNI):
            newPrice= (double) newData [i][1]+1.2;
            System.out.print(newPrice+" ");
            //---------------------------------------

            newData [i][1]=newPrice;
            //model.setValueAt(newPrice, i, 1);
            //model.fireTableCellUpdated(i, 1);
        }
        System.out.println();


        model.setData(newData);
        table.setModel(model);
        model.fireTableDataChanged();

    }

    //MAIN MEETOD:
    public static void main(String[] args) throws InterruptedException, IOException {

        //MasterPortfolio JSON-ina:
        Iu handler = new Iu();

        //RUN IEX DATA COLLECTOR AS THREAD
        Thread dataCollector = new Thread(new UpdatingPrices(handler));
        dataCollector.start();

        //MasterPortfolio as JSON (nii nagu klient selle saab):
        Portfolio masterPortfolio = handler.getMasterPortfolio();
        String masterAsString = new Gson().toJson(masterPortfolio); //selline string saadetakse Client'ile
        JsonParser jp=new JsonParser();
        JsonObject masterAsJson=jp.parse(masterAsString).getAsJsonObject();

        //ALGNE TABEL:
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    createAndShowGUI(masterAsJson);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread.sleep(1000);  //Selle aja pärast tekib handlerisse esimene hindade update

        //UPDATE'ITUD TABEL:

        for (int i = 0; i < 20; i++) {  //TESTIMISEKS 5 korda!!!

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JsonObject priceUpdateForClients=handler.getPriceUpdateForClients();
                    updateGUI(priceUpdateForClients);
                }

            });

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
