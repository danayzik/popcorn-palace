
# API Changes and Error Handling Documentation

## Changes from Original API:
- **POST requests to add an entry now return a `201 Created` status** instead of the previous `200 OK` status.

---

## Error Handling

### 1. Add a Movie (POST /movies)

#### Request Body:
```json
{
  "title": "Sample Movie Title",
  "genre": "Action",
  "duration": 120,
  "rating": 8.7,
  "releaseYear": 2025
}
```

#### Constraints:
- **Title**: Must not belong to another movie in the database. If it does, the API returns a `400 Bad Request`.
- **Title** and **Genre**: Must be non-empty.
- **Duration**: Must be greater than `0`.
- **Rating**: Must be between `0` and `10` (inclusive).
- **Release Year**: Must be greater than `0`.

#### Possible Responses:
- `201 Created` (success)
- `400 Bad Request`: If the title already exists in the database or if any of the constraints are violated.

---

### 2. Update a Movie (POST /movies/update/{movieTitle})

#### Request Body:
```json
{
  "title": "Sample Movie Title",
  "genre": "Action",
  "duration": 120,
  "rating": 8.7,
  "releaseYear": 2025
}
```

#### Constraints:
- **Movie Title**: If the `movieTitle` does not exist in the database, the API returns `404 Not Found`.
- **Title** and **Genre**: Must be non-empty.
- **Duration**: Must be greater than `0`.
- **Rating**: Must be between `0` and `10` (inclusive).
- **Release Year**: Must be greater than `0`.

#### Possible Responses:
- `200 OK` (success)
- `404 Not Found`: If the movie title is not found.
- `400 Bad Request`: If the title already exists in the database or if any of the constraints are violated.

---

### 3. Delete a Movie (DELETE /movies/{movieTitle})

#### Constraints:
- If the **Movie Title** does not exist in the database, the API returns `404 Not Found`.

#### Possible Responses:
- `200 OK` (success)
- `404 Not Found`: If the movie title is not found.

---

### 4. Get Showtime by ID (GET /showtimes/{showtimeId})

#### Constraints:
- If the **showtimeId** does not exist in the database, the API returns `404 Not Found`.

#### Possible Responses:
- `200 OK` (success)
- `404 Not Found`: If the showtime ID is not found.

---

### 5. Add a Showtime (POST /showtimes)

#### Request Body:
```json
{
  "movieId": 1,
  "price": 20.2,
  "theater": "Sample Theater",
  "startTime": "2025-02-14T11:47:46.125405Z",
  "endTime": "2025-02-14T14:47:46.125405Z"
}
```

#### Constraints:
- **Movie ID**: Must exist in the database. If not, the API returns `404 Not Found`.
- **Price**: Must be greater than or equal to `0`.
- **Theater**: Must be non-empty.
- **Start Time**: Must be before the `endTime`.
- **End Time**: Must be after the `startTime`.
- **Bad fields**: If any fields are incorrect, the API returns `400 Bad Request`.
- **No overlapping showtimes**: Times must not overlap with another showtime in the same theater.
- (Currently unenforced constraint)**Running time of movie**: The showtime duration must be long enough to accommodate the movie’s runtime.
- **endTime and startTime**: Times must be in UTC in the example format.

#### Possible Responses:
- `201 Created` (success)
- `400 Bad Request`: If constraints are violated.
- `404 Not Found`: If the movie ID doesn't exist.

---

### 6. Update a Showtime (POST /showtimes/update/{showtimeId})

#### Constraints:
- Same as **Add a Showtime**, but for updating an existing showtime.

#### Possible Responses:
- `200 OK` (success)
- `400 Bad Request`: If constraints are violated.
- `404 Not Found`: If the showtime ID doesn't exist.

---

### 7. Delete a Showtime (DELETE /showtimes/{showtimeId})

#### Constraints:
- If the **showtimeId** does not exist in the database, the API returns `404 Not Found`.

#### Possible Responses:
- `200 OK` (success)
- `404 Not Found`: If the showtime ID is not found.

---

### 8. Book a Ticket (POST /bookings)

#### Request Body:
```json
{
  "showtimeId": 1,
  "seatNumber": 15,
  "userId": "84438967-f68f-4fa0-b620-0f08217e76af"
}
```

#### Constraints:
- **Showtime ID**: Must exist in the database. If not, the API returns `404 Not Found`.
- **Seat Number**: Cannot be the same for two bookings with the same showtime ID. If the seat number is already booked, the API returns `400 Bad Request`.
- **userId**: Must be a UUID

#### Possible Responses:
- `201 Created` (success)
- `400 Bad Request`: If the seat number is already taken or another constraint is violated.
- `404 Not Found`: If the showtime ID doesn't exist.

---
