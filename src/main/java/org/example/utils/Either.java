package org.example.utils;

import java.util.Objects;
import java.util.function.*;
import java.util.stream.Stream;


/**
 * @param <A> The type of the left projection.
 * @param <B> The type of the right projection.
 */
public abstract class Either<A, B> {

    /**
     * @return a <em>Left</em> Either - the error condition
     */
    public static <A, B> Either<A, B> createLeft(final A left) {

        return Left.create(left);
    }

    public static <A, B> Either<A, B> createRight(final B right) {

        return Right.create(right);
    }

    public static <A, B> Either<A, B> iff(final boolean c, final Supplier<B> right, final Supplier<A> left) {

        return c
                ? createRight(right.get())
                : createLeft(left.get());
    }

    public abstract boolean isLeft();

    public abstract boolean isRight();

    public abstract A getLeft();

    public abstract B getRight();

    public abstract <W> W apply(final Function<A, W> leftFun, final Function<B, W> rightFun);

    public abstract <W, E extends Throwable> W applyOrElseThrow(final Function<A, E> exFactory, final Function<B, W> rightFun) throws E;

    public abstract <E extends Throwable> B orElseThrow(final Function<A, E> exFactory) throws E;

    public abstract Either<A, B> or(final Supplier<Either<A, B>> supplier);

    public abstract void consume(final Consumer<A> leftConsumer, final Consumer<B> rightConsumer);

    public <C> Either<A, C> flatMap(final Function<B, Either<A, C>> rightFun) {

        return apply(Either::createLeft, rightFun);
    }

    public <C> Either<A, C> map(final Function<B, C> rightFun) {

        return apply(Either::createLeft, r -> createRight(rightFun.apply(r)));
    }

    public Either<A, B> filter(final Predicate<B> rightPredicate, final Supplier<A> leftSupplier) {

        return flatMap(x -> rightPredicate.test(x) ? this : createLeft(leftSupplier.get()));
    }

    public Either<A, B> filter(final Predicate<B> rightPredicate, final Function<B, A> leftFun) {

        return flatMap(x -> rightPredicate.test(x) ? this : createLeft(leftFun.apply(x)));
    }

    public abstract Stream<A> leftStream();

    public abstract Stream<B> rightStream();


    public static <L0, R0, L1, R1, W> W applySuccess(
            final Either<L0, R0> e0,
            final Either<L1, R1> e1,
            final BiFunction<Either<L0, R0>, Either<L1, R1>, W> failureFunctor,
            final BiFunction<R0, R1, W> successFunctor) {

        if (e0.isRight() && e1.isRight()) {
            return successFunctor.apply(e0.getRight(), e1.getRight());
        } else {
            return failureFunctor.apply(e0, e1);
        }
    }

    public static <L, R0, R1, W> Either<L, W> applySuccess(
            final Either<L, R0> e0,
            final Either<L, R1> e1,
            final Function<Stream<L>, L> failureFunctor,
            final BiFunction<R0, R1, W> successFunctor) {

        return applySuccess(
                e0,
                e1,
                (f0, f1) -> Either.createLeft(failureFunctor.apply(Stream.of(f0, f1).flatMap(Either::leftStream))),
                (s0, s1) -> Either.createRight(successFunctor.apply(s0, s1)));
    }


    public static <L0, R0, L1, R1> void consumeSuccess(
            final Either<L0, R0> e0,
            final Either<L1, R1> e1,
            final BiConsumer<Either<L0, R0>, Either<L1, R1>> failureFunctor,
            final BiConsumer<R0, R1> successFunctor) {

        if (e0.isRight() && e1.isRight()) {
            successFunctor.accept(e0.getRight(), e1.getRight());
        } else {
            failureFunctor.accept(e0, e1);
        }
    }


    private static final class Left<A, B> extends Either<A, B> {
        static <A, B> Left<A, B> create(final A left) {
            return new Left<>(left);
        }

        private Left(final A left) {
            mLeft = left;
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public A getLeft() {
            return mLeft;
        }

        @Override
        public B getRight() {
            throw new RuntimeException("Either<> object was not a right.");
        }

        @Override
        public <W> W apply(final Function<A, W> leftFun, final Function<B, W> rightFun) {
            return leftFun.apply(mLeft);
        }

        @Override
        public <W, E extends Throwable> W applyOrElseThrow(final Function<A, E> exFactory, final Function<B, W> rightFun) throws E {

            throw exFactory.apply(mLeft);
        }

        @Override
        public <E extends Throwable> B orElseThrow(final Function<A, E> exFactory) throws E {

            throw exFactory.apply(mLeft);
        }

        @Override
        public Either<A, B> or(final Supplier<Either<A, B>> supplier) {
            return supplier.get();
        }

        @Override
        public void consume(final Consumer<A> leftConsumer, final Consumer<B> rightConsumer) {
            leftConsumer.accept(mLeft);
        }

        @Override
        public Stream<A> leftStream() {
            return Stream.of(mLeft);
        }

        @Override
        public Stream<B> rightStream() {
            return Stream.empty();
        }

        @Override
        public String toString() {
            return "Left{mLeft=" + mLeft + '}';
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final Left<?, ?> left = (Left<?, ?>) o;
            return Objects.equals(mLeft, left.mLeft);
        }

        @Override
        public int hashCode() {
            return Objects.hash(Left.class, mLeft);
        }

        private final A mLeft;
    }

    private static final class Right<A, B> extends Either<A, B> {
        static <A, B> Right<A, B> create(final B right) {
            return new Right<>(right);
        }

        private Right(final B right) {
            mRight = right;
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public B getRight() {
            return mRight;
        }

        @Override
        public A getLeft() {
            throw new RuntimeException("Either<> object was not a left.");
        }

        @Override
        public <W> W apply(final Function<A, W> leftFun, final Function<B, W> rightFun) {
            return rightFun.apply(mRight);
        }

        @Override
        public <W, E extends Throwable> W applyOrElseThrow(final Function<A, E> exFactory, final Function<B, W> rightFun) throws E {

            return rightFun.apply(mRight);
        }

        @Override
        public <E extends Throwable> B orElseThrow(final Function<A, E> exFactory) throws E {

            return mRight;
        }

        @Override
        public Either<A, B> or(final Supplier<Either<A, B>> supplier) {
            return this;
        }

        @Override
        public void consume(final Consumer<A> leftConsumer, final Consumer<B> rightConsumer) {
            rightConsumer.accept(mRight);
        }

        @Override
        public Stream<A> leftStream() {
            return Stream.empty();
        }

        @Override
        public Stream<B> rightStream() {
            return Stream.of(mRight);
        }

        @Override
        public String toString() {
            return "Right{mRight=" + mRight + '}';
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final Right<?, ?> right = (Right<?, ?>) o;
            return Objects.equals(mRight, right.mRight);
        }

        @Override
        public int hashCode() {
            return Objects.hash(Right.class, mRight);
        }

        private final B mRight;
    }

}
