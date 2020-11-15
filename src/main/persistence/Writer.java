package persistence;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Account;
import model.ShoppingCart;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/* This class writes data into respective Json files, using the Gson library, and GsonBuilder in particular
  reference: https://howtodoinjava.com/gson/
             in sections: "Serialize and Deserialize JSON" and "GsonBuilder Configuration Examples"  */
public class Writer {

    /* EFFECTS: writes an account or a item list or a shopping cart into respective Json file,
                or throws an exception if the parameter passed in is not of those three types */
    public void writeData(Object o) throws IOException {
        FileWriter fileWriter;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (o.getClass() == Account.class) {
            fileWriter = new FileWriter("./data/Account.json");
        } else if (o.getClass() == ArrayList.class) {
            fileWriter = new FileWriter("./data/Items.json");
        } else if (o.getClass() == ShoppingCart.class) {
            fileWriter = new FileWriter("./data/ShoppingCart.json");
        } else {
            throw new IOException("Programming error");
        }
        gson.toJson(o, fileWriter);
        fileWriter.close();
    }




}
