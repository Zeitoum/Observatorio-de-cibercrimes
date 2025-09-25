export interface Cibercrime {
  id: number;
  processCode: string;
  acordaoCode: number;
  className: string;
  subject: string;
  rapporteur: string;
  district: string;
  judgingBody: string;
  judgmentDate: string;
  publishDate: string;
  headnote: string;
  legalProcessFileId: number;
  pdfUrl: string;
  isCybercrime: boolean;
}

export interface CibercrimeResponse {
  totalItems: number;
  items: Cibercrime[];
}
