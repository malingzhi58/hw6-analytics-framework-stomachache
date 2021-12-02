import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  Box,
  Button,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  TextField,
  Typography,
} from '@mui/material';
import { SelectChangeEvent } from '@mui/material/Select/SelectInput';
import React, { useCallback, useMemo } from 'react';
import { Plugin } from '../interfaces/plugin';
import { DataPluginInput } from '../interfaces/data-plugin-input';
import { StockQuotesResult } from '../interfaces/stock-quotes-result';

interface DataPluginFormProps {
  index: number;
  plugins: Plugin[];
  input: DataPluginInput;
  onChange: (id: number, parameters: DataPluginInput) => void;
  onDelete: (id: number) => void;
  stockQuotesResult?: StockQuotesResult;
}

const DataPluginForm: React.FC<DataPluginFormProps> = (props) => {
  const {
    index, plugins, input, onChange, onDelete, stockQuotesResult,
  } = props;
  const handlePluginChange = useCallback((event: SelectChangeEvent) => {
    const key = event.target.value;
    const plugin = plugins.find((p) => p.key === key);
    onChange(index, { ...input, key: plugin?.key || '', name: plugin?.name || '' });
  }, [index, onChange, input, plugins]);

  const handleArgChange: React.ChangeEventHandler<HTMLTextAreaElement | HTMLInputElement> = useCallback((event) => {
    onChange(index, { ...input, arg: event.target.value });
  }, [index, onChange, input]);

  const handleSymbolsChange: React.ChangeEventHandler<HTMLTextAreaElement | HTMLInputElement> = useCallback((event) => {
    onChange(index, { ...input, symbols: event.target.value });
  }, [index, onChange, input]);

  const totalCount = useMemo(() => {
    let count = 0;
    if (stockQuotesResult) {
      count = Object.values(stockQuotesResult.stockQuotesCountBySymbol).reduce((prev, curr) => prev + curr, 0);
    }
    return count;
  }, [stockQuotesResult]);

  const handleDelete = useCallback(() => onDelete(index), [index, onDelete]);

  return (
    <Box>
      <Box width="100%" display="flex" paddingTop="20px">
        <FormControl sx={{ minWidth: '200px' }}>
          <InputLabel id={`data-plugin-plugin-${index}-label`}>Plugin</InputLabel>
          <Select
            labelId={`data-plugin-plugin-${index}-label`}
            id={`data-plugin-plugin-${index}`}
            value={input.key}
            label="Plugin"
            onChange={handlePluginChange}
          >
            {
              plugins.map((plugin) => (
                <MenuItem key={plugin.key} value={plugin.key}>{plugin.name}</MenuItem>
              ))
            }
          </Select>
        </FormControl>
        <TextField
          id={`data-plugin-arg-${index}`}
          label="Arg"
          value={input.arg}
          onChange={handleArgChange}
          sx={{ width: '100%' }}
        />
        <TextField
          required
          id={`data-plugin-symbols-${index}`}
          label="Symbols"
          value={input.symbols}
          onChange={handleSymbolsChange}
          sx={{ width: '100%' }}
        />
        <Button onClick={handleDelete}>X</Button>
      </Box>
      <Box>
        <Accordion sx={{ maxWidth: '400px', boxShadow: stockQuotesResult ? undefined : 'none' }}>
          <AccordionSummary
            expandIcon={stockQuotesResult && <Box>▼</Box>}
            aria-controls="panel1a-content"
            id="panel1a-header"
            disabled={!stockQuotesResult}
          >
            {
              stockQuotesResult && (
                <Typography>{`Loaded ${totalCount} quotes ${stockQuotesResult.hasError ? 'with error' : 'successfully'}`}</Typography>
              )
            }
          </AccordionSummary>
          {
            stockQuotesResult && (
              <AccordionDetails>
                {stockQuotesResult.hasError && (
                  <Typography>
                    {`Error: ${stockQuotesResult.errorMessage}`}
                  </Typography>
                )}
                <Typography>
                  Quotes:
                </Typography>
                {Object.entries(stockQuotesResult.stockQuotesCountBySymbol).map(([symbol, count]) => (
                  <Typography key={symbol}>
                    {`${symbol}: ${count}`}
                  </Typography>
                ))}
              </AccordionDetails>
            )
          }
        </Accordion>
      </Box>
    </Box>
  );
};

export default DataPluginForm;
