import React from 'react';
import { AppBar, Toolbar, Typography, IconButton } from '@mui/material';
import { Menu as MenuIcon } from '@mui/icons-material';

const Header = () => {
  return (
    <AppBar position="static" sx={{ bgcolor: '#3d3d3d' }}>
      <Toolbar>
        <IconButton edge="start" color="inherit" aria-label="menu" sx={{ mr: 2 }}>
          <MenuIcon />
        </IconButton>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          Windsurf Library Catalogue
        </Typography>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
