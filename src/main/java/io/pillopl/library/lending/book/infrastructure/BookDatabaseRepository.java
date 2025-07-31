package io.pillopl.library.lending.book.infrastructure;

import io.pillopl.library.catalogue.BookId;
import io.pillopl.library.catalogue.BookType;
import io.pillopl.library.commons.aggregates.AggregateRootIsStale;
import io.pillopl.library.lending.book.new_model.*;
import io.pillopl.library.lending.patron.application.hold.FindAvailableBook;
import io.pillopl.library.lending.patron.application.hold.FindBookOnHold;
import io.pillopl.library.lending.patron.model.PatronId;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.util.UUID;

import static io.pillopl.library.lending.book.infrastructure.BookDatabaseEntity.BookState.*;
import static io.vavr.API.*;
import static io.vavr.Patterns.$Some;
import static io.vavr.Predicates.instanceOf;
import static io.vavr.control.Option.none;
import static io.vavr.control.Option.of;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
class BookDatabaseRepository implements BookRepository, FindAvailableBook, FindBookOnHold {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Option<Book> findBy(BookId bookId) {
        return findBookById(bookId)
                .map(BookDatabaseEntity::toDomainModel);
    }

    private Option<BookDatabaseEntity> findBookById(BookId bookId) {
        return Try
                .ofSupplier(() -> of(jdbcTemplate.queryForObject("SELECT b.* FROM book_database_entity b WHERE b.book_id = ?", new BeanPropertyRowMapper<>(BookDatabaseEntity.class), bookId.getBookId())))
                .getOrElse(none());
    }

    @Override
    public void save(Book book) {
        findBy(book.getBookId())
                .map(entity -> updateOptimistically(book))
                .onEmpty(() -> insertNew(book));
    }

    private int updateOptimistically(Book book) {
        int result = updateBookState(book);
        if (result == 0) {
            throw new AggregateRootIsStale("Someone has updated book in the meantime, book: " + book);
        }
        return result;
    }

    private int updateBookState(Book book) {
        String state = book.getCurrentState();
        switch (state) {
            case "AVAILABLE":
                return updateAvailable(book);
            case "ON_HOLD":
                return updateOnHold(book);
            case "CHECKED_OUT":
                return updateCheckedOut(book);
            default:
                throw new IllegalStateException("Unknown book state: " + state);
        }
    }

    private int updateAvailable(Book book) {
        return jdbcTemplate.update("UPDATE book_database_entity b SET b.book_state = ?, b.available_at_branch = ?, b.version = ? WHERE book_id = ? AND version = ?",
                Available.toString(),
                book.getCurrentBranch().getLibraryBranchId(),
                book.getVersion().getVersion() + 1,
                book.getBookId().getBookId(),
                book.getVersion().getVersion());
    }

    private int updateOnHold(Book book) {
        return jdbcTemplate.update("UPDATE book_database_entity b SET b.book_state = ?, b.on_hold_at_branch = ?, b.on_hold_by_patron = ?, b.version = ? WHERE book_id = ? AND version = ?",
                OnHold.toString(),
                book.getCurrentBranch().getLibraryBranchId(),
                book.getCurrentPatron().getPatronId(),
                book.getVersion().getVersion() + 1,
                book.getBookId().getBookId(),
                book.getVersion().getVersion());
    }

    private int updateCheckedOut(Book book) {
        return jdbcTemplate.update("UPDATE book_database_entity b SET b.book_state = ?, b.checked_out_at_branch = ?, b.checked_out_by_patron = ?, b.version = ? WHERE book_id = ? AND version = ?",
                CheckedOut.toString(),
                book.getCurrentBranch().getLibraryBranchId(),
                book.getCurrentPatron().getPatronId(),
                book.getVersion().getVersion() + 1,
                book.getBookId().getBookId(),
                book.getVersion().getVersion());
    }

    private void insertNew(Book book) {
        String state = book.getCurrentState();
        switch (state) {
            case "AVAILABLE":
                insertAvailable(book);
                break;
            case "ON_HOLD":
                insertOnHold(book);
                break;
            case "CHECKED_OUT":
                insertCheckedOut(book);
                break;
            default:
                throw new IllegalStateException("Unknown book state: " + state);
        }
    }

    private int insertAvailable(Book book) {
        return insert(book.getBookId(), book.getBookType(), Available, book.getCurrentBranch().getLibraryBranchId(), null, null, null, null, null);
    }

    private int insertOnHold(Book book) {
        return insert(book.getBookId(), book.getBookType(), OnHold, null, book.getCurrentBranch().getLibraryBranchId(), book.getCurrentPatron().getPatronId(), null, null, null);
    }

    private int insertCheckedOut(Book book) {
        return insert(book.getBookId(), book.getBookType(), CheckedOut, null, null, null, null, book.getCurrentBranch().getLibraryBranchId(), book.getCurrentPatron().getPatronId());
    }

    private int insert(BookId bookId, BookType bookType, BookDatabaseEntity.BookState state, UUID availableAt, UUID onHoldAt, UUID onHoldBy, Instant onHoldTill, UUID checkedOutAt, UUID checkedOutBy) {
        return jdbcTemplate.update("INSERT INTO book_database_entity " +
                        "(id, " +
                        "book_id, " +
                        "book_type, " +
                        "book_state, " +
                        "available_at_branch," +
                        "on_hold_at_branch, " +
                        "on_hold_by_patron, " +
                        "on_hold_till, " +
                        "checked_out_at_branch, " +
                        "checked_out_by_patron, " +
                        "version) VALUES " +
                        "(book_database_entity_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)",
                bookId.getBookId(), bookType.toString(), state.toString(), availableAt, onHoldAt, onHoldBy, onHoldTill, checkedOutAt, checkedOutBy);
    }

    @Override
    public Option<Book> findAvailableBook(BookId bookId) {
        return findBookById(bookId)
                .filter(entity -> entity.book_state.equals(Available))
                .map(BookDatabaseEntity::toDomainModel);
    }

    @Override
    public Option<Book> findBookOnHold(BookId bookId, PatronId patronId) {
        return findBookById(bookId)
                .filter(entity -> entity.book_state.equals(OnHold))
                .filter(entity -> entity.on_hold_by_patron.equals(patronId.getPatronId()))
                .map(BookDatabaseEntity::toDomainModel);
    }

}

