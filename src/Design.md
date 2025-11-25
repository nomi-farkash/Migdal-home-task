library:
    books:
        1.GET-get all the books(optional:filter by:name/author/available)
        2.POST-add new book
        3.GET-read one book
        4.PUT-update book status(available/unavailable)
    users:
        1.POST-add new user
        2.PUT-update user details
        3.GET-a manager can see all the users
        4.GET-by details only the user can see his details
    borrows:
        1.POST-add a new borrow(by user)
        2.GET-can get all the borrows by user
        3.PUT-update the borrow when user returned the book/s
        4.GET-get a borrow(the last one)
    dto:
        books:
            id(auto-string),name(a must,string),author(string),status(boolean,a must)
        users:
            id(auto-string),first name(a must,string) last name(a must,string),phone(a must,string),email(string)
        borrows:
            id(auto,string),date(auto,Date),user(a must,User),status(a must,boolean),returnDate(initial as null,Date)

Orders:
    departments: 
        1.orders-add/remove items,calculate amount ,update status(NEW->PAID/CANCELED)
          customers-update details,provide details for orders
          payments-keep the status of the payment(FAILED/SUCCESSES),connect the payment to order(by order id),verify the amount is suitable to order
          items-calculate amount*price,keep item details in an order(snapshot of price in the order time),available
    * 1.Idempotency key(the customer send to the server on each payment)
      2.Each payment has orderID(a FK) a must
      3.checks if the payment equal the order amount
      4.can pay only if the order status is NEW(not PAID/CANCELED)
      5.create the order and the payment in a transaction
      6.logs for each api