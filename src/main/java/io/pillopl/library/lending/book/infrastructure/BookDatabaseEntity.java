package io.pillopl.library.lending.book.infrastructure;

import io.pillopl.library.catalogue.BookId;
import io.pillopl.library.catalogue.BookType;
import io.pillopl.library.commons.aggregates.Version;
import io.pillopl.library.lending.book.new_model.*;
import io.pillopl.library.lending.librarybranch.model.LibraryBranchId;
import io.pillopl.library.lending.patron.model.PatronId;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

import static io.pillopl.library.lending.book.infrastructure.BookDatabaseEntity.BookState.*;
import static io.vavr.API.*;

@NoArgsConstructor
@Data
class BookDatabaseEntity {

    enum BookState {
        Available, OnHold, CheckedOut
    }

    UUID book_id;
    BookType book_type;
    BookState book_state;
    UUID available_at_branch;
    UUID on_hold_at_branch;
    UUID on_hold_by_patron;
    Instant on_hold_till;
    UUID checked_out_at_branch;
    UUID checked_out_by_patron;
    int version;

    Book toDomainModel() {
        Book book = new Book(new BookId(book_id), book_type, getInitialBranch(), new Version(version));
        
        switch (book_state) {
            case OnHold:
                book.placeOnHold(new PatronId(on_hold_by_patron), new LibraryBranchId(on_hold_at_branch), on_hold_till);
                break;
            case CheckedOut:
                book.checkout(new PatronId(checked_out_by_patron), new LibraryBranchId(checked_out_at_branch));
                break;
            case Available:
            default:
                // Book is already in Available state by default
                break;
        }
        
        return book;
    }

    private LibraryBranchId getInitialBranch() {
        if (available_at_branch != null) {
            return new LibraryBranchId(available_at_branch);
        } else if (on_hold_at_branch != null) {
            return new LibraryBranchId(on_hold_at_branch);
        } else {
            return new LibraryBranchId(checked_out_at_branch);
        }
    }
}
