package persistence;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.Account;
import model.Item;
import model.ItemWithDiscount;
import model.ShoppingCart;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/* This class reads Json files into required objects, using the Gson library
  reference: https://howtodoinjava.com/gson/
             in sections: "Serialize and Deserialize JSON" and "Parse JSON array to Java array or list" */
public class Reader {

    // EFFECTS: parses the list of items from file
    public ArrayList<Item> readItems(String filepath) throws IOException {
        /* a workaround to avoid issues from type erasure
           reference: https://stackoverflow.com/questions/27253555/
           com-google-gson-internal-linkedtreemap-cannot-be-cast-to-my-class/52224425#52224425  */
        Type myType = new TypeToken<ArrayList<ItemWithDiscount>>(){}.getType();

        FileReader fileReader = new FileReader(filepath);
        Gson gson = new Gson();
        ArrayList<Item> items = gson.fromJson(fileReader, myType);
        fileReader.close();
        return items;

    }

    // EFFECTS: parses the account from file
    public Account readAccount(String filepath) throws IOException {

        FileReader fileReader = new FileReader(filepath);
        Gson gson = new Gson();
        Account account = gson.fromJson(fileReader, Account.class);
        fileReader.close();
        return account;
    }

    // EFFECTS: parses a list of item names in previous shopping cart from file
    public List<String> readNames(String filepath) throws IOException {
        FileReader fileReader = new FileReader(filepath);

        /*  iterate over ShoppingCart.json to extract the names of the items in cart,
            in order to re-construct the shopping cart, bypassing the type erasure problem
          reference:
          https://stackoverflow.com/questions/45448933/how-to-iterate-over-json-data-with-gson
         */
        JsonObject jsonObject = JsonParser.parseReader(fileReader).getAsJsonObject();
        final JsonArray data = jsonObject.getAsJsonArray("cart");

        List<String> list = new ArrayList<>();

        for (JsonElement element : data) {
            list.add(((JsonObject) element).get("name").getAsString());
        }
        fileReader.close();
        return list;
    }
}
