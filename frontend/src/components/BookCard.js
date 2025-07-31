import React from 'react';
import { Card, CardContent, Typography, Chip, Box } from '@mui/material';

const BookCard = ({ book }) => {
  const getStateColor = (state) => {
    switch (state) {
      case 'AVAILABLE':
        return 'success';
      case 'ON_HOLD':
        return 'warning';
      case 'CHECKED_OUT':
        return 'error';
      default:
        return 'default';
    }
  };

  return (
    <Card sx={{ minWidth: 275, m: 2 }}>
      <CardContent>
        <Typography variant="h6" component="div">
          {book.title}
        </Typography>
        <Typography sx={{ mb: 1.5 }} color="text.secondary">
          ID: {book.bookId}
        </Typography>
        <Box sx={{ display: 'flex', gap: 1, mb: 1 }}>
          <Chip 
            label={book.state}
            color={getStateColor(book.state)}
            size="small"
          />
          <Chip 
            label={book.type}
            variant="outlined"
            size="small"
          />
        </Box>
        {book.currentBranch && (
          <Typography variant="body2">
            Branch: {book.currentBranch}
          </Typography>
        )}
        {book.currentPatron && (
          <Typography variant="body2">
            Patron: {book.currentPatron}
          </Typography>
        )}
      </CardContent>
    </Card>
  );
};

export default BookCard;
