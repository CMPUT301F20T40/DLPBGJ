NOTE : 

The location attribute used (in User and Request ) is an object of type Location. We have created a different Location class to store a location given the longitude and latitude. Using a class to store the location will make it easier for us to use the location whenever a borrow/return takes place and we can also link a different location to each user.

The picture attribute used in books and users is of type Image which is a class that we have used to store different images. Using a class in this will make it easier for us to link each image to a User or Book. 

The Home_Activity is where the user searches for books. It displays a ListView and 4 buttons. This leads to 4 new activities ( BorrowedBooks, RentedOutBooks, View_Edit_Profile, and Request_Activity ) 

Request_Activity has been used to process requests. It takes an object of type Request, this object holds all the important information like owner,borrower,Book,etc. 






