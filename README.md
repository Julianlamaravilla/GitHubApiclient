# GitHub User Activity

A simple command-line app, written in plain Java with no external libraries, that fetches a GitHub user's recent public activity and prints it in the terminal.

Project idea from roadmap.sh: https://roadmap.sh/projects/github-user-activity

## Features

- Fetches recent events from the GitHub API (`https://api.github.com/users/<username>/events`)
- Parses the JSON response by hand, without a JSON library
- Prints a readable summary of each event (pushes, issues, comments, pull requests, stars, forks, created branches/repos)
- Handles errors: user not found, API errors, and connection timeouts

## Requirements

- Java 17 or newer (uses switch expressions and `java.net.http.HttpClient`)

## How to run

Run these commands from the project folder:

```bash
# 1. Compile
javac -d out src/*.java

# 2. Run, passing a GitHub username
java -cp out GitHubActivity <username>
```

Example:

```bash
java -cp out GitHubActivity torvalds
```

On Windows PowerShell you can use `src\*.java` instead of `src/*.java`.

## Example output

```
- Pushed 1 commit to torvalds/linux
- Commented on an issue in torvalds/GuitarPedal
- Merged a pull request in torvalds/GuitarPedal
- Pushed 1 commit to torvalds/GuitarPedal
```

If the user has no recent public activity:

```
No recent activity found for user 'octocat' .
```

## Project structure

| File | Responsibility |
|---|---|
| `src/GitHubActivity.java` | Entry point; connects the API client, parser, and models |
| `src/GitHubApiClient.java` | Sends the HTTP request to the GitHub API and handles status codes |
| `src/JsonParserUtil.java` | Splits the JSON array into events and extracts values |
| `src/GitHubEvent.java` | Represents one event and formats it for output |

## Notes

- The GitHub API only returns public events from the last 90 days.
- Unauthenticated requests are limited to 60 per hour per IP address.
