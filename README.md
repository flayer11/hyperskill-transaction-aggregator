# Transaction Aggregator

My solution to the [Transaction Aggregator](https://hyperskill.org/projects/424) project on Hyperskill (JetBrains Academy), completed in November 2026.

The project is part of my Java/Spring learning path. This repository contains my final solution only; the course's task descriptions are not included.

## What it does

A small Spring Boot service with a single endpoint:

```
GET /aggregate?account=<accountId>
```

It fetches an account's transactions from two upstream data services, merges both lists and returns them sorted by timestamp (newest first). The upstream services are deliberately unreliable and slow, so the service retries failed requests, queries both services in parallel and caches results.

## Stages

| # | Stage          | What I implemented                                         | Concepts                                              |
|---|----------------|------------------------------------------------------------|-------------------------------------------------------|
| 1 | Send a ping    | First REST endpoint                                        | Spring Boot, `@RestController`, `@GetMapping`         |
| 2 | Fetch data     | Calling the two upstream services and merging the results  | `RestClient`, JSON mapping, sorting with `Comparator` |
| 3 | Faulty servers | Retrying failed upstream requests                          | Error handling, retries                               |
| 4 | Long queries   | Parallel upstream calls and caching                        | `CompletableFuture`, Spring Cache (`@Cacheable`)      |

## Tech stack

- Java 17+
- Spring Boot 3 (Web, Actuator, Cache)
- Gradle

## Project structure

```
Transaction Aggregator/task/
├── src/aggregator/
│   ├── Application.java          # Spring Boot entry point
│   ├── AggregateController.java  # GET /aggregate endpoint
│   ├── AggregateService.java     # upstream calls, retries, caching, merging
│   └── Account.java              # transaction record
└── test/                         # tests and mock data servers provided by Hyperskill
```

The project uses Hyperskill's Gradle setup and is meant to be opened in IntelliJ IDEA with the JetBrains Academy plugin. The upstream services on `localhost:8888` and `localhost:8889` are mock servers started by the course's test suite (`DataServer.java`).