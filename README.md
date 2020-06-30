
THE CHECKOUT MICROSERVICE

Considerations:

- The service has not a DAO connection due that the same was thought as a mock service and the deadline was not that long to go deep into the database connections configuration. All the repositories were built using the JVM memory.
- The service is composed of three spring-boot applications(checkout-service,logistic-service,billing-service) that are maven based.
- The Id of the order is assigned automatically by the service, from 100000 onwards for the first created order.


DOCUMENTATION

1. Explanation of the usage, for testing purposes.

The Logistic Service:
This service was thought to perform some operations related to fixing the address of the clients who create orders. It has only the service and the controller layers as well as the entites that are related to the one. The service only provides with the method "getOrderToShip" in teh controller side, which takes the client name, the order id, and the order in a specific format in order to process it and get back with the adress in US format, assuming that the application will be used in the US territory. However it can be modified in the code or could be expanded to be applicable to other places such as Europe, if needed. Staring from the "getOrderToShip" you can drill down up to the service layer to figure out how the calculations are done.  

To make use of this server alone, only for testing purposes, one can use either "localhost" or "logistic" of in following urls:

http://localhost:8092/logistic/orders/1/David/1
http://logistic:8092/logistic/orders/1/David/1
Where the format of the url is:
http://logistic:8092/logistic/orders/{clientId}/{userName}/{orderId}

Given by the POST request in the getOrderToShip method.

On the other hand, as the method requests a body you should pass one JSON as the following:

{
    "userId":"1",
    "userName":"David",
    "address":"David, 25 san Jose, Wills Street, 255, San Francisco, CF, 45532",
    "theDetailedSum": {"1": 40.0,"2": 10.0},
    "theTotal": {"Total": 50.0}
}

The Billing Service:
The billing service is the one that calculates the sum of the products, grouping them by the Id, or summing all them up. This service is composed of controller, service and repository layers, as well as the entities that are the base of this one. From the controller, viewing the methods "getSumOfProducts" and "getTotal", one can drill down up to the service and understand the logic of the algorithms that are in charge of the calculation of totals and processing of data that is required. So far, one can get concerned by the usage of the service, in case that one decide to use this alone, for testing purposes or somehow. Thus, here is an example of a request to use the service:

To make use of this server alone, only for testing purposes, one can use either "localhost" or "billing" of in following urls:
Take into account that these urls point to a POST request in the controller of the billing service.

http://billing:8091/products/total/1 = http://billing:8091/products/total/{clientId}
http://billing:8091/products/total-per-product/1 = http://billing:8091/products/total-per-product/{clientId}

On the other hand, as the method requests a body you should pass one JSON as the following:

{
    "products":[
    {
    "id":"1",
    "quantity":"2",
    "cost":"30"
    },
    {
    "id":"2",
    "quantity":"3",
    "cost":"10"
     }
    ]
}

The Checkout Service:
This is the service that integrates the billing and logistic services, by mapping requests into them, so that we can receive a correctly formatted ad summarized order, 
allowing the JavaScript developer to map correctly the fields so that they can be seen correctly by the users of the application, think of them as couriers that need 
to deliver some order to the customer. This service is composed of the controller, service, and repository layers as well as the entities. Here are the methods that 
integrate this service in the controller layer:

1.1 createOrder
1.2 getAllOrders
1.3 getOrderByOrderId
1.4 updateOrder
1.5 deleteOrder

From there, you can go down to the service and repository layers, so that you can understand the logic better.

The following request calls to the "createOrder" method which has POST request in the controller, as its name points out, it creates an order.

POST
http://checkout:8093/checkout/create-order
http://localhost:8093/checkout/create-order

The method requires a body such as this: 

{
    "clientId":2,
    "clientName":"Daniel",
    "date":"2020-06-29T09:44:46",
    "address":"Daniel, 25 san Jose, Wills Street, 255, San Francisco, CF, 45532",
    "productsList":[{"id":"1","quantity":"2","cost":"30"},{"id":"2","quantity":"3", "cost":"10"}]
}

The following request calls to the "updateOrder" method which has POST request in the controller. It updates and already created order, if the order has not been created, it returns the Http.NO_CONTENT exception.

POST
http://checkout:8093/checkout/update-order/100000 = http://checkout:8093/checkout/update-order/{orderId}
http://localhost:8093/checkout/update-order/100000

{
    "clientId":3,
    "clientName":"Jose",
    "date":"2020-06-29T09:44:46",
    "address":"Daniel, 25 san Jose, Wills Street, 255, San Francisco, CF, 45532",
    "productsList":[{"id":"1","quantity":"3","cost":"30"},{"id":"2","quantity":"4", "cost":"10"}]
}

The following request calls to the "getAllOrders" method in the controller which has GET request attached. It diaplays tha created orders that a specific user has submitted.If either the order has not been created or the client identified with that client id has not created orders, it returns the Http.NO_CONTENT exception.

GET
http://checkout:8093/checkout/view-all-orders/2 = http://checkout:8093/checkout/view-all-orders/{clientId}
http://localhost:8093/checkout/view-all-orders/2

The following request points to the "getAllOrders" method in the controller which has GET request attached. It diaplays all the orders, no matter the client that has created them. If there are no orders in the repository or history of orders, it returns the Http.NO_CONTENT exception.

GET
http://checkout:8093/checkout/view-all-orders
http://localhost:8093/checkout/view-all-orders

The following request points to the "getOrderByOrderId" method in the controller which has GET request attached. It diaplays an order depending on the id that is passed as pathvariable. If there are no orders that match the id passed, it returns the Http.NO_CONTENT exception. This request could be useful if one want update an order, due that it provides with the same format that is required to update it, and it also has embedded the data that is required, in case that one do not want to modify the entire order.

GET
http://checkout:8093/view-order-summary/100000 = http://checkout:8093/view-order-summary/100004/{orderId}
http://localhost:8093/view-order-summary/100000

The following request points to the "getAllOrders" method in the controller which has DELETE request attached. It deletes an order, depending on the id that is passed as pathvariable. If there are no orders that match the id passed, it returns the Http.NO_CONTENT exception.

DELETE
http://localhost:8093/checkout/delete-order/100000 = http://localhost:8093/checkout/delete-order/100000/{orderId}
http://localhost:8093/checkout/delete-order/100000

To view examples of the happy path of the requests that have been explained you can go to the "./Purchase-orders-checkout-service/Evidence/SwaggerUI-Postman" folder.

2.Docker configuration

Before starting, please take a look to the "DockerFile" and the "application.properties", as well as the following URI variables:

- "uriSumTotalsPerProduct" and "uriSumTotals", both of them in the "billingCalculationFormatted" method of the CheckoutService.java class.
- uriToGetShippingOrder, inside the "logisticFixesToBill" method of the CheckoutService.java class.

The host of the URIs must be configured so that if match the {container-name} in the step 2.3.

Additionally, please run a maven clean install for each of the projects, so that the jar that is set in the "DockerFile" can be created or updated. Please make sure that the jar is present in the "Target" folder.


2.1 Create a network, so that all the service can be run inside them, is recommended to create its diver as Bridge.

docker network create {network-name}
e.g.
docker network create checkout-billing-logistic

2.2 Build an image for each service (checkout-service,logistic-service,billing-service). Make sure that you are inside the service directory e.g
"./Purchase-orders-checkout-service/checkout-service>", and do not forget the dot at the end of the command, it is a common error :(.

docker build -f {docker-file-name} -t {image-name} .
e.g.
docker build -f DockerFile -t billingservice .

2.3 Run the image in a container, executing the following command:

docker container run --network {network-name} --name {container-name} -p 8091:8091 {image-name}
e.g.
docker run --network checkout-billing-logistic --name billing -p 8091:8091 billingservice
docker run --network checkout-billing-logistic --name logistic -p 8092:8092 logisticservice
docker run --network checkout-billing-logistic --name checkout -p 8093:8093 checkoutservice

2.4 Stop the already running containers by executing.

docker container stop {container-name}
e.g.
docker container stop billing
docker container stop logistic
docker container stop checkout

If you need more help on this, please refer to the images in the "./Purchase-orders-checkout-service/Evidence/Docker" folder, so that you get clear about it.



Still have doubts?, feel free to contact me on ivan.david.galvis.sandoval@gmail.com

Thank you! :)
