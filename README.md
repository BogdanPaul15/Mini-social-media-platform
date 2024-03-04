# Description
The `TemaTest` package contains:
- an `App` class - manages commands from the command line and returns a validator;
- the `Likeable` interface - provides functionality to like and unlike;
- a `JsonHandler` class - manipulates JSON files;
- a `Validator` class - represents a response;
- a `User` class - manages users and their interactions;
- a `Post` class - manages posts and implements the `Likeable` interface;
- a `Comment` class - manages comments and implements the `Likeable` interface.

## App

### Usage

- JSON files for storing data about users, posts, and comments;
- Upon execution, it loads the data from the specified JSON files for users, posts, and comments.
- Accesses and modifies this data based on the command received from the command line, then updates the corresponding JSON files with the modified data.

### Methods:
- `getParam`: returns the content between two apostrophes;
- `replaceWithSingle`: replaces double quotes with single quotes in a string of characters and places a space before the open square bracket;
- `wrapIntegersInQuotes`: this method wraps all integers in a JSON string with single quotes.

## Likeable

### Usage

The `Likeable` interface provides two methods:
- `like`: Allows the user to like an element;
- `unlike`: Allows the user to unlike an element.

## JsonHandler

### Usage

The `JsonHandler` class contains the following methods:

### `jsonToArrayList(String filePath, Class<T[]> clazz)`

- Converts a JSON file to an `ArrayList` of objects of the specified type.
- Parameters:
    - `filePath`: The path to the JSON file;
    - `clazz`: The type of object to convert from JSON.

### `writeClassAsJson(T object)`

- Serializes a specific object into a JSON string.
- Parameters:
    - `object`: The object to be serialized into JSON.

### `writeToFile(String text, String filePath)`

- Writes a JSON string to a file.
- Parameters:
    - `text`: The JSON string to be written to the file;
    - `filePath`: The path to the file to write to.

## Validator

### Usage

The `Validator` class has the following attributes:

- `status`: A string to indicate the validation status;
- `message`: An object containing a message associated with the result.

This class returns the validation results of the commands received within the application and provides the ability to directly transform it into a JSON object.

## User

### Main Methods:

- `addCreatedPost`: Adds a post ID created by the user;
- `updateNumberOfFollowers`: Updates the number of followers for the user;
- `deletePost`: Deletes a post with a specific ID;
- `checkValidPost`: Checks if the user created the post with the specified ID;
- `checkValidUserFollow`: Checks if the user can follow a user with a certain username;
- `checkValidUserUnfollow`: Checks if the user can unfollow a user with a certain username;
- `follow`: Follows a specified user by username;
- `unfollow`: Unfollows a specified user by username;
- `addComment`: Adds a comment to a specified post;
- `deleteComment`: Deletes a comment with a specific ID;
- `checkValidComment`: Checks if the user created the comment with the specified ID;
- `alreadyLikedPost`: Checks if the user has already liked the post with the specified ID;
- `alreadyLikedComment`: Checks if the user has already liked the comment with the specified ID;
- `getMostFollowedUsers`: Returns the top 5 most followed users;
- `getMostLikedUsers`: Returns the top 5 most liked users.

## Post

### Main Methods:

- getters and setters;
- like and unlike;
- `updateNumberOfLikes`: Updates the number of likes for the post;
- `updateNumberOfComments`: Updates the number of comments for the post.

### Other Methods:

- top most liked or commented posts (`getMostLikedPosts`, `getMostCommentedPosts`);
- posts of a specific followed user or followed users (`getUserPosts`, `getFollowingPosts`);
- `getPostDetails`: Returns details about a specific post, including associated comments.

## Comment

### Main Methods:

- getters and setters;
- like and unlike.
