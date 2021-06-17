# SRDemo
A small application that fetches the last episode of a show given that shows name.

This was a good opportunity for me to try out Kotlin, as it's a first for me.

## Testing locally
This should be fairly easy to test locally. I've supplied a `requests.http` file that at least IntelliJ can run locally to help run a couple of different requests. I opted for that over swagger or similar tools just to be able to go fast.

To test the requirement of not hitting SRs' API I've personally used Apache Bench with commands like this, where `n` is the total number of requests and `c` is the maximum amount of concurrent requests:
```sh
ab -n 20 -c 20 http://localhost:8080/shows/Nyheter%20P4%20Kalmar/latest
```

## About the caching strategy
A requirement was to only hit SR a maximum of 3 times a second. To achieve this I've gone with the following strategy:
 - Memoize the result of fetching of all shows with a very arbitrary timeout of 1 minute.
 - Use a rate limiter to let requests wait for an empty slot.

In a real world scenario (multiple instances of the same service) we can't keep a count in memory as that'd mean we could hit the api 3*n times a second. A cache would also have to be lifted out for the same reason - we simply wouldn't be able to use the cache properly. Redis would probably help with this.

Another thing to take into consideration is the performance cost - RateLimiter as far as I know blocks execution until a spot opens up, so if this service did more things than acting on these requests we'd probably have to find a better solution.
 