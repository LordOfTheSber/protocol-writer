// Типы зеркалят доменную модель бэкенда. Discriminated union по полю `type`
// — прямой аналог Java `sealed interface Section` с полиморфным JSON.

export type DecisionStatus = 'OPEN' | 'IN_PROGRESS' | 'DONE' | 'REJECTED';

export interface Decision {
  text: string;
  status: DecisionStatus;
  assignee?: string | null;
  dueDate?: string | null;
}

export interface TextSection {
  type: 'text';
  heading: string;
  body: string;
}

export interface DecisionListSection {
  type: 'decisions';
  heading: string;
  decisions: Decision[];
}

export interface TableSection {
  type: 'table';
  heading: string;
  columns: string[];
  rows: string[][];
}

export type Section = TextSection | DecisionListSection | TableSection;

export type SectionType = Section['type'];

export type ProtocolStatus = 'DRAFT' | 'PUBLISHED' | 'ARCHIVED';

export interface ProtocolSummary {
  id: string;
  title: string;
  author: string | null;
  status: ProtocolStatus;
  sectionCount: number;
  updatedAt: string;
}

export interface Protocol {
  id: string;
  title: string;
  author: string | null;
  status: ProtocolStatus;
  sections: Section[];
  createdAt: string;
  updatedAt: string;
}

export interface ProtocolInput {
  title: string;
  author?: string | null;
  status: ProtocolStatus;
  sections: Section[];
}

export interface ProtocolStatistics {
  sectionCount: number;
  textSections: number;
  decisionSections: number;
  tableSections: number;
  totalDecisions: number;
  decisionsByStatus: Record<DecisionStatus, number>;
}
