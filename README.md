# realestate
A backend part of a real-estate application.


Creating a backend part of a real estate application at PROGmasters.

We worked in a team of two to develop the backend of a real estate application. In addition to the basic functions, the functions were the following:

- the owner has the opportunity to register as the first administrator, after that, the link is not active;

- users, with the appropriate validations, have the opportunity to register with an activation link and a time limit, and if they do not respond, they are deleted from the database;

- users, if requested, receive a newsletter every 24 hours about the properties registered within 24 hours, which they can also unsubscribe from;

- the data is queried from several tables at the same time in an orderly manner, paginated, and filtered by minimum and maximum values, even if not everything is entered at the same time;

- it is possible to upload images for the property and the user, either in URL form or locally from the user's computer, which is arranged in separate, appropriate folders on the Cloudinary storage, and the images can also be deleted. The URL addresses of uploaded images are stored in separate tables;

- the possibility of payment through the PayPal application is provided;

- the use of ChatGPT is also built-in;

- a roulette-based prize game can also be played;

- there is an esatet_agent role and a comment can be written to them;

- after selling a property, an estate agent gets points;

- logging;

- creating a PDF of the data of a specific property;

- the price of the property could be converted to the current exchange rate of any currency;

- the current weather data could also be requested.

We used a Docker container to store data and had a test database for integration tests, which was also stored in such a container. We prepared 151 tests, of which 46 were unit tests and 105 were integration tests. Our test coverage was 71%.
