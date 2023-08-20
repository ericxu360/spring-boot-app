# spring-boot-app

A simple application for managing users, cats, and vomit incidents.  Each user owns cats, and each cat can have mutliple incidents.  This is the backend for the application.

Requirements:
PostgreSQL 15.4
MongoDB 6.0.9
Maven 3.8.7
cURL (for testing)
Docker 24.0.5 (if desired)
Docker Compose 2.18.1 (if desired)

API:
For all endpoints besides /api/login, the cookie "AppsessionID" must be set with a value contained within the cookie returned by /api/login.
If this does not happen the endpoint will return with a 403.
The session expires after 10 minutes after the last API call made with the session, which is configurable.

/api/cats: 
  POST: creates and returns a new cat.  The ID is irrelevant.
  GET: gets a list of cats owned by the user associated with the session cookie.
/api/cats/{id}:
  GET: gets the cat with this id.
  DELETE: deletes the cat (if authorized to do so), no return value.
  PATCH: update the cat (if authorized to do so), returns the new cat
/api/cat-image:
  POST: Provide a cat's ID and a JPG, associates a cat with a picture (internally stores the document ID from mongodb image DB)
/api/cat-image/{id}:
  GET: Retrieve a cat's image.  Returns the image as a JPG, or nothing if there is no image.
/api/incidents:
  POST: creates and returns a new incident.  The ID is irrelevant.
  GET: gets a list of incidents owned by the user associated with the session cookie.
/api/incidents/cat/{id}:
  GET: gets a list of incidents owned by the user associated with the session cookie that belong to the cat with ID id.
/api/incidents/{id}:
  GET: gets the cat with this ID
  DELETE: deletes the incident (if authorized to do so), no return value.
  PATCH: update the incident (if authorized to do so), returns the new incident.  The cat associated with the incident cannot be changed.
/api/login:
  POST: provide the user/password, and the endpoint will return a cookie with the session ID and the username.
/api/logout:
  POST: Invalidates the session provided
/api/users:
  POST: creates a new user
/api/users/{id}:
  GET: Gets the user information from their username
  PATCH: Updates the user's password
  DELETE: deletes the user
