export interface StockQuotesResult {
  stockQuotesCountBySymbol: { [key: string]: number; };
  hasError: boolean;
  errorMessage?: string;
}
