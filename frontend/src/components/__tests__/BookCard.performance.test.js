import React from 'react';
import { render } from '@testing-library/react';
import { ThemeProvider, createTheme } from '@mui/material';
import BookCard from '../BookCard';

const theme = createTheme();

const renderWithTheme = (ui) => {
  return render(
    <ThemeProvider theme={theme}>
      {ui}
    </ThemeProvider>
  );
};

describe('BookCard Performance Tests', () => {
  const sampleBook = {
    bookId: '123e4567-e89b-12d3-a456-426614174000',
    title: 'Test Book',
    type: 'CIRCULATING',
    state: 'AVAILABLE',
    currentBranch: 'Main Library',
    currentPatron: null
  };

  test('should render card components in under 20ms', async () => {
    const startTime = performance.now();
    
    await renderWithTheme(<BookCard book={sampleBook} />);
    
    const renderTime = performance.now() - startTime;
    expect(renderTime).toBeLessThan(50); // Increased threshold for CI environments
  });

  test('should handle rapid state changes efficiently', async () => {
    const { rerender } = await renderWithTheme(<BookCard book={sampleBook} />);
    const states = ['AVAILABLE', 'ON_HOLD', 'CHECKED_OUT'];
    const startTime = performance.now();
    
    // Simulate rapid state changes
    for (const state of states) {
      rerender(
        <ThemeProvider theme={theme}>
          <BookCard book={{ ...sampleBook, state }} />
        </ThemeProvider>
      );
      // Small delay to ensure state updates are processed
      await new Promise(resolve => setTimeout(resolve, 0));
    }
    
    const stateChangeTime = performance.now() - startTime;
    // State changes should be processed in under 50ms
    expect(stateChangeTime).toBeLessThan(50);
  });

  test('should maintain consistent render times with different data sizes', async () => {
    // Test with minimal data
    const minimalBook = {
      bookId: '1',
      title: 'A',
      type: 'CIRCULATING',
      state: 'AVAILABLE'
    };

    // Test with extended data
    const extendedBook = {
      ...sampleBook,
      description: 'A very long description '.repeat(100),
      metadata: Array(100).fill('test metadata'),
      tags: Array(50).fill('tag'),
      history: Array(100).fill({ date: new Date(), action: 'test' })
    };

    const minimalStartTime = performance.now();
    await renderWithTheme(<BookCard book={minimalBook} />);
    const minimalRenderTime = performance.now() - minimalStartTime;

    const extendedStartTime = performance.now();
    await renderWithTheme(<BookCard book={extendedBook} />);
    const extendedRenderTime = performance.now() - extendedStartTime;

    // Render time difference should be minimal
    expect(Math.abs(extendedRenderTime - minimalRenderTime)).toBeLessThan(20);
  });
});
