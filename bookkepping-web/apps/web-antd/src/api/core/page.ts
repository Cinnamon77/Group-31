export namespace Page {
  export interface PageResult<T> {
    records: T[];
    current: number;
    pages: number;
    size: number;
    total: number;
  }
}
