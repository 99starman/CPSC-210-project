package ui;

import model.Item;
import model.ShoppingCart;
import persistence.Writer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/*  This class manages the graphical user interface of ShoppingCart,
    which enables the user to view, adjust and save shopping cart.
   Adding JList, JPanel, JButton and JTabbedPane, and Event handling are based on
   https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
   including the ListDemo project, TabbedPaneDemo project and TabComponentsDemo project.
 */

class CartGUI  extends JPanel
        implements ListSelectionListener {

    private JTable tableItem;
    private JList listCart;
    private ShoppingCart cartReturn = new ShoppingCart();
    private ArrayList<Item> items;

    private DefaultListModel<String> listModelCart;


    private static final String deleteString = "Delete";
    private static final String addString = "Add to cart";
    private static final String saveString = "Save cart to file";
    private JButton button;
    private JButton deleteButton;
    private JLabel saveLabel;
    private JLabel addLabel;
    private JLabel reloadLabel;
    private JScrollPane scrollPaneItem;
    private JScrollPane listScrollPaneCart;
    private AudioInputStream audio;
    private Clip clip;

    public CartGUI(ShoppingCart cart, ArrayList<Item> items) {

        super(new BorderLayout());
        this.items = items;
        //Create a tabbed pane consisting of two panels: cart and item
        JTabbedPane tabbedPane = new JTabbedPane();
        JComponent panelCart = new JPanel();
        JComponent panelItem = new JPanel();

        loadData(cart, items);
        createJList();
        createJTable();
        deleteButton = createButtons(deleteString);
        JButton addButton = createButtons(addString);
        JButton saveButton = createButtons(saveString);

        createCartPanel(deleteButton, saveButton, panelCart);

        //Create an item panel
        JComponent buttonPaneItem = new JPanel();
        buttonPaneItem.add(addButton);
        buttonPaneItem.add(addLabel);

        panelItem.add(scrollPaneItem, BorderLayout.CENTER);
        panelItem.add(buttonPaneItem, BorderLayout.PAGE_END);



        tabbedPane.addTab("Shopping cart", null, panelCart, "Shopping cart");
        tabbedPane.addTab("All items", null, panelItem, "Items");
        add(tabbedPane);
    }


    // EFFECTS: load and play the audio
    // https://stackoverflow.com/questions/15526255/best-way-to-get-sound-on-button-press-for-a-java-calculator
    private void createAudio() {
        String soundName = "./data/mySound.wav";
        try {
            audio = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    //EFFECTS: Create a cart panel that uses BoxLayout.
    // https://docs.oracle.com/javase/tutorial/uiswing/layout/box.html
    private void createCartPanel(JButton deleteButton, JButton saveButton, JComponent panelCart) {

        reloadLabel = new JLabel("Shopping cart resumed!");

        JComponent buttonPaneCart = new JPanel();
        buttonPaneCart.setLayout(new BoxLayout(buttonPaneCart,
                BoxLayout.LINE_AXIS));
        buttonPaneCart.add(deleteButton);
        buttonPaneCart.add(Box.createHorizontalStrut(15));
        buttonPaneCart.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPaneCart.add(Box.createHorizontalStrut(15));
        buttonPaneCart.add(saveButton);
        buttonPaneCart.add(saveLabel);

        buttonPaneCart.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        panelCart.add(reloadLabel, BorderLayout.PAGE_START);
        panelCart.add(listScrollPaneCart, BorderLayout.CENTER);
        panelCart.add(buttonPaneCart, BorderLayout.PAGE_END);
    }

    //EFFECTS: Create buttons for adding, deleting and saving
    private JButton createButtons(String command) {

        button = new JButton(command);
        button.setActionCommand(command);
        button.setEnabled(true);
        if (command.equals(deleteString)) {
            button.addActionListener(new DeleteListener());
        } else if (command.equals(addString)) {
            button.addActionListener(new AddListener());
        } else {
            button.addActionListener(new SaveListener(items));
        }
        addLabel = new JLabel("Added!");
        saveLabel = new JLabel("Saved!");
        addLabel.setVisible(false);
        saveLabel.setVisible(false);
        return button;
    }


    //EFFECTS: Create a listCart and put it in a scroll pane for cart.
    // https://docs.oracle.com/javase/tutorial/uiswing/components/list.html
    private void createJList() {

        listCart = new JList(listModelCart);
        listCart.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listCart.setSelectedIndex(0);
        listCart.addListSelectionListener(this);
        listCart.setVisibleRowCount(5);
        listScrollPaneCart = new JScrollPane(listCart);
        listScrollPaneCart.setPreferredSize(new Dimension(300, 200));
    }

    ////EFFECTS: Create the JTable and a scroll pane for items
    private void createJTable() {

        tableItem.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tableItem.setRowSelectionAllowed(true);
        tableItem.getSelectionModel().addListSelectionListener(this);
        scrollPaneItem = new JScrollPane(tableItem);
        scrollPaneItem.setPreferredSize(new Dimension(520, 200));
    }


    // MODIFIES: listModelCart, tableItem
    // EFFECETS: load data into listModelCart and tableItem
    private void loadData(ShoppingCart cart, ArrayList<Item> items) {
        listModelCart = new DefaultListModel<>();
        for (int i = 0; i < cart.size(); i++) {
            listModelCart.addElement(cart.getI(i).getName());
        }

        //Create data for items
        int rowNum = items.size();
        String[][] data = new String[rowNum][5];

        for (int i = 0; i < rowNum; i++) {
            Item it = items.get(i);
            data[i][0] = String.valueOf(i + 1);
            data[i][1] = it.getName();
            data[i][2] = String.valueOf(it.getPrice());
            data[i][3] = it.getCategory();
            if (it.getDiscount() == 0) {
                data[i][4] = "No discount";
            } else {
                data[i][4] = String.valueOf(it.getDiscount());
            }
        }
        String[] colNames = {"ID", "Name", "Price (CAD)", "Category", "Discount amount"};
        tableItem = new JTable(data, colNames);
    }

    // This class  handles the event of saving
    // based on the listDemo project
    class SaveListener implements ActionListener {
        private ArrayList<Item> items;

        public SaveListener(ArrayList<Item> items) {
            this.items = items;
        }

        // MODIFIES: cartReturn, saveLabel
        // EFFECTS: returns current items in cart and writes them into file
        public void actionPerformed(ActionEvent e) {
            int length = listModelCart.getSize();
            if (length > 0) {

                for (int i = 0; i < length; i++) {
                    for (Item it : items) {
                        if (it.getName().equals(listModelCart.get(i))) {
                            cartReturn.addItem(it);
                        }
                    }
                }
            }
            try {
                Writer writer = new Writer();
                writer.writeData(cartReturn);
                saveLabel.setVisible(true);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            createAudio();

        }
    }


    // This class  handles the event of deleting
    // based on the listDemo project
    class DeleteListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            int index = listCart.getSelectedIndex();
            listModelCart.remove(index);

            int size = listModelCart.getSize();

            if (size == 0) { //No item left, disable deleting.
                deleteButton.setEnabled(false);

            } else { //Select an index.
                if (index == listModelCart.getSize()) {
                    //removed item in last position
                    index--;
                }

                listCart.setSelectedIndex(index);
                listCart.ensureIndexIsVisible(index);
            }
        }
    }


    // This class  handles the event of adding
    // based on the listDemo project
    class AddListener implements ActionListener {

        //Required by ActionListener.
        public void actionPerformed(ActionEvent e) {
            int[] index = tableItem.getSelectedRows();
            for (int i: index) {
                String name = (String) tableItem.getValueAt(i, 1);
                listModelCart.addElement(name);
            }
            addLabel.setVisible(true);
            createAudio();
        }

    }


    // This method is required by ListSelectionListener, based on the listDemo project

    // MODIFIES: deleteButton
    // EFFECTS: enable delete button if selected
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {

            if (listCart.getSelectedIndex() == -1)  {
                //No selection, disable delete button.
                deleteButton.setEnabled(false);

            } else {
                //Selection, enable the delete button.
                deleteButton.setEnabled(true);
            }
        }

    }


    // EFFECTS: sets up a new GUI frame
    public static void createAndShowGUI(ShoppingCart cart, ArrayList<Item> items) {


        //Create and set up the window.
        JFrame frame = new JFrame("Shopping cart GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(550, 400));


        //Create and set up the content pane.
        JComponent newContentPane = new CartGUI(cart, items);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }






}
