import React from 'react';
import { render, screen } from '@testing-library/react';
import Editor from './index';

test('renders learn react link', () => {
  render(<Editor onMouseUp={console.log} initValue='Test 123' readOnly={true} onChange={console.log} />);
  const element = screen.getByTestId('editor');
  expect(element).toBeInTheDocument();
});
