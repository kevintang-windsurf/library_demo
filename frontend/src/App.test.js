import { render, screen } from '@testing-library/react';
import { ThemeProvider, createTheme, CssBaseline } from '@mui/material';
import App from './App';

const theme = createTheme();

const renderWithTheme = (ui) => {
  return render(
    <ThemeProvider theme={theme}>
      <CssBaseline />
      {ui}
    </ThemeProvider>
  );
};

test('renders library catalogue header', async () => {
  renderWithTheme(<App />);
  const headerElement = screen.getByText(/Windsurf Library System/i);
  expect(headerElement).toBeInTheDocument();
});

test('renders search bar', async () => {
  renderWithTheme(<App />);
  const searchInput = await screen.findByRole('textbox');
  expect(searchInput).toBeInTheDocument();
});
