# parking-lot-service
A simple service to eceive incorrect messages on predefined queues and saving them in order to correct them and resend them to the original exchange.
The service receives messages from RabbitMQ which are not correct or accepted by other services and sent to the parkinglot queue. Then the messages get saved in MongoDB. After that, through the GUI provided by the service, the user can display the saved messages depending on many filters and correct them, before resending them to the original exchange.
