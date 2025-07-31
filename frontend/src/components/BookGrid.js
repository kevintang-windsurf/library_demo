import React from 'react';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import BookCard from './BookCard';

const BookGrid = ({ books }) => {
  return (
    <Box sx={{ p: 2 }}>
      <Grid container spacing={1}>
        {books.map((book) => (
          <Grid item xs={12} sm={6} md={4} lg={3} key={book.bookId}>
            <BookCard book={book} />
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default BookGrid;
