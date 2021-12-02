import { Box } from '@mui/material';
import React from 'react';
import InnerHTML from 'dangerously-set-html-content';

interface VisualizationPluginDisplayProps {
  html: string;
}

const VisualizationPluginDisplay: React.FC<VisualizationPluginDisplayProps> = ({ html }) => (
  <Box>
    <InnerHTML html={html} />
  </Box>
);

export default VisualizationPluginDisplay;
