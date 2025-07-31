import React from 'react';
import { render, fireEvent, screen } from '@testing-library/react';
import SearchBar from '../SearchBar';

describe('SearchBar Performance Tests', () => {
  test('should handle rapid input changes efficiently', async () => {
    await render(<SearchBar />);
    const searchInput = screen.getByRole('textbox');
    const startTime = performance.now();
    
    // Simulate rapid typing
    for (let i = 0; i < 50; i++) {
        await fireEvent.change(searchInput, { target: { value: `test input ${i}` } });
    }
    
    const endTime = performance.now();
    const inputTime = endTime - startTime;
    
    // 50 changes should be handled in under 200ms
    expect(inputTime).toBeLessThan(200);
  });

  test('should debounce search input efficiently', async () => {
    let searchCallCount = 0;
    const mockSearch = jest.fn(() => {
      searchCallCount++;
    });

    await render(<SearchBar onSearch={mockSearch} />);
    const searchInput = screen.getByRole('textbox');
    
    // Simulate rapid typing
    for (let i = 0; i < 10; i++) {
      await fireEvent.change(searchInput, { target: { value: `test ${i}` } });
    }
    
    // Wait for debounce
    await new Promise(resolve => setTimeout(resolve, 300));
    
    // Should only call search once after debounce
    expect(searchCallCount).toBeLessThanOrEqual(1);
  });

  test('should maintain responsive UI during search operations', async () => {
    const simulateHeavySearch = jest.fn(() => new Promise(resolve => setTimeout(resolve, 100)));
    await render(<SearchBar onSearch={simulateHeavySearch} />);
    const searchInput = screen.getByRole('textbox');
    
    const startTime = performance.now();
    
    // Trigger search
    await fireEvent.change(searchInput, { target: { value: 'test' } });
    
    // Immediately try to type more
    await fireEvent.change(searchInput, { target: { value: 'test more' } });
    
    const responseTime = performance.now() - startTime;
    
    // UI should remain responsive (input changes should be under 50ms)
    expect(responseTime).toBeLessThan(50);
  });
});
