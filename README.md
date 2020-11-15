# Online Grocery Store

### *personal project CPSC 210*


This Java desktop application simulates a simplified online grocery store (like the Save-on-Food). Customers can 
add items to the shopping cart and make purchases. They can also receive and redeem reward points to enjoy discounts.
This form of shopping is convenient and
has become increasingly popular in strange times that we are currently facing. That's why I
find it interesting.

## User Stories:
- As a user, I want to be able to view the list of goods and add a new item to that list
- As a user, I want to be able to add items to the shopping cart or delete an item from the shopping cart
- As a user, I want to be able to view the shopping cart and make a purchase if having sufficient account balance 
- As a user, I want to be able to add an amount to the account
- As a user, I want to be able to receive and redeem reward points to enjoy discount for certain items
- As a user, when I select the quit option from the main menu, I want the option to save my account, 
  as well as the store's list of goods and shopping cart to file
- As a user, I want to be able to always load the store's list of goods 
  and my account, and optionally resume the shopping cart from file when the program starts

## Instructions for Grader
- You can trigger the cart GUI by "c -> resume and display shopping cart" 
  or "m -> make a purchase -> adjust cart" from the main menu
- (First select "All items" tab) You can select multiple items by pressing Ctrl while using mouse (right-click). 
  Then click "Add to cart" to add selected items to the shopping cart. (You can see the "Added" prompt)
- (First select "Shopping cart" tab) You can delete an item by clicking on the delete button.
- You can trigger my audio component by clicking on the "Add to cart" or "Save cart to file" button.
- You can save the state of my application by clicking on the "Save cart to file" button, 
  then close the window and restart program.  (You can see the "Saved" prompt)
- The shopping cart automatically resumes when the GUI is triggered. (You can see the "resumed" prompt)

## Phase 4: Task 2
- Option 1: the NotPositiveException is thrown by load() in Account in model, 
          and is caught by loadAccount() in GroceryStore in ui. The test cases are in the AccountTest. 
- (NotADoubleException is thrown and caught by methods in GroceryStore inside ui, so it's not tested)
- This task has been done at phase 2.

## Phase 4: Task 3
- (1).High semantic coupling and duplication between showAccount and loadAccount in GroceryStore 
  (print balance and reward points), and it's not a responsibility for GroceryStore, but more related to Account
   Changes: extract printAccount as a helper method inside the Account class  
- (2).Improve cohesion: createAndShowGUI is not really a responsibility for the ShoppingCart 
         (especially setting up the gui frame)
  Changes: move createAndShowGUI() to the CartGUI class, and calls it in GroceryStore 
