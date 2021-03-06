package microservices.flightPrice;

import datamodels.TripRequest;
import datamodels.TripResponse;
import io.reactivex.rxjava3.core.Single;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.function.Function;

/**
 * This class serves as a proxy to the FlightPrice microservice.
 */
public class FlightPriceProxy {
    /**
     * The URI that denotes the remote method to find the best price
     * for a trip.
     */
    private final String mFindBestPriceURIAsync =
        "/microservices/flightPrice/_bestPriceAsync";

    /**
     * The WebClient provides the means to access the FlightPrice
     * microservice.
     */
    private final WebClient mFlightPrice;

    /**
     * Host/port where the server resides.
     */
    private final String mSERVER_BASE_URL =
        "http://localhost:8083";

    /**
     * Constructor initializes the fields.
     */
    public FlightPriceProxy() {
        mFlightPrice = WebClient
            // Start building.
            .builder()

            // The URL where the server is running.
            .baseUrl(mSERVER_BASE_URL)

            // Build the webclient.
            .build();
    }

    /**
     * Finds the best price for the {@code flightLeg} asynchronously.
     *
     * @param scheduler The Scheduler context in which to run the operation
     * @param tripRequest The trip to price
     * @return A Trip containing the best price.
     */
    public Mono<TripResponse> findBestPriceAsync(Scheduler scheduler,
                                                 TripRequest tripRequest) {
        // Return a Mono to the best price.
        return Mono
            .fromCallable(() -> mFlightPrice
                          // Create an HTTP POST request.
                          .post()

                          // Add the uri to the baseUrl.
                          .uri(mFindBestPriceURIAsync)

                          // Encode the trip in the body of the request.
                          .bodyValue(tripRequest)

                          // Retrieve the response.
                          .retrieve()

                          // Convert it to a Mono of Trip.
                          .bodyToMono(TripResponse.class))
            
            // Schedule this to run on the given scheduler.
            .subscribeOn(scheduler)

            // De-nest the result so it's a Mono<TripResponse>.
            .flatMap(Function.identity());
    }

    /**
     * Finds the best price for the {@code trip} asynchronously.
     *
     * @param scheduler The Scheduler context in which to run the operation
     * @param tripRequest The trip to price
     * @return A Single containing the best price.
     */
    public Single<TripResponse> findBestPriceAsyncRx(Scheduler scheduler,
                                                     TripRequest tripRequest) {
        return Single
            // Return a Single to the best price.
            .fromPublisher(Mono
                           // Create a Mono from a Callable.
                           .fromCallable(() -> mFlightPrice
                                         // Create an HTTP post
                                         // request.
                                         .post()

                                         // Add the uri to the
                                         // baseUrl.
                                         .uri(mFindBestPriceURIAsync)

                                         // Encode the trip in the body of the request.
                                         .bodyValue(tripRequest)

                                         // Retrieve the response.
                                         .retrieve()

                                         // Convert it to a Mono of
                                         // Trip.
                                         .bodyToMono(TripResponse.class))

                           // Schedule this to run on the given
                           // scheduler.
                           .subscribeOn(Schedulers.parallel())

                           // De-nest the result so it's a
                           // Mono<TripResponse>.
                           .flatMap(Function.identity()));
    }

    /**
     * Finds the best price for the {@code flightLeg} synchronously.
     *
     * @param trip The trip to price
     * @param maxTime Max time to wait before throwing TimeoutException
     * @return A Mono containing the best price
     */
    public Mono<TripResponse> findBestPriceSync(TripRequest trip,
                                                Duration maxTime) {
        // Return a Mono to the best price.
        return Mono
            .fromCallable(() -> mFlightPrice
                          // Create an HTTP POST request.
                          .post()

                          // Add the uri to the baseUrl.
                          .uri(mFindBestPriceURIAsync)

                          // Encode the trip in the body of the request.
                          .bodyValue(trip)

                          // Retrieve the response.
                          .retrieve()

                          // Convert it to a Mono of Trip.
                          .bodyToMono(TripResponse.class))
            
            // De-nest the result so it's a Mono<TripResponse>.
            .flatMap(Function.identity())

            // If the total processing takes more than maxTime a
            // TimeoutException will be thrown.
            .timeout(maxTime);
    }
}
