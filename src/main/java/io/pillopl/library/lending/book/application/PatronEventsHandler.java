package io.pillopl.library.lending.book.application;

import io.pillopl.library.catalogue.BookId;
import io.pillopl.library.commons.events.DomainEvents;
import io.pillopl.library.lending.book.new_model.*;
import io.pillopl.library.lending.patron.model.PatronEvent.*;
import io.pillopl.library.lending.patron.model.PatronId;
import io.vavr.API;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;

import java.time.Instant;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;

@AllArgsConstructor
public class PatronEventsHandler {

    private final BookRepository bookRepository;
    private final DomainEvents domainEvents;

    @EventListener
    void handle(BookPlacedOnHold bookPlacedOnHold) {
        bookRepository.findBy(new BookId(bookPlacedOnHold.getBookId()))
                .map(book -> handleBookPlacedOnHold(book, bookPlacedOnHold))
                .map(this::saveBook);
    }

    @EventListener
    void handle(BookCheckedOut bookCheckedOut) {
        bookRepository.findBy(new BookId(bookCheckedOut.getBookId()))
                .map(book -> handleBookCheckedOut(book, bookCheckedOut))
                .map(this::saveBook);
    }

    @EventListener
    void handle(BookHoldExpired holdExpired) {
        bookRepository.findBy(new BookId(holdExpired.getBookId()))
                .map(book -> handleBookHoldExpired(book, holdExpired))
                .map(this::saveBook);
    }

    @EventListener
    void handle(BookHoldCanceled holdCanceled) {
        bookRepository.findBy(new BookId(holdCanceled.getBookId()))
                .map(book -> handleBookHoldCanceled(book,  holdCanceled))
                .map(this::saveBook);
    }

    @EventListener
    void handle(BookReturned bookReturned) {
        bookRepository.findBy(new BookId(bookReturned.getBookId()))
                .map(book -> handleBookReturned(book, bookReturned))
                .map(this::saveBook);
    }


    private Book handleBookPlacedOnHold(Book book, BookPlacedOnHold bookPlacedOnHold) {
        return API.Match(book).of(
                Case($(book.isAvailable()), () -> book.handle(bookPlacedOnHold)),
                Case($(book.isOnHold()), () -> raiseDuplicateHoldFoundEvent(book, bookPlacedOnHold)),
                Case($(), () -> book)
        );
    }

    private Book raiseDuplicateHoldFoundEvent(Book book, BookPlacedOnHold bookPlacedOnHold) {
        if(book.isHeldBy(new PatronId(bookPlacedOnHold.getPatronId()))) {
            return book;
        }
        domainEvents.publish(
                new BookDuplicateHoldFound(
                        Instant.now(),
                        book.getCurrentPatron().getPatronId(),
                        bookPlacedOnHold.getPatronId(),
                        bookPlacedOnHold.getLibraryBranchId(),
                        bookPlacedOnHold.getBookId()));
        return book;
    }


    private Book handleBookHoldExpired(Book book, BookHoldExpired holdExpired) {
        return API.Match(book).of(
                Case($(book.isOnHold()), () -> book.handle(holdExpired)),
                Case($(), () -> book)
        );
    }

    private Book handleBookHoldCanceled(Book book, BookHoldCanceled holdCanceled) {
        return API.Match(book).of(
                Case($(book.isOnHold()), () -> book.handle(holdCanceled)),
                Case($(), () -> book)
        );
    }

    private Book handleBookCheckedOut(Book book, BookCheckedOut bookCheckedOut) {
        return API.Match(book).of(
                Case($(book.isOnHold()), () -> book.handle(bookCheckedOut)),
                Case($(), () -> book)
        );
    }

    private Book handleBookReturned(Book book, BookReturned bookReturned) {
        return API.Match(book).of(
                Case($(book.isCheckedOut()), () -> book.handle(bookReturned)),
                Case($(), () -> book)
        );
    }

    private Book saveBook(Book book) {
        bookRepository.save(book);
        return book;
    }

}
