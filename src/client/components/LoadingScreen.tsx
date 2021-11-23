import  React from "react";
import { Box, CircularProgress } from "@mui/material";

const LoadingScreen = () => {
  return (
    <Box
      sx={{ display: "flex", flexFlow: "column" }}
      alignItems="center"
      justifyContent="center"
    >
      <CircularProgress color="warning" />
    </Box>
  );
};

export default LoadingScreen;
