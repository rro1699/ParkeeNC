# ParkeeNC app
## Description
This repository represent a service with a minimalist and user-friendly interface that allows users to book parking spaces.
The service also allows users to cancel reservations, view the cost of reservations and view the history of previous reservations.
This repository is a back-end part of parking booking app.
Repository of the front-end part of parking booking app.(https://github.com/komrade2801/NC_Spring) 

## Functional requirements
- Authorization via Google API
- Parking place details displaying
- Adding/Removing a booking
- Getting users history of bookings
- Getting information of booking cost 
- Displaying parking map with bookings on it

## Architecture
- Client-server architecture with REST
- Object-relational database
- Server-side on Spring boot
- System logging using Slf4j

## Technology stack
- Spring boot
- Java 11
- Hibernate
- Lombok
- MySQL

## Project components interaction
![Components interaction](https://user-images.githubusercontent.com/79422421/173038677-aa1545bd-cfcb-487a-af31-33073afc02c3.png)

## Application functionality demonstration
![Main page app](https://user-images.githubusercontent.com/79422421/173041728-b00f6e77-d555-4dd0-8668-b3f65ed718a2.png)<br> *Pic 2. Main page.* <br>
![Authorization](https://user-images.githubusercontent.com/79422421/173044455-b9359c75-e13a-4c81-9bf5-79116f947f63.png)<br> *Pic 3. Authorization page.* <br>
![Creating new booking](https://user-images.githubusercontent.com/79422421/173044744-5ba5480c-2989-4c1b-8199-b4bc2e474e69.png)<br> *Pic 4. Creating new booking.* <br>
![Current bookings](https://user-images.githubusercontent.com/79422421/173044981-77d83b4f-1d84-4e28-9b9b-9e526bcb115c.png)<br> *Pic 5. Current bookings.* <br>
![History of bookings](https://user-images.githubusercontent.com/79422421/173045204-4d9ee832-0de5-44e9-a48d-40b8b904a712.png)<br> *Pic 6. History of bookings.* <br>

## Project execution plan
- Creating Spring boot app :white_check_mark:<br>
- Designing and building a MySQL database :white_check_mark: <br>
- Deploy database on Heroku :white_check_mark:<br>
- Creating DAO layer
  - Creating Models :white_check_mark:<br>
  - Creating Repositories :white_check_mark:<br>
- Creating Controllers layer :white_check_mark:<br>
- Creating Service layer :white_check_mark:<br>
- Adding WebSockets :white_check_mark:<br>
- Adding app config, CORS's config, WebSocket's config :white_check_mark:<br>
- Adding logging :white_check_mark:<br>



