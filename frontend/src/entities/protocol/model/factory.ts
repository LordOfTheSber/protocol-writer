import type { Section, SectionType } from './types';

/** Создаёт пустую секцию заданного типа (для редактора). */
export function createEmptySection(type: SectionType): Section {
  switch (type) {
    case 'text':
      return { type: 'text', heading: '', body: '' };
    case 'decisions':
      return { type: 'decisions', heading: '', decisions: [] };
    case 'table':
      return { type: 'table', heading: '', columns: ['Колонка 1'], rows: [['']] };
  }
}
