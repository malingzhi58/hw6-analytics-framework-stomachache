// @ts-ignore
import  React from "react";
import { Box, Button, Typography } from "@mui/material";

export interface PagingParams {
  totalPageNumber: number;
  countPerPage: number;
  currentPageNumber: number;
  onNext: () => void;
  onPrevious: () => void;
}

const Paging = (params: PagingParams) => {
  return (
    <Box
      sx={{ display: "flex", flexFlow: "row", width: "100%" }}
      alignItems="center"
      justifyContent="space-between"
    >
      <Button variant="outlined" onClick={params.onPrevious}>
        Previous
      </Button>
      <Typography variant="body1">{`${params.currentPageNumber} / ${params.totalPageNumber}`}</Typography>
      <Button variant="outlined" onClick={params.onNext}>
        Next
      </Button>
    </Box>
  );
};
export default Paging;
