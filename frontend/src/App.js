import React from 'react';
import { Container, CssBaseline, ThemeProvider, createTheme } from '@mui/material';
import Header from './components/Header';
import SearchBar from './components/SearchBar';
import BookGrid from './components/BookGrid';

// Sample data representing our domain model
const sampleBooks = [
  {
    bookId: '123e4567-e89b-12d3-a456-426614174000',
    title: 'Codebase Onboarding',
    type: 'CIRCULATING',
    state: 'AVAILABLE',
    currentBranch: 'Main Library'
  },
  {
    bookId: '987fcdeb-51d3-12a4-b456-426614174001',
    title: 'Migrations',
    type: 'RESTRICTED',
    state: 'ON_HOLD',
    currentBranch: 'Tech Branch',
    currentPatron: 'John Doe'
  },
  {
    bookId: '543bcdef-89a1-43d2-c789-426614174002',
    title: 'Unit Testing',
    type: 'CIRCULATING',
    state: 'CHECKED_OUT',
    currentBranch: 'Downtown Branch',
    currentPatron: 'Jane Smith'
  },
  {
    bookId: '789defgh-ijkl-45m6-n890-426614174003',
    title: 'Documentation',
    type: 'CIRCULATING',
    state: 'AVAILABLE',
    currentBranch: 'Main Library'
  }
];

// Create a theme instance
const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
  },
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <div className="App">
        <Header />
        <Container maxWidth="lg">
          <SearchBar />
          <BookGrid books={sampleBooks} />
        </Container>
      </div>
    </ThemeProvider>
  );
}

export default App;
