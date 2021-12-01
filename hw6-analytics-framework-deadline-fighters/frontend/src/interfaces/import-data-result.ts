import { StockQuotesResult } from './stock-quotes-result';

export interface ImportDataResult {
  stockQuotesResults: StockQuotesResult[];
  hasError: boolean;
  errorMessages: string[];
}
