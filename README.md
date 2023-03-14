# hotel-cancun
Booking API for a hotel

This a an API that makes reservations to the only hotel left after covid-19 pandemia, and this hotel has only one room
that can be booked , but not more than 30 days ahead and also the reservation cannot be for more than 3 days.


# Configuration and deploy
  
  The REST API is already deployed on railway and can be acessed by the url : 
  
  https://hotel-cancun-production.up.railway.app/swagger-ui.html#/
  
  Through swagger you can do the testing of all endpoints.
  
# Endpoints

- GET - /api/v1/booking/{id} - Find a booking by its id
- GET - /api/v1/booking/findall - Find all booking list
- POST - /api/v1/booking/create - Booking a new reservation
- UPDATE - /api/v1/booking/update/{id} - Update a booking by its id
- DELETE - /api/v1/booking/delete/{id} - Delete a booking by its id

# Database 

The database used is MYSQL and the properties for localhost installation are bellow.

datasource.url=jdbc:mysql://localhost:3306/hotel?createDatabaseIfNotExist=true&autoReconnect=true&useSSL=false&useTimezone=true&serverTimezone=America/Sao_Paulo

datasource.username=root

datasource.password= 'your local password'


    
    
