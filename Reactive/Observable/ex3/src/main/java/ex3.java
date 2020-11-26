import utils.AsyncTester;

/**
 * This class shows how to reduce and/or multiply big fractions
 * asynchronously and concurrently using many advanced RxJava
 * Observable operations, including fromIterable(), map(), create(),
 * flatMap(), flatMapCompletable(), fromCallable(), filter(),
 * reduce(), collectInto(), subscribeOn(), onErrorReturn(), and
 * Schedulers.computation().  It also shows advanced RxJava Single and
 * Maybe operations, such as ambArray(), subscribeOn(), and
 * doOnSuccess().
 */
public class ex3 {
    /**
     * Main entry point into the test program.
     */
    public static void main (String[] argv) {
        // Use an asynchronous Observable stream and a pool of threads
        // to showcase exception handling of BigFraction objects.
        AsyncTester.register(ObservableEx::testFractionExceptions);

        // Use an asynchronous Observable stream and a pool of threads
        // to perform BigFraction multiplications.
        AsyncTester.register(ObservableEx::testFractionMultiplications1);

        // Use an asynchronous Observable stream and a pool of threads
        // to perform BigFraction reductions and multiplications.
        AsyncTester.register(ObservableEx::testFractionMultiplications2);

        long testCount = AsyncTester
            // Run all the tests.
            .runTests()

            // Block until all the tests are done to allow future
            // computations to complete running asynchronously.
            .blockingGet();

        // Print the results.
        System.out.println("Completed " + testCount + " tests");
    }
}
