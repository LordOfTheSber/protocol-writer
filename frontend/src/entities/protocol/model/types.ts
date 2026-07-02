// Протокол — единый markdown-документ. Диаграммы (mermaid/excalidraw) живут
// прямо в тексте как огороженные блоки кода, поэтому отдельной модели секций нет.

export type ProtocolStatus = 'DRAFT' | 'PUBLISHED' | 'ARCHIVED';

export interface ProtocolSummary {
  id: string;
  title: string;
  author: string | null;
  status: ProtocolStatus;
  updatedAt: string;
}

export interface Protocol {
  id: string;
  title: string;
  author: string | null;
  status: ProtocolStatus;
  body: string;
  createdAt: string;
  updatedAt: string;
}

export interface ProtocolInput {
  title: string;
  author?: string | null;
  status: ProtocolStatus;
  body: string;
}

export interface ProtocolStatistics {
  wordCount: number;
  headingCount: number;
  paragraphCount: number;
  codeBlocks: number;
  mermaidDiagrams: number;
  excalidrawDrawings: number;
}

export interface OutlineHeading {
  level: number;
  text: string;
}

export interface ProtocolOutline {
  headings: OutlineHeading[];
}
